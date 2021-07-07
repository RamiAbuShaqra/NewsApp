package com.example.newsapp;

public class News {

    // Fields
    private final String contentTitle;
    private final String sectionName;
    private final String authorName;
    private final String publishedDate;
    private final String webUrl;

    // Constructor
    public News(String contentTitle, String sectionName, String authorName, String publishedDate, String webUrl){
        this.contentTitle = contentTitle;
        this.sectionName = sectionName;
        this.authorName = authorName;
        this.publishedDate = publishedDate;
        this.webUrl = webUrl;
    }

    // Getters
    public String getContentTitle(){return contentTitle;}
    public String getSectionName(){return sectionName;}
    public String getAuthorName(){return authorName;}
    public String getPublishedDate(){return publishedDate;}
    public String getWebUrl(){return webUrl;}
}
