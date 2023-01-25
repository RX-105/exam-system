package io.n0sense.examsystem.entity;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    private Integer applicantCount;
    private Integer enrollmentCount;
    private Double acceptScore;
    private Integer admissionCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Major major = (Major) o;
        return id != null && Objects.equals(id, major.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}