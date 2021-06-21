package de.gerritstapper.casscheduler.config;

import de.gerritstapper.casscheduler.services.modules.pdf.ModuleDataCleansingService;
import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePagesGroupingService;
import de.gerritstapper.casscheduler.services.modules.pdf.stripping.ModulePdfTextStripper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ModulesPdfConfiguration {

    @Bean
    @Qualifier("technik")
    public ModulePagesGroupingService technikGroupingService(
            @Value("${cas-scheduler.modules.pdf.filename.technik}") String filename
    ) throws IOException {
        return new ModulePagesGroupingService(
                new ModulePdfTextStripper(
                        filename
                ),
                new ModuleDataCleansingService()
        );
    }

    @Bean
    @Qualifier("wirtschaft")
    public ModulePagesGroupingService wirtschaftGroupingService(
            @Value("${cas-scheduler.modules.pdf.filename.wirtschaft}") String filename
    ) throws IOException {
        return new ModulePagesGroupingService(
                new ModulePdfTextStripper(
                        filename
                ),
                new ModuleDataCleansingService()
        );
    }
}
