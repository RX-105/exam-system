package io.n0sense.examsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
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
    private String identityId;
    private String gender;
    private String politicalStatus;
    private Long phone;
    private String source;
    private String graduateSchool;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduateTime;
    private Boolean isCurrent; // 以true表示应届，false往届
    private Boolean isScience; // 以true表示理科，false文科
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    @ToString.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private School school;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major", nullable = false)
    @ToString.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Major major;
    private String englishLevel;
    private String homeAddress;
    private String nationality;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
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
