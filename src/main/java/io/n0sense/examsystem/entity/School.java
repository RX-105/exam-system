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
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long schoolId;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    private String address;
    private Long zipcode;
    private String contactNumber;
    private String examName;
    private String recruitYears;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        School school = (School) o;
        return schoolId != null && Objects.equals(schoolId, school.schoolId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
