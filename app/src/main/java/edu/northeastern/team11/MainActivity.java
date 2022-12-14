package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import edu.northeastern.team11.slurp.CurrentUserActivity;
import edu.northeastern.team11.slurp.MainSlurpActivity;
import edu.northeastern.team11.slurp.SlurpSignUpActivity;

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

    public void openSlurpActivity(View view) {
        Intent intent = new Intent(this, CurrentUserActivity.class);
        startActivity(intent);
    }

}