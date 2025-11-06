package com.placementportal.placement_website.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SittingPlanEntry {
    private String studentId;
    private String applicationId;
    private String room;
    private int seatNumber;
}
