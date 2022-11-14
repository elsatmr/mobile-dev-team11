package edu.northeastern.team11.slurp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.team11.R;


public class LoginScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slurp_login);
    }

    public void goToFeed(View view){
        Intent intent = new Intent(this, FeedScreen.class);
        startActivity(intent);
    }
}

