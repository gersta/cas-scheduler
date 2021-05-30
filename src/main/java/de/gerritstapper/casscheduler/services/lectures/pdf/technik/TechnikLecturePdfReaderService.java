package de.gerritstapper.casscheduler.services.lectures.pdf.technik;

import static de.gerritstapper.casscheduler.util.MillimeterUtil.mmToUnits;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import de.gerritstapper.casscheduler.services.lectures.pdf.AbstractLecturePdfReaderService;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.PdfColumns;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * central class to orchestrate the scraping of the content
 */
@Service
@Log4j2
public class TechnikLecturePdfReaderService extends AbstractLecturePdfReaderService {

    // DEPENDENCIES
    private final TechnikLectureValidatorService technikLectureValidatorService;
    private final TechnikLectureFieldExtractorService technikLectureFieldExtractorService;
    private final TechnikInputDataCleansingService technikInputDataCleansingService;

    // CONFIGURATION
    private final double LINE_HEIGHT;
    private final int MINIMAL_Y_OFFSET; // at least 55mm offset from the top of the page. Saves on empty iteration steps to find first line

    // STATE
    private final PDFTextStripperByArea stripper;
    
    public TechnikLecturePdfReaderService(
            final TechnikLectureValidatorService technikLectureValidatorService,
            final TechnikLectureFieldExtractorService technikLectureFieldExtractorService,
            final TechnikInputDataCleansingService technikInputDataCleansingService,
            @Value("${cas-scheduler.lectures.pdf.filename}") String filename,
            @Value("${cas-scheduler.lectures.pdf.line-height}") Double lineHeight,
            @Value("${cas-scheduler.lectures.pdf.minimal-y-offset}") Integer minimalYOffset
    ) throws IOException {
        super(filename);

        this.technikLectureValidatorService = technikLectureValidatorService;
        this.technikLectureFieldExtractorService = technikLectureFieldExtractorService;
        this.technikInputDataCleansingService = technikInputDataCleansingService;
        
        // CONFIGURATION
        this.LINE_HEIGHT = lineHeight;
        this.MINIMAL_Y_OFFSET = minimalYOffset;

        // STATE 
        stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
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
     * walk the given pdf page step-by-step and read the next line
     * removes any invalid rows (lecure instances)
     * @param page: pdf page from the given pdf document
     * @return: list of valid {@link Lecture} instances scraped from the given pdf page
     */
    protected List<Lecture> processPage(PDPage page) {
        log.trace("processPage(): {}", page);

        List<Lecture> lectures = IntStream.range(MINIMAL_Y_OFFSET, 600)
                .mapToObj(step -> readNextRow(page, step))
                .filter(technikLectureValidatorService::isValid)
                .collect(Collectors.toList());

        log.trace("Read {} valid lectures from page {}", lectures.size(), page);

        return lectures;
    }

    /**
     * creates the next regions on the pdf page that the content is then extracted from
     * @param page: a {@link PDPage} instance
     * @param nextY: the Y coordinate on the page for the next row to read content from
     * @return: returns the instance of {@link Lecture} that was read at the given Y coordinate
     */
    private Lecture readNextRow(PDPage page, double nextY) {
        log.trace("readNextRow(): {}, {}", page, nextY);

        addRegions(nextY);

        try {
            stripper.extractRegions(page);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String content = extractText();
        content = technikInputDataCleansingService.cleanse(content);

        String id = get(PdfColumns.ID, content);
        String name = get(PdfColumns.NAME, content);

        String startOne = get(PdfColumns.START_ONE, content);
        String endOne = get(PdfColumns.END_ONE, content);
        String placeOne = get(PdfColumns.PLACE_ONE, content);

        String startTwo = get(PdfColumns.START_TWO, content);
        String endTwo = get(PdfColumns.END_TWO, content);
        String placeTwo = get(PdfColumns.PLACE_TWO, content);

        Lecture lecture = Lecture.builder()
                .lectureCode(id)
                .name(name)
                .startOne(startOne)
                .endOne(endOne)
                .locationOne(placeOne)
                .startTwo(startTwo)
                .endTwo(endTwo)
                .locationTwo(placeTwo)
                .coordinate(nextY)
                .build();

        log.trace("Read lecture: {}", lecture);

        return lecture;
    }

    /**
     * add a region (x, y, width, height) to the {@link PDFTextStripperByArea} that content can then be read from
     * uses the LINE_HEIGHT specified as a static parameter of the class
     * @param y: the Y coordinate of the row that is about to be extracted from the {@link PDPage}
     */
    private void addRegions(double y) {
        log.trace("addRegions(): {}", y);

        // x, y, width, height
        Rectangle2D row = new Rectangle2D.Double(mmToUnits(15), mmToUnits(y), mmToUnits(210), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfColumns.ROW.name(), row);
    }

    /**
     * actually read the text for the given region from the {@link PDPage} and remove new lines
     * @return: the content that was scraped from the given region
     */
    private String extractText() {
        log.trace("extractText()");

        return stripper.getTextForRegion(PdfColumns.ROW.name()).replace("\n", "");
    }

    /**
     * reset the stripper by removing all associated regions
     */
    private void removeRegions() {
        log.info("removeRegions()");

        stripper.removeRegion(PdfColumns.ROW.name());
    }

    /**
     * get the specific values for the data fields of a {@link Lecture} from the string that was scraped from the {@link PDPage} for a specific row
     * @param column: the {@link PdfColumns} to get the value for
     * @param content: the value for the given column
     * @return
     */
    private String get(PdfColumns column, String content) {
        log.trace("get(): {}, {}", column, content);

        return switch (column) {
            case ID -> technikLectureFieldExtractorService.getId(content);
            case NAME -> technikLectureFieldExtractorService.getName(content);
            case START_ONE -> technikLectureFieldExtractorService.getStartOne(content);
            case START_TWO -> technikLectureFieldExtractorService.getStartTwo(content);
            case END_ONE -> technikLectureFieldExtractorService.getEndOne(content);
            case END_TWO -> technikLectureFieldExtractorService.getEndTwo(content);
            case PLACE_ONE -> technikLectureFieldExtractorService.getPlaceOne(content);
            case PLACE_TWO -> technikLectureFieldExtractorService.getPlaceTwo(content);
            default -> content;
        };
    }
}