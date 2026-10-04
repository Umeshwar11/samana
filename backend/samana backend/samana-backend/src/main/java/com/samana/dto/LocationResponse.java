package com.samana.dto;

public class LocationResponse {

    private Long id;
    private String country;
    private String state;
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;


    public LocationResponse(
            Long id,
            String country,
            String state,
            String city,
            String address,
            Double latitude,
            Double longitude) {

        this.id = id;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}