package de.gerritstapper.casscheduler.services.lectures.pdf;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.enums.*;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public class CasLecturePdfTextStripper {

    private static final int MINIMAL_Y_OFFSET_PT = 150;
    private static final double LINE_HEIGHT_PT = 7;

    private final LectureFieldExtractorService fieldExtractorService;
    private final PDFTextStripperByArea stripper;

    private Lecture lastLecture;

    public CasLecturePdfTextStripper(
            LectureFieldExtractorService fieldExtractorService
    ) throws IOException {
        this.fieldExtractorService = fieldExtractorService;
        this.stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
    }

    public List<Lecture> stripLectures(PDPage page, Faculty faculty) {
        return IntStream.range(MINIMAL_Y_OFFSET_PT, 760)
                .mapToObj(step -> readNextRow(page, step, faculty))
                .collect(Collectors.toList());
    }

    private Lecture readNextRow(PDPage page, double nextY, Faculty faculty) {
        String id = extractContent(page, nextY, faculty, PdfColumns.ID);
        String name = extractContent(page, nextY, faculty, PdfColumns.NAME);

        String startOne = extractContent(page, nextY, faculty, PdfColumns.FIRST_BLOCK_START);
        String endOne = extractContent(page, nextY, faculty, PdfColumns.FIRST_BLOCK_END);
        String locationOne = extractContent(page, nextY, faculty, PdfColumns.FIRST_BLOCK_LOCATION);

        String startTwo = extractContent(page, nextY, faculty, PdfColumns.SECOND_BLOCK_START);
        String endTwo = extractContent(page, nextY, faculty, PdfColumns.SECOND_BLOCK_END);
        String locationTwo = extractContent(page, nextY, faculty, PdfColumns.SECOND_BLOCK_LOCATION);

        if ( isAdditionalBlock(name) ) {
            return lastLecture.toBuilder()
                    .firstBlockStart(startOne)
                    .firstBlockEnd(endOne)
                    .firstBlockLocation(locationOne)
                    .secondBlockStart(startTwo)
                    .secondBlockEnd(endTwo)
                    .secondBlockLocation(locationTwo)
                    .build();
        }

        Lecture lecture = Lecture.builder()
                .lectureCode(id)
                .name(name)
                .firstBlockStart(startOne)
                .firstBlockEnd(endOne)
                .firstBlockLocation(locationOne)
                .secondBlockStart(startTwo)
                .secondBlockEnd(endTwo)
                .secondBlockLocation(locationTwo)
                .build();

        log.trace("Extracted lecture: {}", lecture);

        if ( id.matches(LectureRegexPatterns.ID.getPattern()) ) {
            lastLecture = lecture;
        }

        return lecture;
    }

    /**
     * add a region (x, y, width, height) to the {@link PDFTextStripperByArea} that content can then be read from
     * uses the LINE_HEIGHT specified as a static parameter of the class
     * @param y: the Y coordinate of the row that is about to be extracted from the {@link PDPage}
     */
    private void addRegions(double y, Faculty faculty, PdfColumns column) {
        log.trace("addRegions(): {}", y);

        Rectangle2D region = new Rectangle2D.Double(
                getXCoordinate(faculty, column),
                y,
                getWidth(faculty, column),
                LINE_HEIGHT_PT
        );

        /*Rectangle2D name = new Rectangle2D.Double(
                getXCoordinate(faculty, PdfColumns.NAME),
                y,
                getWidth(faculty, PdfColumns.NAME),
                LINE_HEIGHT_PT
        );

        Rectangle2D firstBlockStart = new Rectangle2D.Double(
                getXCoordinate(faculty, PdfColumns.FIRST_BLOCK_START),
                y,
                getWidth(faculty, PdfColumns.FIRST_BLOCK_START),
                LINE_HEIGHT_PT
        );

        Rectangle2D firstBlockEnd = new Rectangle2D.Double(
                getXCoordinate(faculty, PdfColumns.FIRST_BLOCK_END),
                y,
                getWidth(faculty, PdfColumns.FIRST_BLOCK_END),
                LINE_HEIGHT_PT
        );

        Rectangle2D firstBlockPlace = new Rectangle2D.Double(
                getXCoordinate(faculty, PdfColumns.FIRST_BLOCK_LOCATION),
                y,
                getWidth(faculty, PdfColumns.FIRST_BLOCK_LOCATION),
                LINE_HEIGHT_PT
        );

        Rectangle2D secondBlockStart = new Rectangle2D.Double(
                getXCoordinate(faculty, PdfColumns.SECOND_BLOCK_START),
                y,
                getWidth(faculty, PdfColumns.SECOND_BLOCK_START),
                LINE_HEIGHT_PT
        );

        Rectangle2D secondBlockEnd = new Rectangle2D.Double(
                getXCoordinate(faculty, PdfColumns.SECOND_BLOCK_END),
                y,
                getWidth(faculty, PdfColumns.SECOND_BLOCK_END),
                LINE_HEIGHT_PT
        );

        Rectangle2D secondBlockPlace = new Rectangle2D.Double(
                getXCoordinate(faculty, PdfColumns.SECOND_BLOCK_LOCATION),
                y,
                getWidth(faculty, PdfColumns.SECOND_BLOCK_LOCATION),
                LINE_HEIGHT_PT
        );*/

        stripper.addRegion("REGION", region);
        /*stripper.addRegion(PdfColumns.NAME.name(), name);

        stripper.addRegion(PdfColumns.FIRST_BLOCK_START.name(), firstBlockStart);
        stripper.addRegion(PdfColumns.FIRST_BLOCK_END.name(), firstBlockEnd);
        stripper.addRegion(PdfColumns.FIRST_BLOCK_LOCATION.name(), firstBlockPlace);

        stripper.addRegion(PdfColumns.SECOND_BLOCK_START.name(), secondBlockStart);
        stripper.addRegion(PdfColumns.SECOND_BLOCK_END.name(), secondBlockEnd);
        stripper.addRegion(PdfColumns.SECOND_BLOCK_LOCATION.name(), secondBlockPlace);*/
    }

    private int getXCoordinate(Faculty faculty, PdfColumns column) {
        return faculty.equals(Faculty.TECHNIK) ? TechnikLecturePdfDimensions.valueOf(column.name()).getX() : WirtschaftLecturePdfDimensions.valueOf(column.name()).getX();
    }

    private int getWidth(Faculty faculty, PdfColumns column) {
        return faculty.equals(Faculty.TECHNIK) ? TechnikLecturePdfDimensions.valueOf(column.name()).getWidth() : WirtschaftLecturePdfDimensions.valueOf(column.name()).getWidth();
    }

    private String extractContent(PDPage page, double nextY, Faculty faculty, PdfColumns column) {
        addRegions(nextY, faculty, column);

        try {
            stripper.extractRegions(page);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = stripContent(column);

        return switch (column) {
            case ID -> fieldExtractorService.getId(content);
            case NAME -> fieldExtractorService.getName(content);
            case FIRST_BLOCK_START, SECOND_BLOCK_START -> fieldExtractorService.getStart(content);
            case FIRST_BLOCK_END, SECOND_BLOCK_END -> fieldExtractorService.getEnd(content);
            case FIRST_BLOCK_LOCATION, SECOND_BLOCK_LOCATION -> fieldExtractorService.getLocation(content);
            default -> content;
        };
    }

    private String stripContent(PdfColumns column) {
        log.trace("extractText(): {}", column);

        // return stripper.getTextForRegion(column.name()).replace("\n", "");
        return stripper.getTextForRegion("REGION").replace("\n", "");
    }

    private boolean isAdditionalBlock(String name) {
        return name.contains("weiterer Termin") || name.contains("weitere Termine");
    }
}
