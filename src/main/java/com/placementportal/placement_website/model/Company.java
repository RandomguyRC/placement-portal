package com.placementportal.placement_website.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @Column(name = "company_id", nullable = false, length = 255)
    private String companyId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "description")
    private String description;

    @Column(name = "person_of_contact")
    private String personOfContact;

    @Column(name = "contact_email")
    private String contactEmail;

    // ✅ Prevent infinite recursion in JSON
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<JobListing> jobListings;

    // 🔹 Constructors
    public Company() {}

    public Company(String companyId, String companyName, String description,
                   String personOfContact, String contactEmail) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.description = description;
        this.personOfContact = personOfContact;
        this.contactEmail = contactEmail;
    }

    // 🔹 Getters and Setters
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPersonOfContact() { return personOfContact; }
    public void setPersonOfContact(String personOfContact) { this.personOfContact = personOfContact; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public List<JobListing> getJobListings() { return jobListings; }
    public void setJobListings(List<JobListing> jobListings) { this.jobListings = jobListings; }
}
