package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.models.module.*;
import de.gerritstapper.casscheduler.services.modules.extraction.ModuleExtractionManager;
import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePagesGroupingService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class ModulesPdfReaderService {

    private final ModulePagesGroupingService technikGroupingService;
    private final ModulePagesGroupingService wirtschaftGroupingService;
    private final ModuleExtractionManager extractionManager;

    public ModulesPdfReaderService(
            @Qualifier("technik") ModulePagesGroupingService technikGroupingService,
            @Qualifier("wirtschaft") ModulePagesGroupingService wirtschaftGroupingService,
            ModuleExtractionManager extractionManager
    )  {
        this.technikGroupingService = technikGroupingService;
        this.wirtschaftGroupingService = wirtschaftGroupingService;
        this.extractionManager = extractionManager;
    }


    public List<Module> extractModules() {
        log.info("extractModules()");

        try {
            return Stream.of(
                    technikGroupingService,
                    wirtschaftGroupingService
            )
                    .parallel()
                    .map(ModulePagesGroupingService::groupPdfPagesByModule)
                    .flatMap(groupedModulesByFaculty -> groupedModulesByFaculty.entrySet().stream())
                    .map(moduleGroup -> extractModuleContent(moduleGroup.getValue()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error in extractModules()");
            e.printStackTrace();

            return Collections.emptyList();
        }

    }


    @SneakyThrows // TODO: remove this
    private Module extractModuleContent(List<ModulePdfPage> pagesPerModule) {
        String moduleContent = pagesPerModule.stream()
                .map(ModulePdfPage::getContent)
                .collect(Collectors.joining());

        return extractionManager.extractModuleContent(moduleContent);
    }
}
