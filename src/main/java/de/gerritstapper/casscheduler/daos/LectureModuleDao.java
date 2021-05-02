package de.gerritstapper.casscheduler.daos;

import de.gerritstapper.casscheduler.daos.lecture.BlockDao;
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
public class LectureModuleDao {

    private boolean moduleAvailable;

    private String lectureCode;
    private String lectureName;
    private String lectureNameEnglish;

    private List<BlockDao> blocks;

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
