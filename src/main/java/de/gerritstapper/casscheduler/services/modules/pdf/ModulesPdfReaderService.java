package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.*;
import de.gerritstapper.casscheduler.models.module.Module;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.springframework.stereotype.Service;

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
    private static final String EXAM_HEADLINE = "PRÜFUNGSLEISTUNG";
    private static final String WORKLOAD_HEADLINE = "WORKLOAD INSGESAMT";
    private static final String METAINFO_HEADLINE = "Stand vom";

    private final ModulePagesGroupingService groupingService;
    private final ModulePdfTextStripper textStripper;

    public ModulesPdfReaderService(
            ModulePagesGroupingService groupingService,
            ModulePdfTextStripper textStripper)  {
        this.groupingService = groupingService;
        this.textStripper = textStripper;
    }


    public List<Module> readPdf() {
        log.info("readPdf()");

        try {
            PDPageTree pages = textStripper.getPdfPages();

            return groupingService.groupPdfPagesByModule(pages).values().stream()
                    .map(this::processModule)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error in readPdf ");
            e.printStackTrace();

            return Collections.emptyList();
        }

    }

    private Module processModule(List<ModulePdfPage> modulePdfPages) {
        ModulePdfPage firstPage = modulePdfPages.get(0);

        return processPage(firstPage.getPageIndexInDocument());
    }



    @SneakyThrows // TODO: remove this
    private Module processPage(int index) {
        String content = textStripper.getTextForPage(index);

        String[] lines = content.split(LINEBREAK);

        Module module = new Module();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if ( isLectureName(i) ) {
                String lectureName = getLectureName(lines[i]);

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

    private boolean isExam(String line) {
        return matchesHeadline(line, EXAM_HEADLINE);
    }

    private boolean isWorkload(String line) {
        return matchesHeadline(line, WORKLOAD_HEADLINE);
    }

    private boolean isMetainfo(String line) {
        return matchesHeadline(line, METAINFO_HEADLINE);
    }

    private boolean matchesHeadline(String line, String headline) {
        return line.toLowerCase().startsWith(headline.toLowerCase());
    }

    private FormalitiesInformation extractFormalities(String formalitiesLine) {
        String[] formalitiesContent = formalitiesLine.split(WHITESPACE);

        String lectureCode = formalitiesContent[0];
        String duration = formalitiesContent[2];
        String owner = getLecturer(formalitiesContent);
        String language = formalitiesContent[formalitiesContent.length - 1];

        return FormalitiesInformation.builder()
                .lectureCode(lectureCode)
                .duration(duration)
                .owner(owner)
                .language(language)
                .build();
    }

    private ExamInfo extractExam(String examLine) {
        String[] examInfo = examLine.split(WHITESPACE);

        String exam = examInfo[0];
        String examDuration = examInfo[1];
        String examMarking = examInfo[2];

        return ExamInfo.builder()
                .exam(exam)
                .examDuration(examDuration)
                .examMarking(examMarking)
                .build();
    }

    private WorkloadInfo extractWorkload(String workloadLine) {
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

    private MetaInformation extractMetainformation(String metaInfoLine) {
        String[] metainfo = metaInfoLine.split(WHITESPACE);
        String updatedOn = metainfo[2];

        return MetaInformation.builder()
                .updatedOn(updatedOn)
                .build();
    }

    private String getLectureName(String line) {
        Pattern regex = Pattern.compile(".*(?= \\(\\w*\\))");
        Matcher matcher = regex.matcher(line);

        if ( matcher.find() ) {
            return matcher.group();
        }

        return "";
    }

    private String getLecturer(String[] formalities) {
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
