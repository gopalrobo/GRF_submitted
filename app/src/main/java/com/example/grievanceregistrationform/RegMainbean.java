package com.example.grievanceregistrationform;

import java.io.Serializable;

public class RegMainbean implements Serializable {
    String id;
    String name;
    String Phonedial;
    String mobile;
    String doornumber;
    String Province;
    String district;
    String commune;
    String village;
    String email;
    String password;
    String confirmpassword;
    String surname;
    String parentname;
    String salutation;
    String relation;
    String position;
    String level;
    public RegMainbean() {
    }

    public String getPosition() {
        return position;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public RegMainbean(String name, String phonedial, String mobile, String doornumber, String province, String district, String commune, String village, String email, String password, String confirmpassword, String surname, String parentname, String salutation, String relation) {
        this.name = name;
        Phonedial = phonedial;
        this.mobile = mobile;
        this.doornumber = doornumber;
        Province = province;
        this.district = district;
        this.commune = commune;
        this.village = village;
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.surname = surname;
        this.parentname = parentname;
        this.salutation = salutation;
        this.relation=relation;

    }

    public RegMainbean(String id, String name, String phonedial, String mobile, String doornumber, String province, String district, String commune, String village, String email, String password, String confirmpassword, String surname, String parentname, String salutation,String relation) {
        this.id = id;
        this.name = name;
        Phonedial = phonedial;
        this.mobile = mobile;
        this.doornumber = doornumber;
        Province = province;
        this.district = district;
        this.commune = commune;
        this.village = village;
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.surname = surname;
        this.parentname = parentname;
        this.salutation = salutation;
        this.relation=relation;
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

    public String getPhonedial() {
        return Phonedial;
    }

    public void setPhonedial(String phonedial) {
        Phonedial = phonedial;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
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

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }


}