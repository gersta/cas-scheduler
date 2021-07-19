package de.gerritstapper.casscheduler.models.module;

import lombok.Builder;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * Wrapper for @{@link org.apache.pdfbox.pdmodel.PDPage}
 */
@Data
@Builder(toBuilder = true)
public class CasPdPage {
    PDPage page;
    String pageContent;
}
