package de.gerritstapper.casscheduler.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    private String lectureCode;
    private String lectureName;
    private String lectureNameEnglish;

    // FORMALITIES
    private String lecturer;
    private String duration;

    // EXAML
    private String exam;
    private String examDuration;
    private String examMarking;

    // WORKLOAD
    private String totalWorkload;
    private String presentWorkload;
    private String selfStudyWorkload;
    private String ectsPoints;

    // Metainfo
    private String updatedOn;
}
