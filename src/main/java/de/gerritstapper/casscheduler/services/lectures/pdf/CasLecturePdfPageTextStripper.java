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

/**
 * this class is deliberately not a spring component/bean to ensure that it is not
 * simply injected into other services. The idea of the class is to be instantiated
 * for EVERY pdf page to extract information from. This is required as the underelying
 * PDFTextStripperByArea object is stateful and thus an object of this class is stateful.
 * Prototype spring beans are instantiated for every service they are injected into, but not
 * for every certain method call as required here.
 * 
 */
@Log4j2
public class CasLecturePdfPageTextStripper {

    private static final int MINIMAL_Y_OFFSET_PT = 150;
    private static final double LINE_HEIGHT_PT = 7;

    private final LectureFieldExtractorService fieldExtractorService;
    private final PDFTextStripperByArea stripper;

    private final PDPage page;

    private Lecture lastLecture;

    public CasLecturePdfPageTextStripper(PDPage page, LectureFieldExtractorService extractorService) throws IOException {
        this.page = page;

        this.fieldExtractorService = extractorService;

        this.stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
    }

    public List<Lecture> stripPage(Faculty faculty) {
        log.info("Stripping page for faculty {}", faculty);

        return IntStream.range(MINIMAL_Y_OFFSET_PT, 760)
                .mapToObj(step -> readNextRow(page, step, faculty))
                .collect(Collectors.toList());
    }

    private Lecture readNextRow(PDPage page, double nextY, Faculty faculty) {
        log.debug("Reading next row from page {} at {} for faculty {}", page, nextY, faculty);

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

        log.debug("Extracted lecture: {} at {}", lecture, nextY);

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

        stripper.addRegion("REGION", region);
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
            log.error("Problem extracting content from page {} at {} for faculty {} in column {}", page, nextY, faculty, column);

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
