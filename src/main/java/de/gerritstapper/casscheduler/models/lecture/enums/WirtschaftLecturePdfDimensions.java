package de.gerritstapper.casscheduler.models.lecture.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All dimensions are given in points ( = 1/72 inch) as the PdfBox API Reactangle2D.Double works
 * with points and thus these numbers are more precise
 */
@AllArgsConstructor
@Getter
public enum WirtschaftLecturePdfDimensions {
    ID(40, 32),
    NAME(75, 225),
    FIRST_BLOCK_START(305, 20),
    FIRST_BLOCK_END(330, 30),
    FIRST_BLOCK_LOCATION(360, 20),
    SECOND_BLOCK_START(400, 15),
    SECOND_BLOCK_END(425, 30),
    SECOND_BLOCK_LOCATION(455, 15);

    private final int x;
    private final int width;
}
