package com.placementportal.placement_website.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_details")
public class Student {

    @Id
    @Column(name = "enrollment_number", nullable = false)
    private String enrollmentNumber;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "branch")
    private String branch;

    @Column(name = "address")
    private String address;

    @Column(name = "local_address")
    private String localAddress;

    @Column(name = "sex")
    private String sex;

    @Column(name = "cpi")
    private Double cpi;

    @Column(name = "profile_stat")
    private String profileStat;

    @Column(name = "dob")
    private String dob;

    @Transient // Not stored in DB
    private String password;

    // Constructors
    public Student() {}

    // Getters and Setters
    public String getEnrollmentNumber() { return enrollmentNumber; }
    public void setEnrollmentNumber(String enrollmentNumber) { this.enrollmentNumber = enrollmentNumber; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLocalAddress() { return localAddress; }
    public void setLocalAddress(String localAddress) { this.localAddress = localAddress; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public Double getCpi() { return cpi; }
    public void setCpi(Double cpi) { this.cpi = cpi; }

    public String getProfileStat() { return profileStat; }
    public void setProfileStat(String profileStat) { this.profileStat = profileStat; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
