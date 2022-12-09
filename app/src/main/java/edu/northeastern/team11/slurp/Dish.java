package edu.northeastern.team11.slurp;

public class Dish {
    private String dishName;
    private String imageUrl;
    private String restaurantId;
    private long slurpScore;
    private long timestamp;
    private String userName;
    private boolean isFavorite;
    private String restaurantName;

    public Dish(String dishName, String imageUrl, String restaurantId, long slurpScore, long timestamp, String userName, boolean isFavorite, String restaurantName) {
        this.dishName = dishName;
        this.imageUrl = imageUrl;
        this.restaurantId = restaurantId;
        this.slurpScore = slurpScore;
        this.timestamp = timestamp;
        this.userName = userName;
        this.isFavorite = isFavorite;
    }

    public Dish() {}

    public String getDishName() {
        return dishName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public long getSlurpScore() {
        return slurpScore;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }
}
