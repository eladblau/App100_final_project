package com.example.eladblau.app100;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by eladblau on 02-Feb-15.
 *//*
public class SharedPreferencesObject {

    private static SharedPreferencesObject ourInstance = new SharedPreferencesObject();
    private SharedPreferences sharedPreferencesFile;

    public static SharedPreferencesObject getInstance() {
        return ourInstance;
    }

    //Constructor
    private SharedPreferencesObject() {

    }


    *//******* Create SharedPreferences *******//*
    public void createSharedPreferencesFile(Context context){
        this.sharedPreferencesFile = context.getSharedPreferences
                (config.SHARED_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = pref.edit();
        return;
    }


*//**************** Storing data as KEY/VALUE pair *******************//*

    public void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        editor.putBoolean(key, value);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes
    }
    public void putInt(String key, int value){
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        editor.putInt(key, value);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes
    }
    public void putFloat(String key, float value){
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        editor.putFloat(key, value);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes
    }
    public void putLong(String key, long value){
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        editor.putLong(key, value);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes
    }
    public void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        editor.putString(key, value);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes
    }


*//**************** Get SharedPreferences data *******************//*

// If value for key not exist then return second param value - In this case null

public boolean getBoolean(String key, boolean defValue){
    return sharedPreferencesFile.getBoolean(key, defValue);
}
    public int getInt(String key, int defValue){
        return sharedPreferencesFile.getInt(key, defValue);
    }
    public float getFloat(String key, float defValue){
        return sharedPreferencesFile.getFloat(key, defValue);
    }
    public long getLong(String key, long defValue){
        return sharedPreferencesFile.getLong(key, defValue);
    }
    public String getString(String key, String defValue){
        return sharedPreferencesFile.getString(key, defValue);
    }


*//************ Deleting Key value from SharedPreferences *****************//*

    public void deleteKey(String key){
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        editor.remove(key);
        // Save the changes in SharedPreferences
        editor.commit(); // commit changes
    }

*//************ Clear all data from SharedPreferences *****************//*

    public void deleteAllDataFromFile(){
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        editor.clear();
        editor.commit(); // commit changes
    }

}
*/