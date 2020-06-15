package com.example.saveMoneyHelper.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.saveMoneyHelper.HomeActivity;
import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarIntro();
            }
        },1500);
    }

    private void mostrarIntro() {
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
            return;
        }
            Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();

    }
}
