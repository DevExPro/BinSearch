package com.binsearch.binsearch;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getApplicationContext(), "Reached the SearchableActivity class", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Toast.makeText(getApplicationContext(), "Handling intent", Toast.LENGTH_LONG).show();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

        }
    }

    private void onQueryTextSubmit (String query)
    {
        Toast.makeText(getApplicationContext(), "Inner intent", Toast.LENGTH_LONG).show();
        Intent resultsScreen = new Intent(this, SearchResult.class); // This intent will be used to start the next activity
        String [] toSend = new String[3];
        // For now these are just set to display the query - Will change later to the retrieved data once we find a way to retrieve it
        toSend[0] = query; // Will hold the Number
        toSend[1] = query; // Will hold the Location
        toSend[2] = query; // Will hold the description
        resultsScreen.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
        startActivity(resultsScreen); // Start the next activity
    }

}
