package io.n0sense.examsystem.entity;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    private Long schoolId;
    private Integer applicantCount;
    private Integer enrollmentCount;
    private BigDecimal acceptScore;
    private Integer admissionCount;
    @OneToMany(mappedBy = "major")
    @ToString.Exclude
    private Set<User> users;

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
