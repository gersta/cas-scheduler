package de.gerritstapper.casscheduler.services.persistence;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import de.gerritstapper.casscheduler.daos.FirestoreLectureDao;
import de.gerritstapper.casscheduler.daos.LectureDao;
import de.gerritstapper.casscheduler.util.LectureDaoConverterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Profile("prod")
public class FirestoreLectureDaoPersistenceService implements ILectureDaoPersistenceService {

    private Firestore firestore;

    private final String collection;

    public FirestoreLectureDaoPersistenceService(
            @Value("${google.firebase.firestore.collection}") String collection,
            Firestore firestore
    ) {
        this.collection = collection;
        this.firestore = firestore;
    }

    @Override
    public void saveAll(List<LectureDao> lectures) {
        CollectionReference collection = firestore.collection(this.collection);

        lectures.stream()
                .map(lecture -> LectureDaoConverterService.convert(lecture))
                .forEach(lecture -> {
                    DocumentReference reference = collection.document(lecture.getName() + UUID.randomUUID());

                    Map<String, Object> data = new HashMap<>();
                    data.put("name", lecture.getName());
                    data.put("start", lecture.getStartDate());
                    data.put("end", lecture.getEndDate());
                    data.put("location", lecture.getLocation());

                    ApiFuture<WriteResult> future = reference.set(data);

                    try {
                        System.out.println(String.format("Lecture %s updated in %s ", lecture.getName(), future.get().getUpdateTime()));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
    }
}
