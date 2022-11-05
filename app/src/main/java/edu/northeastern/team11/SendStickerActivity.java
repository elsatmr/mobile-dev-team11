package edu.northeastern.team11;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SendStickerActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        // get UI elements
        listView = findViewById(R.id.list_view);

        // set up listView adapter and arraylist to add to adapter
        ArrayList<String> usersList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_item, usersList);
        listView.setAdapter(adapter);

        // connect with database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        // read users in the database and notify adapter to display users on ui
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // get username from each user
                    usersList.add(snapshot.getKey());
                }
                // display each user in listview
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Send Stickers error", error.toString());
            }
        });
        sendSticker();
    }

    // handles clicking on user item in list view, then sending sticker to this user
    private void sendSticker() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the user the sticker will be sent too
                String receiver =  (String) adapterView.getItemAtPosition(i);
                addNewTransactionToDb(receiver);
            }
        });
    }

    // get index of new entry, then write the new transaction to the db
    private void addNewTransactionToDb(String receiver) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            // get the current date and time
            Calendar calendar = Calendar.getInstance();
            Date dt = calendar.getTime();

            // get current user that's signed in
            SharedPreferences sp = getSharedPreferences("settings", 0);
            String sender  = sp.getString("username", null);

            // create transaction object
            Transaction transaction = new Transaction(dt.toString(), sender, receiver, 0);

            // generate a unique Id for new transactions
            String key = ref.child("transactions").push().getKey();
            // write to database
            ref.child("transactions").child(key).setValue(transaction);
    }


    // when finished sending stickers, go back to the all stickers screen
    public void doneButtonPressed(View view) {
        Intent intent = new Intent(this, AllStickersActivity.class);
        startActivity(intent);
    }

    private class Transaction {
        public String dateTime ;
        public String sender;
        public String receiver;
        public int stickerId;


        public Transaction(String dateTime, String sender, String receiver, int stickerId) {
            this.dateTime = dateTime;
            this.sender = sender;
            this.receiver = receiver;
            this.stickerId = stickerId;
        }

        public String getDateTime() {
            return dateTime;
        }

        public String getSender() {
            return sender;
        }

        public String getReceiver() {
            return receiver;
        }

        public int getStickerId() {
            return stickerId;
        }
    }

}
