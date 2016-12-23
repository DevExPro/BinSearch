package com.binsearch.binsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResult extends AppCompatActivity {

    String [] searchReceived; // Holds the bin firebase information received from MainActivity
    static final int PICK_CONTACT_REQUEST = 1;

    /* Purpose: This class is used as an activity called by MainActivity when the user searches for existing bin information. It receives an array of information the bin found
     * by MainActivity, and displays the information in TextViews for the user to see. It also provides an 'edit' button. When clicked, the button initiates an instance
     * of the EditBinData activity, and sends the array of bin information with it. When the EditBinData activity finishes, it receives the array of edited information
     * (in the onActivity Result function which is implicitly called upon return) which it then sends as its return value to the MainActivity. */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultsScreen) { // Called implicitly when EditBinData returns with a return value
        if(requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                {
                   // String [] received = new String[3];
                   // received = resultsScreen.getStringArrayExtra("newInfo");
                   // resultsScreen.putExtra("newInfo", received);
                    setResult(RESULT_OK, resultsScreen); // Set the returned Intent to be the intent received by MainActivity
                    finish(); // Finish the SearchResult activity
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // When the SearchResult activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent gatheredIntent = getIntent(); // Get the intent that was passed into the activity
        searchReceived = gatheredIntent.getStringArrayExtra("foundItem"); // Take the array of item info out of the intent

        TextView itemNum = (TextView)findViewById(R.id.NumberVal); // Set connection to the TextView that will display the bin number
        TextView itemLocate = (TextView)findViewById(R.id.LocationVal); // Set connection to the TextView that will display the bin location
        TextView itemDescript = (TextView)findViewById(R.id.DescriptionVal); // Set connection to TextView that will display the description

        itemNum.setText(searchReceived[0]); // Display the bin number
        itemLocate.setText(searchReceived[1]); // Display the bin location
        itemDescript.setText(searchReceived[2]); // Display the description

        Button editReceived = (Button)findViewById(R.id.editInfo); // Create reference to Edit button
        editReceived.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // If the edit button is clicked by the user
                Intent resultsScreen = new Intent(getApplicationContext(), EditBinData.class); // Create an intent that will start the EditBinData activity
                resultsScreen.putExtra("foundItem", searchReceived); // Store the array of strings in the intent that gets passed to the next activity
                startActivityForResult(resultsScreen, 1); // Start the EditBinData activity expecting return data. onActivityResult will be called implicitly upon its return
            }
        });
    }
}
