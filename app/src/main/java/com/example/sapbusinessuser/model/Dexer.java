package com.example.sapbusinessuser.model;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import io.realm.Realm;

public class Dexer extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}