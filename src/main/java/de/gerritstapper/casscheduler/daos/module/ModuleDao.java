package de.gerritstapper.casscheduler.daos.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleDao {

    private String lectureCode;
    private String lectureName;
    private String lectureNameEnglish;

    private String owner;
    private int duration;
    private String language;

    private List<String> lecturingForms;
    private List<String> lecturingMethods;

    private String exam;
    private String examDuration;
    private boolean examMarking;

    private int totalWorkload;
    private int presentWorkload;
    private int selfStudyWorkload;
    private int ectsPoints;

    private LocalDate updatedOn;
}
