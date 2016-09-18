package com.binsearch.binsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toast.makeText(getApplicationContext(), "Reached the SearchResult class", Toast.LENGTH_LONG).show();

        Intent gatheredIntent = getIntent(); // Get the intent that was passed into the activity
        String [] searchReceived = gatheredIntent.getStringArrayExtra("foundItem"); // Take the array of item info out of the intent

        // Set connection to TextViews that will hold the received information
        TextView itemNum = (TextView)findViewById(R.id.NumberVal);
        TextView itemLocate = (TextView)findViewById(R.id.LocationVal);
        TextView itemDescript = (TextView)findViewById(R.id.DescriptionVal);

        // Set the TextViews to to the received information
        itemNum.setText(searchReceived[0]);
        itemLocate.setText(searchReceived[1]);
        itemDescript.setText(searchReceived[2]);
    }
}
