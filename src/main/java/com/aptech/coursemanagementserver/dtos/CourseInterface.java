package com.aptech.coursemanagementserver.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public interface CourseInterface {
    public long getId();

    public int getEnrollmentCount();

    public String getName();

    public String getCategoryName();

    public String getDescription();

    public String getSlug();

    public String getImage();

    public long getProgress();

    public String getComment();

    public double getRating();

    public double getPrice();

    public double getNet_price();
}