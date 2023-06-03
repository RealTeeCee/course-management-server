package com.aptech.coursemanagementserver.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CourseDto {

    private long id;
    private String name;
    private String description;
    private String slug;
    private String image;
    private long progress;
    private String comment;
    private double rating;
    private double price;

    @Builder.Default
    private int level = 0;
    @Builder.Default
    private int status = 1;

    @JsonProperty("sale_price")
    private double net_price;
    private int duration;

    private List<String> sections;

    @JsonProperty("category_id")
    private long category;

    private String category_name;

    @JsonProperty("tags")
    private String tagName;

    @JsonProperty("achievements")
    private String achievementName;

}
