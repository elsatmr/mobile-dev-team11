package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ConstraintLayout constLayout;
    private Toolbar toolbar;
    private EditText searchInput;
    private ImageButton clearButton;
    private String textInputContents;
    protected List<String> searchList; // List of search strings
    private List<Integer> chipIDs; // List of the ids for the chips
    private int chipID; // counter used to dynamically create chip Ids
    private List<Chip> chipList;
    private FloatingActionButton searchButton;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);
        addSearchButtonListener();
        result_tv = findViewById(R.id.results);
        search_button = findViewById(R.id.search_button);
        searchBar = findViewById(R.id.search_bar);
        foodList = new ArrayList<>();
        searchList = new ArrayList<String>();
        searchInput = findViewById(R.id.searchInput);
        clearButton = findViewById(R.id.clearTextBtn);
        constLayout = findViewById(R.id.constLayoutView);
        searchButton = findViewById(R.id.searchButton);
        toolbar = findViewById(R.id.toolbar);
        searchButton.setEnabled(false);
        chipID = 0;
        chipIDs = new ArrayList<Integer>();
        chipList = new ArrayList<Chip>();
        handleTyping();

    }


    // Set the
    private void addSearchButtonListener() {
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
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (!response.isSuccessful()) {
                            result_tv.setText("Code: " + response.code());
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
                        for (Food food : foodList) {
                            Log.d("FOODNAME", food.getmStrMeal());
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