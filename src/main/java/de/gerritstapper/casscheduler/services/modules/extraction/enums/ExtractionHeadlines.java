package de.gerritstapper.casscheduler.services.modules.extraction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExtractionHeadlines {
    FORMALITIES_HEADLINE("MODULNUMMER VERORTUNG IM STUDIENVERLAUF"),
    LECTURING_FORMS_METHODS_HEADLINE("LEHRFORMEN LEHRMETHODEN"),
    EXAM_HEADLINE("PRUEFUNGSLEISTUNG"), // TODO: the Ü was manually converted to UE here to ensure matches
    WORKLOAD_HEADLINE("WORKLOAD INSGESAMT"),
    METAINFO_HEADLINE("Stand vom");

    @Getter
    private final String headline;
}
