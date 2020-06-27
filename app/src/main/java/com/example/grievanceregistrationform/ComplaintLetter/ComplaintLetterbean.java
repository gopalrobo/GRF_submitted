package com.example.grievanceregistrationform.ComplaintLetter;


import com.example.grievanceregistrationform.Mailbean;
import com.example.grievanceregistrationform.Projectbean;
import com.example.grievanceregistrationform.Signbean;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplaintLetterbean implements Serializable {
    public String id;
    public String compliantNo;
    public String uniId;
    public String date;
    public String adboffice;
    public String projectname;
    public String suffer;
    public String status;
    public String representative;
    public String emailid;
    public String telephone1;
    public ArrayList<Mailbean> mailbeans;
    public ArrayList<Projectbean> projectbeans;
    public ArrayList<Mailbean> representativebeans;
    public ArrayList<Signbean> signbeans;

    public String facilitator;
    public String reviewpanel;
    public String confidential;
    public String materialharm;
    public String residentmission;
    public String desiredoutcome;
    public String add;
    public String name;
    public String charSign;

    public String getCharSign() {
        return charSign;
    }

    public String getCompliantNo() {
        return compliantNo;
    }

    public void setCompliantNo(String compliantNo) {
        this.compliantNo = compliantNo;
    }

    public void setCharSign(String charSign) {
        this.charSign = charSign;
    }

    public ComplaintLetterbean(String date, String adboffice, String projectname, String suffer,
                               String representative, String emailid, String telephone1, String confidential,
                               ArrayList<Mailbean> mailbeans, ArrayList<Projectbean> projectbeans, ArrayList<Mailbean> representativebeans, ArrayList<Signbean> signbeans) {
        this.date = date;
        this.adboffice = adboffice;
        this.projectname = projectname;
        this.suffer = suffer;
        this.representative = representative;
        this.emailid = emailid;
        this.telephone1 = telephone1;

        this.mailbeans = mailbeans;
        this.projectbeans = projectbeans;
        this.representativebeans = representativebeans;
        this.signbeans = signbeans;
        this.confidential=confidential;
    }

    public String getUniId() {
        return uniId;
    }

    public void setUniId(String uniId) {
        this.uniId = uniId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdboffice() {
        return adboffice;
    }

    public void setAdboffice(String adboffice) {
        this.adboffice = adboffice;
    }

    public String getSuffer() {
        return suffer;
    }

    public void setSuffer(String suffer) {
        this.suffer = suffer;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }


    public ArrayList<Mailbean> getMailbeans() {
        return mailbeans;
    }

    public void setMailbeans(ArrayList<Mailbean> mailbeans) {
        this.mailbeans = mailbeans;
    }

    public ArrayList<Projectbean> getProjectbeans() {
        return projectbeans;
    }

    public void setProjectbeans(ArrayList<Projectbean> projectbeans) {
        this.projectbeans = projectbeans;
    }

    public ArrayList<Signbean> getSignbeans() {
        return signbeans;
    }

    public void setSignbeans(ArrayList<Signbean> signbeans) {
        this.signbeans = signbeans;
    }

    public ArrayList<Mailbean> getRepresentativebeans() {
        return representativebeans;
    }

    public void setRepresentativebeans(ArrayList<Mailbean> representativebeans) {
        this.representativebeans = representativebeans;
    }

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
    }

    public String getReviewpanel() {
        return reviewpanel;
    }

    public void setReviewpanel(String reviewpanel) {
        this.reviewpanel = reviewpanel;
    }

    public String getConfidential() {
        return confidential;
    }

    public void setConfidential(String confidential) {
        this.confidential = confidential;
    }

    public String getMaterialharm() {
        return materialharm;
    }

    public void setMaterialharm(String materialharm) {
        this.materialharm = materialharm;
    }

    public String getResidentmission() {
        return residentmission;
    }

    public void setResidentmission(String residentmission) {
        this.residentmission = residentmission;
    }

    public String getDesiredoutcome() {
        return desiredoutcome;
    }

    public void setDesiredoutcome(String desiredoutcome) {
        this.desiredoutcome = desiredoutcome;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}