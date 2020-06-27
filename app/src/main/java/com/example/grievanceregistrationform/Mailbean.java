package com.example.grievanceregistrationform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Mailbean implements Serializable {

    public String id;
    public String name;
    public String surname;
    public String parentname;
    public String mobile;
    public String salutation;
    public String email;
    public String country;
    public String doornumber;
    public String Province;
    public String district;
    public String commune;
    public String village;

    public String compliantrepresented;
    public String sign;
    public String position;
    public String numberset;
    public String relation;
    ArrayList<String> images;

    public Mailbean() {
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Mailbean(String name, String surname, String parentname, String mobile, String salutation, String email,
                    String country, String doornumber, String province, String district, String commune, String village, String relation) {

        this.name = name;
        this.surname = surname;
        this.salutation = salutation;
        this.parentname = parentname;
        this.mobile = mobile;
        this.email = email;
        this.country = country;
        this.doornumber = doornumber;
        Province = province;
        this.district = district;
        this.commune = commune;
        this.village = village;
        this.relation = relation;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoornumber() {
        return doornumber;
    }

    public void setDoornumber(String doornumber) {
        this.doornumber = doornumber;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompliantrepresented() {
        return compliantrepresented;
    }

    public void setCompliantrepresented(String compliantrepresented) {
        this.compliantrepresented = compliantrepresented;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNumberset() {
        return numberset;
    }

    public void setNumberset(String numberset) {
        this.numberset = numberset;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mailbean)) return false;
        Mailbean mailbean = (Mailbean) o;
        return Objects.equals(getName(), mailbean.getName()) &&
                Objects.equals(getSurname(), mailbean.getSurname()) &&
                Objects.equals(getParentname(), mailbean.getParentname()) &&
                Objects.equals(getMobile(), mailbean.getMobile()) &&
                Objects.equals(getSalutation(), mailbean.getSalutation()) &&
                Objects.equals(getEmail(), mailbean.getEmail()) &&
                Objects.equals(getCountry(), mailbean.getCountry()) &&
                Objects.equals(getDoornumber(), mailbean.getDoornumber()) &&
                Objects.equals(getProvince(), mailbean.getProvince()) &&
                Objects.equals(getDistrict(), mailbean.getDistrict()) &&
                Objects.equals(getCommune(), mailbean.getCommune()) &&
                Objects.equals(getVillage(), mailbean.getVillage()) &&
                Objects.equals(getCompliantrepresented(), mailbean.getCompliantrepresented()) &&
                Objects.equals(getSign(), mailbean.getSign()) &&
                Objects.equals(getPosition(), mailbean.getPosition()) &&
                Objects.equals(getNumberset(), mailbean.getNumberset()) &&
                Objects.equals(getRelation(), mailbean.getRelation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getParentname(), getMobile(), getSalutation(), getEmail(), getCountry(), getDoornumber(), getProvince(), getDistrict(), getCommune(), getVillage(), getCompliantrepresented(), getSign(), getPosition(), getNumberset(), getRelation());
    }
}
