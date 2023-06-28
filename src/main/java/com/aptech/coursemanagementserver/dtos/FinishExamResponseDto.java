package com.aptech.coursemanagementserver.dtos;

import com.aptech.coursemanagementserver.enums.GradeType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinishExamResponseDto {
    private int totalExamTime;
    private double totalPoint;
    private GradeType grade;
}
