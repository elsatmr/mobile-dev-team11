package edu.northeastern.team11;

public class Sticker {
    private int receivedCount;
    private int sentCount;
    private String urlString;

    public Sticker(int receivedCount, int sentCount, String urlString) {
        this.receivedCount = receivedCount;
        this.sentCount = sentCount;
        this.urlString = urlString;
    }

    public Sticker(){}

    public int getReceivedCount() {
        return receivedCount;
    }

    public int getSentCount() {
        return sentCount;
    }

    public String getUrlString() {
        return urlString;
    }
}
