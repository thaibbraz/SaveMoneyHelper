package com.example.saveMoneyHelper.categories;

public class TopCategoryListViewModel{
    private long money;

    private final Category category;
    private String categoryName;

    public TopCategoryListViewModel(Category category, String categoryName, long money) {
        this.category = category;
        this.categoryName = categoryName;

        this.money = money;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public long getMoney() {
        return money;
    }

    public Category getCategory() {
        return category;
    }
}

