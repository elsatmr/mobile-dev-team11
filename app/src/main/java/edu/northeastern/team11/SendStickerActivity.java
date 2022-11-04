package edu.northeastern.team11;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendStickerActivity extends AppCompatActivity {

    private TextView header;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        // get UI elements
        header = findViewById(R.id.send_screen_instructions);
        listView = findViewById(R.id.list_view);

        // set up listView adapter and arraylist to add to adapter
        ArrayList<String> usersList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_item, usersList);
        listView.setAdapter(adapter);

        // get ref to database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    usersList.add(snapshot.getKey());

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
