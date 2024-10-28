package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fleursonthego.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(CartActivity.this, Dashboard.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_messages) {
                    startActivity(new Intent(CartActivity.this, MessagesActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_cart) {
                    startActivity(new Intent(CartActivity.this, CartActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_account) {
                    startActivity(new Intent(CartActivity.this, AccountActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

    }
}