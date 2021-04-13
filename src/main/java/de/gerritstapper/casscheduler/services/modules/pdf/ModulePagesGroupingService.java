package de.gerritstapper.casscheduler.services.modules.pdf;

import de.gerritstapper.casscheduler.models.module.ModulePdfPage;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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

    public Map<Integer, List<ModulePdfPage>> groupPdfPagesByModule(PDPageTree pageTree) throws IOException {
        log.info("groupPdfPagesByModule(): {}", pageTree);

        List<PDPage> pages = convertTreeToList(pageTree);

        Map<Integer, List<ModulePdfPage>> pagesPerModule = new HashMap<>();
        Integer moduleIndex = 0;

        for (int i = 0; i < pages.size(); i++) {
            PDPage page = pages.get(i);
            int pageIndexInDocument = i + 1;
            String content = textStripper.getTextForPage(pageIndexInDocument);

            if ( fieldExtractorService.isNewModule(content) ) {
                moduleIndex++;
                pagesPerModule.put(moduleIndex, new ArrayList<>());

                if ( isFirstPage(pageIndexInDocument) ) {
                    ModulePdfPage pdfPage = ModulePdfPage.builder()
                            .pageIndexInDocument(pageIndexInDocument)
                            .page(page)
                            .build();

                    pagesPerModule.get(moduleIndex).add(pdfPage);
                }
            } else {
                ModulePdfPage pdfPage = ModulePdfPage.builder()
                        .pageIndexInDocument(pageIndexInDocument)
                        .page(page)
                        .build();

                pagesPerModule.get(moduleIndex).add(pdfPage);
            }
        }

        return pagesPerModule;
    }

    private boolean isFirstPage(int pageIndexInDocument) {
        return pageIndexInDocument == 1;
    }

    private List<PDPage> convertTreeToList(PDPageTree tree) {
        List<PDPage> pages = new ArrayList<>();
        tree.iterator().forEachRemaining(pages::add);

        return pages;
    }
}
