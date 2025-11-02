// package com.placementportal.placement_website.model;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;
// import java.util.Map;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;

// @Entity
// @Table(name = "job_listings")
// public class JobListing {

//     @Id
//     @Column(name = "listing_id")
//     private String listingId;

//     @ManyToOne(fetch = FetchType.EAGER)
//     @JoinColumn(name = "company_id")
//     private Company company;

//     @Column(name = "job_role")
//     private String jobRole;

//     @Column(name = "job_description")
//     private String jobDescription;

//     private Double ctc;

//     @Column(name = "eligibility_criteria", columnDefinition = "JSON")
//     private String eligibilityCriteria;

//     // ✅ transient map for parsed JSON
//     @Transient
//     private Map<String, Object> eligibilityCriteriaMap;

//     @Column(name = "deadline")
//     private LocalDateTime deadline;

//     @Column(name = "mode_of_hiring")
//     private String modeOfHiring;

//     // ✅ Parse JSON into Map automatically after loading
//     @PostLoad
//     private void parseEligibilityCriteria() {
//         if (eligibilityCriteria != null && !eligibilityCriteria.isEmpty()) {
//             try {
//                 ObjectMapper mapper = new ObjectMapper();
//                 this.eligibilityCriteriaMap = mapper.readValue(
//                         eligibilityCriteria,
//                         new TypeReference<Map<String, Object>>() {}
//                 );
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     // Getters and Setters
//     public String getListingId() { return listingId; }
//     public void setListingId(String listingId) { this.listingId = listingId; }

//     public Company getCompany() { return company; }
//     public void setCompany(Company company) { this.company = company; }

//     public String getJobRole() { return jobRole; }
//     public void setJobRole(String jobRole) { this.jobRole = jobRole; }

//     public String getJobDescription() { return jobDescription; }
//     public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

//     public Double getCtc() { return ctc; }
//     public void setCtc(Double ctc) { this.ctc = ctc; }

//     public String getEligibilityCriteria() { return eligibilityCriteria; }
//     public void setEligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; }

//     public Map<String, Object> getEligibilityCriteriaMap() { return eligibilityCriteriaMap; }
//     public void setEligibilityCriteriaMap(Map<String, Object> eligibilityCriteriaMap) { this.eligibilityCriteriaMap = eligibilityCriteriaMap; }

//     public LocalDateTime getDeadline() { return deadline; }
//     public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

//     public String getModeOfHiring() { return modeOfHiring; }
//     public void setModeOfHiring(String modeOfHiring) { this.modeOfHiring = modeOfHiring; }
// }














package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "job_listings")
public class JobListing {

    @Id
    @Column(name = "listing_id")
    private String listingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "job_description")
    private String jobDescription;

    private Double ctc;

    @Column(name = "eligibility_criteria", columnDefinition = "JSON")
    private String eligibilityCriteria;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "mode_of_hiring")
    private String modeOfHiring;

    // ------------------ Getters & Setters ------------------
    public String getListingId() { return listingId; }
    public void setListingId(String listingId) { this.listingId = listingId; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public Double getCtc() { return ctc; }
    public void setCtc(Double ctc) { this.ctc = ctc; }

    public String getEligibilityCriteria() { return eligibilityCriteria; }
    public void setEligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public String getModeOfHiring() { return modeOfHiring; }
    public void setModeOfHiring(String modeOfHiring) { this.modeOfHiring = modeOfHiring; }

    // ------------------ Formatted Eligibility ------------------
    @Transient
    public String getFormattedEligibility() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(this.eligibilityCriteria);

            String minCpi = node.has("min_cpi") ? node.get("min_cpi").asText() : "N/A";
            StringBuilder branches = new StringBuilder();

            if (node.has("eligible_branches")) {
                for (JsonNode b : node.get("eligible_branches")) {
                    if (branches.length() > 0) branches.append(", ");
                    branches.append(b.asText());
                }
            }

            return "<strong>Minimum CPI:</strong> " + minCpi + "<br>" +
                   "<strong>Eligible Branches:</strong> " + branches.toString();
        } catch (Exception e) {
            return "N/A";
        }
    }
}
