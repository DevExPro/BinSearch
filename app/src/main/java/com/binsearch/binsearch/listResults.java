package com.binsearch.binsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class listResults extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    String [] results = {"Result1", "Result2", "Result 3", "Result 4"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_results);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);
    }
}
