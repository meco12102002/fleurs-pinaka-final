package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.Models.User; // Import your User model class
import com.example.fleursonthego.R;



public class RegisterInputActivity extends AppCompatActivity {

    private EditText etFullName, password;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input);

        etFullName = findViewById(R.id.txt_fullName);
        password = findViewById(R.id.txt_password);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = password.getText().toString().trim();

        // Navigate to the next activity
        Intent intent = new Intent(RegisterInputActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void saveUserToDatabase(User user) {
        // Implement your database logic here (e.g., Firebase)
        Log.d("RegisterInputActivity", "User registered: " + user.getFullName());
    }
}
