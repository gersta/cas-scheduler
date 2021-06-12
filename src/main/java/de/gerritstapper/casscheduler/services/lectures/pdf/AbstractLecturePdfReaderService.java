package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
public abstract class AbstractLecturePdfReaderService {

    protected final PDDocument document;

    private final String filename;

    protected AbstractLecturePdfReaderService(String filename) throws IOException {
        this.filename = filename;
        this.document = getDocument(filename);
    }

    /**
     * extract the lectures from the pdf document passed from the constructor
     * @param pageIndex: the page of the document to scrape or null in case of all pages
     * @return: list of {@link Lecture} instances scraped from the given pdf document (and page)
     * @throws IOException
     */
    public List<Lecture> extractLectures(Integer pageIndex) {
        log.info("readPdf(): page index {} of {}", pageIndex, filename);

        List<Lecture> lectures = new ArrayList<>();

        List<PDPage> pages = convertTreeToList(document.getPages());

        try {
            lectures = pages.stream()
                    .parallel()
                    .map(this::processPage)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());

            log.info("extractLectures(): page index {}. Read {} lectures", pageIndex, lectures.size());

            closeDocument();
        } catch (IOException exception) {
            log.error("Problem in extractLecture({})", pageIndex);

            exception.printStackTrace();
        }


        return lectures;
    }

    /**
     * reads the pdf document from the classpath
     * @param filename: the name of the pdf document to scrape
     * @return: an instance of {@link PDDocument} wrapping the pdf document
     * @throws IOException
     */
    protected PDDocument getDocument(String filename) throws IOException {
        log.info("getDocument(): {}", filename);

        String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile();
        return PDDocument.load(new File(filePath));
    }

    protected void closeDocument() throws IOException {
        log.info("closeDocument(): {}", filename);

        document.close();
    }

    private List<PDPage> convertTreeToList(PDPageTree tree) {
        List<PDPage> pages = new ArrayList<>();
        tree.iterator().forEachRemaining(pages::add);

        return pages;
    }

    protected abstract List<Lecture> processPage(PDPage page);
}
