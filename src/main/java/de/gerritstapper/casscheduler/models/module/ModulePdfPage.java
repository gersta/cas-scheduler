package de.gerritstapper.casscheduler.models.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDPage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModulePdfPage {

    private int pageIndexInDocument;
    private PDPage page;
    private String content;
}
