package com.example.saveMoneyHelper.budgets;

import com.example.saveMoneyHelper.categories.Category;

public class BudgetListViewModel {
    private long money;
    private Category category;
    private long limit;
    private final String categoryID;

    public BudgetListViewModel(long money, Category category, long limit, String categoryID) {
        this.money = money;
        this.category = category;
        this.limit = limit;
        this.categoryID = categoryID;
    }

    public long getMoney() {
        return money;
    }

    public Category getCategory() {
        return category;
    }

    public long getLimit() {
        return limit;
    }

    public String getCategoryID() {
        return categoryID;
    }
}
