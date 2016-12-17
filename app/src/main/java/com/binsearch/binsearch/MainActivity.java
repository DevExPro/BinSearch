package com.binsearch.binsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    Firebase mRef = new Firebase("https://bin-search.firebaseio.com/");
    BinData foundStuff;
    String userSearch;
    TextView textView;
    int searchType = 0;
    static final int PICK_CONTACT_REQUEST = 1;
    String[] toSend = new String[3];


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultsScreen) {
        if(requestCode == PICK_CONTACT_REQUEST)
            if(resultCode ==RESULT_OK) {
                String [] received = resultsScreen.getStringArrayExtra("newInfo");
                    if(received[0].equals(null) == false && received[0].equals("") == false) {
                        if(toSend[0].equals("") == false) {
                            if (toSend[0].equals(null) == false && toSend[0].equals(received[0]) == false)
                                mRef.child(toSend[0]).removeValue();
                            if (received[1].equals(null) == false && received[1].equals("") == false)
                                mRef.child(received[0]).child("bin").setValue(received[1]);
                            if (received[2].equals(null) == false && received[2].equals("") == false)
                                mRef.child(received[0]).child("description").setValue(received[2]);
                        }
                        else
                        {
                            if (received[1].equals(null) == false && received[1].equals("") == false)
                                mRef.child(received[0]).child("bin").setValue(received[1]);
                            if (received[2].equals(null) == false && received[2].equals("") == false)
                                mRef.child(received[0]).child("description").setValue(received[2]);
                        }
                 }
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchView searchField = (SearchView)findViewById(R.id.searchView);
        searchField.setIconifiedByDefault(false);
        searchField.setQueryHint("Item number or location");
        textView = (TextView)findViewById(R.id.errorMessage);

        Button newBinButton = (Button)findViewById(R.id.newBin);
        newBinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent gatheredIntent = new Intent(getApplicationContext(), EditBinData.class);
                toSend[0] = "";
                toSend[1] = "";
                toSend[2] = "";
                gatheredIntent.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
                //startActivity(resultsScreen); // Start the next activity

                startActivityForResult(gatheredIntent, 1);
            }
        });

        Spinner searchOption = (Spinner) findViewById(R.id.searchOptions);
        searchOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchField.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userSearch = query;
               // mRef = new Firebase("https://bin-search.firebaseio.com/");


                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() == false) {
                    textView.setText("Please connect to internet");
                    textView.setTextColor(Color.RED);
                }


                if(searchType == 0) {


                    mRef.child(query).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                if(dataSnapshot.hasChild("bin") && dataSnapshot.hasChild("description")){
                                    foundStuff = dataSnapshot.getValue(BinData.class);
                                }
                                else if (dataSnapshot.hasChild("bin"))
                                {
                                    foundStuff = new BinData();
                                    foundStuff.setBin(dataSnapshot.child("bin").getValue(String.class));
                                    foundStuff.setDescription("No Description");
                                }
                                else if( dataSnapshot.hasChild("description"))
                                {
                                    foundStuff = new BinData();
                                    foundStuff.setBin("No Bin Location");
                                    foundStuff.setDescription(dataSnapshot.child("description").getValue(String.class));
                                }
                                else
                                {
                                    foundStuff = new BinData();
                                    foundStuff.setBin("No Bin Location");
                                    foundStuff.setDescription("No Description");
                                }

                                foundStuff.setKey(dataSnapshot.getKey());

                                textView.setText("");
                                Intent resultsScreen = new Intent(getApplicationContext(), SearchResult.class); // This intent will be used to start the next activity
                                //  Intent resultsScreen = new Intent();
                                // For now these are just set to display the query - Will change later to the retrieved data once we find a way to retrieve it
                                toSend[0] = foundStuff.getKey(); // Will hold the Number
                                toSend[1] = foundStuff.getBin(); // Will hold the Location
                                toSend[2] = foundStuff.getDescription(); // Will hold the description
                                resultsScreen.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
                                //startActivity(resultsScreen); // Start the next activity

                                startActivityForResult(resultsScreen, 1);
                            } else {
                                textView.setText("Item number '" + userSearch + "' does not exist.");
                                textView.setTextColor(Color.RED);
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("Failure");
                        }
                    });


                }
                else if (searchType == 1)
                {
                    Query searchLocation = mRef;


                    searchLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int foundLocation = 0;
                            for(DataSnapshot child : dataSnapshot.getChildren()) {
                                if(child.hasChild("bin")) {
                                    if (child.child("bin").getValue(String.class).equals(userSearch)) {
                                        if(child.hasChild("description") == false) {
                                            foundStuff = new BinData();
                                            foundStuff.setDescription("No Description");
                                            foundStuff.setBin(child.child("bin").getValue(String.class));
                                        }
                                        else {
                                            foundStuff = child.getValue(BinData.class);
                                        }
                                        foundStuff.setKey(child.getKey());
                                        textView.setText("");
                                        Intent resultsScreen = new Intent(getApplicationContext(), SearchResult.class); // This intent will be used to start the next activity
                                        //  Intent resultsScreen = new Intent();
                                        // For now these are just set to display the query - Will change later to the retrieved data once we find a way to retrieve it
                                        toSend[0] = foundStuff.getKey(); // Will hold the Number
                                        toSend[1] = foundStuff.getBin(); // Will hold the Location
                                        toSend[2] = foundStuff.getDescription(); // Will hold the description
                                        resultsScreen.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
                                        startActivityForResult(resultsScreen, 1);
                                        foundLocation = 1;
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
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
           //     Toast.makeText(getApplicationContext(), "Changed Text", Toast.LENGTH_LONG).show();
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
        if(snapshot.hasChild(userSearch)){
            return true;
        }
        else{
            return false;
        }
    }


    // this is a thing that I have added //
}
