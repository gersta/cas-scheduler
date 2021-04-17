package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ModulePagesGroupingService {

    private final ModulePdfTextStripper textStripper;
    private final ModulesFieldExtractorService fieldExtractorService;

    public ModulePagesGroupingService(
            ModulePdfTextStripper textStripper,
            ModulesFieldExtractorService fieldExtractorService
    ) {
        this.textStripper = textStripper;
        this.fieldExtractorService = fieldExtractorService;
    }

    public Map<String, List<ModulePdfPage>> groupPdfPagesByModule(PDPageTree pageTree) {
        log.info("groupPdfPagesByModule(): {}", pageTree);

        List<PDPage> pages = convertTreeToList(pageTree);

        Map<String, List<ModulePdfPage>> pagesPerModule = new HashMap<>();

        for (int i = 0; i < pages.size(); i++) {
            PDPage page = pages.get(i);
            int pageIndexInDocument = i + 1;

            String lectureCode = textStripper.getLectureCodeForPage(pageIndexInDocument);

            pagesPerModule.putIfAbsent(lectureCode, new ArrayList<>());

            ModulePdfPage modulePage = ModulePdfPage.builder()
                    .page(page)
                    .pageIndexInDocument(pageIndexInDocument)
                    .build();

            pagesPerModule.get(lectureCode).add(modulePage);
        }

        return pagesPerModule;
    }

    private List<PDPage> convertTreeToList(PDPageTree tree) {
        List<PDPage> pages = new ArrayList<>();
        tree.iterator().forEachRemaining(pages::add);

        return pages;
    }
}
