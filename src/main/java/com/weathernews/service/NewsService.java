package com.weathernews.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.weathernews.model.Location;
import com.weathernews.model.NewsArticle;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service to fetch local news using NewsAPI
 */
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private static final String NEWS_API_URL = "https://newsapi.org/v2/everything";
    private static final String TOP_HEADLINES_URL = "https://newsapi.org/v2/top-headlines";
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    
    public NewsService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }
    
    /**
     * Fetch local news for a given location
     * @param location The location to get news for
     * @param maxArticles Maximum number of articles to return
     * @return List of news articles
     * @throws IOException if the API request fails
     */
    public List<NewsArticle> getLocalNews(Location location, int maxArticles) throws IOException {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        
        // Try to get news by city name first
        List<NewsArticle> articles = getNewsByQuery(location.getCity(), maxArticles);
        
        // If no articles found, try state name
        if (articles.isEmpty() && location.getState() != null) {
            articles = getNewsByQuery(location.getState(), maxArticles);
        }
        
        // If still no articles, get general US news
        if (articles.isEmpty()) {
            articles = getTopHeadlines("us", maxArticles);
        }
        
        return articles;
    }
    
    /**
     * Fetch news by search query
     * @param query The search query (city name, state, etc.)
     * @param maxArticles Maximum number of articles to return
     * @return List of news articles
     * @throws IOException if the API request fails
     */
    public List<NewsArticle> getNewsByQuery(String query, int maxArticles) throws IOException {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
        
        String url = String.format("%s?q=%s&sortBy=publishedAt&pageSize=%d&apiKey=%s",
                NEWS_API_URL, query.trim(), Math.min(maxArticles, 100), apiKey);
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.execute(request)) {
            if (!response.isSuccessful()) {
                throw new IOException("News API request failed: " + response.code() + " " + response.message());
            }
            
            String responseBody = response.body().string();
            return parseNewsResponse(responseBody);
        }
    }
    
    /**
     * Fetch top headlines for a country
     * @param country Country code (e.g., "us")
     * @param maxArticles Maximum number of articles to return
     * @return List of news articles
     * @throws IOException if the API request fails
     */
    public List<NewsArticle> getTopHeadlines(String country, int maxArticles) throws IOException {
        String url = String.format("%s?country=%s&pageSize=%d&apiKey=%s",
                TOP_HEADLINES_URL, country, Math.min(maxArticles, 100), apiKey);
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.execute(request)) {
            if (!response.isSuccessful()) {
                throw new IOException("News API request failed: " + response.code() + " " + response.message());
            }
            
            String responseBody = response.body().string();
            return parseNewsResponse(responseBody);
        }
    }
    
    /**
     * Parse the news API response into a list of NewsArticle objects
     * @param responseBody The JSON response from the API
     * @return List of NewsArticle objects
     */
    private List<NewsArticle> parseNewsResponse(String responseBody) {
        List<NewsArticle> articles = new ArrayList<>();
        
        try {
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            
            if (!jsonObject.get("status").getAsString().equals("ok")) {
                logger.warn("News API returned error status: {}", jsonObject.get("status").getAsString());
                return articles;
            }
            
            JsonArray articlesArray = jsonObject.getAsJsonArray("articles");
            
            for (int i = 0; i < articlesArray.size(); i++) {
                JsonObject articleJson = articlesArray.get(i).getAsJsonObject();
                NewsArticle article = new NewsArticle();
                
                // Basic information
                if (articleJson.has("title") && !articleJson.get("title").isJsonNull()) {
                    article.setTitle(articleJson.get("title").getAsString());
                }
                
                if (articleJson.has("description") && !articleJson.get("description").isJsonNull()) {
                    article.setDescription(articleJson.get("description").getAsString());
                }
                
                if (articleJson.has("content") && !articleJson.get("content").isJsonNull()) {
                    article.setContent(articleJson.get("content").getAsString());
                }
                
                if (articleJson.has("url") && !articleJson.get("url").isJsonNull()) {
                    article.setUrl(articleJson.get("url").getAsString());
                }
                
                if (articleJson.has("urlToImage") && !articleJson.get("urlToImage").isJsonNull()) {
                    article.setUrlToImage(articleJson.get("urlToImage").getAsString());
                }
                
                if (articleJson.has("author") && !articleJson.get("author").isJsonNull()) {
                    article.setAuthor(articleJson.get("author").getAsString());
                }
                
                // Source information
                if (articleJson.has("source") && !articleJson.get("source").isJsonNull()) {
                    JsonObject sourceJson = articleJson.getAsJsonObject("source");
                    if (sourceJson.has("name") && !sourceJson.get("name").isJsonNull()) {
                        article.setSource(sourceJson.get("name").getAsString());
                    }
                }
                
                // Published date
                if (articleJson.has("publishedAt") && !articleJson.get("publishedAt").isJsonNull()) {
                    try {
                        String publishedAtStr = articleJson.get("publishedAt").getAsString();
                        LocalDateTime publishedAt = LocalDateTime.parse(publishedAtStr, 
                                DateTimeFormatter.ISO_DATE_TIME);
                        article.setPublishedAt(publishedAt);
                    } catch (Exception e) {
                        logger.warn("Failed to parse published date: {}", 
                                articleJson.get("publishedAt").getAsString());
                    }
                }
                
                // Only add articles with at least a title
                if (article.getTitle() != null && !article.getTitle().trim().isEmpty()) {
                    articles.add(article);
                }
            }
            
            logger.info("Successfully parsed {} news articles", articles.size());
            
        } catch (Exception e) {
            logger.error("Failed to parse news response", e);
        }
        
        return articles;
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