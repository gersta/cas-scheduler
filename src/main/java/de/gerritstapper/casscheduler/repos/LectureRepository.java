package de.gerritstapper.casscheduler.repos;

import de.gerritstapper.casscheduler.daos.LectureDao;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("local")
public interface LectureRepository extends JpaRepository<LectureDao, String> {
}
