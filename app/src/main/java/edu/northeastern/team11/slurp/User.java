package edu.northeastern.team11.slurp;

public class User {
    private String cityState;
    private int numTimesVoted;
    private int numFriends;
    private int slurperStatusPoints;
    private String profilePhotoLink;
    private int numPosts;

    public User(String cityState, int numTimesVoted, int numFriends, int slurperStatusPoints, String profilePhotoLink, int numPosts) {
        this.cityState = cityState;
        this.numTimesVoted = numTimesVoted;
        this.numFriends = numFriends;
        this.slurperStatusPoints = slurperStatusPoints;
        this.profilePhotoLink = profilePhotoLink;
        this.numPosts = numPosts;
    }

    public User() {

    }

    public String getCityState() {
        return cityState;
    }

    public int getNumFriends() {
        return numFriends;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public int getNumTimesVoted() {
        return numTimesVoted;
    }

    public String getProfilePhotoLink() {
        return profilePhotoLink;
    }

    public int getSlurperStatusPoints() {
        return slurperStatusPoints;
    }
}
