package com.aptech.coursemanagementserver.dtos;

import com.aptech.coursemanagementserver.enums.BlogStatus;

public interface BlogsInterface {
    public long getId();
    public String getName();
    public String getSlug();
    public BlogStatus getStatus();
    public String getDescription();
    public int getViewCount();
    public String getImage();
    public long getCategoryId();
    public String getCategoryName();
}
