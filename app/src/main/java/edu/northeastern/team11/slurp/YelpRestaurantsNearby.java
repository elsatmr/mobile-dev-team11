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

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.team11.Food;
import edu.northeastern.team11.MealAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Helper class that queries Yelp Fusion API to get a list of nearby restaurants within a 500 meter radius
public class YelpRestaurantsNearby {

    private YelpAPI yelpAPI;
    private List<Restaurant> restaurantList;
    private Long myLatitude;
    private Long myLongitude;
    private Long searchRadius;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;

    public YelpRestaurantsNearby(Context c) {
        this.restaurantList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yelp.com/v3/businesses/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yelpAPI = retrofit.create(YelpAPI.class);
        searchRadius = 500L;
        context = c;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Get permissions if needed and then query Yelp Fusion -> populates restaurantList
    public void getNearbyRestaurants() {
        // Check if permissions are enabled
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }, 1223);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            myLatitude = (long) location.getLatitude();
                            myLongitude = (long) location.getLongitude();
                        }
                    }
                });
        // Make API call
        requestRestaurantsFromAPI(myLatitude, myLongitude, searchRadius);
    }

    // GET request from API
    private void requestRestaurantsFromAPI(Long latitude, Long longitude, Long radius) {
        Call<JsonElement> call = yelpAPI.getRestaurants(String.valueOf(latitude), String.valueOf(longitude), String.valueOf(radius));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful()) {
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
                    for (Restaurant rest : restaurantList) {
                        Log.d("Got Restaurant:", rest.getName());
                    }
                }
            }

            // Check if a food exists in foodList based on the id received from the API
            // Return true if the idMeal parameter is not already in foodList
            private boolean isNotDuplicate(String id) {
                return !restaurantList.stream().anyMatch(x -> id.equals(x.getId()));
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("YELP API CALL", "FAIL");
            }
        });
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void printRestaurants() {
        final int[] counter = {0};
        restaurantList.forEach(d -> {
            Log.d(String.valueOf(counter[0]), d.getName());
            counter[0] += 1;
        });
    }

}
