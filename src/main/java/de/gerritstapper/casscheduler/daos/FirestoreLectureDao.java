package de.gerritstapper.casscheduler.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * Firestore cannat serialize LocatDate getChrono due to reflection restrictions
 * thus, the dao must be transformed to remove the LocalDate
 */
public class FirestoreLectureDao {

    String id;
    String name;
    String startDate;
    String endDate;
    String location;
}
