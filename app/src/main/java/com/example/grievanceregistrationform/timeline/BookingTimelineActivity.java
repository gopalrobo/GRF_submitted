package com.example.grievanceregistrationform.timeline;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.grievanceregistrationform.AppController;
import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.MailAdapterTimlineCopy;
import com.example.grievanceregistrationform.Mailbean;
import com.example.grievanceregistrationform.OnItemClick;
import com.example.grievanceregistrationform.Projectbean;
import com.example.grievanceregistrationform.R;
import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.BaseActivity;
import com.example.grievanceregistrationform.app.LocaleManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static com.example.grievanceregistrationform.Complaintletter.mypreference;

/**
 * Activity with RecyclerView that displays TimelineView
 */

public class BookingTimelineActivity extends BaseActivity implements TimelineListener, OnItemClick {

    private static DecimalFormat df = new DecimalFormat("0.00");
    SharedPreferences sharedpreferences;
    ProgressDialog pDialog;

    ArrayList<Timeline> listItems = new ArrayList<>();
    TimelineAdapter adapter;
    private String TAG = getClass().getSimpleName();
    RecyclerView recycler;
    String total = "";
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    NestedScrollView receiptLinear;
    Bitmap bitmap = null;


    TextView chcName,
            chcAddress,
            farmerName, gstTxt, totaltxt;
    LinearLayout paidSecondLayout, paidFirstLayout,
            line3;

    private RecyclerView mailaddresslist;
    private ArrayList<Mailbean> mailbeans = new ArrayList<>();
    MailAdapterTimlineCopy mailAdapter;
    private Map<String, String> nameProjectMap = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_timeline);

        if (!checkPermission(Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }


        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        //String title = getIntent().getStringExtra("vehicleId");
        getSupportActionBar().setTitle(getResources().getString(R.string.projectName));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.timeline));

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new TimelineAdapter(LinearLayoutManager.VERTICAL, listItems, BookingTimelineActivity.this, this);
        recycler.setAdapter(adapter);

        receiptLinear = findViewById(R.id.receiptLinear);

        FloatingTextButton receiptBtn = findViewById(R.id.receiptBtn);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        fetchStudentData();

        chcName = findViewById(R.id.chcName);
        gstTxt = findViewById(R.id.gstTxt);
        totaltxt = findViewById(R.id.totaltxt);
        chcAddress = findViewById(R.id.chcAddress);
        farmerName = findViewById(R.id.farmerName);
        paidFirstLayout = findViewById(R.id.paidFirstLayout);
        paidSecondLayout = findViewById(R.id.paidSecondLayout);
        line3 = findViewById(R.id.line3);

        mailaddresslist = (RecyclerView) findViewById(R.id.mailaddresslist);
        mailAdapter = new MailAdapterTimlineCopy(this, mailbeans,
                this, nameProjectMap);
        final LinearLayoutManager addManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mailaddresslist.setLayoutManager(addManager);
        mailaddresslist.setAdapter(mailAdapter);


    }


    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }

    private void fetchStudentData() {

        this.pDialog.setMessage("fetching...");
        showDialog();
        StringRequest local16 = new StringRequest(1, AppConfig.URL_GET_TIMELINE, new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString("message");
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    if (localJSONObject1.getBoolean("success")) {
                        JSONArray jsonArray = localJSONObject1.getJSONArray("data");
                        listItems = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Timeline item = new Timeline();
                            item.setStatus(jsonArray.getJSONObject(i).getString("status"));
                            item.setTime(jsonArray.getJSONObject(i).getString("time"));
                            item.setInstatus(jsonArray.getJSONObject(i).getString("instatus"));
                            item.setDescription(jsonArray.getJSONObject(i).getString("description"));
                            item.setStatus(jsonArray.getJSONObject(i).getString("status"));
                            listItems.add(item);
                            // adapter.notifiData(listItems);

                            if (i == jsonArray.length() - 1) {
                                Timeline timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Second Level");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Resubmitted");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Acknowledgement");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Assigning field Investigator");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Field investigation report");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Meeting invitation");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Resolution Meeting");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Feed back");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Issues resolved or escalated");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Third Level");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Resubmitted");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Acknowledgement");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Assigning field Investigator");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Field investigation report");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Meeting invitation");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Resolution Meeting");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Feed back");
                                listItems.add(timeline);
                                timeline = new Timeline();
                                timeline.setInstatus("pending");
                                timeline.setStatus("Issues resolved or escalated");
                                listItems.add(timeline);

                                adapter = new TimelineAdapter(LinearLayoutManager.VERTICAL, listItems, BookingTimelineActivity.this, BookingTimelineActivity.this);
                                recycler.setAdapter(adapter);
                            }
                        }


                        ComplaintLetterbean complaintLetterbean = new Gson().fromJson(localJSONObject1.getString("dataVal"), ComplaintLetterbean.class);
                        farmerName.setText(complaintLetterbean.compliantNo+"-"+localJSONObject1.getString("formId"));
                        ArrayList<Projectbean> projectbeans = complaintLetterbean.projectbeans;
                        if (projectbeans == null || projectbeans.size() == 0) {
                            projectbeans = new ArrayList<>();
                        }

                        nameProjectMap = new HashMap<>();
                        for (int i = 0; i < projectbeans.size(); i++) {
                            nameProjectMap.put(projectbeans.get(i).getName(),
                                    projectbeans.get(i).attachment);
                        }
                        mailbeans = complaintLetterbean.mailbeans;
                        if (mailbeans == null || mailbeans.size() == 0) {
                            mailbeans = new ArrayList<>();
                        }
                        mailAdapter.notifyData(mailbeans, nameProjectMap);

                        return;
                    }
                    return;
                } catch (JSONException localJSONException) {
                    localJSONException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Log.e("tag", "Fetch Error: " + paramVolleyError.getMessage());
                Toast.makeText(getApplicationContext(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {

                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put("trackid", getIntent().getStringExtra("id"));
                localHashMap.put("isBook", "true");
                if (getIntent().getStringExtra("isBook") != null) {
                    localHashMap.put("isBook", getIntent().getStringExtra("isBook"));
                }

                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }

    @Override
    public void onDoneClick(String fromname, String toname, String description) {
        changeData(fromname, toname, description);
    }

    @Override
    public void onCancelClick() {
        cancelAnsPicker();
    }

    @Override
    public void onCallClick(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            if (checkPermission(Manifest.permission.CALL_PHONE)) {
                String dial = "tel:" + phoneNumber;
            } else {
                Toast.makeText(BookingTimelineActivity.this, AppConfig.convertKhmer("Permission Call Phone denied", getApplicationContext()), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(BookingTimelineActivity.this, AppConfig.convertKhmer("Mobile Number not found", getApplicationContext()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentClick(String totalVal) {


    }

    @Override
    public void onCashClick(String total) {
        paidCost(total, "Cash");
    }

    private void changeData(final String fromname, final String toname, String description) {
        this.pDialog.setMessage("fetching...");
        showDialog();
        StringRequest local16 = new StringRequest(1, AppConfig.URL_CHANGE_STATUS, new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString("message");
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    if (localJSONObject1.getBoolean("success")) {
                        fetchStudentData();
                        return;
                    }
                    return;
                } catch (JSONException localJSONException) {
                    localJSONException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Toast.makeText(getApplicationContext(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {

                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put("bookid", getIntent().getStringExtra("id"));
                localHashMap.put("fromname", fromname);
                localHashMap.put("toname", toname);
                localHashMap.put("description", description);

                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }

    private void paidCost(final String total, final String type) {

    }


    public void cancelAnsPicker() {

    }


    private void cancelUpdate(final String reason) {

    }


    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, AppConfig.convertKhmer("You can call the number by clicking on the button", getApplicationContext()), Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_lan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.local_english) {
            setNewLocale(this, LocaleManager.ENGLISH);
            return true;
        }
        if (id == R.id.local_khmer) {
            setNewLocale(this, LocaleManager.KHMER);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    @Override
    public void itemMailaddressClick(int position) {

    }

    @Override
    public void itemProjectDetailClick(int position) {

    }

    @Override
    public void itemProjectReportDetailClick(int position) {

    }

    @Override
    public void itemRepresentativeClick(int position) {

    }

    @Override
    public void itemSignClick(int position) {

    }
}
