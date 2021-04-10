package de.gerritstapper.casscheduler.daos;

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

    Long id;
    String lectureCode;
    String name;

    List<BlockDao> blocks;
}
