package edu.northeastern.team11.slurp;

// Represents a dish at a restaurant with an aggregate reviewCount and Slurper score
public class RestaurantDish {
    private String restName;
    private String dishName;
    private String category;
    private String street;
    private String city;
    private String state;
    private String zip;
    private Float latitude;
    private Float longitude;
    private Float slurpScore;
    private Integer reviewCount;
    private String restImageUrl;

    public RestaurantDish(){}


    public RestaurantDish(String restName, String dishName, String category, String street, String city, String state, String zip, Float latitude, Float longitude, Float slurpScore, Integer reviewCount, String restImageUrl) {
        this.restName = restName;
        this.dishName = dishName;
        this.category = category;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.slurpScore = slurpScore;
        this.reviewCount = reviewCount;
        this.restImageUrl = restImageUrl;
    }

    // Add a review and adjust the slurpScore and reviewCount
    public void addReview(long newScore){
        Float updatedScore = (getSlurpScore() * getReviewCount() + newScore) / (getReviewCount() + 1);
        setSlurpScore(updatedScore);
        setReviewCount(getReviewCount() + 1);
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getSlurpScore() {
        return slurpScore;
    }

    public void setSlurpScore(Float slurpScore) {
        this.slurpScore = slurpScore;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getRestImageUrl() {
        return restImageUrl;
    }

    public void setRestImageUrl(String restImageUrl) {
        this.restImageUrl = restImageUrl;
    }

}
