package com.weathernews.model;

/**
 * Represents weather information for a location
 */
public class Weather {
    private String location;
    private double temperature;
    private double feelsLike;
    private String description;
    private String mainCondition;
    private int humidity;
    private double windSpeed;
    private int windDirection;
    private double pressure;
    private int visibility;
    private String icon;
    
    public Weather() {}
    
    public Weather(String location, double temperature, String description, 
                  String mainCondition, int humidity, double windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.description = description;
        this.mainCondition = mainCondition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }
    
    // Getters and Setters
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public double getFeelsLike() {
        return feelsLike;
    }
    
    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getMainCondition() {
        return mainCondition;
    }
    
    public void setMainCondition(String mainCondition) {
        this.mainCondition = mainCondition;
    }
    
    public int getHumidity() {
        return humidity;
    }
    
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    
    public double getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    public int getWindDirection() {
        return windDirection;
    }
    
    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }
    
    public double getPressure() {
        return pressure;
    }
    
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    
    public int getVisibility() {
        return visibility;
    }
    
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Weather in %s:\n" +
            "  Temperature: %.1f°F (feels like %.1f°F)\n" +
            "  Condition: %s - %s\n" +
            "  Humidity: %d%%\n" +
            "  Wind: %.1f mph\n" +
            "  Pressure: %.1f hPa\n" +
            "  Visibility: %d meters",
            location, temperature, feelsLike, mainCondition, description,
            humidity, windSpeed, pressure, visibility
        );
    }
} 