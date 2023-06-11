package com.aptech.coursemanagementserver.models;

import java.util.Date;

import com.aptech.coursemanagementserver.enums.BlogStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
    @Column(columnDefinition = "varchar(MAX)")
    private String description;
    @Column(columnDefinition = "int")
    private int view_count;
    @Column(columnDefinition = "bigint")
    private long user_id;
    @Column(columnDefinition = "datetime")
    private Date created_at;
    @Column(columnDefinition = "datetime")
    private Date updated_at;

    // Modified - START
    private String image;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Blog_Category"))
    private Category category;
    // Modified - E N D
}
