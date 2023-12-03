package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ItemDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();
        if (intent != null) {
            String itemName = intent.getStringExtra("itemName");
            double itemPrice = intent.getDoubleExtra("itemPrice", 0.0);
            String itemType = intent.getStringExtra("type");
            String distributorID = intent.getStringExtra("distributorID");

            Log.d("ittvagyok", "onCreate: " + itemType);
            Log.d("ittvagyok", "onCreate: " + distributorID);

            // Find views in the layout
            TextView textViewItemName = findViewById(R.id.textViewItemName);
            TextView textViewItemPrice = findViewById(R.id.textViewItemPrice);
            TextView textViewItemType = findViewById(R.id.textViewItemType);
            TextView textViewItemDistributor = findViewById(R.id.textViewItemDistributor);

            // Set retrieved item details to views
            textViewItemName.setText(itemName);
            textViewItemPrice.setText(String.valueOf(itemPrice));
            textViewItemType.setText(itemType);
            textViewItemDistributor.setText(distributorID);

        }
    }
}