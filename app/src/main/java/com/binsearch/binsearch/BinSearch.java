package com.binsearch.binsearch;

import com.firebase.client.Firebase;

/**
 * Created by Gabriel on 9/17/2016.
 */
public class BinSearch extends android.app.Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
