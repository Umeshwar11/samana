package com.samana.dto;

public class HostResponse {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String bio;
    private String phone;

    public HostResponse() {
    }

    public HostResponse(
            Long id,
            Long userId,
            String name,
            String email,
            String bio,
            String phone) {

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getPhone() {
        return phone;
    }
}