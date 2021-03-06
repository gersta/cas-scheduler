package de.gerritstapper.casscheduler.services.pdf;

import org.springframework.stereotype.Service;

@Service
public class InputDataCleansingService {

    /**
     * try to cleanse the data from any typos, missing characters and others
     * this makes the pattern matching with regex possible without data loss
     * @param content
     * @return
     */
    public String cleanse(String content) {
        // in some cases, parentheses surrounding the places are missing/incomplete. Thus remove them entirely for easier pattern matching
        return content.replace("(", "").replace(")", "");
    }
}
