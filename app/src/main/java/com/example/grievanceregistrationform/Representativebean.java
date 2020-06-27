package com.example.grievanceregistrationform;

import java.io.Serializable;
import java.util.Objects;

public class Representativebean implements Serializable {

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
    public String numberset;
    public String relation;


    public Representativebean( String name, String surname, String parentname, String mobile,String salutation, String email, String country, String doornumber, String province, String district, String commune, String village,String relation) {

        this.name = name;
        this.surname = surname;
        this.salutation=salutation;
        this.parentname = parentname;
        this.mobile = mobile;
        this.email = email;
        this.country = country;
        this.doornumber = doornumber;
        Province = province;
        this.district = district;
        this.commune = commune;
        this.village = village;
        this.relation=relation;
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
        if (!(o instanceof Representativebean)) return false;
        Representativebean that = (Representativebean) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getSurname(), that.getSurname()) &&
                Objects.equals(getParentname(), that.getParentname()) &&
                Objects.equals(getMobile(), that.getMobile()) &&
                Objects.equals(getSalutation(), that.getSalutation()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getDoornumber(), that.getDoornumber()) &&
                Objects.equals(getProvince(), that.getProvince()) &&
                Objects.equals(getDistrict(), that.getDistrict()) &&
                Objects.equals(getCommune(), that.getCommune()) &&
                Objects.equals(getVillage(), that.getVillage()) &&
                Objects.equals(getNumberset(), that.getNumberset()) &&
                Objects.equals(getRelation(), that.getRelation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getParentname(), getMobile(), getSalutation(), getEmail(), getCountry(), getDoornumber(), getProvince(), getDistrict(), getCommune(), getVillage(), getNumberset(), getRelation());
    }
}
