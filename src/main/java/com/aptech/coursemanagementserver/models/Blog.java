package com.aptech.coursemanagementserver.models;

import java.time.LocalDateTime;

import com.aptech.coursemanagementserver.enums.BlogStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;
    @Column(columnDefinition = "varchar(100)")
    private String name;
    @Column(unique = true, columnDefinition = "varchar(255)")
    private String slug;
    @Column(columnDefinition = "tinyint default 2")
    private BlogStatus status;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "int")
    private int view_count;
    @Column(columnDefinition = "bigint")
    private long user_id;
    @Column(columnDefinition = "datetime")
    private LocalDateTime created_at;
    @Column(columnDefinition = "datetime")
    private LocalDateTime updated_at;
}
