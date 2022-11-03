package edu.northeastern.team11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AllStickersActivity extends AppCompatActivity {
    private TextView currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stickers_screen);

        // PLACEHOLDER - This is how you get the current user from Shared Preferences
        currentUser = findViewById(R.id.currentUser);
        currentUser.setText("My username is " + getCurrentUser());

    }


    // get the username of who is signed in
    private String getCurrentUser() {
        SharedPreferences sharedPref = getSharedPreferences("settings",0);
        return sharedPref.getString("username", null);
    }
}
