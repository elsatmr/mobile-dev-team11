package edu.northeastern.team11.slurp;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface YelpAPI  {

    @GET("search")
    Call<JsonElement> getRestaurants(@Query("latitude") String latitude,
                                     @Query("longitude") String longitude,
                                     @Query("radius") String radius);


}