package io.n0sense.examsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long userId;
    private Long tableId;
    @Column(nullable = false)
    private String name;
    private String realname;
    private Long identityId;
    private String gender;
    private String politicalStatus;
    private Long phone;
    private String source;
    private String graduateSchool;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime graduateTime;
    private Boolean isCurrent;
    private Boolean isScience;
    private String major;
    private String targetMajor;
    private Integer cet4;
    private Integer cet6;
    private String homeAddress;
    private String nationality;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime birthday;
    private String avatarName;
    private Long roomId;
    private Long seatId;
    private Long admissionId;
    private Boolean isConfirmed;
    private String receiver;
    private String contactAddress;
    private Long zipcode;
    private String contactNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return userId != null && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
