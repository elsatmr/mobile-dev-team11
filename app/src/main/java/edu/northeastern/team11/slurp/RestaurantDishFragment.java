package edu.northeastern.team11.slurp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import edu.northeastern.team11.R;

public class RestaurantDishFragment extends Fragment {
    RestaurantDish restDish;
    TextView restName;
    TextView dishName;
    TextView street;
    TextView city;
    TextView zip;
    TextView state;
    Chip reviewCount;
    Chip slurpScore;
    ImageView restImageView;
    FloatingActionButton goBackFAB;

    public RestaurantDishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restDish = getArguments().getParcelable("restDish");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slurp_fragment_restaurant_dish, container, false);
        restName = view.findViewById(R.id.restaurantNameTextView);
        dishName = view.findViewById(R.id.dishNameTextView);
        street = view.findViewById(R.id.streetTextView);
        city = view.findViewById(R.id.cityNameTextView);
        state = view.findViewById(R.id.stateNameTextView);
        zip = view.findViewById(R.id.zipCodeTextView);
        reviewCount = view.findViewById(R.id.reviewsChip);
        slurpScore = view.findViewById(R.id.scoreChip);
        restImageView = view.findViewById(R.id.restaurantImageView);
        goBackFAB = view.findViewById(R.id.restDishGoBackFAB);
        goBackFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        if (restDish != null) {
            restName.setText(restDish.getRestName());
            dishName.setText(restDish.getDishName());
            street.setText(restDish.getStreet());
            city.setText(restDish.getCity());
            zip.setText(restDish.getZip());
            reviewCount.setText("Reviews: " + restDish.getReviewCount());
            slurpScore.setText("Score: " + restDish.getSlurpScore());
        }
        new BackgroundThread(restImageView, restDish.getRestImageUrl()).start();
        return view;
    }

    // Load the image of the restaurant on the top of the screen

    class BackgroundThread extends Thread {
        ImageView imageView;
        String imageUri;

        BackgroundThread(ImageView imageView, String url) {
            this.imageView = imageView;
            this.imageUri = url;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                imageView.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });
//                threadHolder.dishCategoryLabel.setText(label);
            } catch (MalformedURLException e) {
                Log.d("THREAD", e.getMessage());
                System.out.println("The URL is not valid.");
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void goBack(){
        Bundle bundle = new Bundle();
        bundle.putString("category", restDish.getCategory());
        bundle.putString("subcategory", restDish.getDishName());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, DishMapFragment.class, bundle).commit();
    }

}
