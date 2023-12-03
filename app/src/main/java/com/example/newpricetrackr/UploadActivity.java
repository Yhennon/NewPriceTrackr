package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UploadActivity extends AppCompatActivity {

    Button manualBtn;
    Button photoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        manualBtn = findViewById(R.id.manualBtn);
        photoBtn = findViewById(R.id.photoBtn);

        manualBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UploadActivity.this, ManualActivity.class);
            startActivity(intent);
        });

        photoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UploadActivity.this, PhotoActivity.class);
            startActivity(intent);
        });
    }
}