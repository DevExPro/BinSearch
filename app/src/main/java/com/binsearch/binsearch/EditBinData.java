package com.binsearch.binsearch;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBinData extends AppCompatActivity {
    EditText itemNum; // Will hold a reference to the bin number EditText box
    EditText itemLocate; // Will hold a reference to the bin location EditText box
    EditText itemDescript; // Will hold a reference to the description EditText box

    /* Purpose: This Class is used as an activity called by either MainActivity (if a new bin is being added to the firebase) or by SearchResult (if the user wants to
     * edit existing data in the firebase. The EditBinData page will display the old/existing data (or empty if new), and allow the user to type in all three fields.
     * The user cannot save bin data without a key (bin number), so if the 'save' button is clicked while bin number is empty, a red warning will appear in the bin
     * number box. This warning will dissapear when the user clicks the bin number box. When save is clicked and at least the bin number box is filled, the activity will
     * return an array of the newly entered/edited strings to either the MainActivity or to SearchResult. */

    @Override
    protected void onCreate(Bundle savedInstanceState) { // When the EditBinData activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_edit);

        Intent gatheredIntent = getIntent(); // Get the intent that was passed into the activity
        String [] searchReceived = gatheredIntent.getStringArrayExtra("foundItem"); // Take the array of item info out of the intent

        itemNum = (EditText)findViewById(R.id.NumberVal); // Set a reference to the bin number EditText box
        itemLocate = (EditText)findViewById(R.id.LocationVal); // Set a reference to the bin location EditText box
        itemDescript = (EditText)findViewById(R.id.DescriptionVal); // Set a reference to description EditText box

        itemNum.setText(searchReceived[0]); // Set the value in bin number EditText box to the bin number or empty(If a new bin is being created)
        itemLocate.setText(searchReceived[1]); // Set the value in bin location EditText box
        itemDescript.setText(searchReceived[2]); // Set the value in

        itemNum.setOnClickListener(new View.OnClickListener() { // Listen for when the bin number EditText box is clicked
            @Override
            public void onClick (View view) { // When the EditText box is clicked by the user
                if(itemNum.getText().toString().equals("This field is required")) // If the text in the EditText is the warning
                    itemNum.setText(""); // Get rid of the text in the EditText
            }
        });

        Button editReceived = (Button)findViewById(R.id.saveInfo); // Set a reference to the Save button
        editReceived.setOnClickListener(new View.OnClickListener() { // Listen for when the 'Save' button is clicked by the user

            @Override
            public void onClick(View view) { // When the 'Save' button is clicked by the user
                if(itemNum.getText().toString().equals(null)|| itemNum.getText().toString().equals("") || itemNum.getText().toString().equals("This field is required"))
                { // If the bin number field is NOT filled, display a red text warning to the user to fill the text field
                    itemNum.setText("This field is required");
                    itemNum.setTextColor(Color.RED);
                }
                else { // If at least the bin number field is filled
                    Intent gatheredIntent = getIntent(); // Get the existing intent
                    String[] toSend = new String[3]; // Create an array of strings that will hold the new bin information
                    toSend[0] = itemNum.getText().toString(); // Bin number
                    toSend[1] = itemLocate.getText().toString(); // Bin location
                    toSend[2] = itemDescript.getText().toString(); // Bin description
                    gatheredIntent.putExtra("newInfo", toSend); // Store the array of strings in the intent that gets passed to the next activity
                    setResult(RESULT_OK, gatheredIntent); // Set the returned intent to be the gatheredIntent
                    finish(); // End the EditBinData activity
                }
            }
    });

}
}
