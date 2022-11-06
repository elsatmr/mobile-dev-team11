package edu.northeastern.team11;

public class NewTransaction {
    public String dateTime ;
    public String sender;
    public String receiver;
    public int stickerId;


    public NewTransaction(String dateTime, String sender, String receiver, int stickerId) {
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.stickerId = stickerId;
    }

    public NewTransaction() {}

    public String getDateTime() {
        return dateTime;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getStickerId() {
        return stickerId;
    }

    @Override
    public String toString() {
        return "NewTransaction{" +
                "dateTime='" + this.dateTime + '\'' +
                ", sender='" + this.sender + '\'' +
                ", receiver='" + this.receiver + '\'' +
                ", stickerId=" + this.stickerId +
                '}';
    }
}

