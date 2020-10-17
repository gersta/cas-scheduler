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