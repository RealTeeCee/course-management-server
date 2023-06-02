package com.aptech.coursemanagementserver.models;

import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;
    @Column(columnDefinition = "bigint")
    private long user_id;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String bio;
    @Column(columnDefinition = "date")
    @Past(message = "Birth date should be in the past")
    private LocalDate birth_date;
    private String image;
    @Column(columnDefinition = "varchar(12)", unique = true)
    private String phone;

    @CreationTimestamp
    private Instant created_at = Instant.now();
    @UpdateTimestamp
    private Instant updated_at;
}
