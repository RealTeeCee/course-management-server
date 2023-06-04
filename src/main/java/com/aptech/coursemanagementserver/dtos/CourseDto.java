package com.aptech.coursemanagementserver.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CourseDto {
    /*
     * {"name":"PHP Core",
     * "duration": 10000,
     * "net_price": 3000,
     * "price": 5000,
     * "tags": "php,my sql",
     * "category_id": 1,
     * "achievements":"ar1,ar2,ar3,ar4",
     * "description": "abc",
     * "image": "abc.jpg",
     * "level": 0,
     * "status": 1}
     */
    private long id;
    private String name;
    private String description;
    private String slug;
    private String image;
    private long progress;
    private String comment;
    private double rating;
    private double price;
    private int enrollmentCount;
    @Builder.Default
    private int level = 0;
    @Builder.Default
    private int status = 1;

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
