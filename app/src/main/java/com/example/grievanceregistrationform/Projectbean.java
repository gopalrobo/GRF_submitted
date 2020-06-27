package com.example.grievanceregistrationform;


import java.io.Serializable;

public class Projectbean implements Serializable {
    public String id;
    public String name;
    public String attachment;
    public String detail;
    public String geotag;
    public String type;

    public String attachmentReport;
    public String detailReport;
    public String geotagReport;
    public String resolution;
    public String feedback1;
    public String feedback2;

    public Projectbean() {
    }

    public String getFeedback1() {
        return feedback1;
    }

    public void setFeedback1(String feedback1) {
        this.feedback1 = feedback1;
    }

    public String getFeedback2() {
        return feedback2;
    }

    public void setFeedback2(String feedback2) {
        this.feedback2 = feedback2;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Projectbean(String name, String attachment, String detail, String geotag) {
        this.name = name;
        this.attachment = attachment;
        this.detail = detail;
        this.geotag = geotag;
    }

    public Projectbean(String name, String attachment, String detail, String geotag, String type) {
        this.name = name;
        this.attachment = attachment;
        this.detail = detail;
        this.geotag = geotag;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getGeotag() {
        return geotag;
    }

    public void setGeotag(String geotag) {
        this.geotag = geotag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttachmentReport() {
        return attachmentReport;
    }

    public void setAttachmentReport(String attachmentReport) {
        this.attachmentReport = attachmentReport;
    }

    public String getDetailReport() {
        return detailReport;
    }

    public void setDetailReport(String detailReport) {
        this.detailReport = detailReport;
    }

    public String getGeotagReport() {
        return geotagReport;
    }

    public void setGeotagReport(String geotagReport) {
        this.geotagReport = geotagReport;
    }
}
