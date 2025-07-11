package com.weathernews.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.weathernews.model.Location;
import com.weathernews.model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Service to fetch weather information using OpenWeatherMap API
 */
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    
    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }
    
    /**
     * Fetch weather information for a given location
     * @param location The location to get weather for
     * @return Weather object containing weather information
     * @throws IOException if the API request fails
     */
    public Weather getWeatherByLocation(Location location) throws IOException {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        
        String url = String.format("%s?lat=%.4f&lon=%.4f&appid=%s&units=imperial",
                WEATHER_API_URL, location.getLatitude(), location.getLongitude(), apiKey);
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.execute(request)) {
            if (!response.isSuccessful()) {
                throw new IOException("Weather API request failed: " + response.code() + " " + response.message());
            }
            
            String responseBody = response.body().string();
            return parseWeatherResponse(responseBody, location);
        }
    }
    
    /**
     * Fetch weather information by zipcode
     * @param zipcode The US zipcode
     * @return Weather object containing weather information
     * @throws IOException if the API request fails
     */
    public Weather getWeatherByZipcode(String zipcode) throws IOException {
        if (zipcode == null || zipcode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zipcode cannot be null or empty");
        }
        
        String url = String.format("%s?zip=%s,US&appid=%s&units=imperial",
                WEATHER_API_URL, zipcode.trim(), apiKey);
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.execute(request)) {
            if (!response.isSuccessful()) {
                throw new IOException("Weather API request failed: " + response.code() + " " + response.message());
            }
            
            String responseBody = response.body().string();
            return parseWeatherResponse(responseBody, null);
        }
    }
    
    /**
     * Parse the weather API response into a Weather object
     * @param responseBody The JSON response from the API
     * @param location The location (can be null)
     * @return Weather object
     */
    private Weather parseWeatherResponse(String responseBody, Location location) {
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        
        Weather weather = new Weather();
        
        // Location information
        if (location != null) {
            weather.setLocation(location.toString());
        } else {
            String cityName = jsonObject.get("name").getAsString();
            String country = jsonObject.getAsJsonObject("sys").get("country").getAsString();
            weather.setLocation(cityName + ", " + country);
        }
        
        // Main weather data
        JsonObject main = jsonObject.getAsJsonObject("main");
        weather.setTemperature(main.get("temp").getAsDouble());
        weather.setFeelsLike(main.get("feels_like").getAsDouble());
        weather.setHumidity(main.get("humidity").getAsInt());
        weather.setPressure(main.get("pressure").getAsDouble());
        
        // Weather condition
        JsonObject weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        weather.setMainCondition(weatherCondition.get("main").getAsString());
        weather.setDescription(weatherCondition.get("description").getAsString());
        weather.setIcon(weatherCondition.get("icon").getAsString());
        
        // Wind data
        if (jsonObject.has("wind")) {
            JsonObject wind = jsonObject.getAsJsonObject("wind");
            weather.setWindSpeed(wind.get("speed").getAsDouble());
            if (wind.has("deg")) {
                weather.setWindDirection(wind.get("deg").getAsInt());
            }
        }
        
        // Visibility
        if (jsonObject.has("visibility")) {
            weather.setVisibility(jsonObject.get("visibility").getAsInt());
        }
        
        logger.info("Successfully fetched weather data for {}", weather.getLocation());
        return weather;
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