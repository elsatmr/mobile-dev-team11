package edu.northeastern.team11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendStickerActivity extends AppCompatActivity {

    private TextView header;
    private ListView listView;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        // get UI elements
        header = findViewById(R.id.send_screen_instructions);
        listView = findViewById(R.id.list_view);
        constraintLayout = findViewById(R.id.layout_send_sticker);

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
        sendSticker();




    }

    private void sendSticker() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // create a new transaction
                // update sentCount from sender
                // update receivedCount from receiver
//                Snackbar snackbar = Snackbar.make(constraintLayout, "Sticker sent!", Snackbar.LENGTH_LONG);
//                snackbar.show();

            }
        });
    }

    // when finished sending stickers, go back to the all stickers screen
    public void doneButtonPressed(View view) {
        Intent intent = new Intent(this, AllStickersActivity.class);
        startActivity(intent);
    }

}
