package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AtYourServiceActivity extends AppCompatActivity {

    List<Food> foodList;
    RecyclerView foodRecyclerView;
    FoodAdapter adapter;

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
    private ProgressBar loadingSpinner;
    MealAPI mealAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);

        // Get references to Views
        searchInput = findViewById(R.id.searchInput);
        clearButton = findViewById(R.id.clearTextBtn);
        constLayout = findViewById(R.id.constLayoutView);
        searchButton = findViewById(R.id.searchButton);
        toolbar = findViewById(R.id.toolbar);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        chipGroup = findViewById(R.id.chipGroup);
        // Instantiate variables

        foodList = new ArrayList<>();
        textInputContents = "";
        searchList = new ArrayList<String>();
        chipID = 0;
        chipIDs = new ArrayList<Integer>();
        chipList = new ArrayList<Chip>();
        searchButton.setEnabled(false);
        loadingSpinner.setVisibility(View.INVISIBLE);
        // Setup UI responsiveness
        handleTyping();
        // Create retrofit mealAPI object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealAPI = retrofit.create(MealAPI.class);

        foodRecyclerView = findViewById(R.id.results);
        foodRecyclerView.setHasFixedSize(true);
        //This defines the way in which the RecyclerView is oriented
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Associates the adapter with the RecyclerView
        adapter = new FoodAdapter(foodList, this);
        foodRecyclerView.setAdapter(adapter);
    }




    // UI to update as user types
    private void handleTyping() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Capture the string and remove leading/trailing whitespaces
                textInputContents = charSequence.toString().trim();
                // Show/hide clearButton and enable/disable search Button
                if (textInputContents.length() > 0) {
                    clearButton.setVisibility(View.VISIBLE);
                    searchButton.setEnabled(true);
                } else {
                    clearButton.setVisibility(View.INVISIBLE);
                    searchButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // Search if user presses "enter" on keyboard
        searchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    searchFood();
                }
                return false;
            }
        });
    }

    // Helper function to clear the search EditText and remove focus
    public void clearSearchInput(View view) {
        searchInput.setText("");
        textInputContents = "";
        searchInput.requestFocus();
    }


    // Search for food and add a chip to the UI
    public void searchFood(View view) {
        if (textInputContents.length() > 0 && chipNotDuplicate(textInputContents)) {
            requestFoodFromAPI(textInputContents);
            addChip(textInputContents);
        }
    }

    // Search for food and add a chip to the UI
    private void searchFood() {
        if (textInputContents.length() > 0 && chipNotDuplicate(textInputContents)) {
            requestFoodFromAPI(textInputContents);
            addChip(textInputContents);
        }
    }

    // return true if searchTerm is not already a chip
    private boolean chipNotDuplicate(String searchTerm) {
        return !searchList.stream().anyMatch(x -> x.equals(searchTerm));
    }

    // Add a chip
    // THIS SHOULD TRIGGER THE GET REQUEST
    private void addChip(String searchString) {
        // Create the chip and give it an ID
        Chip newChip = new Chip(this);
        newChip.setId(chipID);
//        chipIDs.add(chipID);
        chipID++;
        searchList.add(searchString);
        newChip.setText(searchString);
        newChip.setTextColor(Color.parseColor("#FAF8F8"));
        newChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#19444C")));
        clearSearchInput(null);

        // Set the onClose listener for each chip
        newChip.setCloseIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_close_clear_cancel));
        newChip.setCloseIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        newChip.setCloseIconVisible(true);
        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(newChip);
                searchList.remove(newChip.getText());
                chipList.remove(newChip);
                for (Food food : foodList) {
                    List<String> searchTerms = food.getmSearchTerms();
                    if (searchTerms.contains(searchString)) {
                        searchTerms.remove(searchString);
                    }
                    if (searchTerms.size() == 0) {
                        foodList.remove(food);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        chipList.add(newChip);
        chipGroup.addView(newChip);
        closeKeyboard();
        searchInput.clearFocus();
    }

    // Close the keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("chipID", chipID);
//        outState.putSerializable("chipIDs", (Serializable) chipIDs);
        outState.putSerializable("searchList", (Serializable) searchList);
        outState.putSerializable("chipList", (Serializable) chipList);
        chipGroup.removeAllViews();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        chipID = savedInstanceState.getInt("chipID");
//        chipIDs = (List<Integer>) savedInstanceState.getSerializable("chipIDs");
        searchList = (List<String>) savedInstanceState.getSerializable("searchList");
        chipList = (List<Chip>) savedInstanceState.getSerializable("chipList");
        chipList.forEach(x -> {
            chipGroup.addView(x);
            x.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipGroup.removeView(x);
                    searchList.remove(x.getText());
                    chipList.remove(x);
                }
            });
        });
    }

    // GET request from API
    private void requestFoodFromAPI(String searchWord) {
        loadingSpinner.setVisibility(View.VISIBLE);
        Call<JsonElement> call = mealAPI.getMeals(searchWord);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                JsonElement jsonElement = response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (!jsonObject.get("meals").isJsonNull()) {
                    JsonArray foods = (JsonArray) jsonObject.get("meals");
                    Log.d("FOOD", String.valueOf(foods.get(0).isJsonObject()));
                    for (int i = 0; i < foods.size(); i++) {
                        JsonObject item = foods.get(i).getAsJsonObject();
                        // if food isn't a duplicate
                        if (foodNotDuplicate(String.valueOf(item.get("idMeal")))) {
                            Food food = new Food();
                            food.setMidMeal(String.valueOf(item.get("idMeal")));
                            food.setmStrMeal(String.valueOf(item.get("strMeal")));
                            food.setmStrMealThumb(String.valueOf(item.get("strMealThumb")));
                            food.setmStrTags(String.valueOf(item.get("strTags")));
                            food.setmSearchTerms(searchWord);
                            foodList.add(food);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    for (Food food : foodList) {
                        Log.d("FOODNAME", food.getmStrMeal());
                    }
                }
            }

            // Check if a food exists in foodList based on the id received from the API
            // Return true if the idMeal parameter is not already in foodList
            private boolean foodNotDuplicate(String idMeal) {
                return !foodList.stream().anyMatch(x -> idMeal.equals(x.getMidMeal()));
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
        loadingSpinner.setVisibility(View.INVISIBLE);
    }
}