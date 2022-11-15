package edu.northeastern.team11.slurp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.team11.R;

public class SearchDishesScreen extends AppCompatActivity {

    private List<DishCategory> dishCategoryList;
    RecyclerView dishCategoryRecycler;
    DishCategoryAdapter adapter;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slurp_search_dishes);
        dishCategoryList = new ArrayList<>();

        // Firebase - get Stickers as placeholder
        db = FirebaseDatabase.getInstance().getReference();
        getCuisines();

        // RecyclerView setup
        dishCategoryRecycler = findViewById(R.id.dishCategoryRecycler);
        dishCategoryRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new DishCategoryAdapter(dishCategoryList, this);
        dishCategoryRecycler.setAdapter(adapter);

    }

    // Get the cuisines from the database
    // USING STICKERS AS PLACEHOLDER!
    private void getCuisines() {
        db.child("stickers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishCategoryList.clear();
                for (DataSnapshot dish : snapshot.getChildren()) {
                    String cat = dish.getValue(String.class);
                    DishCategory dishCategory = new DishCategory(cat);
                    dishCategoryList.add(dishCategory);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
