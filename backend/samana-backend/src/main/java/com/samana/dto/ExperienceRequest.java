package com.samana.dto;

import jakarta.validation.constraints.*;

public class ExperienceRequest {

    // ==========================================
    // BASIC INFORMATION
    // ==========================================

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @Positive
    private Integer duration;

    @NotNull
    private Long categoryId;


    // ==========================================
    // EXPERIENCE DETAILS
    // ==========================================

    @NotBlank
    private String location;

    @NotNull
    @Positive
    private Integer maxGuests;

    private String meetingPoint;

    private String whatYouWillDo;

    private String included;

    private String notIncluded;

    private String cancellationPolicy;


    // ==========================================
    // MEDIA
    // ==========================================

    private String coverImageUrl;

    private String videoUrl;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public ExperienceRequest() {
    }


    // ==========================================
    // TITLE
    // ==========================================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    // ==========================================
    // DESCRIPTION
    // ==========================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // ==========================================
    // PRICE
    // ==========================================

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    // ==========================================
    // DURATION
    // ==========================================

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    // ==========================================
    // CATEGORY
    // ==========================================

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    // ==========================================
    // LOCATION
    // ==========================================

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    // ==========================================
    // MAXIMUM GUESTS
    // ==========================================

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }


    // ==========================================
    // MEETING POINT
    // ==========================================

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }


    // ==========================================
    // WHAT YOU WILL DO
    // ==========================================

    public String getWhatYouWillDo() {
        return whatYouWillDo;
    }

    public void setWhatYouWillDo(String whatYouWillDo) {
        this.whatYouWillDo = whatYouWillDo;
    }


    // ==========================================
    // INCLUDED
    // ==========================================

    public String getIncluded() {
        return included;
    }

    public void setIncluded(String included) {
        this.included = included;
    }


    // ==========================================
    // NOT INCLUDED
    // ==========================================

    public String getNotIncluded() {
        return notIncluded;
    }

    public void setNotIncluded(String notIncluded) {
        this.notIncluded = notIncluded;
    }


    // ==========================================
    // CANCELLATION POLICY
    // ==========================================

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }


    // ==========================================
    // COVER IMAGE URL
    // ==========================================

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }


    // ==========================================
    // VIDEO URL
    // ==========================================

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}