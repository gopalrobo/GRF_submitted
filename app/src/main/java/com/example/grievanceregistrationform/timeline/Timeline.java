package com.example.grievanceregistrationform.timeline;

public class Timeline {
    String status;
    String time;
    String approvedBy;
    String instatus;
    String driverId;
    String driverName;
    String managerC;
    String farmerC;
    String priceC;
    String description;

    public Timeline() {
    }

    public String getPriceC() {
        return priceC;
    }

    public void setPriceC(String priceC) {
        this.priceC = priceC;
    }

    public String getInstatus() {
        return instatus;
    }

    public void setInstatus(String instatus) {
        this.instatus = instatus;
    }

    public Timeline(String status, String time, String approvedBy) {
        this.status = status;
        this.time = time;
        this.approvedBy = approvedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManagerC() {
        return managerC;
    }

    public void setManagerC(String managerC) {
        this.managerC = managerC;
    }

    public String getFarmerC() {
        return farmerC;
    }

    public void setFarmerC(String farmerC) {
        this.farmerC = farmerC;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
