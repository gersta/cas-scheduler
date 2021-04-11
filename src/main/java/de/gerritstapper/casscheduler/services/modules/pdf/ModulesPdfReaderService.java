package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.Module;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ModulesPdfReaderService {


    private static final String LINEBREAK = "\\r?\\n";
    private static final String WHITESPACE = " ";

    private final PDFTextStripper textStripper;
    private final PDDocument document;

    private final ModulesFieldExtractorService fieldExtractorService;

    public ModulesPdfReaderService(
            @Value("${cas-scheduler.modules.filename}") String filename,
            ModulesFieldExtractorService fieldExtractorService
    ) throws IOException {
        this.fieldExtractorService = fieldExtractorService;
        // TODO: move this to constructor
        textStripper = new PDFTextStripper();
        textStripper.setSortByPosition(true);

        document = getDocument(filename);
    }

    /** TODO: move this to a common class for both pdf ders
     * reads the pdf document from the classpath
     * @param filename: the name of the pdf document to scrape
     * @return: an instance of {@link PDDocument} wrapping the pdf document
     * @throws IOException
     */
    private PDDocument getDocument(String filename) throws IOException {
        log.info("getDocument(): {}", filename);

        String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile();
        return PDDocument.load(new File(filePath));
    }

    public List<Module> readPdf() {
        log.info("readPdf()");

        PDPageTree pages = document.getPages();

        List<Module> modules = new ArrayList<>();

        for(int i = 1; i <= pages.getCount(); i++) { // TODO: the index of the pages starts at 1 instead of 0
            Module module = processPage(i);
            System.out.println(module);
            modules.add(module);
        }

        return modules;
    }

    @SneakyThrows // TODO: remove this
    private Module processPage(int index) {
        textStripper.setStartPage(index);
        textStripper.setEndPage(index);

        String content = textStripper.getText(document);
        String[] lines = content.split(LINEBREAK);


        if ( fieldExtractorService.isNewModule(lines) ) {

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

        } else {
            return null;
        }
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
