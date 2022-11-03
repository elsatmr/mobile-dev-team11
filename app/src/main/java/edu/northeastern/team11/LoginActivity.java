package edu.northeastern.team11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView errorText;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.username_edittext);
        errorText = findViewById(R.id.errorLoginMsg);
        errorText.setVisibility(View.INVISIBLE);
    }

    public void loginScreenLoginButtonClicked(View view) {
        if (userName.getText().toString().trim().length() > 0) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            String queryString = "users/" + userName.getText().toString().trim();
            db.child(queryString).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) { // username exists
                        SharedPreferences.Editor editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
                        editor.putString("username", userName.getText().toString().trim());
                        editor.apply();
                        // Go to All Stickers Page
                        goToAllStickersScreen();

                    } else { // username doesn't exist
                        errorText.setVisibility(View.VISIBLE);
                        userName.setText("");
                        userName.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
        });

        }
    }

    private void goToAllStickersScreen() {
        Intent intent = new Intent(this, AllStickersActivity.class);
        startActivity(intent);
    }

}
