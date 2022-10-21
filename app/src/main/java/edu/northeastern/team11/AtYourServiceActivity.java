package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                Call<List<Food>> call = mealAPI.getFoods();

                // execute network request on a different thread using enqueue() so that
                // the main thread is not blocked
                call.enqueue(new Callback<List<Food>>() {
                    @Override
                    public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                        // triggered when get a response back from the server, but it doesn't mean that the
                        // response was successful (code between 200-300). otherwise, print
                        // the response code
                        if (!response.isSuccessful()) {
                            result_tv.setText("Code: " + response.code());
                            return;
                        }

                        // the data you get from the webservice, then display in textview
                        // TODO: edit content to include relevant information about the food item
                        List<Food> foods = response.body();
                        for (Food food : foods) {
                            String content = "";
                            content += "Meal: " + food.getMeals() + "\n";

                            // append so don't overwrite the current text
                            result_tv.append(content);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Food>> call, Throwable t) {
                        // something when wrong in communication with the server
                        // when attempting to get a response back
                        result_tv.setText(t.getMessage());

                    }
                });

            }
        });

    }
}