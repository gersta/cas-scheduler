package de.gerritstapper.casscheduler.models.lecture.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All dimensions are given in points ( = 1/72 inch) as the PdfBox API Reactangle2D.Double works
 * with points and thus these numbers are more precise
 */
@AllArgsConstructor
@Getter
public enum TechnikLecturePdfDimensions {
    ID(40, 30),
    NAME(75, 215),
    FIRST_BLOCK_START(290, 40),
    FIRST_BLOCK_END(320, 50),
    FIRST_BLOCK_LOCATION(352, 30),
    SECOND_BLOCK_START(390, 35),
    SECOND_BLOCK_END(415, 45),
    SECOND_BLOCK_LOCATION(445, 35);

    /*ID(40, 30),
    NAME(75, 215),
    FIRST_BLOCK_START(295, 40),
    FIRST_BLOCK_END(320, 60),
    FIRST_BLOCK_LOCATION(355, 35),
    SECOND_BLOCK_START(390, 40),
    SECOND_BLOCK_END(415, 50),
    SECOND_BLOCK_LOCATION(440, 40);*/

    private final int x;
    private final int width;
}
