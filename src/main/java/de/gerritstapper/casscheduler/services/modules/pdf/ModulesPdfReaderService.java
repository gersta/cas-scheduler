package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.models.module.*;
import de.gerritstapper.casscheduler.services.modules.extraction.ModuleExtractionManager;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ModulesPdfReaderService {

    private final ModulePagesGroupingService groupingService;
    private final ModulePdfTextStripper textStripper;
    private final ModuleDataCleansingService cleansingService;
    private final ModuleExtractionManager extractionManager;

    public ModulesPdfReaderService(
            ModulePagesGroupingService groupingService,
            ModulePdfTextStripper textStripper,
            ModuleDataCleansingService cleansingService, ModuleExtractionManager extractionManager)  {
        this.groupingService = groupingService;
        this.textStripper = textStripper;
        this.cleansingService = cleansingService;
        this.extractionManager = extractionManager;
    }


    public List<Module> extractModules() {
        log.info("readPdf()");

        try {
            PDPageTree pages = textStripper.getPdfPages();

            return groupingService.groupPdfPagesByModule(pages)
                    .entrySet().stream()
                    .map(module -> processModule(module.getKey(), module.getValue()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error in readPdf ");
            e.printStackTrace();

            return Collections.emptyList();
        }

    }

    private Module processModule(String lectureCode, List<ModulePdfPage> modulePdfPages) {
        log.debug("processModule(): {}", lectureCode);

        String moduleTextContent = modulePdfPages.stream()
                .map(page -> textStripper.getTextForPage(page.getPageIndexInDocument()))
                .map(cleansingService::removeGermanUmlaute)
                .collect(Collectors.joining());

        return extractModuleContent(moduleTextContent);
    }



    @SneakyThrows // TODO: remove this
    private Module extractModuleContent(String moduleTextContent) {
        return extractionManager.extractModuleContent(moduleTextContent);
    }
}
