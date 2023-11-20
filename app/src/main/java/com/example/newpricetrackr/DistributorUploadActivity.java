package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DistributorUploadActivity extends AppCompatActivity {

    private Spinner titleInput;
    private EditText addressInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_upload);

        addressInput = findViewById(R.id.addressInput);

        //Fill out the dropdown list
        ArrayAdapter<Distributor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Distributor.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        titleInput = findViewById(R.id.titleInput);
        titleInput.setAdapter(adapter);
    }

    public void onSubmitBtnClick(View view)
    {
        Distributor selectedValue = (Distributor) titleInput.getSelectedItem();
        String title = selectedValue.toString();

        String address = addressInput.getText().toString().trim();
        if (address.isEmpty()) {
            showToast("Address cannot be empty");
        } else {
            uploadDistributor(title, address);
        }
    }

    private void uploadDistributor(String title, String address) {
        String url = "http://10.0.2.2:8000/distributors/";
        RequestQueue requestQueue = Volley.newRequestQueue(DistributorUploadActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showToast("Distributor uploaded");
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
            public byte[] getBody(){
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("title", title);
                    jsonBody.put("address", address);
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

    //TODO: Maybe find a way to not hardcode this
    // also there might be a simpler way to do this...
    public enum Distributor {
        Rema("Rema 1000"),
        Netto("Netto"),
        IKEA("IKEA"),
        Bilka("Bilka"),

        //TODO: Change the enum to capital f for the API
        Fotex("føtex")
        ;

        private final String text;
        Distributor(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}