package de.gerritstapper.casscheduler.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockDao {

    private Long id;
    private LocalDate blockStart;
    private LocalDate blockEnd;
    private String location;
    private String filename;
}
