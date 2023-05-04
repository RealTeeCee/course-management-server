package com.aptech.coursemanagementserver.models;

import java.time.Instant;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(indexes = {
        @Index(name = "IDX_Course_Name", columnList = "name Desc"),
        @Index(name = "IDX_Course_price", columnList = "price Asc") })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;
    @Column(columnDefinition = "varchar(100)")
    @EqualsAndHashCode.Include
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    private String image;
    @Column(columnDefinition = "decimal(10,2)")
    private double price;
    @Column(columnDefinition = "decimal(10,2)")
    private double net_price;
    @Column(columnDefinition = "datetime")
    @NaturalId
    private LocalTime duration;
    // @Column(columnDefinition = "bigint")
    // private long category_id;

    @CreationTimestamp
    private Instant created_at;
    @UpdateTimestamp
    private Instant updated_at;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    private Set<Section> sections = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    private Set<Enrollment> enrollments = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Course_Category"))
    private Category category;

}
