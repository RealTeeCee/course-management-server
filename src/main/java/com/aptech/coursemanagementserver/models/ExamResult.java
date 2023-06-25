package com.aptech.coursemanagementserver.models;

import com.aptech.coursemanagementserver.enums.GradeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;

    @Column(columnDefinition = "FLOAT DEFAULT(0)")
    private double totalPoint;

    private GradeType grade;

    @Column(columnDefinition = "INTEGER DEFAULT(0)")
    private int totalExamTime;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String questionDescription;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String anwserDescription;

    @Column(columnDefinition = "FLOAT DEFAULT(0)")
    private double questionPoint;

    @Column(columnDefinition = "bit DEFAULT(0)")
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ExamResult_User"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ExamResult_Part"))
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ExamResult_Question"))
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ExamResult_Answer"))
    private Answer answer;

    @Column(columnDefinition = "bigint DEFAULT(0)")
    private long userAnswerId;
}
