package edu.northeastern.team11.slurp;

public class User {
    private String cityState;
    private String profilePhotoLink;

    public User(String cityState, String profilePhotoLink) {
        this.cityState = cityState;
        this.profilePhotoLink = profilePhotoLink;
    }

    public User() {

    }

    public String getCityState() {
        return cityState;
    }

    public String getProfilePhotoLink() {
        return profilePhotoLink;
    }

}
