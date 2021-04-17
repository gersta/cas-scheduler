package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.WorkloadInfo;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionHeadlines.WORKLOAD_HEADLINE;

@Service
@Log4j2
public class ModuleWorkloadExtractionService implements IExtractionHelper {

    public boolean isWorkload(String line) {
        return matchesHeadlineLowercase(line, WORKLOAD_HEADLINE);
    }

    public WorkloadInfo extractWorkload(String workloadLine) {
        log.debug("extractWorkload(): {}", workloadLine);

        String[] workload = workloadLine.split(ExtractionPatterns.WHITESPACE.getPattern());

        String totalWorkload = workload[0];
        String presentWorkload = workload[1];
        String selfStudyWorkload = workload[2];
        String ectsPoints = workload[3];

        return WorkloadInfo.builder()
                .totalWorkload(totalWorkload)
                .presentWorkload(presentWorkload)
                .selfStudyWorkload(selfStudyWorkload)
                .ectsPoints(ectsPoints)
                .build();
    }
}
