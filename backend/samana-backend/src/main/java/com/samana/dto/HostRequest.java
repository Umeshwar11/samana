package com.samana.dto;

import jakarta.validation.constraints.NotBlank;

public class HostRequest {

    @NotBlank
    private String bio;

    @NotBlank
    private String phone;

    public HostRequest() {
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}