package de.gerritstapper.casscheduler.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lecture {
    
    String id;
    String name;
    String startOne;
    String endOne;
    String placeOne;
    String startTwo;
    String endTwo;
    String placeTwo;

    @EqualsAndHashCode.Exclude
    Double coordinate;
}