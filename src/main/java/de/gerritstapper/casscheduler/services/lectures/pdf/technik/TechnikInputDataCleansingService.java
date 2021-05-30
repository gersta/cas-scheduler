package de.gerritstapper.casscheduler.services.lectures.pdf.technik;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TechnikInputDataCleansingService {

    /**
     * try to cleanse the data from any typos, missing characters and others
     * this makes the pattern matching with regex possible without data loss
     * @param content
     * @return
     */
    public String cleanse(String content) {
        log.trace("cleanse()");

        // in some cases, parentheses surrounding the places are missing/incomplete. Thus remove them entirely for easier pattern matching
        return content.replace("(", "").replace(")", "");
    }
}
