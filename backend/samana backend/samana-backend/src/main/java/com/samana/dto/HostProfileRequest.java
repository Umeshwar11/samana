package com.samana.dto;

import jakarta.validation.constraints.Min;

public class HostProfileRequest {

    private String location;
    private String education;
    private String profession;
    private String aboutHost;
    private String familyDescription;
    private String previousHostingExperience;

    @Min(value = 0, message = "Years of hosting cannot be negative")
    private Integer yearsOfHosting;

    // Optional social media / website links
    private String instagramUrl;
    private String facebookUrl;
    private String linkedinUrl;
    private String twitterUrl;
    private String youtubeUrl;
    private String websiteUrl;

    // =========================
    // Constructor
    // =========================

    public HostProfileRequest() {
    }

    // =========================
    // Getters & Setters
    // =========================

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAboutHost() {
        return aboutHost;
    }

    public void setAboutHost(String aboutHost) {
        this.aboutHost = aboutHost;
    }

    public String getFamilyDescription() {
        return familyDescription;
    }

    public void setFamilyDescription(String familyDescription) {
        this.familyDescription = familyDescription;
    }

    public String getPreviousHostingExperience() {
        return previousHostingExperience;
    }

    public void setPreviousHostingExperience(String previousHostingExperience) {
        this.previousHostingExperience = previousHostingExperience;
    }

    public Integer getYearsOfHosting() {
        return yearsOfHosting;
    }

    public void setYearsOfHosting(Integer yearsOfHosting) {
        this.yearsOfHosting = yearsOfHosting;
    }

    // =========================
    // Social Media / Website
    // =========================

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}