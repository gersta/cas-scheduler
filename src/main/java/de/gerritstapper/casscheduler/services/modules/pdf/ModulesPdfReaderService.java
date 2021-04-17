package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.models.module.*;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ModulesPdfReaderService {

    private static final String LINEBREAK = "\\r?\\n";
    private static final String WHITESPACE = " ";

    private static final String FORMALITIES_HEADLINE = "MODULNUMMER VERORTUNG IM STUDIENVERLAUF";
    private static final String LECTURING_FORMS_HEADLINE = "LEHRFORMEN LEHRMETHODEN";
    private static final String EXAM_HEADLINE = "PRUEFUNGSLEISTUNG"; // TODO: the Ü was manually converted to UE here to ensure matches
    private static final String WORKLOAD_HEADLINE = "WORKLOAD INSGESAMT";
    private static final String METAINFO_HEADLINE = "Stand vom";
    private static final String SPECIFICS_HEADLINE = "BESONDERHEITEN";

    private final ModulePagesGroupingService groupingService;
    private final ModulePdfTextStripper textStripper;
    private final ModuleDataCleansingService cleansingService;

    public ModulesPdfReaderService(
            ModulePagesGroupingService groupingService,
            ModulePdfTextStripper textStripper,
            ModuleDataCleansingService cleansingService)  {
        this.groupingService = groupingService;
        this.textStripper = textStripper;
        this.cleansingService = cleansingService;
    }


    public List<Module> extractModules() {
        log.info("readPdf()");

        try {
            PDPageTree pages = textStripper.getPdfPages();

            return groupingService.groupPdfPagesByModule(pages)
                    .entrySet().stream()
                    .map(module -> processModule(module.getKey(), module.getValue()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error in readPdf ");
            e.printStackTrace();

            return Collections.emptyList();
        }

    }

    private Module processModule(String lectureCode, List<ModulePdfPage> modulePdfPages) {
        log.debug("processModule(): {}", lectureCode);

        String moduleTextContent = modulePdfPages.stream()
                .map(page -> textStripper.getTextForPage(page.getPageIndexInDocument()))
                .map(cleansingService::removeGermanUmlaute)
                .collect(Collectors.joining());

        return extractModuleContent(moduleTextContent);
    }



    @SneakyThrows // TODO: remove this
    private Module extractModuleContent(String moduleTextContent) {
        String[] lines = moduleTextContent.split(LINEBREAK);

        Module module = new Module();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if ( isLectureName(i) ) {
                String lectureName = extractLectureName(lines[i]);

                module.setLectureName(lectureName);
            }

            if ( isLectureNameEnglish(i) ) {
                String lectureNameEnglish = lines[i];

                module.setLectureNameEnglish(lectureNameEnglish);
            }

            if (  isFormalities(line) ) {
                String nextLine = lines[i+1];

                FormalitiesInformation formalities = extractFormalities(nextLine);

                module.setLectureCode(formalities.getLectureCode());
                module.setDuration(formalities.getDuration());
                module.setOwner(formalities.getOwner());
                module.setLanguage(formalities.getLanguage());
            }

            if ( isLecturingMethods(line) ) {
                String nextLine = lines[i+1];

                LecturingFormsInformation formsAndMethods = extractLecturingFormsAndMethods(nextLine);

                module.setLecturingForms(formsAndMethods.getLecturingForms());
                module.setLecturingMethods(formsAndMethods.getLecturingMethods());
            }

            if ( isExam(line) ) {
                String nextLine = lines[i+1];

                ExamInfo examInfo = extractExam(nextLine);

                module.setExam(examInfo.getExam());
                module.setExamDuration(examInfo.getExamDuration());
                module.setExamMarking(examInfo.getExamMarking());
            }

            if ( isWorkload(line) ) {
                String nextLine = lines[i+1];

                WorkloadInfo workload = extractWorkload(nextLine);

                module.setTotalWorkload(workload.getTotalWorkload());
                module.setPresentWorkload(workload.getPresentWorkload());
                module.setSelfStudyWorkload(workload.getSelfStudyWorkload());
                module.setEctsPoints(workload.getEctsPoints());
            }

            // TODO: Check if this is really necessary for a first draft
            // especially as multi-line is still a to do
            if ( isSpecifics(line) ) {
                String nextLine = lines[i+1];

                String specifics = extractSpecifics(nextLine);

                module.setSpecifics(specifics);
            }

            if ( isMetainfo(line) ) {
                MetaInformation metaInfo = extractMetainformation(line);

                module.setUpdatedOn(metaInfo.getUpdatedOn());
            }
        }

        return module;
    }

    private boolean isLectureName(int lineIndex) {
        return lineIndex == 1;
    }

    private boolean isLectureNameEnglish(int lineIndex) {
        return lineIndex == 2;
    }

    private boolean isFormalities(String line) {
        return matchesHeadline(line, FORMALITIES_HEADLINE);
    }

    private boolean isLecturingMethods(String line) {
        return matchesHeadline(line, LECTURING_FORMS_HEADLINE);
    }

    private boolean isExam(String line) {
        return matchesHeadline(line, EXAM_HEADLINE);
    }

    private boolean isWorkload(String line) {
        return matchesHeadline(line, WORKLOAD_HEADLINE);
    }

    private boolean isMetainfo(String line) {
        return matchesHeadline(line, METAINFO_HEADLINE);
    }

    private boolean isSpecifics(String line) {
        return matchesHeadline(line, SPECIFICS_HEADLINE);
    }

    private boolean matchesHeadline(String line, String headline) {
        return line.toLowerCase().startsWith(headline.toLowerCase());
    }

    private FormalitiesInformation extractFormalities(String formalitiesLine) {
        log.debug("extractFormalities(): {}", formalitiesLine);

        String[] formalitiesContent = formalitiesLine.split(WHITESPACE);

        String lectureCode = formalitiesContent[0];
        String duration = formalitiesContent[2];
        String owner = extractOwner(formalitiesContent);
        String language = formalitiesContent[formalitiesContent.length - 1];

        return FormalitiesInformation.builder()
                .lectureCode(lectureCode)
                .duration(duration)
                .owner(owner)
                .language(language)
                .build();
    }

    private LecturingFormsInformation extractLecturingFormsAndMethods(String lecturingMethodsLine) {
        log.debug("extractLecturingFormsAndMethods(): {}", lecturingMethodsLine);

        String[] methodsContent = lecturingMethodsLine.split("(?<=\\w) (?=\\w)");

        String forms = methodsContent[0].trim();
        String methods = methodsContent[1].trim();

        return LecturingFormsInformation.builder()
                .lecturingForms(forms)
                .lecturingMethods(methods)
                .build();
    }

    private ExamInfo extractExam(String examLine) {
        log.debug("extractExam(): {}", examLine);

        String[] examInfo = examLine.split(WHITESPACE);

        // the field exam can be of variable length and complexitiy, but the two remaining fields are consistent
        // take those first
        String examMarking = examInfo[examInfo.length - 1];
        String examDuration = examInfo[examInfo.length - 2];

        StringBuilder exam = new StringBuilder();
        int endExclusive = examInfo.length - 2;
        List<String> examTest = Arrays.stream(examInfo, 0, endExclusive).collect(Collectors.toList());

        for (String part : examTest) {
            exam.append(part);
        }

        return ExamInfo.builder()
                .exam(exam.toString().trim())
                .examDuration(examDuration)
                .examMarking(examMarking)
                .build();
    }

    private WorkloadInfo extractWorkload(String workloadLine) {
        log.debug("extractWorkload(): {}", workloadLine);

        String[] workload = workloadLine.split(WHITESPACE);

        String totalWorkload = workload[0];
        String presentWorkload = workload[1];
        String selfStudyWorkload = workload[2];
        String ectsPoints = workload[3];

        return WorkloadInfo.builder()
                .totalWorkload(totalWorkload)
                .presentWorkload(presentWorkload)
                .selfStudyWorkload(selfStudyWorkload)
                .ectsPoints(ectsPoints)
                .build();
    }

    private String extractSpecifics(String specificsText) {
        log.debug("extractSpecifics(): {}", specificsText);

        return specificsText;
    }

    private MetaInformation extractMetainformation(String metaInfoLine) {
        log.debug("extractMetainformation(): {}", metaInfoLine);

        String[] metainfo = metaInfoLine.split(WHITESPACE);
        String updatedOn = metainfo[2];

        return MetaInformation.builder()
                .updatedOn(updatedOn)
                .build();
    }

    private String extractLectureName(String line) {
        log.debug("extractLectureName(): {}", line);

        Pattern regex = Pattern.compile(".*(?= \\(\\w*\\))");
        Matcher matcher = regex.matcher(line);

        if ( matcher.find() ) {
            return matcher.group();
        }

        return "";
    }

    private String extractOwner(String[] formalities) {
        log.debug("extractOwner(): {}", formalities);

        int last = formalities.length - 1;
        int start = 3;

        StringBuilder output = new StringBuilder();

        List<String> lecturerParts = java.util.Arrays.stream(formalities, start, last)
                .collect(Collectors.toList());

        for (String part : lecturerParts) {
            output.append(part).append(" ");
        }

        return output.toString().trim();
    }
}
