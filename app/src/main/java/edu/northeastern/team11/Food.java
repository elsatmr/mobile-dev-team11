package edu.northeastern.team11;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Food {
    @SerializedName("idMeal")
    private String midMeal;

    @SerializedName("strMeal")
    private String mStrMeal;

    // url to the image
    @SerializedName("strMealThumb")
    private String mStrMealThumb;

    @SerializedName("strTags")
    private String mStrTags;

    private List<String> mSearchTerms;

    public List<String> getmSearchTerms() {
        return mSearchTerms;
    }

    public void setmSearchTerms(String mSearchTerms) {
        if (this.mSearchTerms == null) {
            List<String> search = new ArrayList<>();
            search.add(mSearchTerms);
            this.mSearchTerms = search;
        } else {
            this.mSearchTerms.add(mSearchTerms);
        }
    }

    public String getMidMeal() {
        return midMeal;
    }

    public void setMidMeal(String midMeal) {
        this.midMeal = midMeal;
    }

    public String getmStrMeal() {
        return mStrMeal;
    }

    public void setmStrMeal(String mStrMeal) {
        this.mStrMeal = mStrMeal;
    }

    public String getmStrMealThumb() {
        return mStrMealThumb;
    }

    public void setmStrMealThumb(String mStrMealThumb) {
        this.mStrMealThumb = mStrMealThumb;
    }

    public String getmStrTags() {
        return mStrTags;
    }

    public void setmStrTags(String mStrTags) {
        this.mStrTags = mStrTags;
    }
}
