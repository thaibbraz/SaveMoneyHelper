package com.example.saveMoneyHelper.transactions;

import com.example.saveMoneyHelper.categories.Category;

public class TransactionsListViewModel {
    private long money;
    private Category category;
    private final String dateTextView;
    private final String categoryID;
    private final String nameTextView;

    public TransactionsListViewModel(long money, Category category, String dateTextView, String nameTextView,String categoryID) {
        this.money = money;
        this.category = category;
        this.dateTextView = dateTextView;
        this.categoryID = categoryID;
        this.nameTextView = nameTextView;
    }
    public long getMoney() {
        return money;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public Category getCategory() {
        return category;
    }

    public String getDateTextView() {
        return dateTextView;
    }

    public String getNameTextView() {
        return nameTextView;
    }


}
