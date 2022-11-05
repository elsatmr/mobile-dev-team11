package edu.northeastern.team11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllStickersActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDb;
    private List<Sticker> stickerList;
    StickerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stickers_screen);
        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReferenceFromUrl("https://stickers-19c0f-default-rtdb.firebaseio.com/");
        String username = this.getCurrentUser();
        Query userQuery = dbRef.child("users").child(username);
        stickerList = new ArrayList<>();
        Log.d("QUERY", userQuery.toString());
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    Sticker newSticker = s.getValue(Sticker.class);
                    newSticker.setUrlString(newSticker.getUrlString().replace("%26", "&"));
                    stickerList.add(newSticker);
                }
                adapter.notifyDataSetChanged();
                Log.d("SIZE", String.valueOf(stickerList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        RecyclerView stickerRecyclerView = findViewById(R.id.sticker_recycler_view);
        stickerRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new StickerAdapter(stickerList, this);
        stickerRecyclerView.setAdapter(adapter);
    }


    // get the username of who is signed in
    private String getCurrentUser() {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);
        return sharedPref.getString("username", null);
    }

}
