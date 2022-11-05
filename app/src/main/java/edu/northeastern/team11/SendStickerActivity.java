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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    // generate a uniqueId for the key, then write the new transaction to the db
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

            // after sending sticker, set the listview to invisible
            // add snackbar - sticker sent!
            // change done button to "send Sticker" which will cal updateSentReccievedCounts
            // and return to all stickers activity

            updateSentReceivedCounts(receiver, sender);
    }

    // update user's receivedCount snd sentCount
    private void updateSentReceivedCounts(String receiver, String sender) {
        // need to get the stickerId, for now just hardcoding 0
        String stickerId = String.valueOf(0);

        // get db ref
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> updates = new HashMap<>();
        // update senderCount
        updates.put("users/"+sender+"/"+stickerId+"/sentCount", ServerValue.increment(1));
        updates.put("users/"+receiver+"/"+stickerId+"/receivedCount", ServerValue.increment(1));
        ref.updateChildren(updates);
        // setValue()
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

    private class User {
        private StickerHistory s0;
        private StickerHistory s1;
        private StickerHistory s2;
        private StickerHistory s3;
        private StickerHistory s4;
        private StickerHistory s5;
        private StickerHistory s6;
        private StickerHistory s7;
        private StickerHistory s8;
        private StickerHistory s9;

        public User(StickerHistory s0, StickerHistory s1, StickerHistory s2, StickerHistory s3,
                    StickerHistory s4, StickerHistory s5, StickerHistory s6, StickerHistory s7,
                    StickerHistory s8, StickerHistory s9) {
            this.s0 = s0;
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
            this.s4 = s4;
            this.s5 = s5;
            this.s6 = s6;
            this.s7 = s7;
            this.s8 = s8;
            this.s9 = s9;
        }

        public StickerHistory getS0() {
            return s0;
        }

        public StickerHistory getS1() {
            return s1;
        }

        public StickerHistory getS2() {
            return s2;
        }

        public StickerHistory getS3() {
            return s3;
        }

        public StickerHistory getS4() {
            return s4;
        }

        public StickerHistory getS5() {
            return s5;
        }

        public StickerHistory getS6() {
            return s6;
        }

        public StickerHistory getS7() {
            return s7;
        }

        public StickerHistory getS8() {
            return s8;
        }

        public StickerHistory getS9() {
            return s9;
        }
    }

    private class StickerHistory {
        private int receivedCount;
        private int sentCount;
        private String urlString;

        public StickerHistory(int receivedCount, int sentCount, String urlString) {
            this.receivedCount = receivedCount;
            this.sentCount = sentCount;
            this.urlString = urlString;
        }
        public StickerHistory() {
            this.receivedCount = receivedCount;
            this.sentCount = sentCount;
            this.urlString = urlString;
        }

        public int getReceivedCount() {
            return receivedCount;
        }

        public int getSentCount() {
            return sentCount;
        }

        public String getUrlString() {
            return urlString;
        }
    }




}
