package de.gerritstapper.casscheduler.services.modules.extraction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExtractionHeadlines {
    FORMALITIES_HEADLINE("MODULNUMMER VERORTUNG IM STUDIENVERLAUF MODULDAUER (SEMESTER) MODULVERANTWORTUNG SPRACHE"),
    LECTURING_FORMS_METHODS_HEADLINE("LEHRFORMEN LEHRMETHODEN"),
    EXAM_HEADLINE("PRUEFUNGSLEISTUNG PRUEFUNGSUMFANG (IN MINUTEN) BENOTUNG"), // TODO: the Ü was manually converted to UE here to ensure matches
    WORKLOAD_HEADLINE("WORKLOAD INSGESAMT (IN H) DAVON PRAESENZZEIT (IN H) DAVON SELBSTSTUDIUM (IN H) ECTS-LEISTUNGSPUNKTE"),
    METAINFO_HEADLINE("Stand vom");

    @Getter
    private final String headline;
}
