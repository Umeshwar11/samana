package com.samana.dto;

public class ExperienceResponse {

    private Long id;

    private String title;

    private String description;

    private Double price;

    private Integer duration;

    private Boolean available;

    private Long hostId;

    private Long categoryId;


    // ==========================================
    // EXPERIENCE DETAILS
    // ==========================================

    private String location;

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

    private String imageUrls;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public ExperienceResponse() {
    }


    public ExperienceResponse(

            Long id,

            String title,

            String description,

            Double price,

            Integer duration,

            Boolean available,

            Long hostId,

            Long categoryId,

            String location,

            Integer maxGuests,

            String meetingPoint,

            String whatYouWillDo,

            String included,

            String notIncluded,

            String cancellationPolicy,

            String coverImageUrl,

            String videoUrl,

            String imageUrls) {

        this.id = id;

        this.title = title;

        this.description = description;

        this.price = price;

        this.duration = duration;

        this.available = available;

        this.hostId = hostId;

        this.categoryId = categoryId;


        // ==========================================
        // EXPERIENCE DETAILS
        // ==========================================

        this.location = location;

        this.maxGuests = maxGuests;

        this.meetingPoint = meetingPoint;

        this.whatYouWillDo = whatYouWillDo;

        this.included = included;

        this.notIncluded = notIncluded;

        this.cancellationPolicy = cancellationPolicy;


        // ==========================================
        // MEDIA
        // ==========================================

        this.coverImageUrl = coverImageUrl;

        this.videoUrl = videoUrl;

        this.imageUrls = imageUrls;
    }


    // ==========================================
    // BASIC INFORMATION
    // ==========================================

    public Long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public Double getPrice() {
        return price;
    }


    public Integer getDuration() {
        return duration;
    }


    public Boolean getAvailable() {
        return available;
    }


    public Long getHostId() {
        return hostId;
    }


    public Long getCategoryId() {
        return categoryId;
    }


    // ==========================================
    // EXPERIENCE DETAILS
    // ==========================================

    public String getLocation() {
        return location;
    }


    public Integer getMaxGuests() {
        return maxGuests;
    }


    public String getMeetingPoint() {
        return meetingPoint;
    }


    public String getWhatYouWillDo() {
        return whatYouWillDo;
    }


    public String getIncluded() {
        return included;
    }


    public String getNotIncluded() {
        return notIncluded;
    }


    public String getCancellationPolicy() {
        return cancellationPolicy;
    }


    // ==========================================
    // MEDIA
    // ==========================================

    public String getCoverImageUrl() {
        return coverImageUrl;
    }


    public String getVideoUrl() {
        return videoUrl;
    }


    public String getImageUrls() {
        return imageUrls;
    }
}