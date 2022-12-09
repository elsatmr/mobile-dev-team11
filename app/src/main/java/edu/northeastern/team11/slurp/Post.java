package edu.northeastern.team11.slurp;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private String imageUrl;
    private String dishName;
    private String restaurantId;
    private Float slurpScore;
    private Integer timestamp;
    private String userName;

    public Post(String imageUrl, String dishName, String restaurantId, Float slurpScore, Integer timestamp, String userName) {
        this.imageUrl = imageUrl;
        this.dishName = dishName;
        this.restaurantId = restaurantId;
        this.slurpScore = slurpScore;
        this.timestamp = timestamp;
        this.userName = userName;
    }

    protected Post(Parcel in) {
        imageUrl = in.readString();
        dishName = in.readString();
        if (in.readByte() == 0) {
            restaurantId = null;
        } else {
            restaurantId = in.readString();
        }
        if (in.readByte() == 0) {
            slurpScore = null;
        } else {
            slurpScore = in.readFloat();
        }
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readInt();
        }
        userName = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(dishName);
        if (restaurantId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeString(restaurantId);
        }
        if (slurpScore == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(slurpScore);
        }
        if (timestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(timestamp);
        }
        parcel.writeString(userName);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Float getSlurpScore() {
        return slurpScore;
    }

    public void setSlurpScore(Float slurpScore) {
        this.slurpScore = slurpScore;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
