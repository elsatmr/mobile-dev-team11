package edu.northeastern.team11;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllStickersActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private DatabaseReference transactionRef;
    private FirebaseDatabase firebaseDb;
    private List<Sticker> stickerList;
    StickerAdapter adapter;
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stickers_screen);
        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReferenceFromUrl("https://stickers-19c0f-default-rtdb.firebaseio.com/");
        username = this.getCurrentUser();
        transactionRef = firebaseDb.getReference().child("transactions");
        Query userQuery = dbRef.child("users").child(username);
        stickerList = new ArrayList<>();
        Log.d("QUERY", userQuery.toString());
        createNotificationChannel();
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

        transactionRef.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        transactionRef.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("DATA SNAPSHOT T", snapshot.getKey());
                String receiver = snapshot.child("receiver").getValue(String.class);
                String sender = snapshot.child("sender").getValue(String.class);
                if (Objects.equals(username, receiver)) {
                    sendNotification(sender);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    public void createNotificationChannel() {
        // This must be called early because it must be called before a notification is sent.
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Received sticker notification";
            String description = "Notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel_Sticker_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String senderName) {

        // Prepare intent which is triggered if the
        // notification is selected
        //Intent intent = new Intent(this, ReceiveNotificationActivity.class);
        //PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        //PendingIntent callIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(),
        //        new Intent(this, FakeCallActivity.class), 0);


        // Build notification
        // Need to define a channel ID after Android Oreo
        String channelId = "Channel_Sticker_ID";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel_Sticker_ID")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("New sticker from " + senderName)
                .setContentText("You received new sticker!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // hide the notification after its selected
                .setAutoCancel(true);

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);

//        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
//                //"Notification icons must be entirely white."
//                .setSmallIcon(R.drawable.foo)
//                .setContentTitle("New mail from " + "test@test.com")
//                .setContentText("Subject")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                // hide the notification after its selected
//                .setAutoCancel(true)
//                .addAction(R.drawable.foo, "Call", callIntent)
//                .setContentIntent(pIntent);

        // // notificationId is a unique int for each notification that you must define
        notificationManagerCompat.notify(0, notification);
    }

}
