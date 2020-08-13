package com.example.saveMoneyHelper.settings;

import android.graphics.Color;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.firebase.models.Budget;

public class UserSettings {

    public static final int PERIOD_MONTHLY = 0;
    public static final int PERIOD_WEEKLY = 1;
    public static final int XP_ON = 0;



    private Budget budget;
    private int homeCounterPeriod = UserSettings.PERIOD_MONTHLY;
    private int XP = UserSettings.XP_ON;
    private int salary;

    public UserSettings() {

    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
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

    public int getXP() {
        return XP;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }

    public int getHomeCounterPeriod() {
        return homeCounterPeriod;
    }
}
