package de.gerritstapper.casscheduler.services.merging;

import de.gerritstapper.casscheduler.daos.LectureModuleDao;
import de.gerritstapper.casscheduler.daos.lecture.LectureDao;
import de.gerritstapper.casscheduler.daos.module.ModuleDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LectureModuleMergerService {

    public List<LectureModuleDao> mergeLecturesWithModules(List<LectureDao> lectures, List<ModuleDao> modules) {
        log.info("Merging lectures and modules: Finding matching module for each lecture");

        return lectures.stream()
                .map(lecture -> mapToModule(lecture, modules))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private LectureModuleDao mapToModule(LectureDao lecture, List<ModuleDao> modules) {
        Optional<ModuleDao> associatedModule = findMatchingModule(lecture, modules);

        if ( associatedModule.isPresent() ) {
            log.debug("Found matching module for lecture code {}: {}", lecture.getLectureCode(), lecture.getName());

            ModuleDao module = associatedModule.get();

            return LectureModuleDao.builder()
                    .moduleAvailable(true)
                    .lectureCode(module.getLectureCode())
                    .lectureName(module.getLectureName())
                    .lectureNameEnglish(module.getLectureNameEnglish())
                    .blocks(lecture.getBlocks())
                    .owner(module.getOwner())
                    .duration(module.getDuration())
                    .language(module.getLanguage())
                    .lecturingForms(module.getLecturingForms())
                    .lecturingMethods(module.getLecturingMethods())
                    .exam(module.getExam())
                    .examDuration(module.getExamDuration())
                    .examMarking(module.isExamMarking())
                    .totalWorkload(module.getTotalWorkload())
                    .presentWorkload(module.getPresentWorkload())
                    .selfStudyWorkload(module.getSelfStudyWorkload())
                    .ectsPoints(module.getEctsPoints())
                    .updatedOn(module.getUpdatedOn())
                    .build();
        }

        log.error("Found no matching module for lecture code {}: {}", lecture.getLectureCode(), lecture.getName());

        return LectureModuleDao.builder()
                .moduleAvailable(false)
                .lectureCode(lecture.getLectureCode())
                .lectureName(lecture.getName())
                .blocks(lecture.getBlocks())
                .build();
    }

    private Optional<ModuleDao> findMatchingModule(LectureDao lecture, List<ModuleDao> modules) {
        return modules.stream()
                .filter(module -> lecture.getLectureCode().equalsIgnoreCase(module.getLectureCode()))
                .findFirst();
    }
}
