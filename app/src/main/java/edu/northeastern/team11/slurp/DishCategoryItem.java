package edu.northeastern.team11.slurp;

public class DishCategoryItem {
    private String categoryName;

    public DishCategoryItem(String categoryName) {
        this.categoryName = categoryName;
    }

    public DishCategoryItem(){}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}