package com.aptech.coursemanagementserver.models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aptech.coursemanagementserver.enums.payment.PaymentType;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;
    @Column(columnDefinition = "nvarchar(100)")
    @EqualsAndHashCode.Include
    private String name;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String image;
    private String description;
    @Column(columnDefinition = "decimal(10,2)")
    private double price;
    @Column(columnDefinition = "decimal(10,2)")
    private double net_price;
    private PaymentType payment = PaymentType.PAYPAL;
    private int duration;
    // @Column(columnDefinition = "bigint")
    // private long category_id;
    @CreationTimestamp
    private Instant created_at = Instant.now();
    @UpdateTimestamp
    private Instant updated_at;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Order_Course"))
    private Course course;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Order_User"))
    private User user;
}
