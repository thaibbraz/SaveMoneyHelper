package com.example.saveMoneyHelper.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.saveMoneyHelper.R;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarIntro();
            }
        },2000);
    }

    private void mostrarIntro() {
        Intent intent = new Intent(SplashActivity.this, DadosActivity.class);
        startActivity(intent);
        finish();

    }
}
