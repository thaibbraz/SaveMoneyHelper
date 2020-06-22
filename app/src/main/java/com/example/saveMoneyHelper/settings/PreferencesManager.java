package com.example.saveMoneyHelper.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PreferencesManager {

    private static PreferencesManager instance;
    private static SharedPreferences sharedTrails;
    private static SharedPreferences.Editor trailsEditor;

    public void setUserSettings(Context context, UserSettings userSettings) {
        if (sharedTrails == null || trailsEditor == null) {
            sharedTrails = context.getApplicationContext().getSharedPreferences("user prefs", Context.MODE_PRIVATE);
            trailsEditor = sharedTrails.edit();

        }
        trailsEditor.putString("user prefs settings", new Gson().toJson(userSettings));
        trailsEditor.apply();
    }

    public UserSettings getUserSettings(Context context) {
        if (sharedTrails == null) {
            sharedTrails = context.getApplicationContext().getSharedPreferences("user prefs", Context.MODE_PRIVATE);
        }
        return new Gson().fromJson(sharedTrails.getString("user prefs settings", ""),UserSettings.class);
    }
    public static PreferencesManager getInstance(){
        if (instance != null) {
            return instance;
        } else {
            instance = new PreferencesManager();
            return instance;
        }
    }

}
