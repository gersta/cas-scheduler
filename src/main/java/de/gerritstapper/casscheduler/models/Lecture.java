package de.gerritstapper.casscheduler.models;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lecture {
    
    String lectureCode;
    String name;
    String startOne;
    String endOne;
    String locationOne;
    String startTwo;
    String endTwo;
    String locationTwo;

    @EqualsAndHashCode.Exclude
    Double coordinate;
}