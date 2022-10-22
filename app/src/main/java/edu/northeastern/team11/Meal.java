package edu.northeastern.team11;

import java.util.List;

public class Meal {
    private String title;
    private String photo;
    private String description;
    private boolean isEasy;
    private List<String> tags;

    public Meal(String title, String photo, String description, boolean isEasy, List<String> tags) {
        this.title = title;
        this.photo = photo;
        this.description = description;
        this.isEasy = isEasy;
        this.tags = tags;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPhoto() {
        return this.photo;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIsEasy() {
        return this.isEasy;
    }

    public List<String> getTags() {
        return this.tags;
    }

}
