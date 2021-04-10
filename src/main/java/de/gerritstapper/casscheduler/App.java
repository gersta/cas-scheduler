package de.gerritstapper.casscheduler;

import de.gerritstapper.casscheduler.services.lectures.LectureOrchestratorService;
import de.gerritstapper.casscheduler.services.modules.ModuleOrchestratorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@Log4j2
public class App implements ApplicationRunner {

    private final boolean doExtractLectures;
    private final boolean doExtractModules;

    private final LectureOrchestratorService lectureOrchestratorService;
    private final ModuleOrchestratorService moduleOrchestratorService;

    @Autowired
    public App(
            @Value("${cas-scheduler.lectures.extract}") final boolean doExtractLectures,
            @Value("${cas-scheduler.modules.extract}") final boolean doExtractModules,
            LectureOrchestratorService lectureOrchestratorService,
            ModuleOrchestratorService moduleOrchestratorService) {
        this.doExtractLectures = doExtractLectures;
        this.doExtractModules = doExtractModules;
        this.lectureOrchestratorService = lectureOrchestratorService;
        this.moduleOrchestratorService = moduleOrchestratorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments arguments) throws IOException {
        log.info("Starting CAS Scheduler");

        log.info("Extract lectures: {}", doExtractLectures);
        log.info("Extract modules: {}", doExtractModules);

        if ( doExtractLectures ) {
            log.info("Extracting lectures");

            lectureOrchestratorService.orchestrate();
        }

        if ( doExtractModules ) {
            log.info("Extracting modules");

            moduleOrchestratorService.orchestrate();
        }
    }
}
