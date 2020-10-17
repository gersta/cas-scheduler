package de.gerritstapper.casscheduler.services.persistence;

import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.repositories.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("local")
public class SqlLectureDaoPersistenceService implements ILectureDaoPersistenceService {

    private LectureRepository repository;

    @Autowired
    public SqlLectureDaoPersistenceService(LectureRepository repository) {
        this.repository = repository;
    }

    public void saveAll(List<LectureDao> lectures) {
        this.repository.saveAll(lectures);
    }
}
