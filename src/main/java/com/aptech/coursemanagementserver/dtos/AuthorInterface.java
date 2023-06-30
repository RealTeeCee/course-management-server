package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

public interface AuthorInterface {
    public long getId();

    public String getName();

    public String getTitle();

    public String getInformation();

    public int getEnrollmentCount();

    public String getImage();

    public Instant getCreated_at();
}
