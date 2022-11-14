package edu.northeastern.team11.slurp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.team11.AllStickersActivity;
import edu.northeastern.team11.R;

public class LandingScreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slurp_landing);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    public void goToSignup(View view) {
        Intent intent = new Intent(this, SignupScreen.class);
        startActivity(intent);
    }

}
