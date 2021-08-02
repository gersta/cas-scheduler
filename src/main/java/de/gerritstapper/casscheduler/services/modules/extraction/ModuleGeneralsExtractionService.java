package de.gerritstapper.casscheduler.services.modules.extraction;

import de.gerritstapper.casscheduler.models.enums.RegexPatterns;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ModuleGeneralsExtractionService {

    private static final int ENGLISH_NAME_NORMAL = 2;
    private static final int ENGLISH_NAME_MULTILINE = 3;

    public String extractLectureName(String line) {
        log.debug("extractLectureName(): {}", line);

        return line
                .replaceAll(RegexPatterns.LECTURE_CODE.getPattern(), "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .trim();
    }

    /**
     * there are currently 4 scenarios on how the title secion can look like:
     *
     * 1. scenario "normal": Index 1 is the german name incl. lecture code, 2 is english name
     * 3 is the FORMALE ANGABEN header already. Here, the important index is 2 (0 is Aus Aktueller Orga-Einheit)
     *
     * 2. scnario "missing english normal": There is no english name. The german name incl. lecture is in index 1.
     * index 2 is already "FORMALE ANGABEN". Index 3 is starting with "MODULNUMMER"
     *
     * 3. scenario "missing english multiline": Similar to 2. scenario, but now the lecture code is in a separate line
     * from the german name. Index 1 is the german name, 2 the lecture code, 3 FORMALE ANGABEN
     *
     * 4. scenario "multiline": index 1 is german, 2 is lecture code, 3 is english name, 4 is FORMALE ANGABEN
     *
     * @param index
     * @param line
     * @return
     */
    public boolean isLectureNameEnglish(int index, String line) {
        if ( index == ENGLISH_NAME_NORMAL || index == ENGLISH_NAME_MULTILINE ) {
            String lineWithoutParantheses = line
                    .replaceAll("\\(", "")
                    .replaceAll("\\)", "");

            if ( lineWithoutParantheses.matches(RegexPatterns.LECTURE_CODE.getPattern()) ) {
                return false;
            }

            if ( line.equalsIgnoreCase("FORMALE ANGABEN ZUM MODUL") ) {
                return false;
            }

            if ( line.startsWith("MODULNUMMER") ) {
                return false;
            }

            return true;
        }

        return false;
    }


    public String extractLectureNameEnglish(String line) {
        return extractLectureName(line);
    }
}
