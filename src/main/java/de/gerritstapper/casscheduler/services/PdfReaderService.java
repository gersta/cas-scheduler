package de.gerritstapper.casscheduler.services;

import static de.gerritstapper.casscheduler.util.MillimeterUtil.mmToUnits;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import de.gerritstapper.casscheduler.models.Lecture;
import de.gerritstapper.casscheduler.models.Regions;

public class PdfReaderService {

    public List<Lecture> readPdf() throws IOException {
        // get the file
        String filePath = getClass().getClassLoader().getResource("M_T_Lehrveranstaltungen.pdf").getFile();
        PDDocument doc = PDDocument.load(new File(filePath));

        // create the PDF reader
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        // iterate over all pages
        List<Lecture> lectures = StreamSupport.stream(doc.getPages().spliterator(), false)
                    .filter(page -> page.equals(doc.getPage(0)))
                    .map(page -> processPage(stripper, page)) // get all vorlesungen for each page
                    .flatMap(lecture -> lecture.stream()) // flatMap to list of vorlesungen
                    .collect(Collectors.toList()); // collect list

        doc.close();

        return lectures;
    }

    /**
     * 
     * @param stripper
     * @param page
     * @return
     */
    public List<Lecture> processPage(PDFTextStripperByArea stripper, PDPage page) {
        return IntStream.range(0, 18).mapToObj(step -> {
            int startY = 82;
            double rowStep = step * 2.5; // each rowStep is 3 mm
            double nextY = startY + rowStep;

            addRegions(stripper, nextY);
            try {
                stripper.extractRegions(page);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String id = get(stripper, Regions.ID);

            String name = get(stripper, Regions.NAME);
            String startOne = get(stripper, Regions.START_ONE);
            String endOne = get(stripper, Regions.END_ONE);
            String placeOne = get(stripper, Regions.PLACE_ONE);

            String startTwo = get(stripper, Regions.START_TWO);
            String endTwo = get(stripper, Regions.END_TWO);
            String placeTwo = get(stripper, Regions.PLACE_TWO);

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
        }).collect(Collectors.toList());
    }

    public void addRegions(PDFTextStripperByArea stripper, double y) {
        int height = 3;

        // x, y, width, height
        Rectangle2D id = new Rectangle2D.Double(mmToUnits(15), mmToUnits(y), mmToUnits(10), mmToUnits(height));
        stripper.addRegion(Regions.ID.name(), id);

        Rectangle2D name = new Rectangle2D.Double(mmToUnits(18), mmToUnits(y), mmToUnits(75), mmToUnits(height));
        stripper.addRegion(Regions.NAME.name(), name);


        Rectangle2D startOne = new Rectangle2D.Double(mmToUnits(104), mmToUnits(y), mmToUnits(6), mmToUnits(height));
        stripper.addRegion(Regions.START_ONE.name(), startOne);

        Rectangle2D endOne = new Rectangle2D.Double(mmToUnits(110), mmToUnits(y), mmToUnits(8), mmToUnits(height));
        stripper.addRegion(Regions.END_ONE.name(), endOne);

        Rectangle2D placeOne = new Rectangle2D.Double(mmToUnits(123), mmToUnits(y), mmToUnits(5), mmToUnits(height));
        stripper.addRegion(Regions.PLACE_ONE.name(), placeOne);


        Rectangle2D startTwo = new Rectangle2D.Double(mmToUnits(125), mmToUnits(y), mmToUnits(6), mmToUnits(height));
        stripper.addRegion(Regions.START_TWO.name(), startTwo);

        Rectangle2D endTwo = new Rectangle2D.Double(mmToUnits(132), mmToUnits(y), mmToUnits(8), mmToUnits(height));
        stripper.addRegion(Regions.END_TWO.name(), endTwo);

        Rectangle2D placeTwo = new Rectangle2D.Double(mmToUnits(140), mmToUnits(y), mmToUnits(4), mmToUnits(height));
        stripper.addRegion(Regions.PLACE_TWO.name(), placeTwo);
    }

    public String get(PDFTextStripperByArea stripper, Regions region) {
        String content = stripper.getTextForRegion(region.name()).replace("\n", "");
        return switch (region) {
            case NAME -> content;//.replaceAll("^\\w", "");
            case START_ONE, END_ONE, START_TWO, END_TWO -> content.replace("-", "").strip();
            case PLACE_ONE, PLACE_TWO -> content.replace("(", "").replace(")", ""); // (MA) to MA
            default -> content;
        };
    }
}