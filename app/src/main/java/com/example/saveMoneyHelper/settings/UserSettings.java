package com.example.saveMoneyHelper.settings;

public class UserSettings {

    public static final int PERIOD_MONTHLY = 0;
    public static final int PERIOD_WEEKLY = 1;


    private long budget;
    private int homeCounterPeriod = UserSettings.PERIOD_MONTHLY;

    public UserSettings() {

    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public void setHomeCounterPeriod(int homeCounterPeriod) {
        this.homeCounterPeriod = homeCounterPeriod;
    }

    public long getBudget() {
        return budget;
    }

    public int getHomeCounterPeriod() {
        return homeCounterPeriod;
    }
}
