package edu.northeastern.team11.slurp;

import android.widget.Button;

public class UsersItem {
    private String username;
    private Button addFriendButton;

    public UsersItem(String username, Button addFriendButton) {
        this.username = username;
        this.addFriendButton = addFriendButton;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Button getAddFriendButton() {
        return addFriendButton;
    }

    public void setAddFriendButton(Button addFriendButton) {
        this.addFriendButton = addFriendButton;
    }
}
