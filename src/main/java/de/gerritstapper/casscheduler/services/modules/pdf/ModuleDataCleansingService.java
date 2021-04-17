package de.gerritstapper.casscheduler.services.modules.pdf;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ModuleDataCleansingService {

    public String removeGermanUmlaute(String content) {
        return content
                .replaceAll("Ö", "Oe")
                .replaceAll("ö", "oe")
                .replaceAll("Ü", "Ue")
                .replaceAll("ü", "ue")
                .replaceAll("Ä", "Ae")
                .replaceAll("ä", "ae")
                .replaceAll("ß", "ss");
    }
}
