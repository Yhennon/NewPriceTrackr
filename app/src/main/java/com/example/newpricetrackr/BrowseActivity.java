package com.example.newpricetrackr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
//        MenuItem menuItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Search here");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                arrayAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

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
//
//    private void showSortOptions() {
//        View view = findViewById(R.id.sort); // Adjust the ID according to your layout
//        PopupMenu popupMenu = new PopupMenu(this, view);
//        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
//
//        popupMenu.setOnMenuItemClickListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.sort_price_asc) {
//                // Call method to fetch items in ascending order of price
//                fetchItemsSortedByPrice(true);
//                return true;
//            } else if (itemId == R.id.sort_price_desc) {
//                // Call method to fetch items in descending order of price
//                fetchItemsSortedByPrice(false);
//                return true;
//            } else if (itemId == R.id.sort_name_asc){
//                fetchItemsSortedByName(true);
//                return true;
//            } else if (itemId == R.id.sort_name_asc) {
//                fetchItemsSortedByName(false);
//                return true;
//            }
//                return false;
//        });
//
//        popupMenu.show();
//    }
//
//    private void fetchItemsSortedByName(boolean ascending,) {
//        String sortOrder = ascending ? "True" : "False";
//        String url = "http://10.0.2.2:8000/items?sort=" + sortOrder;
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Handle successful response and update itemList
//                        try {
//                            Gson gson = new Gson();
//                            List<ModelsClass.Item> listOfItems = gson.fromJson(response, new TypeToken<List<ModelsClass.Item>>(){}.getType());
//
//                            itemList.clear();
//                            for (ModelsClass.Item item : listOfItems) {
//                                String name = item.getName();
//                                double price = item.getPrice();
//
//                                String itemInfo = name + ", " + price + " dkk";
//                                itemList.add(itemInfo);
//                            }
//
//                            arrayAdapter.notifyDataSetChanged();
//                        } catch (Exception e) {
//                            Log.e(TAG, "Error parsing response: " + e.getMessage());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
//                        Log.e(TAG, "Error fetching items: " + error.toString());
//                    }
//                }
//        );
//
//        requestQueue.add(stringRequest);
//    }


    private void showSortOptions2() {
        View view = findViewById(R.id.sort); // Adjust the ID according to your layout
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.sort_price_asc) {
                // Call method to fetch items in ascending order of price
                fetchItemsSorted("asc", "");
                return true;
            } else if (itemId == R.id.sort_price_desc) {
                // Call method to fetch items in descending order of price
                fetchItemsSorted("desc","");
                return true;
            } else if (itemId == R.id.sort_name_asc){
                fetchItemsSorted("","asc");
                return true;
            } else if (itemId == R.id.sort_name_asc) {
                fetchItemsSorted("","desc");
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    private void fetchItemsSorted(String price, String name) {
        String priceOrder = "&price_sort="+ price;
        String nameOrder = "&name_sort=" +name;

        if (name == "") {
            nameOrder = "";
        }

        String url = "http://10.0.2.2:8000/items-sorted?sort=True"+priceOrder+nameOrder;

        Log.d(TAG, "fetchItemsSorted: " + url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response and update itemList
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

            // Sort the list based on price in descending order
            Collections.sort(listOfItems, (item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));

            itemList.clear();
            for (ModelsClass.Item item : listOfItems){
                String name = item.getName();
                double price = item.getPrice();

                String itemInfo = name + ", " + price + " dkk";
                itemList.add(itemInfo);
            }

            arrayAdapter.notifyDataSetChanged();
        }, error -> Log.e(TAG, "populateItems: Error occurred: " + error.getMessage()));

        requestQueue.add(stringRequest);
    }
}