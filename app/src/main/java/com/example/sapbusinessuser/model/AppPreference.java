package com.example.sapbusinessuser.model;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    private Context context;
    private SharedPreferences sharedPref;
    public AppPreference(Context context){
        sharedPref = context.getSharedPreferences("App",Context.MODE_PRIVATE);
    }
    public void SetUser(String id){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("id",id);
        editor.commit();
    }
    public String getId(){
        return sharedPref.getString("id","");
    }

    public void Clear(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

    }
}
