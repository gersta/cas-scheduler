package de.gerritstapper.casscheduler.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Lectures")
public class LectureDao {

    @Id @GeneratedValue
    @Column(name = "LECTURE_ID")
    Long id;
    String lectureCode;
    String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<BlockDao> blocks;
}
