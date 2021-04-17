package de.gerritstapper.casscheduler.services.modules;

import de.gerritstapper.casscheduler.models.module.Module;
import de.gerritstapper.casscheduler.services.modules.pdf.ModuleValidatorService;
import de.gerritstapper.casscheduler.services.modules.pdf.ModulesPdfReaderService;
import de.gerritstapper.casscheduler.util.JsonFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ModuleOrchestratorService {

    private final String MODULES_JSON_FILENAME;

    private final ModulesPdfReaderService modulesPdfReaderService;
    private final ModuleValidatorService validatorService;
    private final JsonFileUtil jsonFileUtil;

    public ModuleOrchestratorService(
            @Value("${cas-scheduler.modules.json.output.filename}") String modules_json_filename,
            ModulesPdfReaderService modulesPdfReaderService,
            ModuleValidatorService validatorService,
            JsonFileUtil jsonFileUtil) {
        MODULES_JSON_FILENAME = modules_json_filename;
        this.modulesPdfReaderService = modulesPdfReaderService;
        this.validatorService = validatorService;
        this.jsonFileUtil = jsonFileUtil;
    }

    public void orchestrate() {
        log.info("orchestrate()");

        List<Module> modules = modulesPdfReaderService.extractModules();

        log.info("Extracted {} modules", modules.size());

        // TODO: move this to the pdf reader as the validation is mandatory and thus should be done automatically
        List<Module> validModules = modules.stream()
                .filter(validatorService::isValidModule)
                .collect(Collectors.toList());

        log.info("Extracted {} valid modules", validModules.size());

        List<Module> invalidModules = modules.stream()
                .filter(module -> !validatorService.isValidModule(module))
                .collect(Collectors.toList());

        log.info("Extracted {} invalid modules", invalidModules.size());

        jsonFileUtil.serializeToFile(validModules, MODULES_JSON_FILENAME);
    }
}
