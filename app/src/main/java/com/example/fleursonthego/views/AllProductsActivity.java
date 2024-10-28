package com.example.fleursonthego.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleursonthego.Adapters.ProductsAdapter;
import com.example.fleursonthego.Models.Product;
import com.example.fleursonthego.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // Declare RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_products_acitivty);

        recyclerView = findViewById(R.id.recyclerViewAllProducts); // Ensure this ID matches your layout

        // Set GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager); // Set layout manager to GridLayoutManager

        fetchProducts();
    }

    private void fetchProducts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class); // Ensure this matches your Product class structure
                    if (product != null) {
                        productList.add(product); // Add product to the list
                    }
                }
                // Update RecyclerView with fetched products
                updateRecyclerView(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void updateRecyclerView(List<Product> productList) {
        ProductsAdapter adapter = new ProductsAdapter(productList); // Initialize the adapter with the product list
        recyclerView.setAdapter(adapter); // Set the adapter for the RecyclerView
    }
}
