package com.aptech.coursemanagementserver.dtos;

public interface CourseInterface {
    public long getId();

    public int getEnrollmentCount();

    public String getName();

    public String getDuration();

    public int getStatus();

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