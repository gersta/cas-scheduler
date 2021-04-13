package de.gerritstapper.casscheduler.models.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamInfo {

    private String exam;
    private String examDuration; // PRUEFUNGSUMFANG
    private String examMarking; // BENOTUNG
}
