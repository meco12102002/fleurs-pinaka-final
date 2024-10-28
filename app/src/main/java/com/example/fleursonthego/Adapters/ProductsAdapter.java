package com.example.fleursonthego.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // If you're using Glide for image loading
import com.example.fleursonthego.Models.Product;
import com.example.fleursonthego.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final List<Product> productList;

    public ProductsAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Logging to verify product data
        // Set product name, category, price, and rating
        holder.productName.setText(product.getProductName());
        holder.productCategory.setText(product.getCategory());
        holder.productPrice.setText(String.format("P%.2f", product.getPrice()));
        holder.productRating.setText(String.format("Rating: %.1f", product.getRating()));
        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productCategory; // New TextView for category
        TextView productRating;   // New TextView for rating
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productCategory = itemView.findViewById(R.id.product_category);
            productRating = itemView.findViewById(R.id.product_rating);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
