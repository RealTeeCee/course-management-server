package com.aptech.coursemanagementserver.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    private long id;
    private String name;
    private String description;
    private String slug;
    private String image;
    private double price;

    @JsonProperty("sale_price")
    private double net_price;
    private int duration;

    private List<String> sections;

    @JsonProperty("category_id")
    private long category;

    private String category_name;

    @JsonProperty("tags")
    private String tagName;

    @JsonProperty("archivements")
    private String achievementName;

}
