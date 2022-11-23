package edu.northeastern.team11.slurp;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface YelpAPI  {

    @Headers({"Authorization: Bearer nkFzyQ5Wz9mTHl3kuCkgVG_tYiUHPF-hykvJqVk5ArYsmy3YusWDJzbcVlPK5mqF3SOnQnXiOZ8MinbWfhgxECAOEw7EsoR2MMkAvapqmaQuoLjcQWPk9AnX0JN3Y3Yx"})
    @GET("search")
    Call<JsonElement> getRestaurants(@Query("latitude") String latitude,
                               @Query("longitude") String longitude,
                               @Query("radius") String radius);


}
