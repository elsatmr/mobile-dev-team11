package edu.northeastern.team11;



import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// TODO: finish implementing the Meal API to only get foods and
//  relevant information that meet the search criteria
public interface MealAPI  {

    // retrofit will autogenerate necessary code to get all the foods in a list
    // TODO: probably need to use Query here to update the relative url param to
    //  include the user's argument from the search

    @GET("search.php")
    Call<JsonElement> getMeals(@Query("s") String query);


}
