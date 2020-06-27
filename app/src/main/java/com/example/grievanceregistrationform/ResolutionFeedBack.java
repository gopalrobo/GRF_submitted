package com.example.grievanceregistrationform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.app.AndroidMultiPartEntity;
import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.GlideApp;
import com.example.grievanceregistrationform.dp.DbFarmer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.abdolahi.CircularMusicProgressBar;
import info.abdolahi.OnCircularSeekBarChangeListener;


public class ResolutionFeedBack extends AppCompatActivity implements OnResolution, OnItemClick {
    private static final int START_SIGN_CODE = 198;
    private String TAG = getClass().getSimpleName();
    Date todaysdate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    String todaydate = format.format(todaysdate);
    public static final String mypreference = "mypref";
    ProgressDialog pDialog;
    LocationTrack locationTrack;
    SharedPreferences sharedpreferences;
    DbFarmer dbFarmer;
    RegMainbean regMainbean;
    ComplaintLetterbean complaintLetterbean = null;
    TextView header_view_title;
    TextView header_view_sub_title2;
    TextView projectDetail;


    ExtendedFloatingActionButton submit;

    private RecyclerView projectlist;
    private ArrayList<Projectbean> projectbeans = new ArrayList<>();
    ProjectResolutionAdapter projectAdapter;

    boolean projectSubmitClick = false;
    private String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolution_meeting);

        dbFarmer = new DbFarmer(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        locationTrack = new LocationTrack(ResolutionFeedBack.this);
        try {
            regMainbean = new Gson().fromJson(dbFarmer
                    .getDataByfarmerId(sharedpreferences.getString(AppConfig.farmerId, "")).get(1), RegMainbean.class);

        } catch (Exception e) {

        }

        projectlist = (RecyclerView) findViewById(R.id.projectlist);
        projectAdapter = new ProjectResolutionAdapter(this, projectbeans, this, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        projectlist.setLayoutManager(addManager1);
        projectlist.setAdapter(projectAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        header_view_title = findViewById(R.id.header_view_title);
        header_view_title.setText("Compliant");
        TextView header_view_sub_title = findViewById(R.id.header_view_sub_title);
        header_view_sub_title.setText(todaydate);
        header_view_sub_title2 = findViewById(R.id.header_view_sub_title2);
        projectDetail = findViewById(R.id.projectDetail);
        header_view_sub_title2.setText("42285-013-001");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaintLetterbean.setProjectbeans(projectbeans);
                new UploadDataToServer().execute();
            }
        });


        try {
            complaintLetterbean = (ComplaintLetterbean) getIntent().getSerializableExtra("object");
            if (complaintLetterbean != null) {
                header_view_sub_title.setText(complaintLetterbean.date);
                header_view_sub_title2.setText(complaintLetterbean.compliantNo);
                projectDetail.setText(complaintLetterbean.projectname);

                projectbeans = complaintLetterbean.projectbeans;
                if (projectbeans == null || projectbeans.size() == 0) {
                    projectbeans = new ArrayList<>();
                }

                projectAdapter.notifyData(projectbeans);
                if (complaintLetterbean.getStatus().equals("Feedback done")) {
                    submit.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Log.e("xxxxxx", "Something went wrong");
        }
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


    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }


    private void hideDialog() {
        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }


    @Override
    public void onFeedOneClick(int position, String value) {
        projectbeans.get(position).setFeedback1(value);
    }

    @Override
    public void onFeedTwoClick(int position, String value) {
        projectbeans.get(position).setFeedback2(value);
        projectAdapter.notifyItemChanged(position);
    }

    private class UploadDataToServer extends AsyncTask<Integer, Integer, String> {

        public long totalSize = 0;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            showDialog();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            // updating percentage value
            // txtPercentage.setText(String.valueOf(progress[0]) + "%");
            pDialog.setTitle(String.valueOf(progress[0]) + "%");

        }

        @Override
        protected String doInBackground(Integer... params) {

            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://climatesmartcity.com/UBA/grm_feedback.php");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (complaintLetterbean != null) {
                    entity.addPart("id", new StringBody(complaintLetterbean.uniId));
                }

                entity.addPart("formId", new StringBody(complaintLetterbean.id));
                entity.addPart("meetingAssign", new StringBody(regMainbean.id));
                entity.addPart("data", new StringBody(new Gson().toJson(complaintLetterbean)));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            hideDialog();
            try {
                JSONObject localJSONObject1 = new JSONObject(result);
                String str = localJSONObject1.getString(AppConfig.convertKhmer("message", getApplicationContext()));
                if (localJSONObject1.getInt("success") == 1) {
                    Toast.makeText(getApplicationContext(), AppConfig.convertKhmer(str, getApplicationContext()), Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getApplicationContext(), AppConfig.convertKhmer(str, getApplicationContext()), Toast.LENGTH_SHORT).show();
                return;
            } catch (JSONException localJSONException) {
                localJSONException.printStackTrace();
            }

            // showing the server response in an alert dialog
            //showAlert(result);
            super.onPostExecute(result);
        }

    }


    public void addResolution(final int position) {

    }

    public void addImage(final int position) {
        imagePath = "";
        projectSubmitClick = false;
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(ResolutionFeedBack.this, R.style.RoundShapeTheme);

        LayoutInflater inflater = ResolutionFeedBack.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.addimage_final_popup, null);
        final TextInputEditText description = dialogView.findViewById(R.id.description);
        final CircularMusicProgressBar image = dialogView.findViewById(R.id.addImage);
        final TextInputEditText geotag = dialogView.findViewById(R.id.geotag);
        final TextInputLayout geotagInput = dialogView.findViewById(R.id.geotagInput);
        final TextInputLayout descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        final BetterSpinner type = dialogView.findViewById(R.id.type);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_dropdown_item_2line, R.id.item, new String[0]);
        type.setAdapter(typeAdapter);
        final TextInputLayout typelayout = dialogView.findViewById(R.id.typelayout);

        final ImageView georefresh = (ImageView) dialogView.findViewById(R.id.refresh);
        Projectbean projectbean = projectbeans.get(position);
        geotag.setText(projectbean.getGeotag());
        description.setText(projectbean.getDetail());
        GlideApp.with(ResolutionFeedBack.this).load(projectbean.getAttachment())
                .placeholder(R.drawable.addimage)
                .into(image);
        imagePath = projectbean.getAttachment();
        type.setText(projectbean.type);

        // get user update
        image.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: progress: " + progress + " / from user? " + fromUser);
            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {

            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {
                Log.d(TAG, "onLongPress");
            }

        });

        dialogBuilder.setTitle("Compliant Photos & Videos")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);

        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
    }

    public void addReportImage(final int position) {
        imagePath = "";
        projectSubmitClick = false;
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(ResolutionFeedBack.this, R.style.RoundShapeTheme);

        LayoutInflater inflater = ResolutionFeedBack.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.addimage_report_popup, null);
        final TextInputEditText description = dialogView.findViewById(R.id.description);
        final CircularMusicProgressBar image = dialogView.findViewById(R.id.addImage);
        final TextInputEditText geotag = dialogView.findViewById(R.id.geotag);
        final TextInputLayout geotagInput = dialogView.findViewById(R.id.geotagInput);
        final TextInputLayout descriptionInput = dialogView.findViewById(R.id.descriptionInput);

        description.setEnabled(false);
        final ImageView georefresh = (ImageView) dialogView.findViewById(R.id.refresh);

        Projectbean projectbean = projectbeans.get(position);
        if (projectbean.getGeotagReport() != null) {
            geotag.setText(projectbean.getGeotagReport());
        }
        if (projectbean.getDetailReport() != null) {
            description.setText(projectbean.getDetail());
        }
        if (projectbean.getAttachmentReport() != null) {
            GlideApp.with(ResolutionFeedBack.this).load(projectbean.getAttachmentReport())
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.addimage)
                    .into(image);
            imagePath = projectbean.getAttachmentReport();
        }

        // get user update
        image.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: progress: " + progress + " / from user? " + fromUser);
            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {

            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {
                Log.d(TAG, "onLongPress");
            }

        });


        dialogBuilder.setTitle("Report Photos & Videos")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);


        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
    }

    @Override
    public void itemMailaddressClick(int position) {
    }

    @Override
    public void itemProjectDetailClick(int position) {
        addImage(position);
    }

    @Override
    public void itemProjectReportDetailClick(int position) {
        addReportImage(position);
    }


    @Override
    public void itemRepresentativeClick(int position) {

    }

    @Override
    public void itemSignClick(int position) {

    }


}

