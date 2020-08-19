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

    public List<Lecture> readPdf(String filename) throws IOException {
        // get the file
        String filePath = getClass().getClassLoader().getResource(filename).getFile();
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
        return IntStream.range(0, 58).mapToObj(step -> {
            int startY = 83; // first content line
            double rowStep = step * 3.1; // 3.1 mm between the different lines
            double nextY = startY + rowStep;  // y distance from the top

            addRegions(stripper, nextY);
            try {
                stripper.extractRegions(page);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String id = get(stripper, PdfRegions.ID);
            String name = get(stripper, PdfRegions.NAME);

            // some lectures spread across multiple lines and thus produce empty lecture objects
            // for the additional lines
            if ( !id.startsWith("T3M") && !id.startsWith("W3M") ) {
                return null;
            }

            String startOne = get(stripper, PdfRegions.START_ONE);
            String endOne = get(stripper, PdfRegions.END_ONE);
            String placeOne = get(stripper, PdfRegions.PLACE_ONE);

            String startTwo = get(stripper, PdfRegions.START_TWO);
            String endTwo = get(stripper, PdfRegions.END_TWO);
            String placeTwo = get(stripper, PdfRegions.PLACE_TWO);

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
        })
        .filter(lecture -> Objects.nonNull(lecture)) // filter the skipped lines
        .collect(Collectors.toList());
    }

    public void addRegions(PDFTextStripperByArea stripper, double y) {
        int height = 3;

        // x, y, width, height
        Rectangle2D id = new Rectangle2D.Double(mmToUnits(15), mmToUnits(y), mmToUnits(10), mmToUnits(height));
        stripper.addRegion(PdfRegions.ID.name(), id);

        Rectangle2D name = new Rectangle2D.Double(mmToUnits(24), mmToUnits(y), mmToUnits(80), mmToUnits(height));
        stripper.addRegion(PdfRegions.NAME.name(), name);

        Rectangle2D startOne = new Rectangle2D.Double(mmToUnits(104), mmToUnits(y), mmToUnits(6), mmToUnits(height));
        stripper.addRegion(PdfRegions.START_ONE.name(), startOne);

        Rectangle2D endOne = new Rectangle2D.Double(mmToUnits(110), mmToUnits(y), mmToUnits(12), mmToUnits(height));
        stripper.addRegion(PdfRegions.END_ONE.name(), endOne);

        Rectangle2D placeOne = new Rectangle2D.Double(mmToUnits(123), mmToUnits(y), mmToUnits(5), mmToUnits(height));
        stripper.addRegion(PdfRegions.PLACE_ONE.name(), placeOne);


        Rectangle2D startTwo = new Rectangle2D.Double(mmToUnits(135), mmToUnits(y), mmToUnits(6), mmToUnits(height));
        stripper.addRegion(PdfRegions.START_TWO.name(), startTwo);

        Rectangle2D endTwo = new Rectangle2D.Double(mmToUnits(145), mmToUnits(y), mmToUnits(9), mmToUnits(height));
        stripper.addRegion(PdfRegions.END_TWO.name(), endTwo);

        Rectangle2D placeTwo = new Rectangle2D.Double(mmToUnits(156), mmToUnits(y), mmToUnits(5), mmToUnits(height));
        stripper.addRegion(PdfRegions.PLACE_TWO.name(), placeTwo);
    }

    public String get(PDFTextStripperByArea stripper, PdfRegions region) {
        String content = stripper.getTextForRegion(region.name()).replace("\n", "");
        return switch (region) {
            case NAME -> content.replaceAll("\\((.*)\\)", ""); // remove everything in parantheses e.g. (Beginn 19.09)
            case START_ONE, END_ONE, START_TWO, END_TWO -> content.replace("-", "").strip();
            case PLACE_ONE, PLACE_TWO -> content.replace("(", "").replace(")", "").replace("Ö", "OE"); // (MA) to MA
            default -> content;
        };
    }
}