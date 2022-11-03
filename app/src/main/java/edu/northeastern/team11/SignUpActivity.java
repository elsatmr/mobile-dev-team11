package edu.northeastern.team11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private EditText newUser;
    private Button signUpButton;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Get UI element references
        newUser = findViewById(R.id.new_username);
        signUpButton = findViewById(R.id.signup_new_user);
        errorText = findViewById(R.id.errorText);

        // connect with firebase
        mDatabase = FirebaseDatabase.getInstance();
        errorText.setVisibility(View.INVISIBLE);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newUser.getText().toString().trim().length() > 0) {
                    handleSignup();
                }
            }
        });

    }

    private void handleSignup() {
        DatabaseReference db = mDatabase.getReference();
        String queryString = "users/" + newUser.getText().toString().trim();
        db.child(queryString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // username already exists
                    errorText.setVisibility(View.VISIBLE);
                    newUser.setText("");
                    newUser.requestFocus();
                } else { // username is available
                    // Write username to database

                    // Save username in shared preferences
                    SharedPreferences.Editor editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
                    editor.putString("username", newUser.getText().toString().trim());
                    editor.apply();
                    // Go to All Stickers Page
                    goToAllStickersScreen();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void goToAllStickersScreen() {
        Intent intent = new Intent(this, AllStickersActivity.class);
        startActivity(intent);
    }
}
