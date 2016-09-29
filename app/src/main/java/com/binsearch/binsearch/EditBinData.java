package com.binsearch.binsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditBinData extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_edit);

        Intent gatheredIntent = getIntent(); // Get the intent that was passed into the activity
        String [] searchReceived = gatheredIntent.getStringArrayExtra("foundItem"); // Take the array of item info out of the intent

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
                
            }
    });
}}
