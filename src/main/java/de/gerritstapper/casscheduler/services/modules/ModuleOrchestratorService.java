package de.gerritstapper.casscheduler.services.modules;

import de.gerritstapper.casscheduler.services.modules.pdf.ModulesPdfReaderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ModuleOrchestratorService {

    private final ModulesPdfReaderService modulesPdfReaderService;

    public ModuleOrchestratorService(ModulesPdfReaderService modulesPdfReaderService) {
        this.modulesPdfReaderService = modulesPdfReaderService;
    }

    public void orchestrate() {
        modulesPdfReaderService.readPdf();
    }
}
