package com.placementportal.placement_website;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequest {

    @NotBlank(message = "Full name cannot be empty")
    private String fullName;
    
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    
    @NotBlank(message = "Phone number cannot be empty")
    private String phone;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    // Getters and Setters remain the same
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}