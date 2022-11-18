package edu.northeastern.team11.slurp;

public class DishCategoryItem {
    private String categoryName;
    private String url;

    public DishCategoryItem(String categoryName, String url) {
        this.categoryName = categoryName;
        this.url = url;
    }

    public DishCategoryItem(){}

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryUrl() {
        return url;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setCategoryUrl(String url) {
        this.url = url;
    }
}