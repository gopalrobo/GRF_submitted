package com.example.grievanceregistrationform;

import com.example.grievanceregistrationform.ComplaintForm.Complaintbean;
import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;

import java.io.Serializable;

public class Mainbean implements Serializable {
    String id;

    ComplaintLetterbean complaintLetterbean;
    Complaintbean complaintbean;

    public Mainbean(ComplaintLetterbean complaintLetterbean, Complaintbean complaintbean) {
        this.complaintLetterbean = complaintLetterbean;
        this.complaintbean = complaintbean;
    }

    public Mainbean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComplaintLetterbean getComplaintLetterbean() {
        return complaintLetterbean;
    }

    public void setComplaintLetterbean(ComplaintLetterbean complaintLetterbean) {
        this.complaintLetterbean = complaintLetterbean;
    }

    public Complaintbean getComplaintbean() {
        return complaintbean;
    }

    public void setComplaintbean(Complaintbean complaintbean) {
        this.complaintbean = complaintbean;
    }
}