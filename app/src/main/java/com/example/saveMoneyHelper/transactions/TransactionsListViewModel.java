package com.example.saveMoneyHelper.transactions;

import com.example.saveMoneyHelper.categories.Category;

public class TransactionsListViewModel {
    private long money;
    private Category category;
    private final String dateTextView;
    private final String moneyTextView;
    private final String categoryTextView;
    private final String nameTextView;

    public TransactionsListViewModel(long money, Category category, String dateTextView, String moneyTextView, String categoryTextView, String nameTextView) {
        this.money = money;
        this.category = category;
        this.dateTextView = dateTextView;
        this.moneyTextView = moneyTextView;
        this.categoryTextView = categoryTextView;
        this.nameTextView = nameTextView;
    }
    public long getMoney() {
        return money;
    }

    public Category getCategory() {
        return category;
    }

    public String getDateTextView() {
        return dateTextView;
    }

    public String getMoneyTextView() {
        return moneyTextView;
    }

    public String getCategoryTextView() {
        return categoryTextView;
    }

    public String getNameTextView() {
        return nameTextView;
    }


}
