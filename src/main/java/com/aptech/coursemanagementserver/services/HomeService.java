package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.SearchDto;
import com.aptech.coursemanagementserver.dtos.SummaryDashboardDto;

public interface HomeService {
    public List<SearchDto> searchAll(String name);

    public SummaryDashboardDto getSummaryDashboard();
}
