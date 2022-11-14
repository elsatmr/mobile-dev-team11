package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.team11.slurp.LandingScreen;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void openAtYourServiceActivity(View view) {
        Intent intent = new Intent(this, AtYourServiceActivity.class);
        startActivity(intent);
    }

    public void openStickersActivity(View view) {
        Intent intent = new Intent(this, StickersMainActivity.class);
        startActivity(intent);
    }

    public void goToSlurp(View view) {
        Intent intent = new Intent(this, LandingScreen.class);
        startActivity(intent);
    }

}