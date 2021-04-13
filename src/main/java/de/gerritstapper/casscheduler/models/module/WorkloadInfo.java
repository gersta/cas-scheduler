package de.gerritstapper.casscheduler.models.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkloadInfo {

    private String totalWorkload;
    private String presentWorkload;
    private String selfStudyWorkload;
    private String ectsPoints;
}
