package com.global.hr.models.DTO;

import java.time.LocalDate;
import java.util.Map;

public class DashboardResponse {
    private String userName;
    private int totalCourses;
    private int totalHours;
    private int recentHours;
    private Map<LocalDate, Integer> last3Days;

    public DashboardResponse(String userName, int totalCourses, int totalHours, 
                            int recentHours, Map<LocalDate, Integer> last3Days) {
        this.userName = userName;
        this.totalCourses = totalCourses;
        this.totalHours = totalHours;
        this.recentHours = recentHours;
        this.last3Days = last3Days;
    }

    // Getters
    public String getUserName() { return userName; }
    public int getTotalCourses() { return totalCourses; }
    public int getTotalHours() { return totalHours; }
    public int getRecentHours() { return recentHours; }
    public Map<LocalDate, Integer> getLast3Days() { return last3Days; }
}