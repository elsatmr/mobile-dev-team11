package edu.northeastern.team11.slurp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import edu.northeastern.team11.R;

public class RestaurantDishFragment extends Fragment {
    RestaurantDish restDish;

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
        View view = inflater.inflate(R.layout.slurp_fragment_dishes_maplist, container, false);


        return view;
    }

}
