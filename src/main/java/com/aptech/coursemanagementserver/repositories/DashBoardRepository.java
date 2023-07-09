package com.aptech.coursemanagementserver.repositories;

import java.sql.Types;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.aptech.coursemanagementserver.dtos.SummaryDashboardDto;

@Component
public class DashBoardRepository {
    private final SimpleJdbcCall simpleJdbcCall;
    // private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DashBoardRepository(JdbcTemplate jdbcTemplate) {

        // this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

    }

    public SummaryDashboardDto getSummaryDashboard() {
        simpleJdbcCall.withProcedureName("sp_summary_dashboard").declareParameters(
                new SqlInOutParameter("total_user", Types.INTEGER),
                new SqlInOutParameter("today_register", Types.INTEGER),
                new SqlInOutParameter("year_revenue", Types.FLOAT),
                new SqlInOutParameter("month_revenue", Types.FLOAT));
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        Map<String, Object> result = simpleJdbcCall.execute(parameterSource);
        SummaryDashboardDto dashboardDto = new SummaryDashboardDto();
        dashboardDto.setTotalUser((int) result.get("total_user"));
        dashboardDto.setTodayRegister((int) result.get("today_register"));
        dashboardDto.setYearRevenue((double) result.get("year_revenue"));
        dashboardDto.setMonthRevenue((double) result.get("month_revenue"));
        return dashboardDto;
    }

}
