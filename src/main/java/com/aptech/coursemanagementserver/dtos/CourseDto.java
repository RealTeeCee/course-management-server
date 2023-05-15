package com.aptech.coursemanagementserver.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CourseDto {
    private String name;
    private String description;
    private String slug;
    private String image;
    private double price;
    private double net_price;
    private int duration;

    private List<String> sections;

    private long category;

    private String tagName;

    private String achievementName;

}
