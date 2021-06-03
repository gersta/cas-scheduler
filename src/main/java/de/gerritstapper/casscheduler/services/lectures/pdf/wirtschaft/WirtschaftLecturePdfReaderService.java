package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.PdfColumns;
import de.gerritstapper.casscheduler.models.lecture.enums.LecturePdfDimensions;
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

    private final WirtschaftLectureFieldExtractorService fieldExtractorService;

    private final PDFTextStripperByArea stripper;

    private final int MINIMAL_Y_OFFSET;
    private final double LINE_HEIGHT;

    public WirtschaftLecturePdfReaderService(
            WirtschaftLectureFieldExtractorService fieldExtractorService,
            String filename,
            int minimalYOffset,
            double lineHeight
    ) throws IOException {
        super(filename);
        this.fieldExtractorService = fieldExtractorService;

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

        String id = extractContent(PdfColumns.ID);
        String name = extractContent(PdfColumns.NAME);

        String startOne = extractContent(PdfColumns.START_ONE);
        String endOne = extractContent(PdfColumns.END_ONE);
        String placeOne = extractContent(PdfColumns.PLACE_ONE);

        String startTwo = extractContent(PdfColumns.START_TWO);
        String endTwo = extractContent(PdfColumns.END_TWO);
        String placeTwo = extractContent(PdfColumns.PLACE_TWO);

        return Lecture.builder()
                .lectureCode(id)
                .name(name)
                .startOne(startOne)
                .endOne(endOne)
                .locationOne(placeOne)
                .startTwo(startTwo)
                .endTwo(endTwo)
                .locationTwo(placeTwo)
                .build();
    }

    /**
     * add a region (x, y, width, height) to the {@link PDFTextStripperByArea} that content can then be read from
     * uses the LINE_HEIGHT specified as a static parameter of the class
     * @param y: the Y coordinate of the row that is about to be extracted from the {@link PDPage}
     */
    private void addRegions(double y) {
        log.trace("addRegions(): {}", y);

        Rectangle2D id = new Rectangle2D.Double(
                LecturePdfDimensions.ID.getX(),
                mmToUnits(y),
                LecturePdfDimensions.ID.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        Rectangle2D name = new Rectangle2D.Double(
                LecturePdfDimensions.NAME.getX(),
                mmToUnits(y),
                LecturePdfDimensions.NAME.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        Rectangle2D firstBlockStart = new Rectangle2D.Double(
                LecturePdfDimensions.FIRST_BLOCK_START.getX(),
                mmToUnits(y),
                LecturePdfDimensions.FIRST_BLOCK_START.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        Rectangle2D firstBlockEnd = new Rectangle2D.Double(
                LecturePdfDimensions.FIRST_BLOCK_END.getX(),
                mmToUnits(y),
                LecturePdfDimensions.FIRST_BLOCK_END.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        Rectangle2D firstBlockPlace = new Rectangle2D.Double(
                LecturePdfDimensions.FIRST_BLOCK_PLCAE.getX(),
                mmToUnits(y),
                LecturePdfDimensions.FIRST_BLOCK_PLCAE.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        Rectangle2D secondBlockStart = new Rectangle2D.Double(
                LecturePdfDimensions.SECOND_BLOCK_START.getX(),
                mmToUnits(y),
                LecturePdfDimensions.SECOND_BLOCK_START.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        Rectangle2D secondBlockEnd = new Rectangle2D.Double(
                LecturePdfDimensions.SECOND_BLOCK_END.getX(),
                mmToUnits(y),
                LecturePdfDimensions.SECOND_BLOCK_END.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        Rectangle2D secondBlockPlace = new Rectangle2D.Double(
                LecturePdfDimensions.SECOND_BLOCK_PLACE.getX(),
                mmToUnits(y),
                LecturePdfDimensions.SECOND_BLOCK_PLACE.getWidth(),
                mmToUnits(LINE_HEIGHT)
        );

        stripper.addRegion(PdfColumns.ID.name(), id);
        stripper.addRegion(PdfColumns.NAME.name(), name);

        stripper.addRegion(PdfColumns.START_ONE.name(), firstBlockStart);
        stripper.addRegion(PdfColumns.END_ONE.name(), firstBlockEnd);
        stripper.addRegion(PdfColumns.PLACE_ONE.name(), firstBlockPlace);

        stripper.addRegion(PdfColumns.START_TWO.name(), secondBlockStart);
        stripper.addRegion(PdfColumns.END_TWO.name(), secondBlockEnd);
        stripper.addRegion(PdfColumns.PLACE_TWO.name(), secondBlockPlace);
    }

    private String extractContent(PdfColumns column) {
        String content = stripContent(column);

        return switch (column) {
            case ID -> fieldExtractorService.getId(content);
            case NAME -> fieldExtractorService.getName(content);
            case START_ONE, START_TWO -> fieldExtractorService.getStart(content);
            case END_ONE, END_TWO -> fieldExtractorService.getEnd(content);
            case PLACE_ONE, PLACE_TWO -> fieldExtractorService.getLocation(content);
            default -> content;
        };
    }

    private String stripContent(PdfColumns column) {
        log.trace("extractText(): {}", column);

        return stripper.getTextForRegion(column.name()).replace("\n", "");
    }
}
