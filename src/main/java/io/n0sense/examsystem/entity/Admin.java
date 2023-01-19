package io.n0sense.examsystem.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin")
@DynamicInsert
public class Admin extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long adminId;
    @Column(nullable = false)
    private String name;
    private String password;
    private String groupName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Admin admin = (Admin) o;
        return adminId != null && Objects.equals(adminId, admin.adminId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static AdminBuilder build(){
        return new AdminBuilder();
    }
}
