package com.example.saveMoneyHelper.firebase.models;

import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.settings.UserSettings;

public class Budget {
    private long limit;
    private long entry;
    private Category category;

    public Budget(long limit, Category category, long entry) {
        this.limit = limit;
        this.category = category;
        this.entry = entry;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public Category getCategory() {
        return category;
    }

    public long getEntry() {
        return entry;
    }

    public void setEntry(long entry) {
        this.entry = entry;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
