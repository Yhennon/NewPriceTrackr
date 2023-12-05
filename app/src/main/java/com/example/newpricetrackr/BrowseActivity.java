package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {
    private static final String TAG = "BrowseActivity";

    ListView browseListView;
    ArrayAdapter<String> arrayAdapter;
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
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search here");

        MenuItem sortItem = menu.add(Menu.NONE, R.id.sort, Menu.NONE, "Sort");
        sortItem.setIcon(android.R.drawable.ic_menu_sort_by_size);
        sortItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        sortItem.setOnMenuItemClickListener(item -> {
            showSortOptions2(); // Method to display sort options
            return true;
        });

        return true;
    }

    private void showSortOptions2() {
        View view = findViewById(R.id.sort); // Adjust the ID according to your layout
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.sort_price_asc) {
                fetchItemsSorted("asc", "");
                return true;
            } else if (itemId == R.id.sort_price_desc) {
                fetchItemsSorted("desc","");
                return true;
            } else if (itemId == R.id.sort_name_asc){
                fetchItemsSorted("","asc");
                return true;
            } else if (itemId == R.id.sort_name_desc) {
                fetchItemsSorted("", "desc");
                return true;
            }
//             else if (itemId == R.id.sort_itemtype_asc){
//                fetchItemsSorted("","","asc");
//                return true;
//            } else if (itemId == R.id.sort_itemtype_desc) {
//                fetchItemsSorted("","","desc");
//                return true;
//            }
                return false;
            });

        popupMenu.show();
    }


    private void fetchItemsSorted(String price, String name) {
        String priceOrder = "&price_sort="+ price;
        String nameOrder = "&name_sort=" +name;
//        String typeOrder = "&item_type_sort="+itemtype;

        if (name == "") {
            nameOrder = "";
        }

        if (price == "") {
            priceOrder = "";
        }

//        if (itemtype == ""){
//            typeOrder = "";
//        }

        String url = "http://10.0.2.2:8000/items-sorted?sort=True"+priceOrder+nameOrder;

        Log.d(TAG, "fetchItemsSorted: " + url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            List<ModelsClass.Item> listOfItems = gson.fromJson(response, new TypeToken<List<ModelsClass.Item>>(){}.getType());

                            itemList.clear();
                            for (ModelsClass.Item item : listOfItems) {
                                String name = item.getName();
                                double price = item.getPrice();

                                String itemInfo = name + ", " + price + " dkk";
                                itemList.add(itemInfo);
                            }

                            arrayAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e(TAG, "Error fetching items: " + error.toString());
                    }
                }
        );

        requestQueue.add(stringRequest);
    }


    private void populateItems(ArrayList<String> itemList) {
        String url = "http://10.0.2.2:8000/items/";

        RequestQueue requestQueue = Volley.newRequestQueue(BrowseActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.d(TAG, "populateItems: " + response);

            Gson gson = new Gson();

            java.lang.reflect.Type itemType = new TypeToken<List<ModelsClass.Item>>() {}.getType();

            List<ModelsClass.Item> listOfItems = gson.fromJson(response, itemType);

            itemList.clear();
            for (ModelsClass.Item item : listOfItems){
                String name = item.getName();
                double price = item.getPrice();

                String itemInfo = name + ", " + price + " dkk";
                itemList.add(itemInfo);
            }

            arrayAdapter.notifyDataSetChanged();

            // Set click listener after populating the items
            browseListView.setOnItemClickListener((parent, view, position, id) -> {
                ModelsClass.Item selectedItem = listOfItems.get(position);

                Intent intent = new Intent(BrowseActivity.this, ItemDetailsActivity.class);

                intent.putExtra("itemName", selectedItem.getName());
                intent.putExtra("itemPrice", selectedItem.getPrice());
                intent.putExtra("type", selectedItem.getItemtype());
                Log.d(TAG, "populateItems: distributorID" + selectedItem.getDistributor_id());
                intent.putExtra("distributorID", String.valueOf(selectedItem.getDistributor_id()));

                startActivity(intent);
            });

        }, error -> Log.e(TAG, "populateItems: Error occurred: " + error.getMessage()));

        requestQueue.add(stringRequest);
    }



//    private void populateItems(ArrayList<String> itemList) {
//        String url = "http://10.0.2.2:8000/items/";
//
//        RequestQueue requestQueue = Volley.newRequestQueue(BrowseActivity.this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//            Log.d(TAG, "populateItems: " + response);
//
//            Gson gson = new Gson();
//
//            java.lang.reflect.Type itemType = new TypeToken<List<ModelsClass.Item>>() {}.getType();
//
//            List<ModelsClass.Item> listOfItems = gson.fromJson(response, itemType);
//
//            // Sort the list based on price in descending order
//            Collections.sort(listOfItems, (item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));
//
//            itemList.clear();
//            for (ModelsClass.Item item : listOfItems){
//                String name = item.getName();
//                double price = item.getPrice();
//                String type = item.getItemtype();
//                String distributorID = String.valueOf(item.getDistributor_id());
//
//                String itemInfo = name + ", " + price + " dkk";
//                itemList.add(itemInfo);
//
//                browseListView.setOnItemClickListener((parent, view, position, id) -> {
//                    // Retrieve the clicked item
//                    String selectedItem = itemList.get(position);
//
//                    // Split the item info to get individual details (name, price, etc.)
//                    String[] itemDetails = selectedItem.split(", ");
//
//                    // Create an Intent to start the ItemDetailsActivity
//                    Intent intent = new Intent(BrowseActivity.this, ItemDetailsActivity.class);
//
//                    // Pass necessary data to the ItemDetailsActivity using intent extras
//                    intent.putExtra("itemName", itemDetails[0]);
//                    intent.putExtra("itemPrice", Double.parseDouble(itemDetails[1].replace(" dkk", "")));
//
//                    // Pass additional attributes directly from the loop
//                    intent.putExtra("type", type);
//                    intent.putExtra("distributorID", distributorID);
//
//                    startActivity(intent);
//                });
//
//            }
//
//            arrayAdapter.notifyDataSetChanged();
//        }, error -> Log.e(TAG, "populateItems: Error occurred: " + error.getMessage()));
//
//        requestQueue.add(stringRequest);
//    }
}