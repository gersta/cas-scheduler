package de.gerritstapper.casscheduler.services.lectures.pdf.wirtschaft;

import de.gerritstapper.casscheduler.models.lecture.Lecture;
import de.gerritstapper.casscheduler.models.lecture.enums.Faculty;
import de.gerritstapper.casscheduler.services.lectures.pdf.AbstractLecturePdfReaderService;
import de.gerritstapper.casscheduler.services.lectures.pdf.CasLecturePdfTextStripper;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class WirtschaftLecturePdfReaderService extends AbstractLecturePdfReaderService {

    private final CasLecturePdfTextStripper pdfTextStripper;
    private final WirtschaftLectureValidationService validationService;

    public WirtschaftLecturePdfReaderService(
            CasLecturePdfTextStripper pdfTextStripper,
            WirtschaftLectureValidationService validationService,
            String filename
    ) throws IOException {
        super(filename);
        this.pdfTextStripper = pdfTextStripper;
        this.validationService = validationService;
    }

    @Override
    protected List<Lecture> processPage(PDPage page) {
        return pdfTextStripper.stripLectures(page, Faculty.WIRTSCHAFT).stream()
                .filter(validationService::isValid)
                .collect(Collectors.toList());
    }
}
