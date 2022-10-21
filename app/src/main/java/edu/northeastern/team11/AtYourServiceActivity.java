package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AtYourServiceActivity extends AppCompatActivity {
    TextView result_tv;
    Button search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);

        result_tv = findViewById(R.id.results);
        search_button = findViewById(R.id.search_button);


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.themealdb.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                MealAPI mealAPI = retrofit.create(MealAPI.class);

                // retrofit does implementation for this
                // TODO: pass the correct parameters (i.e. category, food name etc)
                Call<List<Meals>> call = mealAPI.getMeals();

                // execute network request on a different thread using enqueue() so that
                // the main thread is not blocked
                call.enqueue(new Callback<List<Meals>>() {
                    @Override
                    public void onResponse(Call<List<Meals>> call, Response<List<Meals>> response) {
                        // triggered when get a response back from the server, but it doesn't mean that the
                        // response was successful (code between 200-300). otherwise, print
                        // the response code
                        if (!response.isSuccessful()) {
                            result_tv.setText("Code: " + response.code());
                            return;
                        }

                        // the data you get from the webservice, then display in textview
                        List<Meals> meals = response.body();


                        // TODO: edit content to include relevant information about the food item (category, title etc)
                       for (Meals meal : meals) {
                           List<Food> foods = meal.getFoodsObjectList();
                           for (Food food : foods) {
                               String content = "";
                               content += food.getmStrMeal();
                               content += food.getmStrCategory();
                               content += food.getmStrTags();

                               // append so don't overwrite current text
                               result_tv.append(content);
                           }
                       }

//                        String content = meals.toString();
//                        result_tv.append(content);
                    }

                    @Override
                    public void onFailure(Call<List<Meals>> call, Throwable t) {
                        // something when wrong in communication with the server
                        // when attempting to get a response back
                        result_tv.setText(t.getMessage());

                    }
                });

            }
        });

    }
}