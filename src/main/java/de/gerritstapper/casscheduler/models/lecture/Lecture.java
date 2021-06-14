package de.gerritstapper.casscheduler.models.lecture;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Lecture {
    
    String lectureCode;
    String name;
    String firstBlockStart;
    String firstBlockEnd;
    String firstBlockLocation;
    String secondBlockStart;
    String secondBlockEnd;
    String secondBlockLocation;

    List<String> additionalInformation;

    @EqualsAndHashCode.Exclude
    Double coordinate;
}