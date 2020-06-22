package com.example.saveMoneyHelper.util;

import android.graphics.Color;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.firebase.models.User;
import com.example.saveMoneyHelper.firebase.models.WalletEntryCategory;
import com.example.saveMoneyHelper.categories.DefaultCategories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CategoriesHelper {
    public static Category searchCategory(String categoryName) {
        for(Category category : DefaultCategories.getDefaultCategories()) {
            if(category.getCategoryID().equals(categoryName)) return category;
        }

        return DefaultCategories.createDefaultCategoryModel("Others");
    }

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.addAll(Arrays.asList(DefaultCategories.getDefaultCategories()));
        return categories;
    }

    public static List<Category> getCustomCategories(User user) {
        ArrayList<Category> categories = new ArrayList<>();
        for(Map.Entry<String, WalletEntryCategory> entry : user.customCategories.entrySet()) {
            String categoryName = entry.getKey();
            categories.add(new Category(categoryName, entry.getValue().visibleName, R.drawable.category_default, Color.parseColor(entry.getValue().htmlColorCode)));
        }
        return categories;
    }
}