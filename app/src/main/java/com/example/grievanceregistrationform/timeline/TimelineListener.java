package com.example.grievanceregistrationform.timeline;

public interface TimelineListener {

    void onDoneClick(String fromname, String toname, String description);

    void onCancelClick();

    void onCallClick(String contct);

    void onPaymentClick(String total);

    void onCashClick(String total);
}
