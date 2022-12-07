package edu.northeastern.team11.slurp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private FloatingActionButton imageButton;
    private FloatingActionButton submitButton;
    private TextInputEditText dishName;
    private TextInputEditText restaurantName;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slurp_fragment_add_item, container, false);
//        String userName = getCurUserAddItemFrag();
        imageButton = (FloatingActionButton) view.findViewById(R.id.captureImage);
        submitButton = (FloatingActionButton) view.findViewById(R.id.submitButton);
        image = (ImageView) view.findViewById(R.id.imageView);
//        private TextInputEditText dishName;
//        private TextInputEditText restaurantName;
        dishName = (TextInputEditText) view.findViewById(R.id.dishName);
        restaurantName = (TextInputEditText) view.findViewById(R.id.restaurantName);


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
                String restaurant = restaurantName.getText().toString();
                addToDataBase(dish, restaurant);
            }
        });


//        TextView tv = (TextView) view.findViewById(R.id.addItem_frag_user);
//        tv.setText("ADD ITEM FRAG, Current User: " + userName);

        return view;
    }

    private void addToDataBase(String dish, String restaurant) {
        System.out.println("Dish: " + dish);
        System.out.println("Restaurant: " + restaurant);
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
                Uri imageUri = FileProvider.getUriForFile(
                        requireActivity(),
                        "edu.northeastern.team11.fileprovider",
                        imageFile
                );
                System.out.println("URI"+imageUri);
                image.setBackgroundColor(Color.parseColor("#ffffff"));
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
            }
            catch (Exception exception) {
                Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    // get the current user from shared preferences
    private String getCurUserAddItemFrag() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        return sharedPreferences.getString("username", null);
    }
}