package com.example.fleursonthego.views;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleursonthego.Adapters.WrapAdapter;
import com.example.fleursonthego.R;
import com.example.fleursonthego.Adapters.FlowerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BouquetCustomizationActivity extends AppCompatActivity {
    private RelativeLayout canvas;
    private RelativeLayout paletteFlowers, paletteWraps, paletteRibbons;

    private final int[] flowerImages = {
            R.drawable.red_rose,
            R.drawable.white_rose,
            R.drawable.yellow_rose,

    };

    private final int[] wrapImages = {
            R.drawable.boquetwrap,
            R.drawable.white_wrap,
            R.drawable.violet_wrap,
            R.drawable.sunny_wrap,
            R.drawable.cute_theme_wrap,
            R.drawable.greenish_white_wrap,
            R.drawable.purple_wrap_1
    };

    private final int[] ribbonImages = {
            R.drawable.flower,
    };

    private RecyclerView recyclerViewFlowers, recyclerViewWraps, recyclerViewRibbons;
    private List<ImageView> clonedImages = new ArrayList<>();
    private ImageView currentSelectedImage = null;

    private Handler rotationHandler = new Handler();
    private Runnable rotateRunnable;
    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;

    private float initialDistance = 0f;
    private float initialScale = 1f;

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

        hideAllPalettes();
    }

    private void hideAllPalettes() {
        paletteFlowers.setVisibility(View.GONE);
        paletteWraps.setVisibility(View.GONE);
        paletteRibbons.setVisibility(View.GONE);
    }

    private void setupRecyclerViews() {
        recyclerViewFlowers = findViewById(R.id.recyclerView_flowers);
        recyclerViewFlowers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFlowers.setAdapter(new FlowerAdapter(this, flowerImages, this::onFlowerSelected));

        recyclerViewWraps = findViewById(R.id.recyclerView_wraps);
        recyclerViewWraps.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWraps.setAdapter(new WrapAdapter(this, wrapImages, this::onWrapSelected));

        recyclerViewRibbons = findViewById(R.id.recyclerView_ribbons);
        recyclerViewRibbons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRibbons.setAdapter(new FlowerAdapter(this, ribbonImages, this::onFlowerSelected));
    }

    private void setupButtonListeners() {
        ImageButton buttonFlowers = findViewById(R.id.button_flowers);
        ImageButton buttonWraps = findViewById(R.id.button_wraps);
        ImageButton buttonRibbons = findViewById(R.id.button_ribbons);
        ImageButton buttonUndo = findViewById(R.id.button_undo);
        ImageButton buttonRotateLeft = findViewById(R.id.button_rotate_left);
        ImageButton buttonRotateRight = findViewById(R.id.button_rotate_right);
        ImageButton buttonDelete = findViewById(R.id.button_delete);
        ImageButton buttonSendToBack = findViewById(R.id.button_send_back);

        buttonFlowers.setOnClickListener(v -> showPalette("flowers"));
        buttonWraps.setOnClickListener(v -> showPalette("wraps"));
        buttonRibbons.setOnClickListener(v -> showPalette("ribbons"));

        buttonUndo.setOnClickListener(v -> undoLastAction());

        buttonRotateLeft.setOnTouchListener((v, event) -> handleRotation(event, true));
        buttonRotateRight.setOnTouchListener((v, event) -> handleRotation(event, false));

        buttonDelete.setOnClickListener(v -> deleteSelectedImage());
        buttonSendToBack.setOnClickListener(v -> sendSelectedImageToBack());
    }

    private boolean handleRotation(MotionEvent event, boolean rotateLeft) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rotateLeft) {
                    isRotatingLeft = true;
                    startRotation(-5);
                } else {
                    isRotatingRight = true;
                    startRotation(5);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (rotateLeft) {
                    isRotatingLeft = false;
                } else {
                    isRotatingRight = false;
                }
                stopRotation();
                break;
        }
        return true;
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
        }
    }

    private void onFlowerSelected(int imageResource) {
        ImageView clonedImage = createClonedImage(imageResource);
        canvas.addView(clonedImage);
        setupTouchListener(clonedImage);
    }

    private void onWrapSelected(int imageResource) {
        ImageView clonedImage = createClonedImage(imageResource);
        canvas.addView(clonedImage);
        setupTouchListener(clonedImage);
    }

    private ImageView createClonedImage(int imageResource) {
        ImageView clonedImage = new ImageView(this);
        clonedImage.setImageResource(imageResource);
        clonedImage.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));
        clonedImage.setX(200);
        clonedImage.setY(200);
        return clonedImage;
    }

    private void setupTouchListener(ImageView clonedImage) {
        clonedImages.add(clonedImage);
        clonedImage.setOnTouchListener(new View.OnTouchListener() {
            private float initialX, initialY, dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.bringToFront();

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getRawX();
                        initialY = event.getRawY();
                        dX = v.getX() - initialX;
                        dY = v.getY() - initialY;
                        currentSelectedImage = clonedImage;
                        initialDistance = 0f; // Reset the initial distance for pinch zoom
                        initialScale = v.getScaleX(); // Store the initial scale
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount() == 1) {
                            v.setX(event.getRawX() + dX);
                            v.setY(event.getRawY() + dY);
                        } else if (event.getPointerCount() == 2) {
                            handlePinchZoom(v, event);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        return true;

                    default:
                        return false;
                }
            }

            private void handlePinchZoom(View v, MotionEvent event) {
                float newDistance = getDistance(event);
                if (initialDistance == 0) {
                    initialDistance = newDistance;
                } else {
                    float scale = newDistance / initialDistance;
                    // Gradually adjust scale for a smoother effect
                    float newScale = initialScale * scale;
                    v.setScaleX(newScale);
                    v.setScaleY(newScale);
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
            currentSelectedImage = null;
            Toast.makeText(this, "Undid last action", Toast.LENGTH_SHORT).show();
        }
    }

    private void startRotation(int degrees) {
        if (rotateRunnable == null) {
            rotateRunnable = () -> {
                if (currentSelectedImage != null) {
                    float newRotation = currentSelectedImage.getRotation() + degrees;
                    currentSelectedImage.setRotation(newRotation);
                }
                rotationHandler.postDelayed(rotateRunnable, 100);
            };
            rotationHandler.post(rotateRunnable);
        }
    }

    private void stopRotation() {
        if (rotateRunnable != null) {
            rotationHandler.removeCallbacks(rotateRunnable);
            rotateRunnable = null;
        }
    }

    private void deleteSelectedImage() {
        if (currentSelectedImage != null) {
            canvas.removeView(currentSelectedImage);
            clonedImages.remove(currentSelectedImage);
            currentSelectedImage = null;
            Toast.makeText(this, "Deleted selected item", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSelectedImageToBack() {
        if (currentSelectedImage != null) {
            // Remove the current selected image and re-add it to the canvas
            canvas.removeView(currentSelectedImage);
            canvas.addView(currentSelectedImage, 0); // Add at index 0 to send it to the back
            currentSelectedImage = null; // Deselect the current image
            Toast.makeText(this, "Sent selected item to back", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
        }
    }
}
