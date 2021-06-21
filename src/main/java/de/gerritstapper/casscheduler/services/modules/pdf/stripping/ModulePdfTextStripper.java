package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.models.enums.RegexPatterns;
import de.gerritstapper.casscheduler.models.module.enums.ModuleRegexPattern;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class ModulePdfTextStripper {

    private final PDFTextStripper textStripper;
    private final PDDocument document;

    public ModulePdfTextStripper(
            String filename
    ) throws IOException { // TODO: Get rid of the exception
        textStripper = new PDFTextStripper();
        textStripper.setSortByPosition(true);

        document = getDocument(filename);
    }

    protected PDPageTree getPdfPages() {
        return document.getPages();
    }

    protected String getTextForPage(int pageIndex) {
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

    protected String getLectureCodeForPage(int pageIndex) {
        String content = getTextForPage(pageIndex);

        String lectureCodePattern = String.format("(%s|%s)", RegexPatterns.LECTURE_CODE.getPattern(), ModuleRegexPattern.MASTER_THESIS.getPattern());

        Pattern pattern = Pattern.compile(lectureCodePattern);

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
