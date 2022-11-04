package edu.northeastern.team11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private EditText newUser;
    private Button signUpButton;
    private TextView errorText;
    DatabaseReference db;
    private int counter = 0;

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
        db = mDatabase.getReference();
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
                    addNewUserToDb(newUser.getText().toString().trim());
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
                Log.e("Signup error", error.toString());
            }
        });
    }

    private void goToAllStickersScreen() {
        Intent intent = new Intent(this, AllStickersActivity.class);
        startActivity(intent);
    }

    // Add a new user to the database and set sentCount=0 and receivedCount=0 for all images
    private void addNewUserToDb(String username) {
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            // Get all files in Storage
            storageRef.child("csStickers").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    // For each file...
                    for (StorageReference fileRef : listResult.getItems()) {
                        Image x = new Image(0, 0);
                        db.child("users").child(username).child(String.valueOf(counter)).setValue(x);
                        counter += 1;
                    }
                    counter = 0;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("dbError", e.toString());
                }
            });
        } catch (Error e) {
            Log.e("ERROR", e.toString());
        }
    }


    // Image object
    private class Image {
        public int sentCount;
        public int receivedCount;

        public Image(int sentCount, int receivedCount) {
            sentCount = sentCount;
            receivedCount = receivedCount;
        }
        public int getSentCount() {
            return sentCount;
        }
        public int getReceivedCount() {
            return receivedCount;
        }

    }
}