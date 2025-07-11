package com.weathernews.model;

import java.time.LocalDateTime;

/**
 * Represents a news article
 */
public class NewsArticle {
    private String title;
    private String description;
    private String content;
    private String source;
    private String author;
    private String url;
    private String urlToImage;
    private LocalDateTime publishedAt;
    
    public NewsArticle() {}
    
    public NewsArticle(String title, String description, String source, String url, LocalDateTime publishedAt) {
        this.title = title;
        this.description = description;
        this.source = source;
        this.url = url;
        this.publishedAt = publishedAt;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUrlToImage() {
        return urlToImage;
    }
    
    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
    
    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
    
    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
    
    @Override
    public String toString() {
        return String.format(
            "📰 %s\n" +
            "   Source: %s\n" +
            "   %s\n" +
            "   Published: %s\n" +
            "   URL: %s\n",
            title != null ? title : "No title",
            source != null ? source : "Unknown source",
            description != null ? description : "No description available",
            publishedAt != null ? publishedAt.toString() : "Unknown date",
            url != null ? url : "No URL available"
        );
    }
} 