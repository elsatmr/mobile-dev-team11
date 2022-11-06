package edu.northeastern.team11;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StickerScreen extends AppCompatActivity {
    TableLayout logTable;
    ImageView stickerImage;
    String userName;
    String stickerId;
    Chip sentChip;
    Chip recChip;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_screen);
        logTable = findViewById(R.id.logTable);
        stickerImage = findViewById(R.id.stickerLogImage);
        sentChip = findViewById(R.id.sentChip);
        recChip = findViewById(R.id.recChip);
        db = FirebaseDatabase.getInstance().getReference();
        getCurrentUser();
        if (getIntent().getExtras().getString("stickerId") != null) {
            stickerId = getIntent().getExtras().getString("stickerId");
        } else {
            stickerId = "0"; // WE CAN REMOVE THIS ONCE COMBINES WITH ELSAS CODE
        }
        String url = stickerUrl(stickerId);
        BackgroundThread getImage = new BackgroundThread(url);
        getImage.start();
        getCounts();
        getLog();
    }


    // Append a row to the log table in the UI
    private void appendRow(Transaction trx) {
        if (trx.getReceiver().equals(userName) || trx.getSender().equals(userName)) {
            TableRow row = new TableRow(this);
            row.setPadding(0, 10, 0, 10);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView dateView = new TextView(this);
            dateView.setWidth(100);
            String input = trx.dateTime;
            DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu")
                    .withLocale(Locale.US);
            ZonedDateTime zdt = ZonedDateTime.parse(input, f);
            LocalDate ld = zdt.toLocalDate();
            DateTimeFormatter fLocalDate = DateTimeFormatter.ofPattern("MM/dd/uuuu");
            String output = ld.format(fLocalDate);

            dateView.setText(output);
            row.addView(dateView);

            TextView usernameView = new TextView(this);
            usernameView.setWidth(125);
            if (trx.sender == userName) {
                usernameView.setText(trx.receiver);
            } else {
                usernameView.setText(trx.sender);
            }
            row.addView(usernameView);

            TextView actionView = new TextView(this);
            actionView.setWidth(100);
            if (trx.getSender().equals(userName)) {
                actionView.setText("sent");
            } else {
                actionView.setText("received");
            }
            actionView.setGravity(Gravity.CENTER);
            row.addView(actionView);

            logTable.addView(row);
        }
    }

    // Get sticker url
    private String stickerUrl(String stickerId) {
        int resId = getResources().getIdentifier("sticker" + stickerId, "string", getPackageName());
        String url = getResources().getString(resId).replace("%26", "&");
        return url;
    }

    // Get the sent and received count for this image
    private void getCounts() {
        // get sentCount
        db.child("users").child(userName).child(stickerId).child("sentCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sentChip.setText(String.valueOf(snapshot.getValue(Long.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // get receivedCount
        db.child("users").child(userName).child(stickerId).child("receivedCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recChip.setText(String.valueOf(snapshot.getValue(Long.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Get the list of transactions for this stickerId and populate the log table with rows
    private void getLog() {
        db.child("transactions").orderByChild("stickerId").equalTo(stickerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot trx : snapshot.getChildren()) {
                    Transaction transaction = new Transaction(
                            trx.child("dateTime").getValue(String.class),
                            trx.child("receiver").getValue(String.class),
                            trx.child("sender").getValue(String.class),
                            trx.child("stickerId").getValue(String.class));
                    appendRow(transaction);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // get the username of who is signed in
    private void getCurrentUser() {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);
        userName = sharedPref.getString("username", null);
    }

    class Transaction {
        private String dateTime;
        private String sender;
        private String receiver;
        private String stickerId;

        public Transaction() {
        }

        public Transaction(String dateTime, String receiver, String sender, String stickerId) {
            this.dateTime = dateTime;
            this.sender = sender;
            this.receiver = receiver;
            this.stickerId = stickerId;
        }

        public String getDateTime() {
            return this.dateTime;
        }

        public String getSender() {
            return this.sender;
        }

        public String getReceiver() {
            return this.receiver;
        }

        public String getStickerId() {
            return this.stickerId;
        }
    }

    // Class to retrive the image via another thread
    class BackgroundThread extends Thread {
        String imageUri;

        BackgroundThread(String url) {
            this.imageUri = url;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                stickerImage.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            stickerImage.setImageBitmap(bitmap);
                        }
                    }
                });
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

    public void sendStickerButtonClicked(View view) {
        Intent intent = new Intent(this, SendStickerActivity.class);
        intent.putExtra("stickerId", stickerId);
        startActivity(intent);
    }
}
