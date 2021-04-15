package de.gerritstapper.casscheduler.services.modules.pdf;

import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ModulePdfTextStripper {

    private static final String LECTURE_CODE_BOTTOM_PAGE_PATTERN = "(?<=\\d{2}\\.\\d{2}\\.\\d{4} )(\\w\\d\\w\\d{5})(?= \\/\\/)";

    private final PDFTextStripper textStripper;
    private final PDDocument document;

    public ModulePdfTextStripper(
            @Value("${cas-scheduler.modules.filename}") String filename
    ) throws IOException { // TODO: Get rid of the exception
        textStripper = new PDFTextStripper();
        textStripper.setSortByPosition(true);

        document = getDocument(filename);
    }

    public PDPageTree getPdfPages() {
        return document.getPages();
    }

    public String getTextForPage(int pageIndex) {
        try {
            textStripper.setStartPage(pageIndex);
            textStripper.setEndPage(pageIndex);

            return textStripper.getText(document);
        } catch (IOException ioException) {
            log.error("IO Exception appeared at getTextForPage() with index {}", pageIndex);

            ioException.printStackTrace();

            return "";
        }
    }

    public String getLectureCodeForPage(int pageIndex) {
        String content = getTextForPage(pageIndex);

        // date followed by whitespace followed by lecture code followed by double forward slash
        // Stand vom 13.07.2020 T3M10101 // Seite 6
        Pattern pattern = Pattern.compile(LECTURE_CODE_BOTTOM_PAGE_PATTERN);

        Matcher matcher = pattern.matcher(content);

        if ( matcher.find() ) {
            String lectureCode = matcher.group();
            return lectureCode;
        }

        return "";
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
}
