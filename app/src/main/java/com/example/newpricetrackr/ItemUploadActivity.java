package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    private EditText nameInput;
    private Spinner typeInput;
    private Spinner distributorInput;
    private EditText priceInput;

    DistributorsManager distributorsManager = new DistributorsManager();

    ArrayAdapter<String> distributorAdapter;
    ArrayAdapter<ItemType> typeAdapter;
    private ArrayList<ModelsClass.Distributor> distributorList = new ArrayList<>();
    private ArrayList<String> formattedDistributorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_upload);

        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);

        //Fill out the dropdown lists
        distributorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formattedDistributorList);
        distributorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distributorsManager.getDistributors(distributorList,new DistributorsCallback() {
            @Override
            public void onDistributorsReceived(ArrayList<ModelsClass.Distributor> distributorList) {
                formattedDistributorList = distributorsManager.formatDistributorList(distributorList);
                distributorAdapter.notifyDataSetChanged();
            }
        });

        distributorInput = findViewById(R.id.distributorInput);
        distributorInput.setAdapter(distributorAdapter);


        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ItemType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeInput = findViewById(R.id.typeInput);
        typeInput.setAdapter(typeAdapter);

    }

    public void onSubmitBtnClick(View view) {
        ItemType selectedValue = (ItemType) typeInput.getSelectedItem();
        String type = selectedValue.toString();

        //TODO: Get Dist.
        int distributorID = 1;

        String name = nameInput.getText().toString().trim();
        //TODO: Force numeric values
        String price = priceInput.getText().toString().trim();
        if (name.isEmpty() || price.isEmpty()) {
            showToast("Name and price cannot be empty");
        } else {
            uploadItem(name, distributorID, type, price);
        }
    }
    public void onRefreshBtnClick(View view) {
        formattedDistributorList = distributorsManager.formatDistributorList(distributorList);
        distributorAdapter.notifyDataSetChanged();
        distributorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formattedDistributorList);
        distributorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distributorInput = findViewById(R.id.distributorInput);
        distributorInput.setAdapter(distributorAdapter);
    }

    private void uploadItem(String name, int distributorID, String type, String price) {
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
                        Log.e("Volley Error", "Error: " + error.toString());
                        //TODO: make this more descriptive
                        showToast("Something went wrong");
                    }
                }) {

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("name", name);
                    jsonBody.put("itemType", type);
                    jsonBody.put("price", price);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //TODO: dont hardcode (?)
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

    public interface DistributorsCallback {
        void onDistributorsReceived(ArrayList<ModelsClass.Distributor> distributorList);
    }
    public class DistributorsManager {
        private void getDistributors(ArrayList<ModelsClass.Distributor> distributorList, final DistributorsCallback callback) {
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
                    distributorList.add(distributor);
                    Log.e("ItemUploadActivity", "Dist ");
                }

            }, error -> Log.e("ItemUploadActivity", "populateItems: Error occured: " + error.getMessage()));


            // Add the request to the queue
            requestQueue.add(stringRequest);

            callback.onDistributorsReceived(distributorList);
        }

        private ArrayList<String> formatDistributorList(ArrayList<ModelsClass.Distributor> distributorList)
        {
            ArrayList<String> formattedList = new ArrayList<String>();
            for (ModelsClass.Distributor distributor : distributorList) {
                Log.e("ItemUploadActivity", "ITEM ");
                formattedList.add(distributor.getTitle() + ", " + distributor.getAddress());
            }
            return formattedList;
        }
    }
}