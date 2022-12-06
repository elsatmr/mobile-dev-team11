package edu.northeastern.team11.slurp;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.LocationUpdate;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.team11.R;

public class DishMapFragment extends Fragment implements OnMapReadyCallback, OnLocationClickListener, PermissionsListener, OnCameraTrackingChangedListener {

    private String category;
    private String subcategory;
    private TextView categoryLabel;
    private ImageButton categoryButton;
    private ImageButton subcategoryButton;
    private TextView subcategoryLabel;
    FloatingActionButton homeFab;
    MapView mapView;
    MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private boolean isInTrackingMode;
    PermissionsManager permissionsManager;
    PermissionsListener permissionsListener;
    private DatabaseReference db;
    private List<RestaurantDish> restDishList;
    private RecyclerView restDishRecycler;
    private RestaurantDishAdapter adapter;
    List<Restaurant> restList;
    ConstraintLayout mapListLayout;
    Button reSearchMap;
    private Style thisStyle;

    // Add pins to map
    private SymbolManager symbolManager;
    private List<Symbol> symbolList;
    private static final String ID_ICON_PIN = "pin";
    MarkerViewManager markerViewManager;
    MarkerView markerView;

    public DishMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category");
            subcategory = getArguments().getString("subcategory");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));
        View view = inflater.inflate(R.layout.slurp_fragment_dishes_maplist, container, false);
        categoryLabel = view.findViewById(R.id.categoryValue);
        categoryLabel.setText(category);
        subcategoryLabel = view.findViewById(R.id.dishValue);
        subcategoryLabel.setText(subcategory);
        categoryButton = view.findViewById(R.id.categoryButton);
        subcategoryButton = view.findViewById(R.id.dishButton);
        addButtonListeners();
        db = FirebaseDatabase.getInstance().getReference();
        homeFab = view.findViewById(R.id.homeFab);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        restDishList = new ArrayList<>();
        symbolList = new ArrayList<>();
        restDishRecycler = view.findViewById(R.id.restDishListRecycler);
        adapter = new RestaurantDishAdapter(restDishList, view.getContext());
        restDishRecycler.setHasFixedSize(true);
        restDishRecycler.setLayoutManager(new StaggeredGridLayoutManager(
                1, StaggeredGridLayoutManager.VERTICAL));
        restDishRecycler.setAdapter(adapter);

        // Create "search again" button on map
        reSearchMap = view.findViewById(R.id.searchMapButton);
        reSearchMap.setVisibility(View.INVISIBLE);
        reSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPins();
                reSearchMap.setVisibility(View.INVISIBLE);
            }
        });
        // YELP restaurants nearby current position
        YelpRestaurants rest = new YelpRestaurants(getContext());
        restList = rest.getNearbyRestaurants();


        return view;

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        markerViewManager = new MarkerViewManager(mapView, mapboxMap);
        mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                addPinImageToStyle(style);
                thisStyle = style;
                // Get the restaurants and add pins to the map
                getRestaurants();
            }
        });
        mapboxMap.getUiSettings().setCompassMargins(0, 560, 20, 0);
        // When the map moves, allow the user to research the map
        mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                reSearchMap.setVisibility(View.VISIBLE);
            }
        });
        reSearchMap.setVisibility(View.INVISIBLE);
    }

    private void addPinImageToStyle(Style style) {
        style.addImage(ID_ICON_PIN,
                BitmapUtils.getBitmapFromDrawable(getContext().getDrawable(R.drawable.red_push_pin_10)), true);
    }


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

// Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getContext())
                    .elevation(5)
                    .accuracyAlpha(.2f)
                    .accuracyColor(Color.RED)
                    .pulseEnabled(true)
//                    .foregroundDrawable(com.mapbox.mapboxsdk.R.drawable.mapbox_info_bg_selector)
                    .build();

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();
            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(this);
            // Add the camera tracking listener. Fires if the map camera is manually moved.
            locationComponent.addOnCameraTrackingChangedListener(this);

            getView().findViewById(R.id.mapView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isInTrackingMode) {
                        isInTrackingMode = true;
                        locationComponent.setCameraMode(CameraMode.TRACKING);
                        locationComponent.zoomWhileTracking(16f);
                        Toast.makeText(getContext(), "???", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(this, "xxxxx",
//                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            addHomeClickListener();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
            enableLocationComponent(loadedMapStyle);
        }
    }


    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onLocationComponentClick() {

        // TESTING THE YELP API CALL!!!!!!
        restList.forEach(d -> {
            Log.d("restzzz", d.getName());
        });
        // END OF TEST

        if (locationComponent.getLastKnownLocation() != null) {
            Toast.makeText(getContext(), String.format("Latitude: %.2f\nLongitude: %.2f",
                    locationComponent.getLastKnownLocation().getLatitude(),
                    locationComponent.getLastKnownLocation().getLongitude()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCameraTrackingDismissed() {
        isInTrackingMode = false;
    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {
// Empty on purpose
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "Need permissions!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });

        } else {

            Toast.makeText(getContext(), "permissions denied", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void addHomeClickListener() {
        homeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(locationComponent.getLastKnownLocation().getLatitude(), locationComponent.getLastKnownLocation().getLongitude()))
                        .zoom(15)
//                        .bearing(180)
//                        .tilt(30)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
            }
        });
    }

    // Display the category recycler view so the user can select the category
    private void displayCategories() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, CategoryFragment.class, null).commit();
    }

    // Display the subcategory recyclerview so the user can select the dish/subcategory
    private void displaySubcategories() {
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, SubcategoryFragment.class, bundle).commit();
    }

    // Add listeners to the category and dish buttons on the UI
    private void addButtonListeners() {
        View.OnClickListener categorySelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCategories();
                Log.d("Should be =", "select category fragment");
            }
        };
        categoryLabel.setOnClickListener(categorySelectListener);
        categoryButton.setOnClickListener(categorySelectListener);
        View.OnClickListener subcategorySelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySubcategories();
                Log.d("Should be =", "select SUBcategory fragment");
            }
        };
        subcategoryButton.setOnClickListener(subcategorySelectListener);
        subcategoryLabel.setOnClickListener(subcategorySelectListener);
    }

    // Get the cuisines from the database
    private void getRestaurants() {
        db.child("slurpRestaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restDishList.clear(); // Clear the list to avoid duplication
                // 1. Build List of Restaurant Dishes
                for (DataSnapshot restDish : snapshot.getChildren()) {
                    if (restDish.child("dishes").child(subcategory).exists()) {
                        String _category = restDish.child("category").getValue(String.class);
                        String _city = restDish.child("city").getValue(String.class);
                        String _street = restDish.child("street").getValue(String.class);
                        String _state = restDish.child("state").getValue(String.class);
                        String _zip = restDish.child("zip").getValue(String.class);
                        String _restName = restDish.child("name").getValue(String.class);
                        Float _lat = restDish.child("lat").getValue(Float.class);
                        Float _long = restDish.child("long").getValue(Float.class);
                        String _restId = restDish.getKey();
                        Float _slurpScore = restDish.child("dishes").child(subcategory).child("slurpScore").getValue(Float.class);
                        Log.d("slurpScore", _slurpScore.toString());
                        Integer _reviewCount = restDish.child("dishes").child(subcategory).child("reviewCount").getValue(Integer.class);
                        String _restImageUrl = restDish.child("imageUrl").getValue(String.class);
                        RestaurantDish newRestDish = new RestaurantDish(_restName, _restId, subcategory, category, _street, _city, _state, _zip, _lat, _long, _slurpScore, _reviewCount, _restImageUrl);
                        restDishList.add(newRestDish);
                        Log.d("Added RestaurantDish:", newRestDish.getRestName());
                    }
                }
                adapter.notifyDataSetChanged();
                addPins();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Add pin to the map
    private void addPins() {
        for (Symbol s : symbolList) {
            symbolManager.delete(s);

        }
        symbolList.clear();

        for (RestaurantDish rest : restDishList) {
            // If the rest is within the bounds
            if (withinVisibleBounds(rest)) {
                Log.d("Adding", "New Pin");

                // Create new symbolManager so Pins can be added to map
                symbolManager = new SymbolManager(mapView, mapboxMap, thisStyle);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(true);

                // Set an onclick listener
                symbolManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public boolean onAnnotationClick(Symbol symbol) {
                        if (symbol.getIconColor().equals("#FF0000")) {
                            symbol.setIconColor("#A020F0");
                            TextView newText = new TextView(getContext());
                            newText.setText("IT WORKS!!!!!");
                            markerView = new MarkerView(new LatLng(rest.getLatitude(), rest.getLongitude()), newText);
                            markerViewManager.addMarker(markerView);
                        } else {
                            symbol.setIconColor("#FF0000");
                            markerViewManager.removeMarker(markerView);
                        }
                        symbolManager.update(symbol);
                        return true;
                    }
                });
                // Create the symbol on the map and add to symbolList
                Symbol symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(rest.getLatitude(), rest.getLongitude()))
                        .withIconImage(ID_ICON_PIN)
                        .withIconColor("#FF0000")
//                    .withTextField(rest.getRestName())
//                    .withTextOffset(new Float[]{1.0f, 1.0f})
                        .withIconSize(2.7f));
                symbolList.add(symbol);
            }
        }
    }

    // Returns true if the restDish is within the visible bounds of the map
    private boolean withinVisibleBounds(RestaurantDish restDish) {
        Double northBound = mapboxMap.getLatLngBoundsZoomFromCamera(mapboxMap.getCameraPosition()).getLatLngBounds().getLatNorth();
        Double southBound = mapboxMap.getLatLngBoundsZoomFromCamera(mapboxMap.getCameraPosition()).getLatLngBounds().getLatSouth();
        Double eastBound = mapboxMap.getLatLngBoundsZoomFromCamera(mapboxMap.getCameraPosition()).getLatLngBounds().getLonEast();
        Double westBound = mapboxMap.getLatLngBoundsZoomFromCamera(mapboxMap.getCameraPosition()).getLatLngBounds().getLonWest();
        if (restDish.getLatitude() <= northBound && restDish.getLatitude() >= southBound && restDish.getLongitude() >= westBound && restDish.getLongitude() <= eastBound) {
            return true;
        } else {
            return false;
        }
    }


}
