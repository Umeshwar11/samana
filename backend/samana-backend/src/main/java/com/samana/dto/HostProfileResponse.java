package com.samana.dto;

public class HostProfileResponse {

    private Long id;
    private Long userId;

    private String hostName;
    private String email;

    private String profilePhotoUrl;
    private String location;
    private String education;
    private String profession;
    private String aboutHost;
    private String familyDescription;
    private String familyPhotoUrl;
    private String housePhotoUrl;
    private String previousHostingExperience;
    private Integer yearsOfHosting;

    private Long experiencesHosted;
    private Double averageRating;
    private Long reviewCount;

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

    public HostProfileResponse() {
    }

    // =========================
    // Getters & Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

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

    public String getFamilyPhotoUrl() {
        return familyPhotoUrl;
    }

    public void setFamilyPhotoUrl(String familyPhotoUrl) {
        this.familyPhotoUrl = familyPhotoUrl;
    }

    public String getHousePhotoUrl() {
        return housePhotoUrl;
    }

    public void setHousePhotoUrl(String housePhotoUrl) {
        this.housePhotoUrl = housePhotoUrl;
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

    public Long getExperiencesHosted() {
        return experiencesHosted;
    }

    public void setExperiencesHosted(Long experiencesHosted) {
        this.experiencesHosted = experiencesHosted;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
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