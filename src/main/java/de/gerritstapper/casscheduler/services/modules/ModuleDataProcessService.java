package de.gerritstapper.casscheduler.services.modules;

import de.gerritstapper.casscheduler.daos.module.ModuleDao;
import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ModuleDataProcessService {

    private final DateTimeFormatter formatter;

    public ModuleDataProcessService(
            @Value("${cas-scheduler.modules.pdf.date-pattern}") String dateFormat
    ) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    public ModuleDao create(Module module) {
        return ModuleDao.builder()
                .lectureCode(module.getLectureCode())
                .lectureName(module.getLectureName())
                .lectureNameEnglish(module.getLectureNameEnglish())
                .owner(module.getOwner())
                .duration(getDuration(module))
                .language(module.getLanguage())
                .lecturingForms(getLecturingForms(module))
                .lecturingMethods(getLecturingMethods(module))
                .exam(module.getExam())
                .examDuration(module.getExamDuration())
                .examMarking(getExamMarking(module))
                .totalWorkload(getTotalWorkload(module))
                .presentWorkload(getPresentWorkload(module))
                .selfStudyWorkload(getSelfStudyWorkload(module))
                .ectsPoints(getEctsPoints(module))
                .updatedOn(getUpdatedOn(module))
                .build();
    }

    private int getDuration(Module module) {
        return Integer.parseInt(module.getDuration());
    }

    private List<String> getLecturingForms(Module module) {
        return splitStringToList(module.getLecturingForms(), ExtractionPatterns.COMMA);
    }

    private List<String> getLecturingMethods(Module module) {
        return splitStringToList(module.getLecturingMethods(), ExtractionPatterns.COMMA);
    }

    private List<String> splitStringToList(String input, ExtractionPatterns delimiter) {
        return Arrays.stream(input.split(delimiter.getPattern()))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private boolean getExamMarking(Module module) {
        String examMarking = module.getExamMarking();

        if ( isYes(examMarking) ) {
            return true;
        } else {
          return false;
        }
    }

    private boolean isYes(String marking) {
        return marking.equalsIgnoreCase("ja");
    }

    private int getTotalWorkload(Module module) {
        return Integer.parseInt(module.getTotalWorkload());
    }

    private int getPresentWorkload(Module module) {
        return Integer.parseInt(module.getPresentWorkload());
    }

    private int getSelfStudyWorkload(Module module) {
        return Integer.parseInt(module.getSelfStudyWorkload());
    }

    private int getEctsPoints(Module module) {
        return Integer.parseInt(module.getEctsPoints());
    }

    private LocalDate getUpdatedOn(Module module) {
        String updatedOn = module.getUpdatedOn();

        return LocalDate.parse(updatedOn, formatter);
    }

}
