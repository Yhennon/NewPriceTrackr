package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ManualActivity extends AppCompatActivity {

    Button uploadItemBtn;
    Button uploadDistributorBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        uploadItemBtn = findViewById(R.id.uploadItemBtn);
        uploadDistributorBtn = findViewById(R.id.uploadDistributorBtn);

        uploadItemBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ManualActivity.this, ItemUploadActivity.class);
            startActivity(intent);
        });

        uploadDistributorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ManualActivity.this, DistributorUploadActivity.class);
            startActivity(intent);
        });
    }
}