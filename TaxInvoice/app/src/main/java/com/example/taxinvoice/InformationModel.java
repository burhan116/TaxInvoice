package com.example.taxinvoice;

import java.util.Date;

public class InformationModel {

    public String imageUrl;
    public String description;
    public Date date;

    public InformationModel(String imageUrl, String description, Date date) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "InformationModel{" +
                "imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
