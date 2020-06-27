package com.example.grievanceregistrationform;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.grievanceregistrationform.app.GlideApp;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ActivityMediaOnline extends AppCompatActivity {
    private String filePath = null;
    private PhotoView imgPreview;
    private ExoVideoView vidPreview;
    ProgressDialog pDialog;

    private void previewMedia(boolean paramBoolean) {

        if (paramBoolean) {
            this.imgPreview.setVisibility(View.VISIBLE);
            this.vidPreview.setVisibility(View.GONE);
            final String url = filePath.replace("small","big");

            try {
                GlideApp.with(getApplicationContext())
                        .load(new URL(url))
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.progress_animation)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                Log.e("TAG", "Load failed " + url, e);

                                // Logs the individual causes:
                                for (Throwable t : e.getRootCauses()) {
                                    Log.e("TAG", "Caused by", t);
                                }
                                // Logs the root causes
                                e.logRootCauses("TAG");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //loaded do something
                                Log.e("TAG", "ready " + url);
                                return false;
                            }
                        })
                        .into(imgPreview);
            } catch (MalformedURLException e) {
                Log.e("TAG ", e.toString() + " " + url);
                e.printStackTrace();
            }
            return;
        } else {
            this.imgPreview.setVisibility(View.GONE);
            this.vidPreview.setVisibility(View.VISIBLE);
            initVideoView(filePath.replaceAll(" ", "%20"));
            //     vidPreview.setVideoPath("http://coconutfpo.smartfpo.com/AndroidFileUpload/uploads/VID_9080593193_Toda%20(F)_7_right_face.mp4");
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(filePath));
//            startActivity(i);
//            finish();
        }

    }

    private void initVideoView(String url) {
        vidPreview.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        vidPreview.setBackListener((view, isPortrait) -> {
            if (isPortrait) {
                finish();
            }
            return false;
        });

        vidPreview.setOrientationListener(orientation -> {
//            if (orientation == SENSOR_PORTRAIT) {
//                changeToPortrait();
//            } else if (orientation == SENSOR_LANDSCAPE) {
//                changeToLandscape();
//            }
        });

//

        SimpleMediaSource mediaSource = new SimpleMediaSource(url);
        //    mediaSource.setDisplayName("Apple HLS");

        //demo only,not real multi quality, urls are the same actually
//        List<ExoMediaSource.Quality> qualities = new ArrayList<>();
//        ExoMediaSource.Quality quality;
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.YELLOW);
//        SpannableString spannableString = new SpannableString("1080p");
//        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        quality = new SimpleQuality(spannableString, mediaSource.url());
//        qualities.add(quality);
//
//        spannableString = new SpannableString("720p");
//        colorSpan = new ForegroundColorSpan(Color.LTGRAY);
//        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        quality = new SimpleQuality(spannableString, mediaSource.url());
//        qualities.add(quality);
//
//        mediaSource.setQualities(qualities);

        vidPreview.play(mediaSource, false);

    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_mediaonline);
        imgPreview = ((PhotoView) findViewById(R.id.previewimg));
        imgPreview.setOnMatrixChangeListener(new MatrixChangeListener());
        imgPreview.setOnPhotoTapListener(new PhotoTapListener());
        imgPreview.setOnSingleFlingListener(new SingleFlingListener());
        vidPreview = ((ExoVideoView) findViewById(R.id.video_view));
        Intent localIntent = getIntent();
        this.filePath = localIntent.getStringExtra("filePath");
        boolean bool = localIntent.getBooleanExtra("isImage", true);
        if (this.filePath != null) {
            previewMedia(bool);

            String[] urlArray = filePath.split("/");
            String result = urlArray[urlArray.length - 1];
            String value=changeCabs(result.replaceAll(".jpg", "").replaceAll(".mp4", "").replaceAll("face","view"));
            getSupportActionBar().setTitle(value);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            return;
        }

        Toast.makeText(getApplicationContext(), "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
    }


    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;

            /// showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }
    }

    private void showToast(CharSequence text) {
//        if (mCurrentToast != null) {
//            mCurrentToast.cancel();
//        }

        //   mCurrentToast = Toast.makeText(SimpleSampleActivity.this, text, Toast.LENGTH_SHORT);
        // mCurrentToast.show();
    }

    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {
            // mCurrMatrixTv.setText(rect.toString());
        }
    }

    private class SingleFlingListener implements OnSingleFlingListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Log.d("PhotoView", String.format(FLING_LOG_STRING, velocityX, velocityY));
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (vidPreview.getVisibility() == View.VISIBLE) {
            vidPreview.pause();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (vidPreview.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT > 23) {
                vidPreview.resume();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT <= 23)) {
            vidPreview.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (vidPreview.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT <= 23) {
                vidPreview.pause();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (vidPreview.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT > 23) {
                vidPreview.pause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vidPreview.getVisibility() == View.VISIBLE) {
            vidPreview.releasePlayer();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (vidPreview.getVisibility() == View.VISIBLE) {
                return vidPreview.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String changeCabs(String name){
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
/* Location:           D:\Gopal\downloads\Apk decompile java\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     smart.breed.contest.ActivityMedia
 * JD-Core Version:    0.6.0
 */