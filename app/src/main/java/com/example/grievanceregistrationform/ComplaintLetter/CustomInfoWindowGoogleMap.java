package com.example.grievanceregistrationform.ComplaintLetter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.grievanceregistrationform.Projectbean;
import com.example.grievanceregistrationform.R;
import com.example.grievanceregistrationform.app.AppConfig;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private RelativeLayout markerItemView;
    private Context context;

    public CustomInfoWindowGoogleMap(Context context) {
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        markerItemView = new RelativeLayout(context);
        mInflater.inflate(R.layout.survey_list_media_row, markerItemView, true);
    }

    @Override
    public View getInfoWindow(Marker marker) { // 2
        ImageView itemsImage = (ImageView) markerItemView.findViewById(R.id.itemsImage);
        TextView geodetails = (TextView) markerItemView.findViewById(R.id.geodetails);
        TextView description = (TextView) markerItemView.findViewById(R.id.description);
        LinearLayout itemsLocation = (LinearLayout) markerItemView.findViewById(R.id.itemsLocation);
        itemsLocation.setVisibility(View.GONE);
        Projectbean infoWindowData = (Projectbean) marker.getTag();
        geodetails.setText(infoWindowData.geotag);
        try {
            geodetails.setText(AppConfig.df.format(Double.parseDouble(
                    infoWindowData.geotag.split(",")[0])) + "," + AppConfig.df.format(Double.parseDouble(
                    infoWindowData.geotag.split(",")[1])));
        } catch (Exception e) {

        }
        if (infoWindowData.detail.length() > 15) {
            description.setText(infoWindowData.detail.substring(0, 14) + "...");
        } else {
            description.setText(infoWindowData.detail);
        }

        Picasso.with(context).load(infoWindowData.attachment)
               .into(itemsImage, new Callback() {
            @Override
            public void onSuccess() {
                if ( marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                    marker.showInfoWindow();
                }
                Log.d("TAG", "onSuccess");
            }

            @Override
            public void onError() {
            }
        });
        return markerItemView;  // 4
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}