package de.gerritstapper.casscheduler.services.modules;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.services.modules.pdf.ModulesPdfReaderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ModuleOrchestratorService {

    private final ModulesPdfReaderService modulesPdfReaderService;

    public ModuleOrchestratorService(ModulesPdfReaderService modulesPdfReaderService) {
        this.modulesPdfReaderService = modulesPdfReaderService;
    }

    public void orchestrate() {
        log.info("orchestrate()");

        List<Module> modules = modulesPdfReaderService.readPdf();

        log.info("Extracted {} modules", modules.size());

        modules.forEach(System.out::println);
    }
}
