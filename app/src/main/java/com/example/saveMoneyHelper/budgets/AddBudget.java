package com.example.saveMoneyHelper.budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.saveMoneyHelper.R;

public class AddBudget extends AppCompatActivity {
    private ImageView imageClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        imageClose = findViewById(R.id.icon_close);



        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             try {
                    Intent i = new Intent(view.getContext(), Budget.class);
                    startActivity(i);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


            }
        });
    }
}