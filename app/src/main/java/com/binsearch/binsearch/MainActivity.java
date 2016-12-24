package com.binsearch.binsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Firebase mRef = new Firebase("https://bin-search.firebaseio.com/"); // Reference to the firebase database
    BinData foundStuff; // Will hold any key, bin location, and description data attained from the firebase
    String userSearch; // Will hold the string that the user searches for in the the search box
    TextView textView; // A reference to the textview. It will be invisible unless there is some warning to display to the user
    int searchType = 0; // 0 indicates that the user wishes to search for bin number. 1 will indicate that they wish to search for bin location
    static final int PICK_CONTACT_REQUEST = 1;
    String[] toSend = new String[3]; // Array of strings that will hold the values of data attained from firebase to send to SearchResult of EditBinData activities
    int callActivity = 0; // 0 means that no matching data has been found, 1 means that a match HAS been found
    int displayMessage = 0; // 0 means that no alertDialog should be displayed, 1 means that alertDialog needs to be displayed
    android.content.Context accessThis = this;

    /* Purpose: This Class is used as the main activity for the app. It gives users the option to search for bin information that exists in the firebase (by either
     * bin number or bin location), add a new bin to the firebase database. It will search the firebase depending on which piece of information the user wants,
     * number or location, and if matching information is found, it initiates an instance of SearchResult to display the found information. If no match is found,
     * a message is displayed to the user explaining that that information could not be found. */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultsScreen) { // This function is called implicitly upon returning from the SearchResult or
        if (requestCode == PICK_CONTACT_REQUEST)                                              // EditBinData activities with return values
            if (resultCode == RESULT_OK) {
                final String[] received = resultsScreen.getStringArrayExtra("newInfo"); // Extract the array of strings(the information to be put in firebase) from the returned intent
                if (!received[0].equals(null) && !received[0].equals("")) { // If there is a key to the new data
                    if (!received[0].equals(toSend[0])) { // If the child exists and it is not the same key that it started with
                        mRef.child(received[0]).addValueEventListener(new ValueEventListener() { // Look for the key in firebase
                            //   Query queryRef = mRef.equalTo(received[0]).limitToFirst(1);
                            //         if (queryRef != null) { // If that key already exists in firebase, report that an alert should be displayed
                            // If the user is trying to add or change data to a different existing key, display a message to the user
                            //System.out.println(queryRef);
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(accessThis);
                                    builder.setMessage("Do you wish to override it?");
                                    builder.setCancelable(false) // Stop alertDialog from disappearing
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) { // If the user says 'yes' to having the data overriden
                                                    // mRef.child(toSend[0]).removeValue(); // Remove the existing key data
                                                    if (!received[1].equals(null) && !received[1].equals("")) // If a bin location has been specified, set it in firebase
                                                        mRef.child(received[0]).child("bin").setValue(received[1]);
                                                    if (!received[2].equals(null) && !received[2].equals("")) // If a description has been specified, set it in firebase
                                                        mRef.child(received[0]).child("description").setValue(received[2]);
                                                    dialog.cancel();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) { // If they don't want to override the data, return to the MainActivity screen
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.setTitle(received[0] + " already exists as an item in the database. ");
                                    alert.show(); // Display the alert to the user
                                } else // Otherwise, create the new data in firebase
                                {
                                    mRef.child(received[0]).setValue("");
                                    if (!received[1].equals(null) && !received[1].equals(""))
                                        mRef.child(received[0]).child("bin").setValue(received[1]);
                                    if (!received[2].equals(null) && !received[2].equals(""))
                                        mRef.child(received[0]).child("description").setValue(received[2]);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
                    } else // If creating new data or editing same data
                    {
                        if (received[0].equals(toSend[0])) // If the data exists and is being edited
                            mRef.child(toSend[0]).removeValue(); // Remove data under the original key from firebase
                        mRef.child(received[0]).setValue(""); // Create the key and set it to be empty
                        if (!received[1].equals(null) && !received[1].equals("")) // If the bin field is not empty, set the bin for the key in firebase
                            mRef.child(received[0]).child("bin").setValue(received[1]);
                        if (!received[2].equals(null) && !received[2].equals("")) // If the description field is not empty, set the description for the key in firebase
                            mRef.child(received[0]).child("description").setValue(received[2]);
                    }
                }
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // When the MainActivity has been created

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchView searchField = (SearchView)findViewById(R.id.searchView); // Create reference to searchView box on the mainActivity page - where user puts search
        searchField.setIconifiedByDefault(false);
        searchField.setQueryHint("Item number or location"); // Hint text placed in the search box
        textView = (TextView)findViewById(R.id.errorMessage); // Create reference to textView on the mainActivity page - used to display error and warnings to user

        final Button result1 = (Button)findViewById(R.id.result1);
        final Button result2 = (Button)findViewById(R.id.result2);
        final Button result3 = (Button)findViewById(R.id.result3);

        Button newBinButton = (Button)findViewById(R.id.newBin); // Create reference to the 'New Bin' button on mainActivity page
        newBinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // When the 'New Bin' button is clicked by the user
                Intent gatheredIntent = new Intent(getApplicationContext(), EditBinData.class); // Create an intent to begin the EditBinData activity
                toSend[0] = "";  // Set the array of strings that will be sent into the activity to be empty
                toSend[1] = "";
                toSend[2] = "";
                gatheredIntent.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the EditBinData activity
                startActivityForResult(gatheredIntent, 1); // Start the EditBinData activity expecting a return value. When it returns, this implicitly calls onActivityFstartResult
            }
        });

        result1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // When the 'New Bin' button is clicked by the user
                final String buttonText = result1.getText().toString();
                mRef.child(buttonText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toSend[1] = dataSnapshot.child("bin").getValue(String.class);  // Set the array of strings that will be sent into the activity to be empty
                        System.out.println("toSend[0]: " + toSend[0]);
                        toSend[0] = buttonText;
                        toSend[2] = dataSnapshot.child("description").getValue(String.class);

                        Intent gatheredIntent = new Intent(getApplicationContext(), SearchResult.class); // Create an intent to begin the EditBinData activity

                        gatheredIntent.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the EditBinData activity
                        startActivityForResult(gatheredIntent, 1); // Start the EditBinData activity expecting a return value. When it returns, this implicitly calls onActivityResult
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });

        result2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // When the 'New Bin' button is clicked by the user
                final String buttonText = result2.getText().toString();
                mRef.child(buttonText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toSend[1] = dataSnapshot.child("bin").getValue(String.class);  // Set the array of strings that will be sent into the activity to be empty
                        System.out.println("toSend[0]: " + toSend[0]);
                        toSend[0] = buttonText;
                        toSend[2] = dataSnapshot.child("description").getValue(String.class);

                        Intent gatheredIntent = new Intent(getApplicationContext(), SearchResult.class); // Create an intent to begin the EditBinData activity

                        gatheredIntent.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the EditBinData activity
                        startActivityForResult(gatheredIntent, 1); // Start the EditBinData activity expecting a return value. When it returns, this implicitly calls onActivityResult
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });

        result3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // When the 'New Bin' button is clicked by the user
                final String buttonText = result3.getText().toString();
                mRef.child(buttonText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toSend[1] = dataSnapshot.child("bin").getValue(String.class);  // Set the array of strings that will be sent into the activity to be empty
                        System.out.println("toSend[0]: " + toSend[0]);
                        toSend[0] = buttonText;
                        toSend[2] = dataSnapshot.child("description").getValue(String.class);

                        Intent gatheredIntent = new Intent(getApplicationContext(), SearchResult.class); // Create an intent to begin the EditBinData activity

                        gatheredIntent.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the EditBinData activity
                        startActivityForResult(gatheredIntent, 1); // Start the EditBinData activity expecting a return value. When it returns, this implicitly calls onActivityResult
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });





        Spinner searchOption = (Spinner) findViewById(R.id.searchOptions); // Create a reference to the searchOptions
        searchOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // When a searchOption has been selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchType = position; // Set 'searchType' to correspond to the user's search preference
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        searchField.setOnQueryTextListener( new SearchView.OnQueryTextListener() { // Listen for when the user submits their search
            @Override
            public boolean onQueryTextSubmit(String query) { // When the user submits their search
                userSearch = query; // Set the userSearch string to be what the user entered into the searchField

                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (!mWifi.isConnected()) { // If the user is not connected to wifi, warn them
                    textView.setText("Please connect to internet"); // Display the warning in the TextView in red
                    textView.setTextColor(Color.RED);
                }

                if(searchType == 0) { // If the user wants to search by item number
                    mRef.child(query).addValueEventListener(new ValueEventListener() { // Look for data by that key in firebase
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) { // If data with that key has been found in firebase
                                if(dataSnapshot.hasChild("bin") && dataSnapshot.hasChild("description")){ // If the firebase has both bin and description data for the requested key
                                    foundStuff = dataSnapshot.getValue(BinData.class); // Load all of the info into an instance of the 'BinData' class
                                }
                                else if(dataSnapshot.hasChild("bin")) // If the firebase only has bin location information for the requested key
                                {
                                    foundStuff = new BinData(); // Create a new 'Bin Data'
                                    foundStuff.setBin(dataSnapshot.child("bin").getValue(String.class)); // Load the value of bin location into the BinData
                                    foundStuff.setDescription("No Description"); // Report that no description has been set
                                }
                                else if(dataSnapshot.hasChild("description")) // If the firebase only has description information for the requested key
                                {
                                    foundStuff = new BinData(); // Create a new 'Bin Data'
                                    foundStuff.setBin("No Bin Location"); // Report that no bin location has been set
                                    foundStuff.setDescription(dataSnapshot.child("description").getValue(String.class)); // Load the value of description into the BinData
                                }
                                else // If the firebase doesn't have a bin or description for the requested key
                                {
                                    foundStuff = new BinData(); // Create a new 'Bin Data'
                                    foundStuff.setBin("No Bin Location"); // Report that no bin location has been set
                                    foundStuff.setDescription("No Description"); // Report that no description has been set
                                }

                                foundStuff.setKey(dataSnapshot.getKey()); // Load the value of the key into the BinData

                                textView.setText(""); // Clear any existing warnings from the MainActivity screen's TextView

                                toSend[0] = foundStuff.getKey(); // Will hold the Bin number
                                toSend[1] = foundStuff.getBin(); // Will hold the Bin Location
                                toSend[2] = foundStuff.getDescription(); // Will hold the description

                                if(callActivity == 0) { // If the user's search has been located in the firebase
                                    Intent resultsScreen = new Intent(getApplicationContext(), SearchResult.class); // Create a intent that will start the SearchResult activity
                                    resultsScreen.putExtra("foundItem", toSend); // Add the array of strings containing the information found in firebase to the intent
                                    startActivityForResult(resultsScreen, 1); // Start the activity expecting a return value. onActivityResult is implicitly called upon return
                                    callActivity = 1; // Reset to 0, showing that no activity needs to be called
                                }
                            } else {
                                textView.setText("Item number '" + userSearch + "' does not exist.");
                                textView.setTextColor(Color.RED);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }
                else if (searchType == 1) // If the user wants to search by bin location
                {
                    Query searchLocation = mRef; // Set the query to reference the firebase database
                    searchLocation.addListenerForSingleValueEvent(new ValueEventListener() { // Set a listener for the data in firebase
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) { // When data is changed or when app is MainActivity first created
                            int foundLocation = 0; // Set it so that no location is reported, by default
                            for(DataSnapshot child : dataSnapshot.getChildren()) { // Go through each key in the firebase
                                if(child.hasChild("bin")) { // If the key has a 'bin' child
                                    if (child.child("bin").getValue(String.class).equals(userSearch)) { // If the requested bin location has been located
                                        if(!child.hasChild("description")) { // If no description has been provided with the bin info
                                            foundStuff = new BinData(); // Create a new instance of BinData
                                            foundStuff.setDescription("No Description"); // Report that no description was set
                                            foundStuff.setBin(child.child("bin").getValue(String.class)); // Set the value of bin location to the value received from firebase
                                        }
                                        else { // If the located bin has a description
                                            foundStuff = child.getValue(BinData.class); // Load all values into instance of BinData
                                        }
                                        foundStuff.setKey(child.getKey()); // Set the key of instance of BinData to the key retrieved from the firebase
                                        textView.setText(""); // Remove existing warnings from the textView in MainActivity

                                        toSend[0] = foundStuff.getKey(); // Will hold the Bin Number
                                        toSend[1] = foundStuff.getBin(); // Will hold the Bin Location
                                        toSend[2] = foundStuff.getDescription(); // Will hold the description

                                        foundLocation = 1; // Report that a bin location has been found
                                        if(callActivity == 0) { // If the user's search has been located in the firebase
                                            Intent resultsScreen = new Intent(getApplicationContext(), SearchResult.class); // Create a intent that will start the SearchResult activity
                                            resultsScreen.putExtra("foundItem", toSend); // Add the array of strings containing the information found in firebase to the intent
                                            startActivityForResult(resultsScreen, 1); // Start the activity expecting a return value. onActivityResult is implicitly called upon return
                                            callActivity = 1; // Reset to 0, showing that no activity needs to be called
                                        }
                                    }
                                }
                            }
                            if(foundLocation == 0)
                            {
                                textView.setText("Bin location '" + userSearch + "' does not exist.");
                                textView.setTextColor(Color.RED);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                    callActivity = 0;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
           //     Toast.makeText(getApplicationContext(), "Changed Text", Toast.LENGTH_LONG).show();
                userSearch = query;

                if (searchType == 0) {
                    Query queryRef = mRef.orderByKey().startAt(userSearch).endAt(userSearch + "\uf8ff").limitToFirst(3);
                    //System.out.println(queryRef);
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int foundLocation = 0;
                            int iteration = 0;
                            System.out.println(userSearch);
                            for(DataSnapshot child : dataSnapshot.getChildren()) {
                                ++iteration;
                                if(iteration == 1){
                                    result1.setVisibility(View.VISIBLE);
                                    result1.setText(child.getKey());
                                }
                                if(iteration == 2){
                                    result2.setVisibility(View.VISIBLE);
                                    result2.setText(child.getKey());
                                }
                                if(iteration == 3){
                                    result3.setVisibility(View.VISIBLE);
                                    result3.setText(child.getKey());
                                }


                            }
                            if(iteration == 0){
                                result1.setVisibility(View.INVISIBLE);

                                result2.setVisibility(View.INVISIBLE);
                                result3.setVisibility(View.INVISIBLE);
                                result1.setText("");
                                result2.setText("");
                                result3.setText("");
                            }
                            if(iteration == 1){
                                result2.setVisibility(View.INVISIBLE);
                                result3.setVisibility(View.INVISIBLE);
                                result2.setText("");
                                result3.setText("");
                            }
                            else if(iteration == 2){
                                result3.setVisibility(View.INVISIBLE);
                                result3.setText("");
                            }
                            iteration = 0;

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                return false;
            }
            });
    }

    @Override
    protected void onStart(){
        super.onStart();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private boolean onDataChange(DataSnapshot snapshot){
        if(snapshot.hasChild(userSearch))
            return true;
        else
            return false;
    }
}
