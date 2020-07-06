package com.example.saveMoneyHelper.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.settings.PreferencesManager;
import com.example.saveMoneyHelper.settings.UserSettings;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileEditActivity extends AppCompatActivity {
    private Button btnLogOut;
    private Button btnChangePeriod;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        btnLogOut = findViewById(R.id.button_logout);
        btnChangePeriod = findViewById(R.id.button_change_period);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    Intent i = new Intent(ProfileEditActivity.this, LoginActivity.class);
                    startActivity(i);
                }

            }
        });

        btnChangePeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSettings userSettings = new UserSettings();
                if (PreferencesManager.getInstance().getSavedUserSettings(v.getContext()) != null) {
                    userSettings = PreferencesManager.getInstance().getSavedUserSettings(v.getContext());
                } else {
                    if (userSettings.getHomeCounterPeriod() == UserSettings.PERIOD_MONTHLY) {
                        //  userSettings.setHomeCounterPeriod(UserSettings.PERIOD_WEEKLY);
                        Toast.makeText(getApplicationContext(), "PERIOD_MONTHLY O UserSettings é:" + userSettings.toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        //  userSettings.setHomeCounterPeriod(UserSettings.PERIOD_MONTHLY);
                        Toast.makeText(getApplicationContext(), " PERIOD_WEEKLY O UserSettings é :" + userSettings.toString(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
    }
}