package de.gerritstapper.casscheduler.daos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Lectures")
public class LectureDao {

    @Id @GeneratedValue
    String id;
    String lectureCode;
    String name;
    LocalDate firstBlockStart;
    LocalDate firstBlockEnd;
    String firstBlockLocation;
    LocalDate secondBlockStart;
    LocalDate secondBlockEnd;
    String secondBlockLocation;
}
