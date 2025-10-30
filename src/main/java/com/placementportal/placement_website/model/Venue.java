package com.placementportal.placement_website.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "venue")
public class Venue {

    @Id
    @Column(name = "venue_id")
    private String venueId;

    @Column(name = "venue_location")
    private String venueLocation;

    @Column(name = "no_of_rows")
    private int noOfRows;

    @Column(name = "no_of_cols")
    private int noOfCols;

    @Column(name = "tpr_count")
    private int tprCount;

    // Getters and Setters
    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueLocation() {
        return venueLocation;
    }

    public void setVenueLocation(String venueLocation) {
        this.venueLocation = venueLocation;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public int getNoOfCols() {
        return noOfCols;
    }

    public void setNoOfCols(int noOfCols) {
        this.noOfCols = noOfCols;
    }

    public int getTprCount() {
        return tprCount;
    }

    public void setTprCount(int tprCount) {
        this.tprCount = tprCount;
    }
}
