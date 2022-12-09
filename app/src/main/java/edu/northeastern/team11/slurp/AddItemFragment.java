package edu.northeastern.team11.slurp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import edu.northeastern.team11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int REQUEST_CODE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String currentImagePath;
    private ImageView image;
    private Uri imageUri;
    private FloatingActionButton imageButton;
    private FloatingActionButton submitButton;
    private AutoCompleteTextView dishName;
    private Spinner restaurantName;
//    private Spinner categorySpinner;
    private Spinner scoreSpinner;
    private List<String> dishesFromDb;
    private List<String> restaurantsToShow;
    private List<String> categoriesFromDb;
    private List<Restaurant> restaurantListFromAPI;
    private ArrayList<String> dishesForRest;
    ArrayAdapter adapter1;
    ArrayAdapter adapter2;

    //Location
    private Location myLocation = null;
    private Double myLatitude = 42.31295765423661;
    private Double myLongitude = -71.1015306291878;
    FusedLocationProviderClient client;


    HashMap<String, String> restaurantsListNEUFromDB;
    HashMap<String, String> restaurantCategoryMap;
    String[] categoriesList = {"Italian", "Mexican", "Chinese", "Korean", "Mediterranean"};
    String[] slurpScoreslist = {
            "Score", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    };

    // Database
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDb;
    // Storage
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String storageURI;
    private Post post;
    private String username;

    private String restaurantSelected;
    private Float slurpScoreSelected;
    private String dishNameSelected;
//    private String categorySelected;
    private boolean restaurantNew;
    String restaurantID;
    String categoryOfDishAdded;

    private String mParam1;
    private String mParam2;

    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddItemFragment newInstance(int param1, int param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putInt(String.valueOf(ARG_PARAM1), param1);
        args.putInt(String.valueOf(ARG_PARAM2), param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(String.valueOf(ARG_PARAM1));
            mParam2 = getArguments().getString(String.valueOf(ARG_PARAM2));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        client = LocationServices
                .getFusedLocationProviderClient(
                        getActivity());
        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReference();
        dishesFromDb = new ArrayList<>();
        restaurantListFromAPI = new ArrayList<>();
        restaurantsListNEUFromDB = new HashMap<>();
        restaurantCategoryMap  = new HashMap<>();
        restaurantsToShow = new ArrayList<>();
        categoriesFromDb = new ArrayList<>();
        dishesForRest = new ArrayList<>();
        getDishes();
//        getCategories();
        getDefaultRestaurantsFromDB();

        if (myLocation != null) {
            //Restaurants from API
            getRestaurants();
        }
        else {
            //Restaurants from DB
            getRestaurantsFromDB();
        }

        username = getCurUserProfileFrag();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slurp_fragment_add_item, container, false);
        imageButton = (FloatingActionButton) view.findViewById(R.id.captureImage);
        submitButton = (FloatingActionButton) view.findViewById(R.id.submitButton);
        image = (ImageView) view.findViewById(R.id.imageView);
        image.setBackgroundColor(Color.parseColor("#673AB7"));
        dishName = (AutoCompleteTextView) view.findViewById(R.id.dishName);
        restaurantName = (Spinner) view.findViewById(R.id.restaurantName);
//        categorySpinner = (Spinner) view.findViewById(R.id.categorySelection);
//        categorySpinner.setEnabled(false);
        scoreSpinner = (Spinner) view.findViewById(R.id.SlurpScoreSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dishesFromDb);
        dishName.setAdapter(adapter);

        restaurantsToShow.add(0, "Restaurant");
        adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, restaurantsToShow);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantName.setAdapter(adapter1);

//        adapter2 = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, categoriesFromDb);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, slurpScoreslist);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreSpinner.setAdapter(adapter3);

        dishName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length() > 0) {
                   dishNameSelected = dishName.getText().toString();
                }
            }
        });

        restaurantName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!restaurantName.getSelectedItem().toString().equals("Restaurant")) {
                    restaurantSelected = restaurantName.getSelectedItem().toString();
                    restaurantID = restaurantsListNEUFromDB.get(restaurantSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        scoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!scoreSpinner.getSelectedItem().toString().equals("Score")) {
                    slurpScoreSelected = Float.parseFloat(scoreSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //Request for permission
        if (ContextCompat.checkSelfPermission(
                getActivity().getApplicationContext(),
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                        getActivity().getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_CODE_PERMISSIONS
            );
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getActivity().getApplicationContext(),
                        Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(
                                getActivity().getApplicationContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            REQUEST_CODE_PERMISSIONS
                    );
                } else {
                    System.out.println("dispatchCaptureImageIntent called");
                    dispatchCaptureImageIntent();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (restaurantSelected != null && dishNameSelected != null && slurpScoreSelected != null && imageUri != null ) {
                        addToDataBase();
                    }
                    else {
                        Toast
                                .makeText(getActivity(),
                                        "Please fill out all fields",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
            }
        });
//        TextView tv = (TextView) view.findViewById(R.id.addItem_frag_user);
//        tv.setText("ADD ITEM FRAG, Current User: " + userName);

        return view;
    }

    private void getDefaultRestaurantsFromDB() {
        dbRef.child("slurpRestaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot restaurant : snapshot.getChildren()) {
                    String name = restaurant.child("name").getValue(String.class);
                    restaurantsListNEUFromDB.put(name, restaurant.getKey());
                    restaurantCategoryMap.put(restaurant.getKey(), restaurant.child("category").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getRestaurantsFromDB() {
        dbRef.child("slurpRestaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot restaurant : snapshot.getChildren()) {
                    String name = restaurant.child("name").getValue(String.class);
                    //RestaurantDish restaurant1 = restaurant.getValue(RestaurantDish.class);
                    restaurantsToShow.add(name);
                    adapter1.notifyDataSetChanged();
                    //System.out.println("Restaurant +" +restaurant + ", ");
                }
//                for (int i = 0; i < restaurantsToShow.size(); i++) {
//                    System.out.println(restaurantsToShow.get(i));
//                }
                //Log.i("restaurantsFromDB", restaurantsListNEUFromDB.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDishes() {
        dbRef.child("categoryTest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot category : snapshot.getChildren()) {
                    for (DataSnapshot dishes : category.getChildren()) {
                        for (DataSnapshot dish : dishes.getChildren()) {
                            dishesFromDb.add(dish.getKey() + "");
                        }
                    }

                }
                Log.i("dishesFromDB", dishesFromDb.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void getCategories() {
//        dbRef.child("categoryTest").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot category : snapshot.getChildren()) {
//                        categoriesFromDb.add(category.getKey() + "");
//                }
//                adapter2.notifyDataSetChanged();
////                System.out.println("categoriesFromDb");
////                System.out.println(categoriesFromDb.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void getRestaurants() {
        YelpRestaurantsForPost restaurantsForPost = new YelpRestaurantsForPost(getActivity(), myLongitude, myLatitude);
        restaurantListFromAPI = restaurantsForPost.getNearbyRestaurants();
    }

    private void addToDataBase() {
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (imageUri != null) {
            ProgressDialog progressDialog
                    = new ProgressDialog(this.getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "slurpPosts/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            storageURI = uri.toString();
                                        }
                                    });

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
//                                    Toast
//                                            .makeText(getActivity(),
//                                                    "Image Uploaded!!",
//                                                    Toast.LENGTH_SHORT)
//                                            .show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
//                            Toast
//                                    .makeText(getActivity(),
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.dismiss();
                                }
                            });
        }

        //Adding post
            Date date = new Date(System.currentTimeMillis());
            Post postToAdd = new Post(storageURI, dishNameSelected, restaurantID, slurpScoreSelected, (int)date.getTime(), username);
            DatabaseReference postsRef = dbRef.child("slurpPosts");
            DatabaseReference newPostRef = postsRef.push();
            newPostRef.setValue(postToAdd);

        //Adding a dish to CategoryTest table
        if (!dishesFromDb.contains(dishNameSelected)) {
            categoryOfDishAdded = restaurantCategoryMap.get(restaurantID);
            System.out.println("Category: "+categoryOfDishAdded);
            System.out.println("Dish name: "+dishNameSelected);
            addADishToDishCategoryTestTable(dishNameSelected, categoryOfDishAdded, storageURI);
        }

        //Adding a dish to Restaurant table
//        get dishes per restaurant
//        if not there add a new dish

        getDishesForRestaurant(restaurantID);

        System.out.println("Dishes For Restaurant");
        System.out.println(dishesForRest.size());

        addADishToRestaurantTable(dishNameSelected);

        //Updating slurper status point
        updateSlurperStatus();


        //Updating Restaurant table slurpScore
        updateSlurpScore(dishNameSelected, restaurantID, slurpScoreSelected);

        Toast.makeText(getActivity(),
                        "Post Uploaded!!",
                        Toast.LENGTH_SHORT)
                .show();
    }

    private void updateSlurpScore(String dishNameSelected, String restaurantID, Float givenScore) {

        dbRef.child("slurpRestaurants").child(restaurantID).child("dishes").child(dishNameSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer count = 0;
                Float newScore = 0.0f;
                Integer score = 0;
                for (DataSnapshot user: snapshot.getChildren()) {
                    count = snapshot.child("reviewCount").getValue(Integer.class);
                    score = snapshot.child("slurpScore").getValue(Integer.class);
                    System.out.println("Count Tomi" + count);
                }
                count =  count + 1;
                newScore = score + ((givenScore - score)/count);
                dbRef.child("slurpRestaurants").child(restaurantID).child("dishes").
                        child(dishNameSelected).child("reviewCount").setValue(count);
                dbRef.child("slurpRestaurants").child(restaurantID).child("dishes").
                        child(dishNameSelected).child("slurpScore").setValue(newScore);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addADishToRestaurantTable(String dishNameSelected) {
//        dbRef.child("categoryTest")
    }

    // TODO: Fix
    private void addADishToDishCategoryTestTable(String dishNameSelected, String category, String url) {
        dbRef.child("slurpPosts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbRef.child("categoryTest").child(category).child("dishes").child(dishNameSelected).push();
                dbRef.child("categoryTest").child(category).child("dishes").child(dishNameSelected).setValue(url);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateSlurperStatus() {

        dbRef.child("slurperStatusPoints").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer count = 0;
                for (DataSnapshot user: snapshot.getChildren()) {
                    count = snapshot.child("count").getValue(Integer.class);
                    System.out.println("Count Tomi" + count);
                }
                count =  count + 1;
                dbRef.child("slurperStatusPoints").child(username).child("count").setValue(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDishesForRestaurant(String restID) {

        dbRef.child("slurpRestaurants").child(restID).child("dishes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dish: snapshot.getChildren()) {
                    String strDish = dish.getKey();
                    dishesForRest.add(strDish);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        dbRef.child("slurpRestaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot restaurant : snapshot.getChildren()) {
                    String name = restaurant.child("name").getValue(String.class);
                    restaurantsListNEUFromDB.put(name, restaurant.getKey());
                    restaurantCategoryMap.put(restaurant.getKey(), restaurant.child("category").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dispatchCaptureImageIntent() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("dispatchCaptureImageIntent started");
        File imageFile = null;
        try {
            System.out.println("dispatchCaptureImageIntent");
            imageFile = createImageFile();
        }
        catch (IOException exception) {
            System.out.println("ERROR dispatchCaptureImageIntent");
            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (imageFile != null) {
            System.out.println("createImageFile");
            imageUri = FileProvider.getUriForFile(
                    requireActivity(),
                    "edu.northeastern.team11.fileprovider",
                    imageFile
            );
            System.out.println("URI"+imageUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);
        }
    }

    private File createImageFile() throws IOException {
        System.out.println("createImageFile started");
        String fileName = "IMAGE_" + new SimpleDateFormat(
                "yyyy_MM_DD_HH_mm_hh", Locale.getDefault()
        ).format(new Date());
        File directory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                fileName, ".jpg", directory
        );
        currentImagePath = imageFile.getAbsolutePath();
        System.out.println("Image path" + currentImagePath);
        return imageFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchCaptureImageIntent();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "NOT all permissions granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                image.setImageBitmap(BitmapFactory.decodeFile(currentImagePath));
                image.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            catch (Exception exception) {
//                Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    // get the current user from shared preferences
    private String getCurUserProfileFrag() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        return sharedPreferences.getString("username", null);
    }
}