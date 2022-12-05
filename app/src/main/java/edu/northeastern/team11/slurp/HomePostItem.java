package edu.northeastern.team11.slurp;

public class HomePostItem {
    private String imgUrl;
    private String dishName;
    private String slurperScore;
    private String author;

    public HomePostItem() {
    }

    public HomePostItem(String imgUrl, String dishName, String slurperScore, String author) {
        this.imgUrl = imgUrl;
        this.dishName = dishName;
        this.slurperScore = slurperScore;
        this.author = author;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getSlurperScore() {
        return slurperScore;
    }

    public void setSlurperScore(String slurperScore) {
        this.slurperScore = slurperScore;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
