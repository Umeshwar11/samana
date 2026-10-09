package com.samana.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "experiences")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private Double price;

    private Integer duration;

    private Boolean available = true;

    // ==========================================
    // EXPERIENCE DETAILS
    // ==========================================

    private String location;

    private Integer maxGuests;

    private String meetingPoint;

    @Column(columnDefinition = "TEXT")
    private String whatYouWillDo;

    @Column(columnDefinition = "TEXT")
    private String included;

    @Column(columnDefinition = "TEXT")
    private String notIncluded;

    @Column(columnDefinition = "TEXT")
    private String cancellationPolicy;

    // ==========================================
    // MEDIA
    // ==========================================

    // Main/cover image
    private String coverImageUrl;

    // Optional video URL
    private String videoUrl;

    // Additional uploaded images
    // Stored as comma-separated URLs
    @Column(columnDefinition = "TEXT")
    private String imageUrls;

    // ==========================================
    // HOST
    // ==========================================

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    // ==========================================
    // CATEGORY
    // ==========================================

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // ==========================================
    // PACKAGES (NEW - ONE-TO-MANY RELATIONSHIP)
    // ==========================================

    @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ExperiencePackage> packages;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public Experience() {
    }

    // ==========================================
    // ID
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    // AVAILABLE
    // ==========================================

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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
    // COVER IMAGE
    // ==========================================

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    // ==========================================
    // VIDEO
    // ==========================================

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    // ==========================================
    // ADDITIONAL IMAGE URLS
    // ==========================================

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    // ==========================================
    // HOST
    // ==========================================

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    // ==========================================
    // CATEGORY
    // ==========================================

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // ==========================================
    // PACKAGES
    // ==========================================

    public List<ExperiencePackage> getPackages() {
        return packages;
    }

    public void setPackages(List<ExperiencePackage> packages) {
        this.packages = packages;
    }
}