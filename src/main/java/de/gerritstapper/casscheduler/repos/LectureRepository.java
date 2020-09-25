package de.gerritstapper.casscheduler.repos;

import de.gerritstapper.casscheduler.daos.LectureDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<LectureDao, String> {
}
