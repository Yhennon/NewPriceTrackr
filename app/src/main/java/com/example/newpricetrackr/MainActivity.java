package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button browseBtn;
    Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browseBtn = findViewById(R.id.browseBtn);
        uploadBtn = findViewById(R.id.uploadBtn);

        browseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
            startActivity(intent);
        });

        uploadBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(intent);
        });
    }
}