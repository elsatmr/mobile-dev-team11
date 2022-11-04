package edu.northeastern.team11;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private TextView currentUserText;
    private String userName;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stickers_screen);

        currentUserText = findViewById(R.id.currentUser);
        db = FirebaseDatabase.getInstance().getReference();

        getCurrentUser();
        currentUserText.setText("My username is " + userName); // PLACEHOLDER ONLY


    }


    // get the username of who is signed in
    private void getCurrentUser() {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);
        userName = sharedPref.getString("username", null);
    }

    public void goToSendStickers(View view) {
        Intent intent = new Intent(this, SendStickerActivity.class);
        startActivity(intent);
    }

}
