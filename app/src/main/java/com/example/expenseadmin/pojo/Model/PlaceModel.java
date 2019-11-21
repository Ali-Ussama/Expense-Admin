package com.example.expenseadmin.pojo.Model;

import java.util.ArrayList;

public class PlaceModel {

    private int id;

    private String name;

    private String category;

    private String phoneNumber;

    private String description;

    private String facebookUrl;

    private String twitterUrl;

    private String websiteUrl;

    private ArrayList<LocationModel> locationModels;

    private int likesCount;

    private int okayCount;

    private int dislikesCount;

    private ArrayList<ImageModel> imagesURL;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public ArrayList<LocationModel> getLocationModels() {
        return locationModels;
    }

    public void setLocationModels(ArrayList<LocationModel> locationModels) {
        this.locationModels = locationModels;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getOkayCount() {
        return okayCount;
    }

    public void setOkayCount(int okayCount) {
        this.okayCount = okayCount;
    }

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public ArrayList<ImageModel> getImagesURL() {
        return imagesURL;
    }

    public void setImagesURL(ArrayList<ImageModel> imagesURL) {
        this.imagesURL = imagesURL;
    }
}
