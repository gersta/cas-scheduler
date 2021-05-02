package de.gerritstapper.casscheduler.services.merging;

import de.gerritstapper.casscheduler.daos.LectureModuleDao;
import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.daos.module.ModuleDao;
import de.gerritstapper.casscheduler.util.JsonFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class LectureModuleMergeOrchestratorService {

    private final String LECTURE_MODULE_JSON_FILENAME;

    private final LectureModuleMergerService lectureModuleMergerService;
    private final JsonFileUtil jsonFileUtil;

    public LectureModuleMergeOrchestratorService(
            @Value("${cas-scheduler.lecture-modules.json.output}") final String lectureModulesOutputPath,
            LectureModuleMergerService lectureModuleMergerService,
            JsonFileUtil jsonFileUtil
    ) {
        LECTURE_MODULE_JSON_FILENAME = lectureModulesOutputPath;

        this.lectureModuleMergerService = lectureModuleMergerService;
        this.jsonFileUtil = jsonFileUtil;
    }

    public List<LectureModuleDao> orchestrate(List<LectureDao> lectures, List<ModuleDao> modules) {
        log.info("Orchestrate()");

        List<LectureModuleDao> lectureModules = lectureModuleMergerService.mergeLecturesWithModules(lectures, modules);

        log.info("Created {} merged lectureModules", lectureModules.size());

        jsonFileUtil.serializeToFile(lectureModules, LECTURE_MODULE_JSON_FILENAME);

        log.info("Written {} lectureModules to file: {}", lectureModules.size(), LECTURE_MODULE_JSON_FILENAME);

        return lectureModules;
    }
}
