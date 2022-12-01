package edu.northeastern.team11.slurp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.team11.Food;
import edu.northeastern.team11.MealAPI;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Helper class that queries Yelp Fusion API to get a list of nearby restaurants within a 500 meter radius
public class YelpRestaurants {

    private YelpAPI yelpAPI;
    private List<Restaurant> restaurantList;
    private Double myLatitude;
    private Double myLongitude;
    private Long searchRadius;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;

    public YelpRestaurants(Context c) {
        this.restaurantList = new ArrayList<>();

        // Define Interceptor and add authentication header - needed for YELP API authentication
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer nkFzyQ5Wz9mTHl3kuCkgVG_tYiUHPF-hykvJqVk5ArYsmy3YusWDJzbcVlPK5mqF3SOnQnXiOZ8MinbWfhgxECAOEw7EsoR2MMkAvapqmaQuoLjcQWPk9AnX0JN3Y3Yx").build();
                return chain.proceed(newRequest);
            }
        };

        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yelp.com/v3/businesses/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yelpAPI = retrofit.create(YelpAPI.class);
        searchRadius = 500L;
        context = c;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Get permissions if needed and then query Yelp Fusion -> populates restaurantList
    public List<Restaurant> getNearbyRestaurants() {
        // Check if permissions are enabled
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Dont have location permissions - return empty List
            Log.d("Yelp Restaurants", "Don't have location permissions");
            return new ArrayList<>();
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                myLatitude = location.getLatitude();
                                myLongitude = location.getLongitude();
                                requestRestaurantsFromAPI(myLatitude, myLongitude, searchRadius);
                            } else {
                                Log.d("Location is", "null");
                            }

                        }
                    });
            return restaurantList;
        }
    }



    // GET request from API
    private void requestRestaurantsFromAPI(Double latitude, Double longitude, Long radius) {
        Log.d("restzzz", "called requestRestaurantsfromAPI");
        Call<JsonElement> call = yelpAPI.getRestaurants(String.valueOf(latitude), String.valueOf(longitude), String.valueOf(radius));
        Log.d("restzzz", call.request().toString());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Log.d("restzzz", String.valueOf(response.code()));
                    return;
                }
                JsonElement jsonElement = response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (!jsonObject.get("businesses").isJsonNull()) {
                    JsonArray businesses = (JsonArray) jsonObject.get("businesses");
                    for (int i = 0; i < businesses.size(); i++) {
                        JsonObject item = businesses.get(i).getAsJsonObject();
                        // if business isn't a duplicate
                        if (isNotDuplicate(String.valueOf(item.get("id")))) {
                            String name = String.valueOf(item.get("name"));
                            String street = String.valueOf(item.get("location").getAsJsonObject().get("address1"));
                            String city = String.valueOf(item.get("location").getAsJsonObject().get("city"));
                            String state = String.valueOf(item.get("location").getAsJsonObject().get("state"));
                            String zip = String.valueOf(item.get("location").getAsJsonObject().get("zip_code"));
                            String phone = String.valueOf(item.get("phone"));
                            String imageUrl = String.valueOf("image_url");
                            String id = String.valueOf("id");
                            Long latitude = item.get("coordinates").getAsJsonObject().get("latitude").getAsLong();
                            Long longitude = item.get("coordinates").getAsJsonObject().get("longitude").getAsLong();
                            Restaurant rest = new Restaurant(name, street, city, state, zip, phone, imageUrl, id, latitude, longitude);
                            restaurantList.add(rest);
                        }
                    }
//                    for (Restaurant rest : restaurantList) {
//                        Log.d("Got Restaurant:", rest.getName());
//                    }
                }
            }

            // Ensure no duplicates
            private boolean isNotDuplicate(String id) {
                return !restaurantList.stream().anyMatch(x -> id.equals(x.getId()));
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("YELP API CALL", "FAILED");
            }
        });

    }

}