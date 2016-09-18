package com.binsearch.binsearch;

<<<<<<< HEAD

=======
>>>>>>> origin/master
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_main);

    }

    protected void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(
                intent.getAction())){
            String query = intent.getStringExtra(
                    SearchManager.QUERY);

=======
        setContentView(R.layout.activity_search_result);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
>>>>>>> origin/master
        }
    }
}
