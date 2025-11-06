package com.placementportal.placement_website.dto;

import java.util.List;
import java.util.Map;

public class PlanVenueDTO {
    private String venueId;
    private String venueLocation;
    private int rows;
    private int cols;
    // layout is rows x cols where each cell is seatId or null
    private List<List<String>> layout;
    // mapping seatId -> studentId (or student info)
    private Map<String, String> seatStudentMap;

    public PlanVenueDTO() {}
    // getters & setters
    public String getVenueId() { return venueId; }
    public void setVenueId(String venueId) { this.venueId = venueId; }
    public String getVenueLocation() { return venueLocation; }
    public void setVenueLocation(String venueLocation) { this.venueLocation = venueLocation; }
    public int getRows() { return rows; }
    public void setRows(int rows) { this.rows = rows; }
    public int getCols() { return cols; }
    public void setCols(int cols) { this.cols = cols; }
    public List<List<String>> getLayout() { return layout; }
    public void setLayout(List<List<String>> layout) { this.layout = layout; }
    public Map<String, String> getSeatStudentMap() { return seatStudentMap; }
    public void setSeatStudentMap(Map<String, String> seatStudentMap) { this.seatStudentMap = seatStudentMap; }
}
