package com.placementportal.placement_website.model;

public class Student {
    private String enrollmentNumber;
    private String studentName;
    private String email;
    private String phoneNumber;
    private String branch;
    private String address;
    private String localAddress;
    private String sex;
    private Double cpi;
    private String profileStat;
    private String dob;
    private String password;

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
