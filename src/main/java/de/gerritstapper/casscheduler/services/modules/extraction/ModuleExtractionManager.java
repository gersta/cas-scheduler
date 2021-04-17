package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.module.*;
import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.services.modules.extraction.enums.ExtractionPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ModuleExtractionManager {

    private final ModuleGeneralsExtractionService generalsExtractionService;
    private final ModuleFormalitiesExtractionService formalitiesExtractorService;
    private final ModuleLecturingMethodsAndFormsExtractionService methodsAndFormsExtractionService;
    private final ModuleExamExtractionService examExtractionService;
    private final ModuleWorkloadExtractionService workloadExtractionService;
    private final ModuleMetaInfoExtractionService metaInfoExtractionService;

    public ModuleExtractionManager(ModuleGeneralsExtractionService generalsExtractionService, ModuleFormalitiesExtractionService formalitiesExtractorService, ModuleLecturingMethodsAndFormsExtractionService methodsAndFormsExtractionService, ModuleExamExtractionService examExtractionService, ModuleWorkloadExtractionService workloadExtractionService, ModuleMetaInfoExtractionService metaInfoExtractionService) {
        this.generalsExtractionService = generalsExtractionService;
        this.formalitiesExtractorService = formalitiesExtractorService;
        this.methodsAndFormsExtractionService = methodsAndFormsExtractionService;
        this.examExtractionService = examExtractionService;
        this.workloadExtractionService = workloadExtractionService;
        this.metaInfoExtractionService = metaInfoExtractionService;
    }

    public Module extractModuleContent(String moduleTextContent) {
        String[] lines = moduleTextContent.split(ExtractionPatterns.LINEBREAK.getPattern());

        Module module = new Module();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if ( generalsExtractionService.isLectureName(i) ) {
                String lectureName = generalsExtractionService.extractLectureName(lines[i]);

                module.setLectureName(lectureName);
            }

            if ( generalsExtractionService.isLectureNameEnglish(i) ) {
                String lectureNameEnglish = lines[i];

                module.setLectureNameEnglish(lectureNameEnglish);
            }

            if (  formalitiesExtractorService.isFormalitiesHeadline(line) ) {
                String nextLine = lines[i+1];

                FormalitiesInformation formalities = formalitiesExtractorService.extractFormalitiesInformation(nextLine);

                module.setLectureCode(formalities.getLectureCode());
                module.setDuration(formalities.getDuration());
                module.setOwner(formalities.getOwner());
                module.setLanguage(formalities.getLanguage());
            }

            if ( methodsAndFormsExtractionService.isLecturingMethods(line) ) {
                String nextLine = lines[i+1];

                LecturingFormsInformation formsAndMethods = methodsAndFormsExtractionService.extractLecturingFormsAndMethods(nextLine);

                module.setLecturingForms(formsAndMethods.getLecturingForms());
                module.setLecturingMethods(formsAndMethods.getLecturingMethods());
            }

            if ( examExtractionService.isExam(line) ) {
                String nextLine = lines[i+1];

                ExamInfo examInfo = examExtractionService.extractExam(nextLine);

                module.setExam(examInfo.getExam());
                module.setExamDuration(examInfo.getExamDuration());
                module.setExamMarking(examInfo.getExamMarking());
            }

            if ( workloadExtractionService.isWorkload(line) ) {
                String nextLine = lines[i+1];

                WorkloadInfo workload = workloadExtractionService.extractWorkload(nextLine);

                module.setTotalWorkload(workload.getTotalWorkload());
                module.setPresentWorkload(workload.getPresentWorkload());
                module.setSelfStudyWorkload(workload.getSelfStudyWorkload());
                module.setEctsPoints(workload.getEctsPoints());
            }

            if ( metaInfoExtractionService.isMetaInfo(line) ) {
                MetaInformation metaInfo = metaInfoExtractionService.extractMetainformation(line);

                module.setUpdatedOn(metaInfo.getUpdatedOn());
            }
        }

        return module;
    }

}
