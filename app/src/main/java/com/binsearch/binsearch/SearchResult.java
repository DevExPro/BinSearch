package com.binsearch.binsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResult extends AppCompatActivity {

    String [] searchReceived;
    String [] newSearchReceived;

    static final int PICK_CONTACT_REQUEST = 1;
    String [] received = new String[3];

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultsScreen) {
        if(requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                {
                    received = resultsScreen.getStringArrayExtra("newInfo");
                    resultsScreen.putExtra("newInfo", received);
                    setResult(RESULT_OK, resultsScreen);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent gatheredIntent = getIntent(); // Get the intent that was passed into the activity
        searchReceived = gatheredIntent.getStringArrayExtra("foundItem"); // Take the array of item info out of the intent

        // Set connection to TextViews that will hold the received information
        TextView itemNum = (TextView)findViewById(R.id.NumberVal);
        TextView itemLocate = (TextView)findViewById(R.id.LocationVal);
        TextView itemDescript = (TextView)findViewById(R.id.DescriptionVal);

        // Set the TextViews to to the received information
        itemNum.setText(searchReceived[0]);
        itemLocate.setText(searchReceived[1]);
        itemDescript.setText(searchReceived[2]);

        Button editReceived = (Button)findViewById(R.id.editInfo);
        editReceived.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent resultsScreen = new Intent(getApplicationContext(), EditBinData.class);
                resultsScreen.putExtra("foundItem", searchReceived); // Store the array of strings in the intent that gets passed to the next activity
                //startActivity(resultsScreen); // Start the next activity
                startActivityForResult(resultsScreen, 1);
            }
        });
    }
}
