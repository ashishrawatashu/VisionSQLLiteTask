package com.example.visionstask;

public class UserDataPojo {
    private String name;
    private String gender;
    private String imagePath;
    private int id;

    public UserDataPojo(String name, String gender, String imagePath) {
        this.name = name;
        this.gender = gender;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
