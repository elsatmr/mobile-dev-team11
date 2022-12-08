package edu.northeastern.team11.slurp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private EditText dishName;
    private EditText restaurantName;

    String[] restaurantsList = {"Donut Villa Diner", "Donut Villa Diner", "Pasta Pasta", "Annie's Pizzeria Medford"};
    String[] categoriesList = {"Italian", "Mexican", "Chinese", "Korean", "Mediterranean"};
    Float[] slurpScoreslist = {
            0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f
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
        username = getCurUserProfileFrag();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slurp_fragment_add_item, container, false);
//        String userName = getCurUserAddItemFrag();
        imageButton = (FloatingActionButton) view.findViewById(R.id.captureImage);
        submitButton = (FloatingActionButton) view.findViewById(R.id.submitButton);
        image = (ImageView) view.findViewById(R.id.imageView);
        image.setBackgroundColor(Color.parseColor("#673AB7"));
        dishName = (EditText) view.findViewById(R.id.dishName);
        //restaurantName = (EditText) view.findViewById(R.id.restaurantName);


        AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.restaurantName);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, restaurantsList);
        autoComplete.setAdapter(adapter);


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
                    new String[] {
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
                            new String[] {
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            REQUEST_CODE_PERMISSIONS
                    );
                }
                else {
                    System.out.println("dispatchCaptureImageIntent called");
                    dispatchCaptureImageIntent();
                }
            }
        });
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dish = dishName.getText().toString();
                //String restaurant = restaurantName.getText().toString();
                addToDataBase();
            }
        });
//        TextView tv = (TextView) view.findViewById(R.id.addItem_frag_user);
//        tv.setText("ADD ITEM FRAG, Current User: " + userName);

        return view;
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
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
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
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }

//        username
//        storageURi
//        dish
//        restaurant

//        private String dishName;
//        private String restId;
//        private Float slurpScore;
//        private Integer timestamp;

        YelpRestaurants restaurants = new YelpRestaurants(getActivity());
        List<Restaurant> restaurantsList = restaurants.getNearbyRestaurants();
        System.out.println("nearby restaurants count" + restaurantsList.size());
        System.out.println("nearby restaurants: ");
        for (int i = 0; i < 50 && i < restaurantsList.size(); i++) {
            System.out.println(restaurantsList.get(i));
        }
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
                Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
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