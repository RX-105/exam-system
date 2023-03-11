package io.n0sense.examsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "school_id")
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
    @OneToMany(mappedBy = "school")
    @ToString.Exclude
    private Set<User> userList;

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
