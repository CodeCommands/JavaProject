package com.weathernews.model;

/**
 * Represents a geographic location with zipcode, city, state, and coordinates
 */
public class Location {
    private String zipcode;
    private String city;
    private String state;
    private double latitude;
    private double longitude;
    
    public Location() {}
    
    public Location(String zipcode, String city, String state, double latitude, double longitude) {
        this.zipcode = zipcode;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters and Setters
    public String getZipcode() {
        return zipcode;
    }
    
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s %s (%.4f, %.4f)", 
                city, state, zipcode, latitude, longitude);
    }
} 