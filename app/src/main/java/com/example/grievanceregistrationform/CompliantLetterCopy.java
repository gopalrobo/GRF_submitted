package com.example.grievanceregistrationform;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.app.AndroidMultiPartEntity;
import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.GlideApp;
import com.example.grievanceregistrationform.app.Imageutils;
import com.example.grievanceregistrationform.app.MyDividerItemDecoration;
import com.example.grievanceregistrationform.dp.DbFarmer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.abdolahi.CircularMusicProgressBar;
import info.abdolahi.OnCircularSeekBarChangeListener;

import static com.example.grievanceregistrationform.MainActivity.buSurveyerId;

public class CompliantLetterCopy extends AppCompatActivity implements
        Imageutils.ImageAttachmentListener, OnItemClick, OnImageSelect, ProjectsAdapter.ContactsAdapterListener {
    private static final int START_SIGN_CODE = 198;
    private String TAG = getClass().getSimpleName();
    Date todaysdate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    String todaydate = format.format(todaysdate);
    public static final String mypreference = "mypref";
    Imageutils imageutils;
    ProgressDialog pDialog;
    LocationTrack locationTrack;
    SharedPreferences sharedpreferences;
    DbFarmer dbFarmer;
    private String imagePath;
    private String signPath;
    CircularMusicProgressBar projectimage;
    ImageView signImage;
    RegMainbean regMainbean;
    ComplaintLetterbean complaintLetterbean = null;

    //project details bean
    private RecyclerView projectlist;
    private ArrayList<Projectbean> projectbeans = new ArrayList<>();
    ProjectAdapterCopy projectAdapter;
    private Map<String, String> nameProjectMap = new HashMap<>();

    private RecyclerView representativelist;
    private ArrayList<Mailbean> representativebeans = new ArrayList<>();
    RepresentativeAdapterCopy representativeAdapter;
    //mail address
    private RecyclerView mailaddresslist;
    private ArrayList<Mailbean> mailbeans = new ArrayList<>();
    MailAdapterCopy mailAdapter;
    BetterSpinner Provincetemp;
    BetterSpinner districttemp;
    BetterSpinner communetemp;
    BetterSpinner villagetemp;
    BetterSpinner Provincerep;
    BetterSpinner districtrep;
    BetterSpinner communerep;
    BetterSpinner villagerep;

    private String[] SALUTATION = new String[]{
            "Mr", "Ms", "Mrs", "Dr",
    };
    private String[] PROVINCEtemp = new String[]{
            "Loading",
    };
    private String[] DISTRICTtemp = new String[]{
            "Loading",
    };
    private String[] COMMUNEtemp = new String[]{
            "Loading",
    };
    private String[] VILLAGEtemp = new String[]{
            "Loading",
    };
    private String[] PROVINCErep = new String[]{
            "Loading",
    };
    private String[] DISTRICTrep = new String[]{
            "Loading",
    };
    private String[] COMMUNErep = new String[]{
            "Loading",
    };
    private String[] VILLAGErep = new String[]{
            "Loading",
    };

    ArrayList<String> mailImages = new ArrayList<>();
    PhotoSelectAdapterCopy photoSelectAdapter;
    String representText = "Yes";
    String confidenceText = "Yes";
    TextView confiYesText;
    TextView confiNoTxt;
    TextView represenYesText;
    TextView represenNoTxt;

    boolean projectSubmitClick = false;
    TextView header_view_title;
    TextView header_view_sub_title2;
    TextView projectDetail;


    private RecyclerView recyclerView;
    private ArrayList<Project> contactList;
    private ProjectsAdapter mAdapter;
    RoundedBottomSheetDialog mBottomSheetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaintlettercopy);

        dbFarmer = new DbFarmer(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        locationTrack = new LocationTrack(CompliantLetterCopy.this);
        try {
            regMainbean = new Gson().fromJson(dbFarmer
                    .getDataByfarmerId(sharedpreferences.getString(AppConfig.farmerId, "")).get(1), RegMainbean.class);

        } catch (Exception e) {

        }
        imageutils = new Imageutils(this);


        CardView itemsAdd = findViewById(R.id.itemsAdd);
        itemsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage(-1);
            }
        });

        CardView itemsAddMail = findViewById(R.id.itemsAddMail);
        itemsAddMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMailaddressDialog(-1);
            }
        });

        CardView itemsAddRep = findViewById(R.id.itemsAddRep);
        itemsAddRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepresentativeDialog(-1);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        header_view_title = findViewById(R.id.header_view_title);
        header_view_title.setText("Post Compliant");
        TextView header_view_sub_title = findViewById(R.id.header_view_sub_title);
        header_view_sub_title.setText(todaydate);
        header_view_sub_title2 = findViewById(R.id.header_view_sub_title2);
        projectDetail = findViewById(R.id.projectDetail);
        header_view_sub_title2.setText("42285-013-001");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout projectDetailLinear = findViewById(R.id.projectDetailLinear);
        projectDetailLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog = new RoundedBottomSheetDialog(CompliantLetterCopy.this);
                LayoutInflater inflater = CompliantLetterCopy.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.activity_main_project_list, null);

                EditText search_field = dialogView.findViewById(R.id.search_field);
                ImageView search_btn = dialogView.findViewById(R.id.search_btn);
                search_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CompliantLetterCopy.this, ScanActivity.class);
                        startActivityForResult(intent, 2);// Activity is started with requestCode 2

                    }
                });
                recyclerView = dialogView.findViewById(R.id.result_list);
                contactList = new ArrayList<>();
                contactList.add(new Project("Integrated Urban Environmental Management in the Tonle Sap Basin Project", "", "42285-013"));
                contactList.add(new Project("Integrated Urban Environmental Management in the Tonle Sap Basin", "", "42285-012"));
                contactList.add(new Project("Irrigated Agriculture Improvement Project", "", "51159-002"));
                contactList.add(new Project("Power Network Development Project", "", "50020-002"));
                contactList.add(new Project("Southeast Asia Transport Project Preparatory Facility", "", "52084-001"));
                contactList.add(new Project("Agricultural Value Chain Infrastructure Improvement Project", "", "50264-001"));
                contactList.add(new Project("Provincial Water Supply and Sanitation Project", "", "48158-002"));
                contactList.add(new Project("Strengthening Capacity for Improved Implementation of Externally Funded Projects in Cambodia", "", "50111-001"));
                contactList.add(new Project("Greater Mekong Subregion Health Security Project", "", "48118-002"));
                contactList.add(new Project("Skills for Competitiveness Project", "", "50394-001"));
                contactList.add(new Project("Provincial Water Supply and Sanitation Project", "", "48158-002"));
                mAdapter = new ProjectsAdapter(CompliantLetterCopy.this, contactList, CompliantLetterCopy.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new
                        MyDividerItemDecoration(CompliantLetterCopy.this,
                        DividerItemDecoration.VERTICAL, 50));
                recyclerView.setAdapter(mAdapter);
                search_field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().length() > 2) {
                            mAdapter.getFilter().filter(s.toString());
                        } else {
                            mAdapter.getFilter().filter("");
                        }
                    }
                });
                mBottomSheetDialog.setContentView(dialogView);
                mBottomSheetDialog.show();

            }
        });
        projectbeans = new ArrayList<>();
        projectlist = (RecyclerView) findViewById(R.id.projectlist);
        projectAdapter = new ProjectAdapterCopy(this, projectbeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        projectlist.setLayoutManager(addManager1);
        projectlist.setAdapter(projectAdapter);


        mailbeans = new ArrayList<>();
        mailaddresslist = (RecyclerView) findViewById(R.id.mailaddresslist);
        mailAdapter = new MailAdapterCopy(this, mailbeans,
                this, nameProjectMap);
        final LinearLayoutManager addManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mailaddresslist.setLayoutManager(addManager);
        mailaddresslist.setAdapter(mailAdapter);

        representativebeans=new ArrayList<>();
        representativelist = (RecyclerView) findViewById(R.id.representativelist);
        representativeAdapter = new RepresentativeAdapterCopy(this,
                representativebeans, this, nameProjectMap);
        final LinearLayoutManager addManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        representativelist.setLayoutManager(addManager2);
        representativelist.setAdapter(representativeAdapter);


        confiYesText = findViewById(R.id.confiYesText);
        confiNoTxt = findViewById(R.id.confiNoTxt);
        ExtendedFloatingActionButton submit = findViewById(R.id.submit);

        confiYesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confidenceText = "Yes";
                confiYesText.setTextColor(Color.parseColor("#ffffff"));
                confiYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                confiNoTxt.setTextColor(Color.parseColor("#000000"));
                confiNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
            }
        });
        confiNoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confidenceText = "No";
                confiNoTxt.setTextColor(Color.parseColor("#ffffff"));
                confiNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                confiYesText.setTextColor(Color.parseColor("#000000"));
                confiYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
            }
        });

        represenYesText = findViewById(R.id.represenYesText);
        represenNoTxt = findViewById(R.id.represenNoTxt);

        represenYesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                representText = "Yes";
                represenYesText.setTextColor(Color.parseColor("#ffffff"));
                represenYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                represenNoTxt.setTextColor(Color.parseColor("#000000"));
                represenNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
            }
        });
        represenNoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                representText = "No";
                represenNoTxt.setTextColor(Color.parseColor("#ffffff"));
                represenNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                represenYesText.setTextColor(Color.parseColor("#000000"));
                represenYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ComplaintLetterbean tempcomplaintLetterbean = new ComplaintLetterbean(
                        todaydate,
                        "Level 1 - Commune",
                        projectDetail.getText().toString(),
                        "",
                        representText,
                        "",
                        "",
                        confidenceText,
                        mailbeans,
                        projectbeans,
                        representativebeans,
                        new ArrayList<>());
                tempcomplaintLetterbean.setCompliantNo(header_view_sub_title2.getText().toString());
                //step 2
                String jsonVal = new Gson().toJson(tempcomplaintLetterbean);
                Log.e("xxxxxxxxxxxxx", jsonVal);
                if (complaintLetterbean == null) {
                    tempcomplaintLetterbean.setId(String.valueOf(System.currentTimeMillis()));
                } else {
                    tempcomplaintLetterbean.setId(complaintLetterbean.id);
                }
                getCreateTest(tempcomplaintLetterbean.id, sharedpreferences.getString(buSurveyerId, ""), jsonVal);
            }


        });


        try {
            complaintLetterbean = (ComplaintLetterbean) getIntent().getSerializableExtra("object");
            if (complaintLetterbean != null) {
                header_view_sub_title.setText(complaintLetterbean.date);
                //adboffice.setText(complaintLetterbean.adboffice);
                header_view_sub_title2.setText(complaintLetterbean.compliantNo);
                projectDetail.setText(complaintLetterbean.projectname);
                //suffer.setText(complaintLetterbean.suffer);
                if (complaintLetterbean.representative.equals("Yes")) {
                    represenYesText.setTextColor(Color.parseColor("#ffffff"));
                    represenYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                    represenNoTxt.setTextColor(Color.parseColor("#000000"));
                    represenNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
                } else {
                    represenNoTxt.setTextColor(Color.parseColor("#ffffff"));
                    represenNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                    represenYesText.setTextColor(Color.parseColor("#000000"));
                    represenYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
                }
                if (complaintLetterbean.confidential.equals("Yes")) {
                    confiYesText.setTextColor(Color.parseColor("#ffffff"));
                    confiYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                    confiNoTxt.setTextColor(Color.parseColor("#000000"));
                    confiNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
                } else {
                    confiNoTxt.setTextColor(Color.parseColor("#ffffff"));
                    confiNoTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                    confiYesText.setTextColor(Color.parseColor("#000000"));
                    confiYesText.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
                }
                projectbeans = complaintLetterbean.projectbeans;
                if (projectbeans == null || projectbeans.size() == 0) {
                    projectbeans = new ArrayList<>();
                }

                projectAdapter.notifyData(projectbeans);
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
                representativebeans = complaintLetterbean.representativebeans;
                if (representativebeans == null || representativebeans.size() == 0) {
                    representativebeans = new ArrayList<>();
                }
                representativeAdapter.notifyData(representativebeans, nameProjectMap);
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

    @Override
    public void itemMailaddressClick(int position) {
        showMailaddressDialog(position);
    }

    @Override
    public void itemProjectDetailClick(int position) {
        addImage(position);
    }

    @Override
    public void itemProjectReportDetailClick(int position) {

    }

    @Override
    public void itemRepresentativeClick(int position) {
        if (position == 0 && representativebeans.size() > 0) {
            Toast.makeText(getApplicationContext(), "more than one representative not allowed", Toast.LENGTH_SHORT).show();
        } else {
            showRepresentativeDialog(position);
        }
    }

    @Override
    public void itemSignClick(int position) {

    }


    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {

    }

    private void getCreateTest(final String mId, final String surveyer, final String data) {
        this.pDialog.setMessage("Creating...");
        showDialog();
        StringRequest local16 = new StringRequest(1, "http://climatesmartcity.com/UBA/create_data_timeline.php", new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
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
                if (mId != null) {
                    localHashMap.put("id", mId);
                }
                localHashMap.put("formId", mId);
                localHashMap.put("surveyer", surveyer);
                localHashMap.put("data", data);
                localHashMap.put("dbname", "grievanceform");


                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }

    public void addImage(final int position) {
        imagePath = "";
        projectSubmitClick = false;
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(CompliantLetterCopy.this, R.style.RoundShapeTheme);

        LayoutInflater inflater = CompliantLetterCopy.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.addimage_popup, null);
        final TextInputEditText description = dialogView.findViewById(R.id.description);
        final CircularMusicProgressBar image = dialogView.findViewById(R.id.addImage);
        final TextInputEditText geotag = dialogView.findViewById(R.id.geotag);
        final TextInputLayout geotagInput = dialogView.findViewById(R.id.geotagInput);
        final TextInputLayout descriptionInput = dialogView.findViewById(R.id.descriptionInput);

        final ImageView georefresh = (ImageView) dialogView.findViewById(R.id.refresh);
        if (position != -1) {
            try {
                Projectbean projectbean = projectbeans.get(position);
                geotag.setText(projectbean.getGeotag());
                description.setText(projectbean.getDetail());
                GlideApp.with(CompliantLetterCopy.this).load(projectbean.getAttachment())
                        .dontAnimate()
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.addimage)
                        .into(image);
                imagePath = projectbean.getAttachment();
            } catch (Exception e) {

            }
        }

        geotag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (projectSubmitClick && (geotag.getText().toString().length() <= 0
                            || Float.parseFloat(
                            geotag.getText().toString().split(",")[0]) <= 0 || Float.parseFloat(
                            geotag.getText().toString().split(",")[1]) <= 0)) {
                        geotagInput.setError("Enter valid Geotag");
                    } else {
                        geotagInput.setError(null);
                    }
                } catch (Exception e) {
                    geotagInput.setError("Enter valid Geotag");
                }
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (projectSubmitClick && (description.getText().toString().length() <= 0)) {
                        descriptionInput.setError("Enter valid Geotag");
                    } else {
                        descriptionInput.setError(null);
                    }
                } catch (Exception e) {
                    descriptionInput.setError("Enter valid Geotag");
                }
            }
        });

        // get user update
        image.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: progress: " + progress + " / from user? " + fromUser);
            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {
                projectimage = image;
                projectimage.setValue(0);
                imageutils.imagepicker(1);
                imageutils.setImageAttachmentListener(new Imageutils.ImageAttachmentListener() {
                    @Override
                    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
                        String path = Environment.getExternalStorageDirectory() + File.separator
                                + "ImageAttach" + File.separator;
                        if (filename != null) {
                            imageutils.createImage(file, filename, path, false);
                        }
                        String imgPath = imageutils.getPath(uri);
                        GlideApp.with(CompliantLetterCopy.this).load(imgPath)
                                .into(projectimage);
                        new UploadFileToServer().execute(imgPath);
                    }
                });
            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {
                Log.d(TAG, "onLongPress");
            }

        });

        georefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if GPS enabled
                locationTrack = new LocationTrack(CompliantLetterCopy.this);
                if (locationTrack.canGetLocation()) {

                    double latitude = locationTrack.getLatitude();
                    double longitude = locationTrack.getLongitude();

                    geotag.setText(latitude + "," + longitude);
                } else {
                    locationTrack.showSettingsAlert();
                }
            }
        });
        // check if GPS enabled
        if (locationTrack.canGetLocation()) {
            locationTrack = new LocationTrack(CompliantLetterCopy.this);
            double latitude = locationTrack.getLatitude();
            double longitude = locationTrack.getLongitude();
            geotag.setText(latitude + "," + longitude);
        } else {
            locationTrack.showSettingsAlert();
        }
        dialogBuilder.setTitle("Compliant Photos & Videos").setPositiveButton("SUBMIT", null)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);

        b.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) b).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (image.getmProgressValue() > 0 && image.getmProgressValue() < 100) {
                            Toast.makeText(getApplicationContext(), "Image Uploading, Please wait", Toast.LENGTH_SHORT).show();
                        } else if (imagePath == null || imagePath.length() <= 0) {
                            image.setValue(0);
                            image.setBorderColor(getResources().getColor(R.color.colortint));
                            Toast.makeText(getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                        } else if (geotag.getText().toString().length() <= 0
                                || Float.parseFloat(
                                geotag.getText().toString().split(",")[0]) <= 0 || Float.parseFloat(
                                geotag.getText().toString().split(",")[1]) <= 0) {
                            geotagInput.setError("Enter valid Geotag");
                            projectSubmitClick = true;
                        } else if (description.getText().toString().length() <= 0) {
                            descriptionInput.setError("Enter valid Description");
                            projectSubmitClick = true;
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("geotag", geotag.getText().toString());
                                jsonObject.put("description", description.getText().toString());
                                jsonObject.put("image", imagePath);
                                String name = String.valueOf(System.currentTimeMillis());
                                if (position == -1) {
                                    Projectbean projectbean = new Projectbean(name, imagePath,
                                            description.getText().toString(), geotag.getText().toString());
                                    projectbeans.add(projectbean);
                                    projectAdapter.notifyData(projectbeans);
                                } else {
                                    projectbeans.get(position).setGeotag(geotag.getText().toString());
                                    projectbeans.get(position).setDetail(description.getText().toString());
                                    projectbeans.get(position).setAttachment(imagePath);
                                    name = projectbeans.get(position).getName();
                                    projectAdapter.notifyData(projectbeans);
                                }
                                nameProjectMap.put(name, imagePath);

                            } catch (JSONException e) {
                                Log.d(AppConfig.convertKhmer("HistoricalTimelinePhoto", getApplicationContext()), e.toString());
                            }
                            dialogInterface.cancel();
                        }

                    }
                });
            }
        });

        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
    }

    @Override
    public void onChecked(boolean checked, int position, String name) {
        if (checked) {
            mailImages.add(name);
        } else {
            for (int i = 0; i < mailImages.size(); i++) {
                if (mailImages.get(i).equals(name)) {
                    mailImages.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onContactSelected(Project contact) {
        header_view_sub_title2.setText(contact.phone + "-001");
        projectDetail.setText(contact.phone + "-" + contact.name);
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        public long totalSize = 0;
        String filepath;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (progress[0] > 10) {
                projectimage.setValue(progress[0] - 10);
            } else {
                projectimage.setValue(progress[0]);
            }

        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];

            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

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
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                if (!jsonObject.getBoolean("error")) {
                    projectimage.setValue(100);
                    GlideApp.with(CompliantLetterCopy.this).load(filepath)
                            .into(projectimage);
                    imagePath = "http://" + AppConfig.ipcloud + "/uploads/" + imageutils.getfilename_from_path(filepath);
                } else {
                    projectimage.setValue(0);
                    imagePath = "";
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }


    private class UploadSignToServer extends AsyncTask<String, Integer, String> {
        public long totalSize = 0;
        String filepath;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (String.valueOf(progress[0])));
        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

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
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                if (!jsonObject.getBoolean("error")) {
                    GlideApp.with(CompliantLetterCopy.this).load(filepath)
                            .into(signImage);
                    signPath = "http://" + AppConfig.ipcloud + "/uploads/" + imageutils.getfilename_from_path(filepath);
                } else {
                    signPath = "";
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }


    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (START_SIGN_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra("sign");
                    int returnPosition = Integer.parseInt(
                            data.getStringExtra("position"));
                    showDialog();
                    new UploadSignToServer().execute(returnValue);
                }
                break;
            case 2:
                try {
                    String message = data.getStringExtra("MESSAGE");
                    header_view_sub_title2.setText(message.split("@")[0] + "-001");
                    projectDetail.setText(message.split("@")[0] + "-" + message.split("@")[1]);
                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog.dismiss();
                    }
                } catch (Exception e) {

                }
            default:
                imageutils.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            imageutils.request_permission_result(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void showMailaddressDialog(final int positionVal) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(
                CompliantLetterCopy.this, R.style.RoundShapeTheme);
        LayoutInflater inflater = CompliantLetterCopy.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mailaddress, null);

        final CheckBox useRegister = (CheckBox) dialogView.findViewById(R.id.useRegister);
        final EditText name = (EditText) dialogView.findViewById(R.id.name);
        final EditText doornumber = (EditText) dialogView.findViewById(R.id.doornumber);
        final BetterSpinner salutation = (BetterSpinner) dialogView.findViewById(R.id.salutation);
        if (positionVal != -1) {
            salutation.setFocusable(false);
        }
        final EditText surname = (EditText) dialogView.findViewById(R.id.surname);
        final EditText parentname = (EditText) dialogView.findViewById(R.id.parentname);
        final TextInputLayout parentnameText = (TextInputLayout) dialogView.findViewById(R.id.parentnameText);
        final RadioButton sonofRadio = dialogView.findViewById(R.id.sonofRadio);
        final RadioButton motofRadio = dialogView.findViewById(R.id.motofRadio);
        final RadioButton datofRadio = dialogView.findViewById(R.id.datofRadio);
        final ImageView signImg = dialogView.findViewById(R.id.signImg);
        final FloatingActionButton addImage = dialogView.findViewById(R.id.addImage);
        final EditText mobile = dialogView.findViewById(R.id.mobile);
        final EditText email = dialogView.findViewById(R.id.email);

        final TextInputLayout salutationlayout = dialogView.findViewById(R.id.salutationlayout);
        final TextInputLayout email_auto_complete = dialogView.findViewById(R.id.email_auto_complete);

        final TextInputLayout namelayout = dialogView.findViewById(R.id.namelayout);
        final TextInputLayout surnamelayout = dialogView.findViewById(R.id.surnamelayout);
        final TextInputLayout mobilelayout = dialogView.findViewById(R.id.mobilelayout);
        final TextInputLayout doornumberlayout = dialogView.findViewById(R.id.doornumberlayout);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signImage = signImg;
                signpicker(0);
            }
        });

        mailImages = new ArrayList<>();
        RecyclerView imagelist = (RecyclerView) dialogView.findViewById(R.id.imagelist);
        photoSelectAdapter = new PhotoSelectAdapterCopy(this,
                projectbeans, this, mailImages);
        final GridLayoutManager addManager = new GridLayoutManager(this, 3);
        imagelist.setLayoutManager(addManager);
        imagelist.setAdapter(photoSelectAdapter);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    namelayout.setError(null);
                }
            }
        });
        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    surnamelayout.setError(null);
                }
            }
        });

        parentname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    parentnameText.setError(null);
                }
            }
        });
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mobilelayout.setError(null);
                }
            }
        });

        doornumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    doornumberlayout.setError(null);
                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    email_auto_complete.setError(null);
                }
            }
        });


        sonofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                    sonofRadio.setChecked(true);
                } else {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);

                }
            }
        });
        motofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(true);
                    datofRadio.setChecked(false);
                } else {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);

                }
            }
        });
        datofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(true);
                } else {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);

                }
            }
        });

        final CountryCodePicker Phonedial = (CountryCodePicker) dialogView.findViewById(R.id.phonedial);


        Provincetemp = dialogView.findViewById(R.id.Province);
        TextInputLayout ProvinceLayout = dialogView.findViewById(R.id.ProvinceLayout);

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PROVINCEtemp);
        Provincetemp.setAdapter(provinceAdapter);
        Provincetemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (positionVal==-1 || mailbeans.size() > 0 && mailbeans.get(0).getProvince() != null
                        && mailbeans.get(0).getProvince().equals(PROVINCEtemp[position])) {
                    fetchProviceDatatemp("province", PROVINCEtemp[position], "district", position);
                    ProvinceLayout.setError(null);
                } else {
                    ProvinceLayout.setError("Select Same as 1st Complainant or Register New Compliant");
                }
            }
        });


        districttemp = dialogView.findViewById(R.id.district);
        TextInputLayout districtlayout = dialogView.findViewById(R.id.districtlayout);

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DISTRICTtemp);
        districttemp.setAdapter(districtAdapter);
        districttemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (positionVal==-1 ||mailbeans.size() > 0 && mailbeans.get(0).getDistrict() != null
                        && mailbeans.get(0).getDistrict().equals(DISTRICTtemp[position])) {
                    fetchProviceDatatemp("district", DISTRICTtemp[position], "commune", position);
                    districtlayout.setError(null);
                } else {
                    districtlayout.setError("Select Same as 1st Complainant or Register New Compliant");
                }
            }
        });

        TextInputLayout communelayout = dialogView.findViewById(R.id.communelayout);
        communetemp = dialogView.findViewById(R.id.commune);
        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COMMUNEtemp);
        communetemp.setAdapter(communeAdapter);
        communetemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (positionVal==-1 ||mailbeans.size() > 0 && mailbeans.get(0).getCommune() != null
                        && mailbeans.get(0).getCommune().equals(COMMUNEtemp[position])) {
                    fetchProviceDatatemp("commune", COMMUNEtemp[position], "village", position);
                    communelayout.setError(null);
                } else {
                    communelayout.setError("Select Same as 1st Complainant or Register New Compliant");
                }
            }
        });

        TextInputLayout villagelayout = dialogView.findViewById(R.id.villagelayout);
        villagetemp = dialogView.findViewById(R.id.village);
        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, VILLAGEtemp);
        villagetemp.setAdapter(villageAdapter);
        villagetemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==-1 ||mailbeans.size() > 0 && mailbeans.get(0).getVillage() != null
                        && mailbeans.get(0).getVillage().equals(VILLAGEtemp[position])) {
                    villagelayout.setError(null);
                } else {
                    villagelayout.setError("Select Same as 1st Complainant or Register New Compliant");
                }
            }
        });
        ArrayAdapter<String> salutationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SALUTATION);
        salutation.setAdapter(salutationAdapter);
        salutation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                salutationlayout.setError(null);
            }
        });
        fetchProviceDatatemp("province", "all", "province", positionVal);

        useRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    name.setText(regMainbean.name);
                    surname.setText(regMainbean.surname);
                    parentname.setText(regMainbean.parentname);
                    mobile.setText(regMainbean.mobile);
                    salutation.setText(regMainbean.salutation);
                    email.setText(regMainbean.email);
                    Phonedial.setCountryForNameCode(regMainbean.Phonedial);
                    doornumber.setText(regMainbean.doornumber);
                    Provincetemp.setText(regMainbean.Province);
                    districttemp.setText(regMainbean.district);
                    communetemp.setText(regMainbean.commune);
                    villagetemp.setText(regMainbean.village);

                    if (regMainbean.relation != null) {
                        if (regMainbean.relation.equals("sonof")) {
                            sonofRadio.setChecked(true);
                        } else if (regMainbean.relation.equals("datof")) {
                            datofRadio.setChecked(true);
                        } else if (regMainbean.relation.equals("wifeof")) {
                            motofRadio.setChecked(true);
                        } else {
                            sonofRadio.setChecked(true);
                        }
                    }
                } else {
                    name.setText("");
                    surname.setText("");
                    parentname.setText("");
                    mobile.setText("");
                    salutation.setText("");
                    email.setText("");
                    Phonedial.setCountryForNameCode("");
                    doornumber.setText("");
                    Provincetemp.setText("");
                    districttemp.setText("");
                    communetemp.setText("");
                    villagetemp.setText("");


                }
            }
        });

        if (positionVal != -1) {
            Mailbean bean = mailbeans.get(positionVal);
            name.setText(bean.name);
            surname.setText(bean.surname);
            parentname.setText(bean.parentname);
            mobile.setText(bean.mobile);
            salutation.setText(bean.salutation);
            email.setText(bean.email);
            signImage = signImg;
            signPath = bean.sign;
            if (signPath != null) {
                GlideApp.with(CompliantLetterCopy.this).load(signPath)
                        .into(signImage);
            }
            Phonedial.setCountryForNameCode(bean.country);
            doornumber.setText(bean.doornumber);
            Provincetemp.setText(bean.Province);
            districttemp.setText(bean.district);
            communetemp.setText(bean.commune);
            villagetemp.setText(bean.village);
            if (bean.images != null) {
                mailImages = bean.images;
            } else {
                mailImages = new ArrayList<>();
            }
            photoSelectAdapter.notifyData(projectbeans, mailImages);
            if (bean.relation != null) {
                if (bean.relation.equals("sonof")) {
                    sonofRadio.setChecked(true);
                } else if (bean.relation.equals("datof")) {
                    datofRadio.setChecked(true);
                } else if (bean.relation.equals("wifeof")) {
                    motofRadio.setChecked(true);
                } else {
                    sonofRadio.setChecked(true);
                }
            }
        }

        dialogBuilder.setTitle("Complainant Address").setPositiveButton("SUBMIT", null)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);

        b.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) b).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String relationVal = "sonof";
                        if (datofRadio.isChecked()) {
                            relationVal = "datof";
                        } else if (motofRadio.isChecked()) {
                            relationVal = "wifeof";
                        }

                        Mailbean bean = new Mailbean(
                                name.getText().toString(),
                                surname.getText().toString(),
                                parentname.getText().toString(),
                                mobile.getText().toString(),
                                salutation.getText().toString(),
                                email.getText().toString(),
                                Phonedial.getSelectedCountryCode(),
                                doornumber.getText().toString(),
                                Provincetemp.getText().toString(),
                                districttemp.getText().toString(),
                                communetemp.getText().toString(),
                                villagetemp.getText().toString(), relationVal);
                        boolean isNotMatch = true;
                        for (int k = 0; k < mailbeans.size(); k++) {
                            if (positionVal != k) {
                                Mailbean tempBean = mailbeans.get(k);
                                if (bean.equals(tempBean)) {
                                    isNotMatch = false;
                                    break;
                                }
                            }
                        }

                        if (salutation.getText().toString().length() <= 0) {
                            salutationlayout.setError("Select the Salutation");
                        } else if (name.getText().toString().length() <= 0) {
                            namelayout.setError("Enter the name");
                        } else if (surname.getText().toString().length() <= 0) {
                            surnamelayout.setError("Enter the Surname");
                        } else if (parentname.getText().toString().length() <= 0) {
                            parentnameText.setError("Enter the Parent name");
                        } else if (mobile.getText().toString().length() <= 0 && mobile.getText().toString().length() > 10) {
                            mobilelayout.setError("Enter the Mobile No");
                        } else if (doornumber.getText().toString().length() <= 0) {
                            doornumberlayout.setError("Enter the DoorNo..");
                        } else if (email.getText().toString().length() <= 0) {
                            email_auto_complete.setError("Enter the Email Id");
                        } else if (mailbeans.size() > 0 && (mailbeans.get(0).getProvince() == null ||
                                !mailbeans.get(0).getProvince().equals(Provincetemp.getText().toString()))) {
                            Toast.makeText(getApplicationContext(),
                                    "Select Same Province as 1st Complainant or Register New Compliant", Toast.LENGTH_SHORT).show();
                        } else if (mailbeans.size() > 0 && (mailbeans.get(0).getDistrict() == null ||
                                !mailbeans.get(0).getDistrict().equals(districttemp.getText().toString()))) {
                            Toast.makeText(getApplicationContext(),
                                    "Select Same District as 1st Complainant or Register New Compliant", Toast.LENGTH_SHORT).show();
                        } else if (mailbeans.size() > 0 && (mailbeans.get(0).getCommune() == null ||
                                !mailbeans.get(0).getCommune().equals(communetemp.getText().toString()))) {
                            Toast.makeText(getApplicationContext(),
                                    "Select Same Commune as 1st Complainant or Register New Compliant", Toast.LENGTH_SHORT).show();
                        } else if (mailbeans.size() > 0 && (mailbeans.get(0).getVillage() == null ||
                                !mailbeans.get(0).getVillage().equals(villagetemp.getText().toString()))) {
                            Toast.makeText(getApplicationContext(),
                                    "Select Same Village as 1st Complainant or Register New Compliant", Toast.LENGTH_SHORT).show();
                        } else if (isNotMatch) {
                            if (positionVal == -1) {
                                bean = new Mailbean(
                                        name.getText().toString(),
                                        surname.getText().toString(),
                                        parentname.getText().toString(),
                                        mobile.getText().toString(),
                                        salutation.getText().toString(),
                                        email.getText().toString(),
                                        Phonedial.getSelectedCountryCode(),
                                        doornumber.getText().toString(),
                                        Provincetemp.getText().toString(),
                                        districttemp.getText().toString(),
                                        communetemp.getText().toString(),
                                        villagetemp.getText().toString(), relationVal);
                                bean.setImages(mailImages);
                                bean.setSign(signPath);
                                mailbeans.add(bean);
                            } else {
                                mailbeans.get(positionVal).setName(name.getText().toString());
                                mailbeans.get(positionVal).setSurname(surname.getText().toString());
                                mailbeans.get(positionVal).setParentname(parentname.getText().toString());
                                mailbeans.get(positionVal).setMobile(mobile.getText().toString());
                                mailbeans.get(positionVal).setEmail(email.getText().toString());
                                mailbeans.get(positionVal).setSalutation(salutation.getText().toString());
                                mailbeans.get(positionVal).setCountry(Phonedial.getSelectedCountryCode());
                                mailbeans.get(positionVal).setDoornumber(doornumber.getText().toString());
                                mailbeans.get(positionVal).setProvince(Provincetemp.getText().toString());
                                mailbeans.get(positionVal).setDistrict(districttemp.getText().toString());
                                mailbeans.get(positionVal).setCommune(communetemp.getText().toString());
                                mailbeans.get(positionVal).setVillage(villagetemp.getText().toString());
                                mailbeans.get(positionVal).setImages(mailImages);
                                mailbeans.get(positionVal).setSign(signPath);
                                mailbeans.get(positionVal).setRelation(relationVal);
                            }

                            mailAdapter.notifyData(mailbeans, nameProjectMap);
                            b.cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter different address", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();


    }

    public void showRepresentativeDialog(final int positionVal) {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(
                CompliantLetterCopy.this, R.style.RoundShapeTheme);
        LayoutInflater inflater = CompliantLetterCopy.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mailaddress, null);

        final EditText name = (EditText) dialogView.findViewById(R.id.name);
        final EditText doornumber = (EditText) dialogView.findViewById(R.id.doornumber);
        final BetterSpinner salutation = (BetterSpinner) dialogView.findViewById(R.id.salutation);
        if (positionVal != -1) {
            salutation.setFocusable(false);
        }
        final TextInputLayout salutationlayout = dialogView.findViewById(R.id.salutationlayout);

        CheckBox useRegister = dialogView.findViewById(R.id.useRegister);

        final EditText surname = (EditText) dialogView.findViewById(R.id.surname);
        final EditText parentname = (EditText) dialogView.findViewById(R.id.parentname);
        final TextInputLayout parentnameText = (TextInputLayout) dialogView.findViewById(R.id.parentnameText);
        final RadioButton sonofRadio = dialogView.findViewById(R.id.sonofRadio);
        final RadioButton motofRadio = dialogView.findViewById(R.id.motofRadio);
        final RadioButton datofRadio = dialogView.findViewById(R.id.datofRadio);
        final ImageView signImg = dialogView.findViewById(R.id.signImg);
        final FloatingActionButton addImage = dialogView.findViewById(R.id.addImage);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signImage = signImg;
                signpicker(0);
            }
        });

        mailImages = new ArrayList<>();


        sonofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sonofRadio.setChecked(true);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                    parentnameText.setHint(AppConfig.convertKhmer("Father/Mother Name", getApplicationContext()));
                } else {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                }
            }
        });
        motofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(true);
                    datofRadio.setChecked(false);
                    parentnameText.setHint(AppConfig.convertKhmer("Son/Daughter Name", getApplicationContext()));
                } else {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                }
            }
        });
        datofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(true);
                    parentnameText.setHint(AppConfig.convertKhmer("Father/Mother Name", getApplicationContext()));
                } else {
                    sonofRadio.setChecked(false);
                    motofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                }
            }
        });
        ArrayAdapter<String> salutationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SALUTATION);
        salutation.setAdapter(salutationAdapter);
        salutation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                salutationlayout.setError(null);
            }
        });

        final CountryCodePicker Phonedial = (CountryCodePicker) dialogView.findViewById(R.id.phonedial);
        final EditText mobile = (EditText) dialogView.findViewById(R.id.mobile);
        final EditText email = (EditText) dialogView.findViewById(R.id.email);
        Provincerep = dialogView.findViewById(R.id.Province);
        TextInputLayout ProvinceLayout = dialogView.findViewById(R.id.ProvinceLayout);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PROVINCErep);
        Provincerep.setAdapter(provinceAdapter);
        Provincerep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (positionVal==-1 ||representativebeans.size() > 0 && representativebeans.get(0).getProvince() != null
                        && representativebeans.get(0).getProvince().equals(PROVINCErep[position])) {
                    fetchProviceDatarep("province", PROVINCErep[position], "district", position);
                    ProvinceLayout.setError(null);
                } else {
                    ProvinceLayout.setError("Select Same as 1st Representative or Register New Compliant");
                }
            }
        });


        districtrep = dialogView.findViewById(R.id.district);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DISTRICTrep);
        districtrep.setAdapter(districtAdapter);
        districtrep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatarep("district", DISTRICTrep[position], "commune", position);

            }
        });

        communerep = dialogView.findViewById(R.id.commune);
        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COMMUNErep);
        communerep.setAdapter(communeAdapter);
        communerep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatarep("commune", COMMUNErep[position], "village", position);

            }
        });

        villagerep = dialogView.findViewById(R.id.village);
        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, VILLAGErep);
        villagerep.setAdapter(villageAdapter);

        fetchProviceDatarep("province", "all", "province", positionVal);

        useRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    name.setText(regMainbean.name);
                    surname.setText(regMainbean.surname);
                    parentname.setText(regMainbean.parentname);
                    mobile.setText(regMainbean.mobile);
                    salutation.setText(regMainbean.salutation);
                    email.setText(regMainbean.email);
                    Phonedial.setCountryForNameCode(regMainbean.Phonedial);
                    doornumber.setText(regMainbean.doornumber);
                    Provincerep.setText(regMainbean.Province);
                    districtrep.setText(regMainbean.district);
                    communerep.setText(regMainbean.commune);
                    villagerep.setText(regMainbean.village);

                    if (regMainbean.relation != null) {
                        if (regMainbean.relation.equals("sonof")) {
                            sonofRadio.setChecked(true);
                        } else if (regMainbean.relation.equals("datof")) {
                            datofRadio.setChecked(true);
                        } else if (regMainbean.relation.equals("wifeof")) {
                            motofRadio.setChecked(true);
                        } else {
                            sonofRadio.setChecked(true);
                        }
                    }

                } else {
                    name.setText("");
                    surname.setText("");
                    parentname.setText("");
                    mobile.setText("");
                    salutation.setText("");
                    email.setText("");
                    Phonedial.setCountryForNameCode("");
                    doornumber.setText("");
                    Provincerep.setText("");
                    districtrep.setText("");
                    communerep.setText("");
                    villagerep.setText("");


                }
            }
        });

        if (positionVal != -1) {
            Mailbean bean = representativebeans.get(positionVal);
            name.setText(bean.name);
            surname.setText(bean.surname);
            parentname.setText(bean.parentname);
            mobile.setText(bean.mobile);
            salutation.setText(bean.salutation);
            email.setText(bean.email);
            Phonedial.setCountryForNameCode(bean.country);
            signImage = signImg;
            signPath = bean.sign;
            if (signPath != null) {
                GlideApp.with(CompliantLetterCopy.this).load(signPath)
                        .into(signImage);
            }
            if (bean.images != null) {
                mailImages = bean.images;
            } else {
                mailImages = new ArrayList<>();
            }

            doornumber.setText(bean.doornumber);
            Provincerep.setText(bean.Province);
            districtrep.setText(bean.district);
            communerep.setText(bean.commune);
            villagerep.setText(bean.village);

            if (bean.relation != null) {
                if (bean.relation.equals("sonof")) {
                    sonofRadio.setChecked(true);
                } else if (bean.relation.equals("datof")) {
                    datofRadio.setChecked(true);
                } else if (bean.relation.equals("wifeof")) {
                    motofRadio.setChecked(true);
                } else {
                    sonofRadio.setChecked(true);
                }
            }
        }


        dialogBuilder.setTitle("Representative Address").setPositiveButton("SUBMIT", null)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);

        b.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) b).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String relationVal = "sonof";
                        if (datofRadio.isChecked()) {
                            relationVal = "datof";
                        } else if (motofRadio.isChecked()) {
                            relationVal = "wifeof";
                        }
                        Mailbean bean = new Mailbean(
                                name.getText().toString(),
                                surname.getText().toString(),
                                parentname.getText().toString(),
                                mobile.getText().toString(),
                                salutation.getText().toString(),
                                email.getText().toString(),
                                Phonedial.getSelectedCountryCode(),
                                doornumber.getText().toString(),
                                Provincerep.getText().toString(),
                                districtrep.getText().toString(),
                                communerep.getText().toString(),
                                villagerep.getText().toString(), relationVal);
                        boolean isNotMatched = true;
                        for (int k = 0; k < representativebeans.size(); k++) {
                            if (positionVal != k) {
                                Mailbean tempBean = representativebeans.get(k);
                                if (bean.equals(tempBean)) {
                                    isNotMatched = false;
                                    break;
                                }
                            }
                        }

                        if (representativebeans.size() > 0 && (representativebeans.get(0).getProvince() == null ||
                                !representativebeans.get(0).getProvince().equals(Provincerep.getText().toString()))) {
                            Toast.makeText(getApplicationContext(),
                                    "Select Same Province as 1st Representative or Register New Compliant", Toast.LENGTH_SHORT).show();
                        } else if (isNotMatched) {
                            if (positionVal == -1) {
                                bean = new Mailbean(
                                        name.getText().toString(),
                                        surname.getText().toString(),
                                        parentname.getText().toString(),
                                        mobile.getText().toString(),
                                        salutation.getText().toString(),
                                        email.getText().toString(),
                                        Phonedial.getSelectedCountryCode(),
                                        doornumber.getText().toString(),
                                        Provincerep.getText().toString(),
                                        districtrep.getText().toString(),
                                        communerep.getText().toString(),
                                        villagerep.getText().toString(), relationVal);
                                bean.setImages(mailImages);
                                bean.setSign(signPath);
                                representativebeans.add(bean);
                            } else {
                                representativebeans.get(positionVal).setName(name.getText().toString());
                                representativebeans.get(positionVal).setSurname(surname.getText().toString());
                                representativebeans.get(positionVal).setParentname(parentname.getText().toString());
                                representativebeans.get(positionVal).setMobile(mobile.getText().toString());
                                representativebeans.get(positionVal).setEmail(email.getText().toString());
                                representativebeans.get(positionVal).setSalutation(salutation.getText().toString());
                                representativebeans.get(positionVal).setCountry(Phonedial.getSelectedCountryCode());
                                representativebeans.get(positionVal).setDoornumber(doornumber.getText().toString());
                                representativebeans.get(positionVal).setProvince(Provincerep.getText().toString());
                                representativebeans.get(positionVal).setDistrict(districtrep.getText().toString());
                                representativebeans.get(positionVal).setCommune(communerep.getText().toString());
                                representativebeans.get(positionVal).setVillage(villagerep.getText().toString());
                                representativebeans.get(positionVal).setImages(mailImages);
                                representativebeans.get(positionVal).setSign(signPath);
                                representativebeans.get(positionVal).setRelation(relationVal);

                            }
                            representativeAdapter.notifyData(representativebeans, nameProjectMap);
                            b.cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter different representative", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
    }


    private void fetchProviceDatatemp(final String key, final String val, final String val1, final int position) {
        this.pDialog.setMessage("fetching...");
        showDialog();
        StringRequest local16 = new StringRequest(1, "http://climatesmartcity.com/rua/students/ajax/readVillageDetails.php", new Response.Listener<String>() {


            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    if (localJSONObject1.getBoolean("success")) {
                        JSONArray jsonArray = localJSONObject1.getJSONArray(AppConfig.convertKhmer("data", getApplicationContext()));

                        if (val1.equals("province")) {
                            PROVINCEtemp = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PROVINCEtemp[i] = jsonArray.getString(i);
                            }
                            DISTRICTtemp = new String[1];
                            DISTRICTtemp[0] = "Select Province";
                            COMMUNEtemp = new String[1];
                            COMMUNEtemp[0] = "Select District";
                            VILLAGEtemp = new String[1];
                            VILLAGEtemp[0] = "Select Commune";
                            if (position == -1) {
                                districttemp.setText("");
                                communetemp.setText("");
                                villagetemp.setText("");
                            }
                        } else if (val1.equals("district")) {
                            DISTRICTtemp = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                DISTRICTtemp[i] = jsonArray.getString(i);
                            }
                            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                    android.R.layout.simple_dropdown_item_1line, DISTRICTtemp);
                            districttemp.setAdapter(districtAdapter);
                            COMMUNEtemp = new String[1];
                            COMMUNEtemp[0] = "Select District";
                            VILLAGEtemp = new String[1];
                            VILLAGEtemp[0] = "Select Commune";
                            districttemp.setText("");
                            communetemp.setText("");
                            villagetemp.setText("");

                        } else if (val1.equals("commune")) {
                            COMMUNEtemp = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                COMMUNEtemp[i] = jsonArray.getString(i);
                            }
                            VILLAGEtemp = new String[1];
                            VILLAGEtemp[0] = "Select Commune";
                            communetemp.setText("");
                            villagetemp.setText("");

                        } else if (val1.equals("village")) {
                            VILLAGEtemp = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                VILLAGEtemp[i] = jsonArray.getString(i);
                            }
                            villagetemp.setText("");
                        }

                        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, PROVINCEtemp);
                        Provincetemp.setAdapter(provinceAdapter);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, DISTRICTtemp);
                        districttemp.setAdapter(districtAdapter);
                        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, COMMUNEtemp);
                        communetemp.setAdapter(communeAdapter);
                        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, VILLAGEtemp);
                        villagetemp.setAdapter(villageAdapter);

                        return;
                    }
                    String str = localJSONObject1.getString("data");
                    Toast.makeText(getApplicationContext(), AppConfig.convertKhmer("Fetch Error: ", getApplicationContext()), Toast.LENGTH_SHORT).show();
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
                localHashMap.put("key", key);
                localHashMap.put("val", val);
                localHashMap.put("val1", val1);


                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }

    private void fetchProviceDatarep(final String key, final String val, final String val1,
                                     final int position) {
        this.pDialog.setMessage("fetching...");
        showDialog();
        StringRequest local16 = new StringRequest(1, "http://climatesmartcity.com/rua/students/ajax/readVillageDetails.php", new Response.Listener<String>() {


            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    if (localJSONObject1.getBoolean("success")) {
                        JSONArray jsonArray = localJSONObject1.getJSONArray(AppConfig.convertKhmer("data", getApplicationContext()));

                        if (val1.equals("province")) {
                            PROVINCErep = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PROVINCErep[i] = jsonArray.getString(i);
                            }
                            DISTRICTrep = new String[1];
                            DISTRICTrep[0] = "Select Province";
                            COMMUNErep = new String[1];
                            COMMUNErep[0] = "Select District";
                            VILLAGErep = new String[1];
                            VILLAGErep[0] = "Select Commune";
                            if (position == -1) {
                                districtrep.setText("");
                                communerep.setText("");
                                villagerep.setText("");
                            }
                        } else if (val1.equals("district")) {
                            DISTRICTrep = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                DISTRICTrep[i] = jsonArray.getString(i);
                            }
                            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                    android.R.layout.simple_dropdown_item_1line, DISTRICTrep);
                            districtrep.setAdapter(districtAdapter);
                            COMMUNErep = new String[1];
                            COMMUNErep[0] = "Select District";
                            VILLAGErep = new String[1];
                            VILLAGErep[0] = "Select Commune";
                            districtrep.setText("");
                            communerep.setText("");
                            villagerep.setText("");

                        } else if (val1.equals("commune")) {
                            COMMUNErep = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                COMMUNErep[i] = jsonArray.getString(i);
                            }
                            VILLAGErep = new String[1];
                            VILLAGErep[0] = "Select Commune";
                            communerep.setText("");
                            villagerep.setText("");

                        } else if (val1.equals("village")) {
                            VILLAGErep = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                VILLAGErep[i] = jsonArray.getString(i);
                            }
                            villagerep.setText("");
                        }

                        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, PROVINCErep);
                        Provincerep.setAdapter(provinceAdapter);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, DISTRICTrep);
                        districtrep.setAdapter(districtAdapter);
                        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, COMMUNErep);
                        communerep.setAdapter(communeAdapter);
                        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(CompliantLetterCopy.this,
                                android.R.layout.simple_dropdown_item_1line, VILLAGErep);
                        villagerep.setAdapter(villageAdapter);

                        return;
                    }
                    String str = localJSONObject1.getString("data");
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException localJSONException) {
                    localJSONException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Log.e("tag", AppConfig.convertKhmer("Fetch Error: ", getApplicationContext()) + paramVolleyError.getMessage());
                Toast.makeText(getApplicationContext(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {

                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put("key", key);
                localHashMap.put("val", val);
                localHashMap.put("val1", val1);


                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }

    public void signpicker(final int position) {

        final CharSequence[] items;


        items = new CharSequence[1];
        items[0] = "Signpad";


        android.app.AlertDialog.Builder alertdialog =
                new android.app.AlertDialog.Builder(CompliantLetterCopy.this);
        alertdialog.setTitle("Add Signature");
        alertdialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Signpad")) {
                    Intent intent = new Intent(CompliantLetterCopy.this, MainActivitySignature.class);
                    intent.putExtra("position", String.valueOf(position));
                    startActivityForResult(intent, START_SIGN_CODE);
                }
            }
        });
        alertdialog.show();
    }

}
