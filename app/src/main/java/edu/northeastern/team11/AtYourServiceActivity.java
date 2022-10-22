package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AtYourServiceActivity extends AppCompatActivity {
    TextView result_tv;
    Button search_button;
    EditText searchBar;
    List<Food> foodList;
    RecyclerView foodRecyclerView;
    FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);

        //result_tv = findViewById(R.id.results);
        search_button = findViewById(R.id.search_button);
        searchBar = findViewById(R.id.search_bar);
        foodList = new ArrayList<>();

        foodRecyclerView = findViewById(R.id.results);
        foodRecyclerView.setHasFixedSize(true);
        //This defines the way in which the RecyclerView is oriented
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Associates the adapter with the RecyclerView
        adapter = new FoodAdapter(foodList, this);
        //adapter = new FoodAdapter(foodList, getApplicationContext());
        foodRecyclerView.setAdapter(adapter);


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                MealAPI mealAPI = retrofit.create(MealAPI.class);

                Call<JsonElement> call = mealAPI.getMeals(searchBar.getText().toString());

                call.enqueue(new Callback<JsonElement>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (!response.isSuccessful()) {
                            //result_tv.setText("Code: " + response.code());
                            return;
                        }

                        JsonElement jsonElement = response.body();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonArray foods = (JsonArray) jsonObject.get("meals");
                        Log.d("FOOD", String.valueOf(foods.get(0).isJsonObject()));
                        for (int i = 0; i < foods.size(); i++) {
                            JsonObject item = foods.get(i).getAsJsonObject();
                            Food food = new Food();
                            food.setMidMeal(String.valueOf(item.get("idMeal")));
                            food.setmStrMeal(String.valueOf(item.get("strMeal")));
                            food.setmStrMealThumb(String.valueOf(item.get("strMealThumb")));
                            food.setmStrTags(String.valueOf(item.get("strTags")));
                            foodList.add(food);
                        }

                        adapter.notifyDataSetChanged();

                        for (Food food : foodList) {
                            Log.d("FOODNAME", food.getmStrMeal());
                            Log.d("FOODPHOTO", food.getmStrMealThumb());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        result_tv.setText(t.getMessage());
                    }
                });

            }
        });

    }
}