package com.example.saveMoneyHelper.settings;

import android.graphics.Color;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.firebase.models.Budget;

public class UserSettings {

    public static final int PERIOD_MONTHLY = 0;
    public static final int PERIOD_WEEKLY = 1;


    private Budget budget;
    private int homeCounterPeriod = UserSettings.PERIOD_MONTHLY;

    public UserSettings() {
        budget = new Budget(100,   new Category(":others", "Outros", R.drawable.category_default, Color.parseColor("#455a64")),0);
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public void setHomeCounterPeriod(int homeCounterPeriod) {
        this.homeCounterPeriod = homeCounterPeriod;
    }



    public int getHomeCounterPeriod() {
        return homeCounterPeriod;
    }
}
