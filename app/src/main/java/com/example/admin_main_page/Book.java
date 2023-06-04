package com.example.admin_main_page;

public class Book {
    private  String AuthorName;
    private String id;
    private String title;
    private String description;
    private String category;
    private int units;
    private String imageUrl;

    private int fLagAccepted=-1;

    public Book()
    {
    }

    public Book(String authorName, String id, String title, String description, String category, int units, String imageUrl,int FLagAccepted) {
        AuthorName = authorName;
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.units = units;
        this.imageUrl = imageUrl;
        this.fLagAccepted=FLagAccepted;
    }

    public int getFLagAccepted()
    {
        return fLagAccepted;
    }

    public void setFLagAccepted(int FLagAccepted) {
        this.fLagAccepted = FLagAccepted;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}


