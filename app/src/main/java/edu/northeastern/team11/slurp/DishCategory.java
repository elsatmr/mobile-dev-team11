package edu.northeastern.team11.slurp;

public class DishCategory {
    private String categoryName;

    public DishCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public DishCategory(){}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}