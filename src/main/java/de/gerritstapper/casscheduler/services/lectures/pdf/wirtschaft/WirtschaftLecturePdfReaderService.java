package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.services.lectures.pdf.AbstractLecturePdfReaderService;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.IOException;
import java.util.List;

@Log4j2
public class WirtschaftLecturePdfReaderService extends AbstractLecturePdfReaderService {

    private final PDFTextStripperByArea stripper;

    public WirtschaftLecturePdfReaderService(String filename) throws IOException {
        super(filename);

        this.stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
    }

    @Override
    protected List<Lecture> processPage(PDPage page) {
        return null;
    }
}
