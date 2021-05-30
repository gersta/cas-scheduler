package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
public abstract class AbstractLecturePdfReaderService {

    protected final PDDocument document;

    protected AbstractLecturePdfReaderService(String filename) throws IOException {
        this.document = getDocument(filename);
    }

    /**
     * extract the lectures from the pdf document passed from the constructor
     * @param pageIndex: the page of the document to scrape or null in case of all pages
     * @return: list of {@link Lecture} instances scraped from the given pdf document (and page)
     * @throws IOException
     */
    public List<Lecture> extractLectures(Integer pageIndex) throws IOException {
        log.info("readPdf(): page index {}", pageIndex);

        // iterate over all pages
        List<Lecture> lectures = StreamSupport.stream(document.getPages().spliterator(), false)
                .filter(page -> pageIndex == null || page.equals(document.getPage(pageIndex))) // only take a specific page if filter is set
                .map(this::processPage) // process each of them
                .flatMap(Collection::stream) // flatMap to list of lectures
                .distinct() // filter all those entries that were read twice due to scanning each page twice
                .collect(Collectors.toList()); // collect list

        log.info("readPdf(): page index {}. Read {} lectures", pageIndex, lectures.size());

        closeDocument();

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
        log.info("closeDocument()");

        document.close();
    }

    public abstract List<Lecture> processPage(PDPage page);
}
