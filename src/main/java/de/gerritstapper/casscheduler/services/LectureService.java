package de.gerritstapper.casscheduler.services;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.repos.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {

    private LectureRepository repository;

    @Autowired
    public LectureService(LectureRepository repository) {
        this.repository = repository;
    }

    public void saveAll(List<LectureDao> lectures) {
        this.repository.saveAll(lectures);
    }
}
