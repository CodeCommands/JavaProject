package com.weathernews.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.weathernews.model.Location;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Service to convert US zipcodes to location data using OpenWeatherMap Geocoding API
 */
public class ZipcodeService {
    private static final Logger logger = LoggerFactory.getLogger(ZipcodeService.class);
    private static final String GEOCODING_API_URL = "https://api.openweathermap.org/geo/1.0/zip";
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    
    public ZipcodeService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }
    
    /**
     * Convert a US zipcode to location data
     * @param zipcode The US zipcode to convert
     * @return Location object with coordinates and location information
     * @throws IOException if the API request fails
     * @throws IllegalArgumentException if the zipcode is invalid
     */
    public Location getLocationByZipcode(String zipcode) throws IOException {
        if (zipcode == null || zipcode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zipcode cannot be null or empty");
        }
        
        // Validate zipcode format (basic US zipcode validation)
        String cleanZipcode = zipcode.trim();
        if (!cleanZipcode.matches("^\\d{5}(-\\d{4})?$")) {
            throw new IllegalArgumentException("Invalid US zipcode format. Expected format: 12345 or 12345-6789");
        }
        
        // Extract 5-digit zipcode if extended format is provided
        if (cleanZipcode.contains("-")) {
            cleanZipcode = cleanZipcode.substring(0, 5);
        }
        
        String url = String.format("%s?zip=%s,US&appid=%s", 
                GEOCODING_API_URL, cleanZipcode, apiKey);
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.execute(request)) {
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new IllegalArgumentException("Zipcode not found: " + zipcode);
                }
                throw new IOException("Geocoding API request failed: " + response.code() + " " + response.message());
            }
            
            String responseBody = response.body().string();
            return parseLocationResponse(responseBody, zipcode);
        }
    }
    
    /**
     * Get location by city and state
     * @param city The city name
     * @param state The state name or abbreviation
     * @return Location object with coordinates and location information
     * @throws IOException if the API request fails
     */
    public Location getLocationByCity(String city, String state) throws IOException {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty");
        }
        
        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s,%s,US&limit=1&appid=%s", 
                city.trim(), state.trim(), apiKey);
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.execute(request)) {
            if (!response.isSuccessful()) {
                throw new IOException("Geocoding API request failed: " + response.code() + " " + response.message());
            }
            
            String responseBody = response.body().string();
            return parseDirectLocationResponse(responseBody, city, state);
        }
    }
    
    /**
     * Parse the geocoding API response for zipcode lookup
     * @param responseBody The JSON response from the API
     * @param originalZipcode The original zipcode for reference
     * @return Location object
     */
    private Location parseLocationResponse(String responseBody, String originalZipcode) {
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        
        Location location = new Location();
        location.setZipcode(originalZipcode);
        
        // Extract location data
        if (jsonObject.has("name")) {
            location.setCity(jsonObject.get("name").getAsString());
        }
        
        if (jsonObject.has("country")) {
            String country = jsonObject.get("country").getAsString();
            // For US locations, try to extract state from the response
            // Note: OpenWeatherMap doesn't always provide state info in zipcode lookup
            location.setState("US"); // Default to US since we're limiting to US zipcodes
        }
        
        if (jsonObject.has("lat")) {
            location.setLatitude(jsonObject.get("lat").getAsDouble());
        }
        
        if (jsonObject.has("lon")) {
            location.setLongitude(jsonObject.get("lon").getAsDouble());
        }
        
        logger.info("Successfully converted zipcode {} to location: {}", originalZipcode, location);
        return location;
    }
    
    /**
     * Parse the geocoding API response for direct location lookup
     * @param responseBody The JSON response from the API
     * @param city The city name
     * @param state The state name
     * @return Location object
     */
    private Location parseDirectLocationResponse(String responseBody, String city, String state) {
        JsonArray jsonArray = gson.fromJson(responseBody, JsonArray.class);
        
        if (jsonArray.size() == 0) {
            throw new IllegalArgumentException("Location not found: " + city + ", " + state);
        }
        
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        
        Location location = new Location();
        location.setCity(city);
        location.setState(state);
        
        if (jsonObject.has("lat")) {
            location.setLatitude(jsonObject.get("lat").getAsDouble());
        }
        
        if (jsonObject.has("lon")) {
            location.setLongitude(jsonObject.get("lon").getAsDouble());
        }
        
        logger.info("Successfully found location: {}", location);
        return location;
    }
    
    /**
     * Validate if a string is a valid US zipcode
     * @param zipcode The zipcode to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidZipcode(String zipcode) {
        if (zipcode == null || zipcode.trim().isEmpty()) {
            return false;
        }
        
        String cleanZipcode = zipcode.trim();
        return cleanZipcode.matches("^\\d{5}(-\\d{4})?$");
    }
    
    /**
     * Clean up resources
     */
    public void close() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
        }
    }
} 