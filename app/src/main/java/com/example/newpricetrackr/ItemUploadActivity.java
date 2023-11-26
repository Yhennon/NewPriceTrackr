package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUploadActivity extends AppCompatActivity {
    Context context = this;
    private EditText nameInput;
    private Spinner typeInput;
    private Spinner distributorInput;
    private EditText priceInput;

    ArrayAdapter<String> distributorAdapter;
    ArrayAdapter<ItemType> typeAdapter;

    private HashMap<String, String> distributorList = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_upload);

        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        distributorInput = findViewById(R.id.distributorInput);
        typeInput = findViewById(R.id.typeInput);


        getDistributors(distributorList);

        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeInput.setAdapter(typeAdapter);

    }

    public void onSubmitBtnClick(View view) {
        ItemType selectedType = (ItemType) typeInput.getSelectedItem();
        String type = selectedType.toString();

        String selectedDistributor = (String) distributorInput.getSelectedItem();
        int distributorID = Integer.parseInt(distributorList.get(selectedDistributor));


        String name = nameInput.getText().toString().trim();
        String price = priceInput.getText().toString().trim();
        if (name.isEmpty() || price.isEmpty()) {
            showToast("Name and price cannot be empty");
        } else  if(!price.matches("([0-9]+)?(\\.[0-9]{0,2})?")) {
            showToast("Please input a number for price");
        }
        else {
                uploadItem(name, distributorID, type, Integer.parseInt(price));
        }
    }

    private void uploadItem(String name, int distributorID, String type, int price) {
        String url = "http://10.0.2.2:8000/distributors/" + distributorID + "/items/";
        RequestQueue requestQueue = Volley.newRequestQueue(ItemUploadActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showToast("Item uploaded");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error: " + error.getMessage());
                        //TODO: make this more descriptive (?)
                        showToast("Something went wrong");
                    }
                }) {

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("name", name);
                    jsonBody.put("itemtype", type);
                    jsonBody.put("price", price);
                    Log.e("Volley Error", jsonBody.toString());
                    return jsonBody.toString().getBytes("utf-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.e("Error", "Error: " + e.getMessage());
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        requestQueue.add(stringRequest);
    }
    private void getDistributors(HashMap<String, String> distributorList) {
        String url = "http://10.0.2.2:8000/distributors/";

        RequestQueue requestQueue = Volley.newRequestQueue(ItemUploadActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("ItemUploadActivity", "getDistributors: " + response);

            Gson gson = new Gson();

            java.lang.reflect.Type distributorType = new TypeToken<List<ModelsClass.Distributor>>() {
            }.getType();

            List<ModelsClass.Distributor> listOfDistributors = gson.fromJson(response, distributorType);

            distributorList.clear();
            for (ModelsClass.Distributor distributor : listOfDistributors) {
                distributorList.put(distributor.getTitle() + ", " + distributor.getAddress(),String.valueOf(distributor.getId()));

            }
            ArrayList<String> distributors = new ArrayList<>(distributorList.keySet());
            distributorAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, distributors);
            distributorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            distributorInput.setAdapter(distributorAdapter);

        }, error -> Log.e("ItemUploadActivity", "populateItems: Error occured: " + error.getMessage()));


        // Add the request to the queue
        requestQueue.add(stringRequest);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private enum ItemType {
        Bakery("Bakery product"),
        Dairy("Dairy"),
        Spice("Spice"),
        Fruit("Fruit and vegetable"),
        Meat("Meat");

        private final String text;
        ItemType(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}