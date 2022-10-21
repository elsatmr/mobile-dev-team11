package edu.northeastern.team11;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

// TODO: finish implementing the java Json object

public class Food {
    private String meals;

    @SerializedName("body")
    private String text;

    public String getMeals() {
        return meals;
    }

    public String getText() {
        return text;
    }
}
