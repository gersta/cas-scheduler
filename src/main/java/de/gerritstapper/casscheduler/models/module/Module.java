package de.gerritstapper.casscheduler.models.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    private String lectureCode; // MODULNUMMER
    private String lectureName;
    private String lectureNameEnglish;

    // FORMALITIES
    private String owner; // MODULVERANTWORTUNG
    private String duration; // MODULDAUER
    private String language;

    // EXAML
    private String exam;
    private String examDuration; // PRUEFUNGSUMFANG
    private String examMarking; // BENOTUNG

    // WORKLOAD
    private String totalWorkload;
    private String presentWorkload;
    private String selfStudyWorkload;
    private String ectsPoints;

    // Metainfo
    private String updatedOn; // STAND VOM

    private String specifics; // BESONDERHEITEN
}
