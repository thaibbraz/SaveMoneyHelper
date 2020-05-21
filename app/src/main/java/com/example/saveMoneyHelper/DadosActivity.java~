package com.example.saveMoneyHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;


import java.util.ArrayList;
import java.util.List;

public class DadosActivity extends AppCompatActivity {
    ViewPager viewPager;
    AdapterDados adapter;
    Animation btnAnim;
    List<Model> models;
    Button btnGetStarted;
    TextView tvSkip;
    TextInputEditText answer;
    private ViewPager screenPager;
    int position =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        models = new ArrayList<>();
        models.add(new Model("Quanto é o seu rendimento mensal?"));
        models.add(new Model("Quanto é o seu rendimento mensal?"));
        models.add(new Model("DADOS TESTE2"));


        adapter = new AdapterDados(models,this);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tvSkip = findViewById(R.id.skip);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        answer = findViewById(R.id.answer);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                btnGetStarted.setText("Next");
                if (position < (adapter.getCount() -1) ) {

                    btnGetStarted.setVisibility(View.VISIBLE);
                    tvSkip.setVisibility(View.VISIBLE);

                }
                else{
                    btnGetStarted.setText("Get started");
                    btnGetStarted.setAnimation(btnAnim);
                    btnGetStarted.setVisibility(View.VISIBLE);
                    tvSkip.setVisibility(View.INVISIBLE);


                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=viewPager.getCurrentItem();
                if (position < models.size()) {

                    position++;
                    viewPager.setCurrentItem(position);

                }

                if (position == models.size()-1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button
                    viewPager.setCurrentItem(models.size());
                    Intent i = new Intent(DadosActivity.this, LoginActivity.class);
                    startActivity(i);

                }
                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // i'm going to use shared preferences to that process
                //savePrefsData();
                //savePrefsData();


            }
        });

        // skip button click listener

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(models.size());
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened",false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened",true);
        editor.commit();
    }
}
