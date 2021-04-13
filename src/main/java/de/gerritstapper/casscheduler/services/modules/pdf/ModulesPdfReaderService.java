package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
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

        String lectureName = getLectureName(lines[1]);
        String lectureNameEnglish = lines[2];


        // FORMALITIES
        String[] formalities = lines[5].split(WHITESPACE);
        String lectureCode = formalities[0];
        String duration = formalities[2];
        String owner = getLecturer(formalities);
        String language = formalities[formalities.length - 1];

        // EXAM
        String[] examInfo = lines[11].split(WHITESPACE);
        String exam = examInfo[0];
        String examDuration = examInfo[1];
        String examMarking = examInfo[2];

        // WORKLOAD
        String[] workload = lines[14].split(WHITESPACE);
        String totalWorkload = workload[0];
        String presentWorkload = workload[1];
        String selfStudyWorkload = workload[2];
        String ectsPoints = workload[3];

        // METAINFO
        String[] metainfo = lines[41].split(WHITESPACE);
        String updatedOn = metainfo[2];

        return Module.builder()
                .lectureCode(lectureCode)
                .lectureName(lectureName)
                .lectureNameEnglish(lectureNameEnglish)
                .owner(owner)
                .duration(duration)
                .language(language)
                .exam(exam)
                .examDuration(examDuration)
                .examMarking(examMarking)
                .totalWorkload(totalWorkload)
                .presentWorkload(presentWorkload)
                .selfStudyWorkload(selfStudyWorkload)
                .ectsPoints(ectsPoints)
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
