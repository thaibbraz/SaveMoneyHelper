package com.example.saveMoneyHelper.budgets;

public class BudgetEntry {

    public String categoryID;
    public String name;
    public long limit;

    public BudgetEntry() {
    }

    public BudgetEntry(String categoryID, String name, long limit) {
        this.categoryID = categoryID;
        this.name = name;
        this.limit = limit;
    }
}
