package edu.northeastern.team11;



import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

// TODO: finish implementing the Meal API to only get foods and
//  relevant information that meet the search criteria
public interface MealAPI  {

    // retrofit will autogenerate necessary code to get all the foods in a list
    @GET("add relative url here")
    Call<List<Food>> getFoods();


}
