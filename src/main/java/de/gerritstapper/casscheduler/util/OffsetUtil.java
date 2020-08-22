package de.gerritstapper.casscheduler.util;

public class OffsetUtil {

    /**
     *
     * @param step: the height of each step between the lines (mm)
     * @param topSpacing: the space from the top of the page to the first entry of the table (mm)
     * @return: the offset to take from the top of the page to start scanning from (mm)
     */
    public double getOffset(double step, double topSpacing) {
        return (topSpacing % step);
    }
}
