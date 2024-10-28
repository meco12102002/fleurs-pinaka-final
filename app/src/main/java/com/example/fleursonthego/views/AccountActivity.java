package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TextView textName = findViewById(R.id.textName);
        TextView textEmail = findViewById(R.id.textEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Set user's profile info
        if (currentUser != null) {
            textName.setText(currentUser.getDisplayName());
            textEmail.setText(currentUser.getEmail());
        }

        // Set up Logout button
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(AccountActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to Login or Main Activity
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
