package edu.northeastern.team11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginScreenLoginButtonClicked(View view) {
        Intent intent = new Intent(this, AllStickersActivity.class);
        startActivity(intent);
    }

}
