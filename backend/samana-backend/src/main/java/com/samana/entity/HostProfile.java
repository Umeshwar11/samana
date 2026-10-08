package com.samana.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "host_profiles")
public class HostProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // HOST / USER
    // =====================================================

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    // =====================================================
    // BASIC PROFILE INFORMATION
    // =====================================================

    @Column(length = 500)
    private String profilePhotoUrl;

    @Column(length = 255)
    private String location;

    @Column(length = 500)
    private String education;

    @Column(length = 500)
    private String profession;

    @Column(columnDefinition = "TEXT")
    private String aboutHost;


    // =====================================================
    // FAMILY INFORMATION
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String familyDescription;

    @Column(length = 500)
    private String familyPhotoUrl;


    // =====================================================
    // HOUSE INFORMATION
    // =====================================================

    @Column(length = 500)
    private String housePhotoUrl;


    // =====================================================
    // HOSTING EXPERIENCE
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String previousHostingExperience;

    private Integer yearsOfHosting;


    // =====================================================
    // SOCIAL MEDIA LINKS
    // =====================================================

    @Column(length = 500)
    private String instagramUrl;

    @Column(length = 500)
    private String facebookUrl;

    @Column(length = 500)
    private String linkedinUrl;

    @Column(length = 500)
    private String twitterUrl;

    @Column(length = 500)
    private String youtubeUrl;

    @Column(length = 500)
    private String websiteUrl;


    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public HostProfile() {
    }


    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    // =====================================================
    // SOCIAL MEDIA GETTERS AND SETTERS
    // =====================================================

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