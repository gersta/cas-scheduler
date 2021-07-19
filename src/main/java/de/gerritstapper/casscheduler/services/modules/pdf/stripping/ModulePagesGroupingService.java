package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.models.enums.RegexPatterns;
import de.gerritstapper.casscheduler.models.module.CasPdPage;
import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
import de.gerritstapper.casscheduler.models.module.enums.ModuleRegexPattern;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class ModulePagesGroupingService {

    private final ModulePdfTextStripper textStripper;

    public ModulePagesGroupingService(
            ModulePdfTextStripper textStripper
    ) {
        this.textStripper = textStripper;
    }

    public Map<String, List<ModulePdfPage>> groupPdfPagesByModule() {
        log.info("groupPdfPagesByModule()");

        List<CasPdPage> pages = textStripper.getPdfPages();

        Map<String, List<ModulePdfPage>> pagesPerModule = new HashMap<>();

        for (CasPdPage casPage : pages) {
            PDPage pdPage = casPage.getPage();

            String lectureCode = getLectureCodeForPage(casPage.getPageContent());

            pagesPerModule.putIfAbsent(lectureCode, new ArrayList<>());

            ModulePdfPage modulePage = ModulePdfPage.builder()
                    .page(pdPage)
                    .content(casPage.getPageContent())
                    .build();

            pagesPerModule.get(lectureCode).add(modulePage);
        }

        return pagesPerModule;
    }

    private String getLectureCodeForPage(String pageContent) {
        String lastLine = getLastLineOfContent(pageContent);

        String lectureCodePattern = String.format("(%s|%s)", RegexPatterns.LECTURE_CODE.getPattern(), ModuleRegexPattern.MASTER_THESIS.getPattern());

        Pattern pattern = Pattern.compile(lectureCodePattern);

        Matcher matcher = pattern.matcher(lastLine);

        if ( matcher.find() ) {
            String lectureCode = matcher.group();
            return lectureCode;
        }

        return "";
    }

    private String getLastLineOfContent(String content) {
        String[] lines = content.split(RegexPatterns.LINE_BREAK.getPattern());

        return lines[lines.length-1];
    }
}
