package com.binsearch.binsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditBinData extends AppCompatActivity {
    String [] searchReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_edit);

        Intent gatheredIntent = getIntent(); // Get the intent that was passed into the activity
        searchReceived = gatheredIntent.getStringArrayExtra("foundItem"); // Take the array of item info out of the intent

        // Set connection to TextViews that will hold the received information
        EditText itemNum = (EditText)findViewById(R.id.NumberVal);
        EditText itemLocate = (EditText)findViewById(R.id.LocationVal);
        EditText itemDescript = (EditText)findViewById(R.id.DescriptionVal);

        // Set the TextViews to to the received information
        itemNum.setText(searchReceived[0]);
        itemLocate.setText(searchReceived[1]);
        itemDescript.setText(searchReceived[2]);

        Button editReceived = (Button)findViewById(R.id.saveInfo);
        editReceived.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent gatheredIntent = getIntent();
                String[] toSend = new String[3];
                // For now these are just set to display the query - Will change later to the retrieved data once we find a way to retrieve it
                toSend[0] = searchReceived[0];
                toSend[1] = searchReceived[1];
                toSend[2] = searchReceived[2];
                gatheredIntent.putExtra("sentItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
                setResult(RESULT_OK, gatheredIntent);
                finish();
            }
    });
}}
