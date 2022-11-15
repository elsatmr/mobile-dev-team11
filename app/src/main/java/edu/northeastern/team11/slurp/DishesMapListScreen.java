package edu.northeastern.team11.slurp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import edu.northeastern.team11.R;

public class DishesMapListScreen extends AppCompatActivity {

    MapView mapView;
    String category;
    TextView categoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slurp_search_dishes_maplist);
        categoryView = findViewById(R.id.category);
        if (getIntent().getExtras().getString("category") != null) {
            category = getIntent().getExtras().getString("category");
            categoryView.setText(category);
        }
        // Setup the bottom navigation tabs
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.search_dishes);
        bottomNavigationView.setOnItemSelectedListener(new BottomTabListener(this));

        // Hide top action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // MapBox
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
    }

}
