package de.gerritstapper.casscheduler.services.persistence;

import de.gerritstapper.casscheduler.daos.LectureDao;

import java.util.List;

public interface ILectureDaoPersistenceService {

    void saveAll(List<LectureDao> lectures);
}
