package edu.northeastern.team11.slurp;

// Helper class that stores a restaurant's data
public class Restaurant {

    private String name;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String imageUrl;
    private final String id;
    private Long latitude;
    private Long longitude;


    public String getId() {
        return id;
    }

    public Restaurant(String name, String street, String city, String state, String zip, String phone, String imageUrl, String id, Long latitude, Long longitude) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }
}
