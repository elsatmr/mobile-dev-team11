package edu.northeastern.team11;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Meals {
    String meal;

    @SerializedName("body")
//    private List<Food> _food = new ArrayList<>();
    String foodListString;

    List<Food> foodsObjectList;

    public Meals(String meal, List<Food> foods) throws JSONException {
        this.meal = meal;
//        this.foodListString = foodList;

        this.foodsObjectList = createFoods(foodListString);
    }

    public List<Food> getFoodsObjectList() {
        return foodsObjectList;
    }


    public List<Food> createFoods(String foodListString) throws JSONException {
        Gson gson = new Gson();

        JSONObject jsonObject = new JSONObject(foodListString);
        JSONArray jsonArray = jsonObject.getJSONArray("meals");
        List<String> foodsList = new ArrayList<>();

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++ ) {
                foodsList.add(jsonArray.getString(i));
            }
        }

        List<Food> foodObjects = new ArrayList<>();
        for (String s : foodsList) {
            Food food = gson.fromJson(s, Food.class);
            foodObjects.add(food);
        }
        return foodObjects;

    }

}
