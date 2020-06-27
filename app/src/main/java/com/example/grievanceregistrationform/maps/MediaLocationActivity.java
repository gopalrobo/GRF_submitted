package com.example.grievanceregistrationform.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.grievanceregistrationform.ComplaintLetter.CustomInfoWindowGoogleMap;
import com.example.grievanceregistrationform.Projectbean;
import com.example.grievanceregistrationform.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;

import static com.example.grievanceregistrationform.app.PdfConfig.decodeSampledBitmapFromResource;

public class MediaLocationActivity extends BaseDemoActivity {


    @Override
    protected void startDemo() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MediaLocationActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Projectbean projectbean = (Projectbean) getIntent().getSerializableExtra("data");
                    double lat = Double.parseDouble(projectbean.geotag.split(",")[0]);
                    double lon = Double.parseDouble(projectbean.geotag.split(",")[1]);
                    MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MediaLocationActivity.this, projectbean.attachment, projectbean.detail)))
                            .position(new LatLng(lat, lon)).title(projectbean.detail);

                    CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(MediaLocationActivity.this);
                    getMap().setInfoWindowAdapter(customInfoWindow);

                    Marker m = getMap().addMarker(markerOptions);
                    m.setTag(projectbean);


                    getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                    getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12f));


                } catch (Exception e) {
                    finish();
                }
            }
        });
    }


    public Bitmap createCustomMarker(Context context, String url, String _name) throws Exception {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = new RelativeLayout(context);
        mInflater.inflate(R.layout.multi_profile_marker, view, true);
        ImageView markerImage = view.findViewById(R.id.image);


        Bitmap bitmapUrl = decodeSampledBitmapFromResource(new URL(url), 50, 50);
        markerImage.setImageBitmap(bitmapUrl);

        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        return newBitmap;
    }


}
