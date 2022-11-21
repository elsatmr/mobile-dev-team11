package edu.northeastern.team11.slurp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.northeastern.team11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubcategoryFragment extends Fragment {

    private String category;

    // USED VARIABLES
    DatabaseReference db;
    RecyclerView recyclerView;
    DishCategoryAdapter adapter;
    List<DishCategoryItem> dishCategoryList;

    public SubcategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slurp_fragment_subcategory, container, false);

        // Firebase - get Stickers as placeholder
        db = FirebaseDatabase.getInstance().getReference();
        getCuisines();

        // Add recycler view for Categories
        dishCategoryList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.dishSubcategoryRecycler);

        adapter = new DishCategoryAdapter(dishCategoryList, view.getContext()) {
            @Override
            public void onBindViewHolder(@NonNull DishCategoryViewHolder holder, int position) {
                String categoryUrl = dishCategoryList.get(position).getCategoryUrl();
                String dish = dishCategoryList.get(position).getCategoryName();
                BackgroundThread thread = new BackgroundThread(holder, categoryUrl, category);
                holder.dishCategoryLabel.setText(dish.toUpperCase(Locale.ROOT));
                thread.start();
                holder.itemView.setOnClickListener(view -> {
                    Context context = view.getContext();
                    String dishCategoryId = String.valueOf(position);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putString("category", category);
                    bundle.putString("subcategory", dishCategoryList.get(position).getCategoryName());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, DishMapFragment.class, bundle).commit();
                });
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        // Have to return the view
        return view;
    }

    // Get the cuisines from the database
    // USING STICKERS AS PLACEHOLDER!
    private void getCuisines() {
        db.child("categoryTest").child(category).child("dishes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishCategoryList.clear();
                for (DataSnapshot dish : snapshot.getChildren()) {
                    String url = dish.getValue(String.class);
                    String label = dish.getKey();
                    DishCategoryItem dishCategoryItem = new DishCategoryItem(label, url);
                    dishCategoryList.add(dishCategoryItem);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}