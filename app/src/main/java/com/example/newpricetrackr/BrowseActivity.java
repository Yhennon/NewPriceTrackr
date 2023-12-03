package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {
    private static final String TAG = "BrowseActivity";

    ListView browseListView;
    ArrayAdapter<String> arrayAdapter;

    //change this to be of type Item maybe
    ArrayList<String> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        populateItems(itemList);
        browseListView = findViewById(R.id.browseListView);
        arrayAdapter = new ArrayAdapter<>(this,R.layout.list_customtext, itemList);
        browseListView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void populateItems(ArrayList<String> itemList) {
        String url = "http://10.0.2.2:8000/items/";

        RequestQueue requestQueue = Volley.newRequestQueue(BrowseActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.d(TAG, "populateItems: " + response);

            Gson gson = new Gson();

            java.lang.reflect.Type itemType = new TypeToken<List<ModelsClass.Item>>() {}.getType();

            List<ModelsClass.Item> listOfItems = gson.fromJson(response, itemType);

            // Log.d(TAG, "populateItems: listofItems:" +listOfItems);


            itemList.clear();
            for (ModelsClass.Item item : listOfItems){
                String name = item.getName();
                String itemtype = item.getItemtype();
                double price = item.getPrice();
                int id = item.getId();
                int distributor_id = item.getDistributor_id();

                itemList.add(name);
               // Log.d(TAG, "populateItems: "+ name + itemtype);

            }
            /*
            * The populateItems method makes an asynchronous network request, and the ListView is being set up in the onCreate method.
            * Since the network request is asynchronous, it might not have completed by the time the ListView is being set up in onCreate.
            * As a result, the ListView is initially empty.
            *
            * To ensure that the ListView is updated with the data after it's retrieved, you should notify the ArrayAdapter of changes
            * to the underlying data (in this case, the itemList) once the data is available.
            * You can do this by calling notifyDataSetChanged() on the adapter after adding items to the list.
            * */
            arrayAdapter.notifyDataSetChanged();

        }, error -> Log.e(TAG, "populateItems: Error occured: " +error.getMessage()));


        // Add the request to the queue
        requestQueue.add(stringRequest);
    }
}