package de.gerritstapper.casscheduler.daos.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureDao {

    String lectureCode;
    String name;

    List<String> additionalInformation;

    List<BlockDao> blocks;
}
