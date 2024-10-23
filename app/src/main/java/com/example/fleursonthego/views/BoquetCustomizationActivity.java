package com.example.fleursonthego.views;

import android.os.Bundle;
import android.os.Handler; // Import Handler
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleursonthego.R;
import com.example.fleursonthego.Adapters.FlowerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BoquetCustomizationActivity extends AppCompatActivity {
    private RelativeLayout canvas;
    private RelativeLayout paletteFlowers, paletteWraps, paletteRibbons;

    // Sample images for each category
    private final int[] flowerImages = {
            R.drawable.red_rose,
            R.drawable.white_rose,
            R.drawable.yellow_rose,
            // Add more flower images here
    };

    private final int[] wrapImages = {
            R.drawable.boquetwrap,
            // Add more wrap images here
    };

    private final int[] ribbonImages = {
            R.drawable.flower,
            // Add more ribbon images here
    };

    private RecyclerView recyclerViewFlowers, recyclerViewWraps, recyclerViewRibbons;

    private List<ImageView> clonedImages = new ArrayList<>(); // List to hold cloned images
    private ImageView currentSelectedImage = null; // Track the currently selected image for rotation and deletion

    private Handler rotationHandler = new Handler(); // Handler for rotation
    private Runnable rotateRunnable; // Runnable for repeating rotation
    private boolean isRotatingLeft = false; // Flag for rotating left
    private boolean isRotatingRight = false; // Flag for rotating right

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_boquet_customization);

        initializeViews();
        setupRecyclerViews();
        setupButtonListeners();
    }

    private void initializeViews() {
        canvas = findViewById(R.id.canvas);
        paletteFlowers = findViewById(R.id.palette_flowers);
        paletteWraps = findViewById(R.id.palette_wraps);
        paletteRibbons = findViewById(R.id.palette_ribbons);

        // Initialize palettes to be hidden initially
        hideAllPalettes();
    }

    private void hideAllPalettes() {
        paletteFlowers.setVisibility(View.GONE);
        paletteWraps.setVisibility(View.GONE);
        paletteRibbons.setVisibility(View.GONE);
    }

    private void setupRecyclerViews() {
        // Set up RecyclerView for Flowers
        recyclerViewFlowers = findViewById(R.id.recyclerView_flowers);
        recyclerViewFlowers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFlowers.setAdapter(new FlowerAdapter(this, flowerImages, this::onFlowerSelected));

        // Set up RecyclerView for Wraps
        recyclerViewWraps = findViewById(R.id.recyclerView_wraps);
        recyclerViewWraps.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWraps.setAdapter(new FlowerAdapter(this, wrapImages, this::onFlowerSelected));

        // Set up RecyclerView for Ribbons
        recyclerViewRibbons = findViewById(R.id.recyclerView_ribbons);
        recyclerViewRibbons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRibbons.setAdapter(new FlowerAdapter(this, ribbonImages, this::onFlowerSelected));
    }

    private void setupButtonListeners() {
        Button buttonFlowers = findViewById(R.id.button_flowers);
        Button buttonWraps = findViewById(R.id.button_wraps);
        Button buttonRibbons = findViewById(R.id.button_ribbons);

        Button buttonUndo = findViewById(R.id.button_undo);
        Button buttonRotateLeft = findViewById(R.id.button_rotate_left);
        Button buttonRotateRight = findViewById(R.id.button_rotate_right);
        Button buttonDelete = findViewById(R.id.button_delete);

        buttonFlowers.setOnClickListener(v -> showPalette("flowers"));
        buttonWraps.setOnClickListener(v -> showPalette("wraps"));
        buttonRibbons.setOnClickListener(v -> showPalette("ribbons"));

        // Undo button functionality
        buttonUndo.setOnClickListener(v -> undoLastAction());

        // Rotate Left button functionality
        buttonRotateLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRotatingLeft = true;
                    startRotation(-5); // Rotate left by 5 degrees
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    isRotatingLeft = false;
                    stopRotation();
                }
                return true;
            }
        });

        // Rotate Right button functionality
        buttonRotateRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRotatingRight = true;
                    startRotation(5); // Rotate right by 5 degrees
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    isRotatingRight = false;
                    stopRotation();
                }
                return true;
            }
        });

        // Delete button functionality
        buttonDelete.setOnClickListener(v -> deleteSelectedImage());
    }

    private void showPalette(String paletteType) {
        hideAllPalettes();
        switch (paletteType) {
            case "flowers":
                paletteFlowers.setVisibility(View.VISIBLE);
                break;
            case "wraps":
                paletteWraps.setVisibility(View.VISIBLE);
                break;
            case "ribbons":
                paletteRibbons.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void onFlowerSelected(int imageResource) {
        ImageView clonedImage = createClonedImage(imageResource);
        canvas.addView(clonedImage);
        setupTouchListener(clonedImage);
    }

    private ImageView createClonedImage(int imageResource) {
        ImageView clonedImage = new ImageView(this);
        clonedImage.setImageResource(imageResource);
        clonedImage.setLayoutParams(new RelativeLayout.LayoutParams(200, 200)); // Adjusted size
        clonedImage.setX(200); // Initial position (you can adjust this)
        clonedImage.setY(200); // Initial position (you can adjust this)
        return clonedImage;
    }

    private void setupTouchListener(ImageView clonedImage) {
        clonedImages.add(clonedImage); // Add cloned image to the list

        clonedImage.setOnTouchListener(new View.OnTouchListener() {
            private float initialX, initialY, dX, dY, initialDistance = 0f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.bringToFront();

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getRawX();
                        initialY = event.getRawY();
                        dX = v.getX() - initialX;
                        dY = v.getY() - initialY;

                        // Select the current image for operations
                        currentSelectedImage = clonedImage;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        handleMoveEvent(v, event);
                        return true;

                    case MotionEvent.ACTION_UP:
                        initialDistance = 0f;
                        return true;

                    default:
                        return false;
                }
            }

            private void handleMoveEvent(View v, MotionEvent event) {
                if (event.getPointerCount() == 1) {
                    v.setX(event.getRawX() + dX);
                    v.setY(event.getRawY() + dY);
                } else if (event.getPointerCount() == 2) {
                    handlePinchZoom(v, event);
                }
            }

            private void handlePinchZoom(View v, MotionEvent event) {
                float newDistance = getDistance(event);
                if (initialDistance == 0) {
                    initialDistance = newDistance;
                } else {
                    float scale = newDistance / initialDistance;
                    v.setScaleX(scale);
                    v.setScaleY(scale);
                }
            }

            private float getDistance(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return (float) Math.sqrt(x * x + y * y);
            }
        });
    }

    private void undoLastAction() {
        if (!clonedImages.isEmpty()) {
            ImageView lastClonedImage = clonedImages.remove(clonedImages.size() - 1);
            canvas.removeView(lastClonedImage);
        }
    }

    private void startRotation(int angle) {
        rotateRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentSelectedImage != null) {
                    currentSelectedImage.setRotation(currentSelectedImage.getRotation() + angle);
                }
                // Continue the rotation if the button is still pressed
                if (isRotatingLeft || isRotatingRight) {
                    rotationHandler.postDelayed(this, 100); // Rotate every 100 ms
                }
            }
        };
        rotationHandler.post(rotateRunnable); // Start the rotation
    }

    private void stopRotation() {
        rotationHandler.removeCallbacks(rotateRunnable); // Stop the rotation
    }

    private void deleteSelectedImage() {
        if (currentSelectedImage != null) {
            clonedImages.remove(currentSelectedImage);
            canvas.removeView(currentSelectedImage);
            currentSelectedImage = null; // Clear the selection
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
