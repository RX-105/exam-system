package io.n0sense.examsystem.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visits")
public class Visits {
    @Id
    private LocalDate time;
    private Long count;
}
