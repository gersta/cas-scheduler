package de.gerritstapper.casscheduler.services.modules.pdf.stripping;

import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
import de.gerritstapper.casscheduler.services.modules.pdf.ModuleDataCleansingService;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class ModulePagesGroupingService {

    private final ModulePdfTextStripper textStripper;
    private final ModuleDataCleansingService cleansingService;

    public ModulePagesGroupingService(
            ModulePdfTextStripper textStripper,
            ModuleDataCleansingService cleansingService
    ) {
        this.textStripper = textStripper;
        this.cleansingService = cleansingService;
    }

    public Map<String, List<ModulePdfPage>> groupPdfPagesByModule() {
        log.info("groupPdfPagesByModule()");

        List<PDPage> pages = getPages();

        Map<String, List<ModulePdfPage>> pagesPerModule = new HashMap<>();

        for (int i = 0; i < pages.size(); i++) {
            PDPage page = pages.get(i);
            int pageIndexInDocument = i + 1;

            String lectureCode = textStripper.getLectureCodeForPage(pageIndexInDocument);
            String pageContent = getPageContent(pageIndexInDocument);

            pagesPerModule.putIfAbsent(lectureCode, new ArrayList<>());

            ModulePdfPage modulePage = ModulePdfPage.builder()
                    .page(page)
                    .pageIndexInDocument(pageIndexInDocument)
                    .content(pageContent)
                    .build();

            pagesPerModule.get(lectureCode).add(modulePage);
        }

        return pagesPerModule;
    }

    private List<PDPage> getPages() {
        return convertTreeToList(
                textStripper.getPdfPages()
        );
    }

    private List<PDPage> convertTreeToList(PDPageTree tree) {
        List<PDPage> pages = new ArrayList<>();
        tree.iterator().forEachRemaining(pages::add);

        return pages;
    }

    private String getPageContent(int pageIndex) {
        log.debug("getPageContent(): {}", pageIndex);

        return cleansingService.removeGermanUmlaute(
                textStripper.getTextForPage(pageIndex)
        );
    }
}
