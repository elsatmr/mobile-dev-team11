package edu.northeastern.team11.slurp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class SlurpLoginActivity extends AppCompatActivity {
    private EditText username;
    private TextView errorText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get ui elements
        username = findViewById(R.id.username_edittext);
        loginButton = findViewById(R.id.login_screen_login);
        errorText = findViewById(R.id.errorLoginMsg);
        errorText.setVisibility(View.INVISIBLE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().trim();
                if (user.length() > 0) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("users_slurp").child(user).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                SharedPreferences.Editor editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
                                editor.putString("username", user);
                                editor.apply();
                                launchMainSlurperActivity();
                            } else {
                                errorText.setVisibility(View.VISIBLE);
                                username.setText("");
                                username.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void launchMainSlurperActivity() {
        Intent intent = new Intent(this, MainSlurpActivity.class);
        startActivity(intent);
    }
}
