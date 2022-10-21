package edu.northeastern.team11;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Food {
    @SerializedName("idMeal")
    private String midMeal;

    @SerializedName("strMeal")
    private String mStrMeal;

    @SerializedName("strDrinkAlternate")
    private String mStrMealAlt;

    @SerializedName("strCategory")
    private String mStrCategory;

    @SerializedName("strArea")
    private String mStrArea;

    @SerializedName("strInstructions")
    private String mStrInstructions;

    // url to the image
    @SerializedName("strMealThumb")
    private String mStrMealThumb;

    @SerializedName("strTags")
    private String mStrTags;

    @SerializedName("strYoutube")
    private String mStrYoutube;

    @SerializedName("strIngredient1")
    private String mStrIngredient1;

    @SerializedName("strIngredient2")
    private String mStrIngredient2;

    @SerializedName("strIngredient3")
    private String mStrIngredient3;

    @SerializedName("strIngredient4")
    private String mStrIngredient4;

    @SerializedName("strIngredient5")
    private String mStrIngredient5;

    @SerializedName("strIngredient6")
    private String mStrIngredient6;

    @SerializedName("strIngredient7")
    private String mStrIngredient7;

    @SerializedName("strIngredient8")
    private String mStrIngredient8;

    @SerializedName("strIngredient9")
    private String mStrIngredient9;

    @SerializedName("strIngredient10")
    private String mStrIngredient10;

    @SerializedName("strIngredient11")
    private String mStrIngredient11;

    @SerializedName("strIngredient12")
    private String mStrIngredient12;

    @SerializedName("strIngredient13")
    private String mStrIngredient13;

    @SerializedName("strIngredient14")
    private String mStrIngredient14;

    @SerializedName("strIngredient15")
    private String mStrIngredient15;

    @SerializedName("strIngredient16")
    private String mStrIngredient16;

    @SerializedName("strIngredient17")
    private String mStrIngredient17;

    @SerializedName("strIngredient18")
    private String mStrIngredient18;

    @SerializedName("strIngredient19")
    private String mStrIngredient19;

    @SerializedName("strIngredient20")
    private String mStrIngredient20;

    @SerializedName("strMeasure1")
    private String mStrMeasure1;

    @SerializedName("strMeasure2")
    private String mStrMeasure2;

    @SerializedName("strMeasure3")
    private String mStrMeasure3;

    @SerializedName("strMeasure4")
    private String mStrMeasure4;

    @SerializedName("strMeasure5")
    private String mStrMeasure5;

    @SerializedName("strMeasure6")
    private String mStrMeasure6;

    @SerializedName("strMeasure7")
    private String mStrMeasure7;

    @SerializedName("strMeasure8")
    private String mStrMeasure8;

    @SerializedName("strMeasure9")
    private String mStrMeasure9;

    @SerializedName("strMeasure10")
    private String mStrMeasure10;

    @SerializedName("strMeasure11")
    private String mStrMeasure11;

    @SerializedName("strMeasure12")
    private String mStrMeasure12;

    @SerializedName("strMeasure13")
    private String mStrMeasure13;

    @SerializedName("strMeasure14")
    private String mStrMeasure14;

    @SerializedName("strMeasure15")
    private String mStrMeasure15;

    @SerializedName("strMeasure16")
    private String mStrMeasure16;

    @SerializedName("strMeasure17")
    private String mStrMeasure17;

    @SerializedName("strMeasure18")
    private String mStrMeasure18;

    @SerializedName("strMeasure19")
    private String mStrMeasure19;

    @SerializedName("strMeasure20")
    private String mStrMeasure20;

    @SerializedName("strSource")
    private String mStrSource;

    @SerializedName("strImageSource")
    private String mStrImageSource;

    @SerializedName("strCreativeCommonsConfirmed")
    private String mStrCreativeCommonsConfirmed;

    @SerializedName("dateModified")
    private String mDateModified;

    public String getMidMeal() {
        return midMeal;
    }

    public String getmStrMeal() {
        return mStrMeal;
    }

    public String getmStrMealAlt() {
        return mStrMealAlt;
    }

    public String getmStrCategory() {
        return mStrCategory;
    }

    public String getmStrArea() {
        return mStrArea;
    }

    public String getmStrInstructions() {
        return mStrInstructions;
    }

    public String getmStrMealThumb() {
        return mStrMealThumb;
    }

    public String getmStrTags() {
        return mStrTags;
    }

    public String getmStrYoutube() {
        return mStrYoutube;
    }

    public String getmStrIngredient1() {
        return mStrIngredient1;
    }

    public String getmStrIngredient2() {
        return mStrIngredient2;
    }

    public String getmStrIngredient3() {
        return mStrIngredient3;
    }

    public String getmStrIngredient4() {
        return mStrIngredient4;
    }

    public String getmStrIngredient5() {
        return mStrIngredient5;
    }

    public String getmStrIngredient6() {
        return mStrIngredient6;
    }

    public String getmStrIngredient7() {
        return mStrIngredient7;
    }

    public String getmStrIngredient8() {
        return mStrIngredient8;
    }

    public String getmStrIngredient9() {
        return mStrIngredient9;
    }

    public String getmStrIngredient10() {
        return mStrIngredient10;
    }

    public String getmStrIngredient11() {
        return mStrIngredient11;
    }

    public String getmStrIngredient12() {
        return mStrIngredient12;
    }

    public String getmStrIngredient13() {
        return mStrIngredient13;
    }

    public String getmStrIngredient14() {
        return mStrIngredient14;
    }

    public String getmStrIngredient15() {
        return mStrIngredient15;
    }

    public String getmStrIngredient16() {
        return mStrIngredient16;
    }

    public String getmStrIngredient17() {
        return mStrIngredient17;
    }

    public String getmStrIngredient18() {
        return mStrIngredient18;
    }

    public String getmStrIngredient19() {
        return mStrIngredient19;
    }

    public String getmStrIngredient20() {
        return mStrIngredient20;
    }

    public String getmStrMeasure1() {
        return mStrMeasure1;
    }

    public String getmStrMeasure2() {
        return mStrMeasure2;
    }

    public String getmStrMeasure3() {
        return mStrMeasure3;
    }

    public String getmStrMeasure4() {
        return mStrMeasure4;
    }

    public String getmStrMeasure5() {
        return mStrMeasure5;
    }

    public String getmStrMeasure6() {
        return mStrMeasure6;
    }

    public String getmStrMeasure7() {
        return mStrMeasure7;
    }

    public String getmStrMeasure8() {
        return mStrMeasure8;
    }

    public String getmStrMeasure9() {
        return mStrMeasure9;
    }

    public String getmStrMeasure10() {
        return mStrMeasure10;
    }

    public String getmStrMeasure11() {
        return mStrMeasure11;
    }

    public String getmStrMeasure12() {
        return mStrMeasure12;
    }

    public String getmStrMeasure13() {
        return mStrMeasure13;
    }

    public String getmStrMeasure14() {
        return mStrMeasure14;
    }

    public String getmStrMeasure15() {
        return mStrMeasure15;
    }

    public String getmStrMeasure16() {
        return mStrMeasure16;
    }

    public String getmStrMeasure17() {
        return mStrMeasure17;
    }

    public String getmStrMeasure18() {
        return mStrMeasure18;
    }

    public String getmStrMeasure19() {
        return mStrMeasure19;
    }

    public String getmStrMeasure20() {
        return mStrMeasure20;
    }

    public String getmStrSource() {
        return mStrSource;
    }

    public String getmStrImageSource() {
        return mStrImageSource;
    }

    public String getmStrCreativeCommonsConfirmed() {
        return mStrCreativeCommonsConfirmed;
    }

    public String getmDateModified() {
        return mDateModified;
    }
}
