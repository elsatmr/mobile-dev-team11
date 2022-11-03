package edu.northeastern.team11;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText newUser = findViewById(R.id.new_username);
        String username = newUser.getText().toString();
        final Button signUpButton = findViewById(R.id.signup_new_user);

        // connect with firebase
        mDatabase = FirebaseDatabase.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = mDatabase.getReferenceFromUrl("https://stickers-19c0f-default-rtdb.firebaseio.com/");

            }
        });







    }
}
