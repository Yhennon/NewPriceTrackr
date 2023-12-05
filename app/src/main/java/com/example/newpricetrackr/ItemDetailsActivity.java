package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
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
            Log.d(TAG, "onCreate: " + distributorID);

            TextView textViewItemName = findViewById(R.id.textViewItemName);
            TextView textViewItemPrice = findViewById(R.id.textViewItemPrice);
            TextView textViewItemType = findViewById(R.id.textViewItemType);
            TextView textViewItemDistributor = findViewById(R.id.textViewItemDistributor);

            textViewItemName.setText(itemName);
            textViewItemPrice.setText(String.valueOf(itemPrice));
            textViewItemType.setText(itemType);

            // Call getDistributorInfo with a callback
            getDistributorInfo(distributorID, new DistributorCallback() {
                @Override
                public void onDistributorInfoReceived(String distributorInfo) {
                    // Set the distributorInfo to textViewItemDistributor
                    textViewItemDistributor.setText(distributorInfo);
                }
            });

        }
    }

    public interface DistributorCallback {
        void onDistributorInfoReceived(String distributorInfo);
    }

    private void getDistributorInfo(String distributorID, DistributorCallback callback) {
        String url = "http://10.0.2.2:8000/distributors/" + distributorID;

        RequestQueue requestQueue = Volley.newRequestQueue(ItemDetailsActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            java.lang.reflect.Type distributorBaseType = new TypeToken<List<ModelsClass.DistributorBase>>() {}.getType();



            ModelsClass.DistributorBase distributorBase = gson.fromJson(response, ModelsClass.DistributorBase.class);

            String title = distributorBase.getTitle();
            String address = distributorBase.getAddress();

            String distributor = title + ", " + address;
            Log.d(TAG, "getDistributorInfo: distributor = " + distributor);

            callback.onDistributorInfoReceived(distributor);

        }, error -> Log.e(TAG, "getDistributorInfo: Error occurred: " + error.getMessage()));

        requestQueue.add(stringRequest);
    }

//    private String getDistributorInfo(String distributorID) {
//        String url = "http://10.0.2.2:8000/items/"+distributorID;
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ItemDetailsActivity.this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//
//            Gson gson = new Gson();
//            java.lang.reflect.Type distributorBaseType = new TypeToken<List<ModelsClass.DistributorBase>>() {}.getType();
//
//            ModelsClass.DistributorBase distributorBase = gson.fromJson(response, distributorBaseType);
//
//            String title = distributorBase.getTitle();
//            String address = distributorBase.getAddress();
//
//            String distributor = title + ", " +address;
//            Log.d(TAG, "getDistributorInfo: distributor = "+distributor);
//
//        }, error -> Log.e(TAG, "getDistributorInfo: Error occurred: " + error.getMessage()));
//
//        requestQueue.add(stringRequest);
//    }
}