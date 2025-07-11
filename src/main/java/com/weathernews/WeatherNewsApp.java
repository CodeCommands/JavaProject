package com.weathernews;

import com.weathernews.model.Location;
import com.weathernews.model.NewsArticle;
import com.weathernews.model.Weather;
import com.weathernews.service.NewsService;
import com.weathernews.service.WeatherService;
import com.weathernews.service.ZipcodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Main application class for Weather and News App
 */
public class WeatherNewsApp {
    private static final Logger logger = LoggerFactory.getLogger(WeatherNewsApp.class);
    
    private final WeatherService weatherService;
    private final NewsService newsService;
    private final ZipcodeService zipcodeService;
    private final Scanner scanner;
    
    public WeatherNewsApp() {
        // Load configuration
        Properties config = loadConfiguration();
        
        // Get API keys from configuration
        String weatherApiKey = config.getProperty("weather.api.key");
        String newsApiKey = config.getProperty("news.api.key");
        
        if (weatherApiKey == null || weatherApiKey.trim().isEmpty()) {
            throw new IllegalStateException("Weather API key not found in configuration. Please set 'weather.api.key' in config.properties");
        }
        
        if (newsApiKey == null || newsApiKey.trim().isEmpty()) {
            throw new IllegalStateException("News API key not found in configuration. Please set 'news.api.key' in config.properties");
        }
        
        // Initialize services
        this.weatherService = new WeatherService(weatherApiKey);
        this.newsService = new NewsService(newsApiKey);
        this.zipcodeService = new ZipcodeService(weatherApiKey);
        this.scanner = new Scanner(System.in);
        
        logger.info("Weather and News App initialized successfully");
    }
    
    /**
     * Load configuration from properties file
     * @return Properties object with configuration
     */
    private Properties loadConfiguration() {
        Properties config = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.warn("config.properties not found, using default configuration");
                return config;
            }
            config.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
        }
        
        return config;
    }
    
    /**
     * Main application loop
     */
    public void run() {
        printWelcomeMessage();
        
        while (true) {
            try {
                System.out.print("\nEnter a US zipcode (or 'quit' to exit): ");
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                    System.out.println("Thank you for using Weather and News App!");
                    break;
                }
                
                if (input.isEmpty()) {
                    System.out.println("Please enter a valid zipcode.");
                    continue;
                }
                
                // Validate zipcode format
                if (!zipcodeService.isValidZipcode(input)) {
                    System.out.println("Invalid zipcode format. Please enter a 5-digit US zipcode (e.g., 12345 or 12345-6789).");
                    continue;
                }
                
                // Process the zipcode
                processZipcode(input);
                
            } catch (Exception e) {
                logger.error("Error processing user input", e);
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }
    
    /**
     * Process a zipcode and display weather and news information
     * @param zipcode The zipcode to process
     */
    private void processZipcode(String zipcode) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Processing zipcode: " + zipcode);
        System.out.println("=".repeat(60));
        
        try {
            // Get location information
            System.out.println("📍 Looking up location...");
            Location location = zipcodeService.getLocationByZipcode(zipcode);
            System.out.println("   Location: " + location);
            
            // Get weather information
            System.out.println("\n🌤️  Fetching weather information...");
            Weather weather = weatherService.getWeatherByZipcode(zipcode);
            System.out.println(weather);
            
            // Get news information
            System.out.println("\n📰 Fetching local news...");
            List<NewsArticle> articles = newsService.getLocalNews(location, 5);
            
            if (articles.isEmpty()) {
                System.out.println("   No local news found for this area.");
            } else {
                System.out.println("   Found " + articles.size() + " news articles:");
                System.out.println("-".repeat(60));
                
                for (int i = 0; i < articles.size(); i++) {
                    NewsArticle article = articles.get(i);
                    System.out.println((i + 1) + ". " + article.getTitle());
                    System.out.println("   Source: " + article.getSource());
                    if (article.getDescription() != null && !article.getDescription().isEmpty()) {
                        String description = article.getDescription();
                        if (description.length() > 150) {
                            description = description.substring(0, 150) + "...";
                        }
                        System.out.println("   " + description);
                    }
                    if (article.getUrl() != null && !article.getUrl().isEmpty()) {
                        System.out.println("   URL: " + article.getUrl());
                    }
                    System.out.println();
                }
            }
            
        } catch (IOException e) {
            logger.error("API request failed for zipcode: " + zipcode, e);
            System.out.println("❌ Failed to fetch data: " + e.getMessage());
            System.out.println("Please check your internet connection and API keys.");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid zipcode: " + zipcode, e);
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error processing zipcode: " + zipcode, e);
            System.out.println("❌ An unexpected error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Print welcome message
     */
    private void printWelcomeMessage() {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Weather & News App                        ║");
        System.out.println("║                                                               ║");
        System.out.println("║  Get current weather conditions and local news for any       ║");
        System.out.println("║  US zipcode!                                                  ║");
        System.out.println("║                                                               ║");
        System.out.println("║  Features:                                                    ║");
        System.out.println("║  • Current weather conditions                                 ║");
        System.out.println("║  • Temperature, humidity, wind speed                         ║");
        System.out.println("║  • Local news articles                                       ║");
        System.out.println("║  • Location information                                       ║");
        System.out.println("║                                                               ║");
        System.out.println("║  Type 'quit' or 'exit' to close the application              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Clean up resources
     */
    public void cleanup() {
        if (weatherService != null) {
            weatherService.close();
        }
        if (newsService != null) {
            newsService.close();
        }
        if (zipcodeService != null) {
            zipcodeService.close();
        }
        if (scanner != null) {
            scanner.close();
        }
        logger.info("Application resources cleaned up");
    }
    
    /**
     * Main method
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        WeatherNewsApp app = null;
        
        try {
            app = new WeatherNewsApp();
            app.run();
        } catch (Exception e) {
            logger.error("Application failed to start", e);
            System.err.println("Failed to start application: " + e.getMessage());
            System.err.println("Please check your configuration and try again.");
            System.exit(1);
        } finally {
            if (app != null) {
                app.cleanup();
            }
        }
    }
} 