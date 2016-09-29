package com.binsearch.binsearch;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
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

    Firebase mRef;
    BinData foundStuff;
    String userSearch;
    TextView textView;
    int searchType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchField = (SearchView)findViewById(R.id.searchView);
        searchField.setIconifiedByDefault(false);
        searchField.setQueryHint("Item number or location");
        textView = (TextView)findViewById(R.id.errorMessage);

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
                mRef = new Firebase("https://bin-search.firebaseio.com/");


                if(searchType == 0) {


                    mRef.child(query).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                foundStuff = dataSnapshot.getValue(BinData.class);
                                foundStuff.setKey(dataSnapshot.getKey());
                                textView.setText("");
                                Intent resultsScreen = new Intent(getApplicationContext(), SearchResult.class); // This intent will be used to start the next activity
                                //  Intent resultsScreen = new Intent();
                                String[] toSend = new String[3];
                                // For now these are just set to display the query - Will change later to the retrieved data once we find a way to retrieve it
                                toSend[0] = foundStuff.getKey(); // Will hold the Number
                                toSend[1] = foundStuff.getBin(); // Will hold the Location
                                toSend[2] = foundStuff.getDescription(); // Will hold the description
                                resultsScreen.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
                                startActivity(resultsScreen); // Start the next activity
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
                                System.out.println("Comparing " + child.child("bin").getValue(String.class) + " and " + userSearch);
                                if(child.child("bin").getValue(String.class).equals(userSearch)) {
                                    foundStuff = child.getValue(BinData.class);
                                    foundStuff.setKey(child.getKey());
                                    textView.setText("");
                                    Intent resultsScreen = new Intent(getApplicationContext(), SearchResult.class); // This intent will be used to start the next activity
                                    //  Intent resultsScreen = new Intent();
                                    String[] toSend = new String[3];
                                    // For now these are just set to display the query - Will change later to the retrieved data once we find a way to retrieve it
                                    toSend[0] = foundStuff.getKey(); // Will hold the Number
                                    toSend[1] = foundStuff.getBin(); // Will hold the Location
                                    toSend[2] = foundStuff.getDescription(); // Will hold the description
                                    resultsScreen.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
                                    startActivity(resultsScreen); // Start the next activity
                                    foundLocation = 1;
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


    // this is a thing that I have added //
}
