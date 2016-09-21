package com.binsearch.binsearch;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchField = (SearchView)findViewById(R.id.searchView);
        searchField.setQueryHint("Enter item number or location");
        searchField.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mRef = new Firebase("https://bin-search.firebaseio.com/");
                mRef.child(query).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Object foundStuff = dataSnapshot.getValue();
                        System.out.println(foundStuff);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("Failureeeeeee");
                    }
                });


                Intent resultsScreen = new Intent(getApplicationContext(), SearchResult.class); // This intent will be used to start the next activity
              //  Intent resultsScreen = new Intent();
                String [] toSend = new String[3];
                // For now these are just set to display the query - Will change later to the retrieved data once we find a way to retrieve it
                toSend[0] = query; // Will hold the Number
                toSend[1] = query; // Will hold the Location
                toSend[2] = query; // Will hold the description
                resultsScreen.putExtra("foundItem", toSend); // Store the array of strings in the intent that gets passed to the next activity
                startActivity(resultsScreen); // Start the next activity
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
