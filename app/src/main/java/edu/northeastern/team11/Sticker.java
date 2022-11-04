package edu.northeastern.team11;

public class Sticker {
    private int receivedCount;
    private int sentCount;
    private String url;

    public Sticker(int receivedCount, int sentCount, String url) {
        this.receivedCount = receivedCount;
        this.sentCount = sentCount;
        this.url = url;
    }

    public Sticker(){}

    public int getReceivedCount() {
        return receivedCount;
    }

    public int getSentCount() {
        return sentCount;
    }

    public String getUrl() {
        return url;
    }
}
