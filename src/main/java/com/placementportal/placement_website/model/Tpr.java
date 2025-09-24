package com.placementportal.placement_website.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tpr")
public class Tpr {

    @Id
    @Column(name = "tpr_id", length = 255)
    private String tprId;

    @Column(name = "tpr_name", length = 255)
    private String tprName;

    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "branch", length = 100)
    private String branch;

    @Column(name = "local_address", columnDefinition = "TEXT")
    private String localAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "roll_no", length = 50)
    private String rollNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "duty_count")
    private Integer dutyCount = 0;

    @Column(name = "password", length = 255)
    private String password;  // <--- Added password field

    // ---------------- ENUMS ----------------
    public enum Sex { MALE, FEMALE, OTHER }
    public enum Role { TPR, TPV }

    // ---------------- GETTERS & SETTERS ----------------
    public String getTprId() { return tprId; }
    public void setTprId(String tprId) { this.tprId = tprId; }

    public String getTprName() { return tprName; }
    public void setTprName(String tprName) { this.tprName = tprName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getLocalAddress() { return localAddress; }
    public void setLocalAddress(String localAddress) { this.localAddress = localAddress; }

    public Sex getSex() { return sex; }
    public void setSex(Sex sex) { this.sex = sex; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Integer getDutyCount() { return dutyCount; }
    public void setDutyCount(Integer dutyCount) { this.dutyCount = dutyCount; }

    public String getPassword() { return password; }  // <--- Getter
    public void setPassword(String password) { this.password = password; }  // <--- Setter
}
