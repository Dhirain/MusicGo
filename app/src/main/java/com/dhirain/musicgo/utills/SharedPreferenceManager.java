package com.dhirain.musicgo.utills;

import android.content.Context;
import android.content.SharedPreferences;

import com.dhirain.musicgo.MusicGOApp;
import com.dhirain.musicgo.model.MusicModel;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kavis on 6/10/16.
 */

public class SharedPreferenceManager {

    // constants.
    private final String PREFERENCES = "mobiefit_preferences";

    // members
    private static SharedPreferenceManager sSingleton;
    private SharedPreferences mSharedPreference;


    ///
    // Get instance.
    ///
    public static synchronized SharedPreferenceManager singleton() {
        if (sSingleton == null) {
            sSingleton = new SharedPreferenceManager();
        }

        return sSingleton;
    }


    ///
    // constructor
    ///
    private SharedPreferenceManager() {

            mSharedPreference = MusicGOApp.singleton().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

    }

    public void remove(String key) {
        try {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.remove(key);
            editor.apply();
        } catch (Exception ex) {
            //Silent fail
        }
    }

    //method to check key is present or not
    public boolean isKeyExists(String key) {
        try {
            return mSharedPreference.contains(key);

        } catch (Exception ex) {
            //Silent fail
        }
        return false;
    }

    ///
    // Save data in shared pref.
    ///
    public void save(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }


    ///
    // Save data in shared pref.
    ///
    public void save(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    ///
    // Save data in shared pref.
    ///
    public void save(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    ///
    // Save data in shared pref.
    ///
    public void save(String key, float value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * Save the values stored in the map in shared preferences.
     *
     * @param values values to save.
     */
    public void save(Map<String, String> values) {

        SharedPreferences.Editor editor = mSharedPreference.edit();

        Set<String> keySet = values.keySet();

        Iterator<String> iterator = keySet.iterator();

        while (iterator.hasNext()) {

            String key = iterator.next();

            String value = values.get(key);

            editor.putString(key, value);

        }

        editor.apply();

    }


    /**
     * Save the values stored in the map in shared preferences.
     *
     * @param values values to save.
     */
    public void saveList(Map<String, List<String>> values) {

        SharedPreferences.Editor editor = mSharedPreference.edit();

        Set<String> keySet = values.keySet();

        Iterator<String> iterator = keySet.iterator();

        while (iterator.hasNext()) {

            String key = iterator.next();
            Set<String> set = new HashSet<String>();
            set.addAll(values.get(key));
            //List<String> value = values.get(key);

            editor.putStringSet(key, set);

        }

        //this.sync(values);
        editor.commit();

    }

    public void saveLastMusic(String key, MusicModel musicModel){
        SharedPreferences.Editor editor = mSharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(musicModel);
        editor.putString(key, json);
        editor.apply();
    }

    public MusicModel getLastMusic(String key){
        Gson gson = new Gson();
        String json = mSharedPreference.getString(key, null);
        if(json==null)
            return null;
        else {
            return gson.fromJson(json, MusicModel.class);
        }
    }

    ///
    // Get String
    ///
    public String getString(String key) {
        return mSharedPreference.getString(key, "");
    }


    ///
    // get float.
    ///
    public float getFloat(String key) {
        return mSharedPreference.getFloat(key, 0.0f);
    }


    ///
    // Get integer.
    ///
    public int getInt(String key) {
        return mSharedPreference.getInt(key, 0);
    }

    ///
    // Get StringList
    ///
    public Set<String> getStringList(String key) {
        return mSharedPreference.getStringSet(key, null);
    }


    ///
    // Get boolean
    ///
    public boolean getBoolean(String key) {
        return mSharedPreference.getBoolean(key, false);
    }

    public boolean getBooleanWithDefaultTrue(String key) {
        return mSharedPreference.getBoolean(key, true);
    }



    public void clear() {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.clear();
        editor.commit();
    }

}
