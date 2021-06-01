package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.PdfColumns;
import de.gerritstapper.casscheduler.services.lectures.pdf.AbstractLecturePdfReaderService;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.gerritstapper.casscheduler.util.MillimeterUtil.mmToUnits;

@Log4j2
public class WirtschaftLecturePdfReaderService extends AbstractLecturePdfReaderService {

    private final PDFTextStripperByArea stripper;

    private final int MINIMAL_Y_OFFSET;
    private final double LINE_HEIGHT;

    public WirtschaftLecturePdfReaderService(
            String filename,
            int minimalYOffset,
            double lineHeight
    ) throws IOException {
        super(filename);

        this.stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        MINIMAL_Y_OFFSET = minimalYOffset;
        LINE_HEIGHT = lineHeight;
    }

    @Override
    protected List<Lecture> processPage(PDPage page) {
        return IntStream.range(MINIMAL_Y_OFFSET, 600)
                .mapToObj(step -> readNextRow(page, step))
                .filter(lecture -> lecture.getLectureCode().contains("W3M")) // filter all the information lines
                .collect(Collectors.toList());
    }

    private Lecture readNextRow(PDPage page, double nextY) {
        addRegions(nextY);

        try {
            stripper.extractRegions(page);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = extractText();
        System.out.println(content);

        return Lecture.builder()
                .lectureCode(content)
                .build();
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
}
