package de.gerritstapper.casscheduler.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Blocks")
public class BlockDao {

    @Id @GeneratedValue
    @Column(name="BLOCK_ID")
    private Long id;
    private LocalDate blockStart;
    private LocalDate blockEnd;
    private String location;
    private String filename;

    /*@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LECTURE_ID")
    private LectureDao lecture;*/

}
