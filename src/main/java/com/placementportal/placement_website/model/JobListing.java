package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_listings")
public class JobListing {

    @Id
    @Column(name = "listing_id")
    private String listingId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "ctc")
    private BigDecimal ctc;

    @Column(name = "eligibility_criteria", columnDefinition = "json")
    private String eligibilityCriteria;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "mode_of_hiring")
    private String modeOfHiring;

    // Getters and setters
    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public BigDecimal getCtc() {
        return ctc;
    }

    public void setCtc(BigDecimal ctc) {
        this.ctc = ctc;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    public void setEligibilityCriteria(String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getModeOfHiring() {
        return modeOfHiring;
    }

    public void setModeOfHiring(String modeOfHiring) {
        this.modeOfHiring = modeOfHiring;
    }
}
