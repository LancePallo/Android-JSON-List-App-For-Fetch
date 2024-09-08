package com.example.androidjsonapp;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private ListView fetchListView;
    private FetchAdapter fetchAdapter;
    private ArrayList<FetchItem> fetchItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchListView = findViewById(R.id.fetchListView);
        fetchItems = new ArrayList<>();
        fetchAdapter = new FetchAdapter(this, fetchItems);
        fetchListView.setAdapter(fetchAdapter);

        fetchData();
    }

    private void fetchData() {
        String url = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        fetchItems.clear(); // Clear the list before populating new data

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                int listId = jsonObject.getInt("listId");
                                String name = jsonObject.optString("name", null);

                                // Filter out items with null or empty names
                                if (name != "null" && !name.trim().isEmpty()) {
                                    fetchItems.add(new FetchItem(id, listId, name));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Sort the items by listId, then by name with numerical sorting
                        Collections.sort(fetchItems, new Comparator<FetchItem>() {
                            @Override
                            public int compare(FetchItem o1, FetchItem o2) {
                                // Compare listId first
                                int compareListId = Integer.compare(o1.listId, o2.listId);
                                if (compareListId == 0) {
                                    // Use custom logic to compare names with numeric parts
                                    return extractNumber(o1.name) - extractNumber(o2.name);
                                }
                                return compareListId;
                            }
                        });

                        fetchAdapter.notifyDataSetChanged(); // Notify the adapter
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    // Helper method to extract numbers from the item name for numerical sorting
    private int extractNumber(String name) {
        String number = name.replaceAll("[^0-9]", ""); // Extract numbers from the string
        return number.isEmpty() ? 0 : Integer.parseInt(number); // Return 0 if no number found
    }

}
