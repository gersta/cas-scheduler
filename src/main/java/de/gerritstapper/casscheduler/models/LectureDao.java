package de.gerritstapper.casscheduler.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureDao {

    String id;
    String name;
    LocalDate startOne;
    LocalDate endOne;
    String placeOne;
    LocalDate startTwo;
    LocalDate endTwo;
    String placeTwo;
}
