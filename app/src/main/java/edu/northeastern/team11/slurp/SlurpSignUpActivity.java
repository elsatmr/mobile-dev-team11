package edu.northeastern.team11.slurp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.team11.R;

public class SlurpSignUpActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference ref;
    private EditText newUser;
    private EditText userCity;
    private Button signUpButton;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slurp_signup);

        // get user input
        newUser = findViewById(R.id.new_username);
        signUpButton = findViewById(R.id.signup_new_user);
        errorText = findViewById(R.id.errorText);
        userCity = findViewById(R.id.location_et);

        // connect with database
        mDatabase = FirebaseDatabase.getInstance();
        errorText.setVisibility(View.INVISIBLE);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newUser.getText().toString().trim().length() > 0) {
                    addUser();
                }
            }
        });
    }

    private void addUser() {
        ref = mDatabase.getReference();
        String userName = newUser.getText().toString().trim();
        ref.child("users_slurp").child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    errorText.setVisibility(View.VISIBLE);
                    newUser.setText("");
                    newUser.requestFocus();
                } else {
                    addNewSlurpUser(newUser.getText().toString().trim(), userCity.getText().toString().trim());
                    // Save username in shared preferences
                    SharedPreferences.Editor editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
                    editor.putString("username", newUser.getText().toString().trim());
                    editor.apply();

                    // launch main slurp activity
                    signInUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Signup error", error.toString());
            }
        });
    }

    // write to db information about the new user
    private void addNewSlurpUser(String username, String cityState) {
        ref.child("users_slurp").child(username).child("cityState").setValue(cityState);
        ref.child("users_slurp").child(username).child("friends").child("total").setValue(0);
        ref.child("users_slurp").child(username).child("numFriends").setValue(0);
        ref.child("users_slurp").child(username).child("numPosts").setValue(0);
        ref.child("users_slurp").child(username).child("numTimesVoted").setValue(0);
        ref.child("users_slurp").child(username).child("profilePhotoLink").setValue("no_profile_pic_set");
        ref.child("users_slurp").child(username).child("slurperStatusPoints").setValue(0);

        // with updated dbase
        ref.child("friends").child(username).child("init").setValue("init");
        ref.child("slurperStatusPoints").child(username).child("count").setValue(0);

    }

    private void signInUser() {
        Intent intent = new Intent(this, MainSlurpActivity.class);
        startActivity(intent);
    }
}
