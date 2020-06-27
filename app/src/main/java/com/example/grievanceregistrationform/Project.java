package com.example.grievanceregistrationform;

/**
 * Created by ravi on 16/11/17.
 */

public class Project {
    String name;
    String image;
    String phone;
    String howtoApply;
    String source;

    public Project() {
    }

    public Project(String name, String image, String phone) {
        this.name = name;
        this.image = image;
        this.phone = phone;
    }

    public Project(String name, String image, String phone, String howtoApply, String source) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.howtoApply = howtoApply;
        this.source = source;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHowtoApply() {
        return howtoApply;
    }

    public void setHowtoApply(String howtoApply) {
        this.howtoApply = howtoApply;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }
}
