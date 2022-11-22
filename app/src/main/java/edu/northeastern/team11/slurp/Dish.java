package edu.northeastern.team11.slurp;

public class Dish {
    private String dishName;
    private String imageUrl;
    private String restaurantId;
    private String slurpScore;
    private int timestamp;
    private String userName;

    public Dish(String dishName, String imageUrl, String restaurantId, String slurpScore, int timestamp, String userName) {
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

    public String getSlurpScore() {
        return slurpScore;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }
}
