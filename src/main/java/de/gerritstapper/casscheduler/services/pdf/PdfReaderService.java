package de.gerritstapper.casscheduler.services.pdf;

import static de.gerritstapper.casscheduler.util.MillimeterUtil.mmToUnits;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.models.PdfColumns;

/**
 * central class to orchestrate the scraping of the content
 */
public class PdfReaderService {
    
    private final PDFTextStripperByArea stripper;
    private final PDDocument document;

    private static final double LINE_HEIGHT = 2.0;
    private static final int MINIMAL_Y_OFFSET = 55; // at least 55mm offset from the top of the page. Saves on empty iteration steps to find first line
    
    public PdfReaderService(String filename) throws IOException {
        stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        document = getDocument(filename);
    }

    /**
     * extract the lectures from the pdf document passed from the constructor
     * @param pageIndex: the page of the document to scrape or null in case of all pages
     * @return: list of {@link Lecture} instances scraped from the given pdf document (and page)
     * @throws IOException
     */
    public List<Lecture> readPdf(Integer pageIndex) throws IOException {
        // iterate over all pages
        List<Lecture> lectures = StreamSupport.stream(document.getPages().spliterator(), false)
                    .filter(page -> pageIndex == null || page.equals(document.getPage(pageIndex))) // only take a specific page if filter is set
                    .map(page -> processPage(page)) // process each of them
                    .flatMap(lecture -> lecture.stream()) // flatMap to list of lectures
                    .distinct() // filter all those entries that were read twice due to scanning each page twice
                    .collect(Collectors.toList()); // collect list

        closeDocument();

        return lectures;
    }

    /**
     * reads the pdf document from the classpath
     * @param filename: the name of the pdf document to scrape
     * @return: an instance of {@link PDDocument} wrapping the pdf document
     * @throws IOException
     */
    public PDDocument getDocument(String filename) throws IOException {
        String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile();
        return PDDocument.load(new File(filePath));
    }

    /**
     * walk the given pdf page step-by-step and read the next line
     * removes any invalid rows (lecure instances)
     * @param page: pdf page from the given pdf document
     * @return: list of valid {@link Lecture} instances scraped from the given pdf page
     */
    public List<Lecture> processPage(PDPage page) {
        return IntStream.range(MINIMAL_Y_OFFSET, 600).mapToObj(step -> readNextRow(page, step))
                                        .filter(lecture -> ValidatorService.isValid(lecture))
                                        .collect(Collectors.toList());
    }

    /**
     * creates the next regions on the pdf page that the content is then extracted from
     * @param page: a {@link PDPage} instance
     * @param nextY: the Y coordinate on the page for the next row to read content from
     * @return: returns the instance of {@link Lecture} that was read at the given Y coordinate
     */
    public Lecture readNextRow(PDPage page, double nextY) {
        addRegions(nextY);

        try {
            stripper.extractRegions(page);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String content = extractText();
        content = InputDataCleansingService.cleanse(content);

        String id = get(PdfColumns.ID, content);
        String name = get(PdfColumns.NAME, content);

        String startOne = get(PdfColumns.START_ONE, content);
        String endOne = get(PdfColumns.END_ONE, content);
        String placeOne = get(PdfColumns.PLACE_ONE, content);

        String startTwo = get(PdfColumns.START_TWO, content);
        String endTwo = get(PdfColumns.END_TWO, content);
        String placeTwo = get(PdfColumns.PLACE_TWO, content);

        return Lecture.builder()
                .id(id)
                .name(name)
                .startOne(startOne)
                .endOne(endOne)
                .locationOne(placeOne)
                .startTwo(startTwo)
                .endTwo(endTwo)
                .locationTwo(placeTwo)
                .coordinate(nextY)
                .build();
    }

    /**
     * add a region (x, y, width, height) to the {@link PDFTextStripperByArea} that content can then be read from
     * uses the LINE_HEIGHT specified as a static parameter of the class
     * @param y: the Y coordinate of the row that is about to be extracted from the {@link PDPage}
     */
    public void addRegions(double y) {
        // x, y, width, height
        Rectangle2D row = new Rectangle2D.Double(mmToUnits(15), mmToUnits(y), mmToUnits(210), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfColumns.ROW.name(), row);
    }

    /**
     * actually read the text for the given region from the {@link PDPage} and remove new lines
     * @return: the content that was scraped from the given region
     */
    public String extractText() {
        return stripper.getTextForRegion(PdfColumns.ROW.name()).replace("\n", "");
    }

    /**
     * reset the stripper by removing all associated regions
     */
    public void removeRegions() {
        stripper.removeRegion(PdfColumns.ROW.name());
    }

    /**
     * get the specific values for the data fields of a {@link Lecture} from the string that was scraped from the {@link PDPage} for a specific row
     * @param column: the {@link PdfColumns} to get the value for
     * @param content: the value for the given column
     * @return
     */
    public String get(PdfColumns column, String content) {
        return switch (column) {
            case ID -> FieldExtractorService.getId(content);
            case NAME -> FieldExtractorService.getName(content);
            case START_ONE -> FieldExtractorService.getStartOne(content);
            case START_TWO -> FieldExtractorService.getStartTwo(content);
            case END_ONE -> FieldExtractorService.getEndOne(content);
            case END_TWO -> FieldExtractorService.getEndTwo(content);
            case PLACE_ONE -> FieldExtractorService.getPlaceOne(content);
            case PLACE_TWO -> FieldExtractorService.getPlaceTwo(content);
            default -> content;
        };
    }

    public void closeDocument() throws IOException {
        document.close();
    }
}