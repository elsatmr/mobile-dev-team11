package edu.northeastern.team11.slurp;

public class Dish {
    private String dishName;
    private String imageUrl;
    private String restaurantId;
    private long slurpScore;
    private long timestamp;
    private String userName;

    public Dish(String dishName, String imageUrl, String restaurantId, long slurpScore, long timestamp, String userName) {
        this.dishName = dishName;
        this.imageUrl = imageUrl;
        this.restaurantId = restaurantId;
        this.slurpScore = slurpScore;
        this.timestamp = timestamp;
        this.userName = userName;
    }

    public Dish() {}

    public String getDishName() {
        return dishName;
    }

    public String getImageUrl() {
        return imageUrl;
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
