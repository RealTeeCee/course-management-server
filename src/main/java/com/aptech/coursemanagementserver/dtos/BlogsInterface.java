package com.aptech.coursemanagementserver.dtos;

public interface BlogsInterface {
    public long getId();
    public String getName();
    public String getSlug();
    public int getStatus();
    public String getDescription();
    public int getViewCount();
    public String getImage();
    public long getCategoryId();
    public String getCategoryName();
}
