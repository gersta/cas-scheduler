package de.gerritstapper.casscheduler.services;

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
import de.gerritstapper.casscheduler.models.PdfRegions;

public class PdfReaderService {
    
    private final PDFTextStripperByArea stripper;
    private final PDDocument document;

    private static final double LINE_HEIGHT = 2.0;
    
    public PdfReaderService(String filename) throws IOException {
        stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        document = getDocument(filename);
    }

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

    public List<Lecture> readPdfWithOffset(Integer pageIndex, double offset) throws IOException {
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

    public PDDocument getDocument(String filename) throws IOException {
        String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile();
        return PDDocument.load(new File(filePath));
    }

    /**
     *
     * @return
     */
    public List<Lecture> processPage(PDPage page) {
        return IntStream.range(0, 600).mapToObj(step -> readNextRow(page, step))
                                        .filter(lecture -> isValid(lecture))
                                        .collect(Collectors.toList());
    }

    public Lecture readNextRow(PDPage page, double nextY) {
        addRegions(nextY);

        try {
            stripper.extractRegions(page);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String id = get(PdfRegions.ID);
        String name = get(PdfRegions.NAME);

        String startOne = get(PdfRegions.START_ONE);
        String endOne = get(PdfRegions.END_ONE);
        String placeOne = get(PdfRegions.PLACE_ONE);

        String startTwo = get(PdfRegions.START_TWO);
        String endTwo = get(PdfRegions.END_TWO);
        String placeTwo = get(PdfRegions.PLACE_TWO);

        return Lecture.builder()
                .id(id)
                .name(name)
                .startOne(startOne)
                .endOne(endOne)
                .placeOne(placeOne)
                .startTwo(startTwo)
                .endTwo(endTwo)
                .placeTwo(placeTwo)
                .build();
    }

    public void addRegions(double y) {
        // x, y, width, height
        Rectangle2D id = new Rectangle2D.Double(mmToUnits(15), mmToUnits(y), mmToUnits(10), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.ID.name(), id);

        Rectangle2D name = new Rectangle2D.Double(mmToUnits(24), mmToUnits(y), mmToUnits(80), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.NAME.name(), name);

        Rectangle2D startOne = new Rectangle2D.Double(mmToUnits(104), mmToUnits(y), mmToUnits(6), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.START_ONE.name(), startOne);

        Rectangle2D endOne = new Rectangle2D.Double(mmToUnits(110), mmToUnits(y), mmToUnits(12), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.END_ONE.name(), endOne);

        Rectangle2D placeOne = new Rectangle2D.Double(mmToUnits(123), mmToUnits(y), mmToUnits(5), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.PLACE_ONE.name(), placeOne);


        Rectangle2D startTwo = new Rectangle2D.Double(mmToUnits(135), mmToUnits(y), mmToUnits(6), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.START_TWO.name(), startTwo);

        Rectangle2D endTwo = new Rectangle2D.Double(mmToUnits(145), mmToUnits(y), mmToUnits(9), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.END_TWO.name(), endTwo);

        Rectangle2D placeTwo = new Rectangle2D.Double(mmToUnits(156), mmToUnits(y), mmToUnits(5), mmToUnits(LINE_HEIGHT));
        stripper.addRegion(PdfRegions.PLACE_TWO.name(), placeTwo);
    }

    /**
     * reset the stripper by removing all associated regions
     */
    public void removeRegions() {
        stripper.removeRegion(PdfRegions.ID.name());
        stripper.removeRegion(PdfRegions.NAME.name());
        stripper.removeRegion(PdfRegions.START_ONE.name());
        stripper.removeRegion(PdfRegions.END_ONE.name());
        stripper.removeRegion(PdfRegions.PLACE_ONE.name());
        stripper.removeRegion(PdfRegions.START_TWO.name());
        stripper.removeRegion(PdfRegions.END_TWO.name());
        stripper.removeRegion(PdfRegions.PLACE_TWO.name());
    }

    public String get(PdfRegions region) {
        String content = stripper.getTextForRegion(region.name()).replace("\n", "");
        return switch (region) {
            case NAME -> content.replaceAll("\\((.*)\\)", "").strip(); // remove everything in parantheses e.g. (Beginn 19.09) and remove whitespaces front and back
            case START_ONE, END_ONE, START_TWO, END_TWO -> content.replace("-", "").strip();
            case PLACE_ONE, PLACE_TWO -> content.replace("(", "").replace(")", "").replace("Ö", "OE"); // (MA) to MA
            default -> content;
        };
    }

    private boolean isValid(Lecture lecture) {
        return
                (lecture.getId().startsWith("W3M") || lecture.getId().startsWith("T3M")) &&
                !lecture.getName().isBlank() &&
                !lecture.getStartOne().isBlank() &&
                !lecture.getEndOne().isBlank() &&
                !lecture.getPlaceOne().isBlank();
    }

    public void closeDocument() throws IOException {
        document.close();
    }
}