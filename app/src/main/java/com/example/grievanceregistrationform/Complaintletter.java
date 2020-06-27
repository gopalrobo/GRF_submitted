package com.example.grievanceregistrationform;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.app.AndroidMultiPartEntity;
import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.BaseActivity;
import com.example.grievanceregistrationform.app.CompliantPdfConfig;
import com.example.grievanceregistrationform.app.GlideApp;
import com.example.grievanceregistrationform.app.Imageutils;
import com.example.grievanceregistrationform.app.LocaleManager;
import com.example.grievanceregistrationform.dp.DbFarmer;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static com.example.grievanceregistrationform.MainActivity.buSurveyerId;

public class Complaintletter extends BaseActivity implements Imageutils.ImageAttachmentListener, OnItemClick {

    public static final String mypreference = "mypref";
    public static final String dates = "dates";
    private static final int FINE_LOCATION_CODE = 199;
    private static final int START_SIGN_CODE = 198;
    private static final int START_GALLERY_CODE = 197;
    LocationTrack locationTrack;
    Imageutils imageutils;
    ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    MailAdapter webAdapter;
    MailAdapter mailAdapter;
    ProjectAdapter projectAdapter;
    RepresentativeAdapter representativeAdapter;
    SignAdapter signAdapter;
    EditText date;
    EditText projectname;
    EditText suffer;
    EditText emailid;
    EditText telephone1;
    MaterialBetterSpinner representative;
    MaterialBetterSpinner surname;
    MaterialBetterSpinner Provincerep;
    MaterialBetterSpinner districtrep;
    MaterialBetterSpinner communerep;
    MaterialBetterSpinner villagerep;
    MaterialBetterSpinner Provincetemp;
    MaterialBetterSpinner districttemp;
    MaterialBetterSpinner communetemp;
    MaterialBetterSpinner villagetemp;

    ImageView projectimage;

    MaterialBetterSpinner adboffice;

    CardView cardprofile;
    TextView submit;
    ImageView url;
    EditText detaildialog;
    ComplaintLetterbean complaintLetterbean = null;
    FloatingActionButton pdfView;
    String projectImage = "";
    DbFarmer dbFarmer;
    LabeledSwitch confidential;
    RegMainbean regMainbean;
    Date todaysdate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    String todaydate = format.format(todaysdate);
    private String imagePath;
    private RecyclerView webviewlist;
    private ArrayList<Mailbean> webbean = new ArrayList<>();
    private RecyclerView mailaddresslist;
    private ArrayList<Mailbean> mailbeans = new ArrayList<>();
    private RecyclerView projectlist;
    private ArrayList<Projectbean> projectbeans = new ArrayList<>();
    private RecyclerView representativelist;
    private ArrayList<Representativebean> representativebeans = new ArrayList<>();
    private RecyclerView signlist;
    private ArrayList<Signbean> signbeans = new ArrayList<>();
    private String[] Representative = new String[]{
            "Yes", "No"
    };
    private String[] Surnametemp = new String[]{
            "Mr", "Mrs", "Ms"
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
    private String[] Toaddress = new String[]{
            "Level 1 - Commune", "Level 2 - Province", "Level 3 - National"
    };
    private String TAG = getClass().getSimpleName();

    private int FINE_LOCATION_REQUEST = 888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaintletter);
        dbFarmer = new DbFarmer(this);

        if (checkPermissions()) {
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        locationTrack = new LocationTrack(Complaintletter.this);


        try {
            regMainbean = new Gson().fromJson(dbFarmer
                    .getDataByfarmerId(sharedpreferences.getString(AppConfig.farmerId, "")).get(1), RegMainbean.class);

        } catch (Exception e) {

        }
        getSupportActionBar().setTitle("GRF");
        getSupportActionBar().setSubtitle(getResources().getString(R.string.compReg));

        imageutils = new Imageutils(this);

        url = (ImageView) findViewById(R.id.url);

        date = (EditText) findViewById(R.id.date);
        projectname = (EditText) findViewById(R.id.projectname);
        suffer = (EditText) findViewById(R.id.suffer);
        emailid = (EditText) findViewById(R.id.emailid);
        telephone1 = (EditText) findViewById(R.id.telephone1);
        final FloatingTextButton representativebtn = (FloatingTextButton) findViewById(R.id.representativebtn);
        date.setText(todaydate);

        confidential = (LabeledSwitch) findViewById(R.id.confidential);

        submit = (TextView) findViewById(R.id.submit);

        adboffice = (MaterialBetterSpinner) findViewById(R.id.adboffice);
        ArrayAdapter<String> toaddressAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Toaddress);
        adboffice.setAdapter(toaddressAdapter);

        representative = (MaterialBetterSpinner) findViewById(R.id.representative);
        ArrayAdapter<String> representativespinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Representative);
        representative.setAdapter(representativespinAdapter);
        representative.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (representative.getText().toString().equals("Yes")) {
                    representativebtn.setVisibility(View.VISIBLE);
                } else {
                    representativebtn.setVisibility(View.GONE);
                }
            }
        });
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWebviewDialog();
            }
        });


        mailaddresslist = (RecyclerView) findViewById(R.id.mailaddresslist);
        mailAdapter = new MailAdapter(this, mailbeans, this);
        final LinearLayoutManager addManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mailaddresslist.setLayoutManager(addManager);
        mailaddresslist.setAdapter(mailAdapter);
        FloatingTextButton mailaddress = (FloatingTextButton) findViewById(R.id.mailaddress);
        mailaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMailaddressDialog(-1);
            }
        });


        projectlist = (RecyclerView) findViewById(R.id.projectlist);
        projectAdapter = new ProjectAdapter(this, projectbeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        projectlist.setLayoutManager(addManager1);
        projectlist.setAdapter(projectAdapter);
        FloatingTextButton project = (FloatingTextButton) findViewById(R.id.project);
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage(-1);

            }
        });

        representativelist = (RecyclerView) findViewById(R.id.representativelist);
        representativeAdapter = new RepresentativeAdapter(this, representativebeans, this);
        final LinearLayoutManager addManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        representativelist.setLayoutManager(addManager2);
        representativelist.setAdapter(representativeAdapter);
        representativebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepresentativeDialog(-1);
            }
        });

        signlist = (RecyclerView) findViewById(R.id.signlist);
        signAdapter = new SignAdapter(this, signbeans, this);
        final LinearLayoutManager addManagers = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        signlist.setLayoutManager(addManagers);
        signlist.setAdapter(signAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ComplaintLetterbean tempcomplaintLetterbean = new ComplaintLetterbean(
                        date.getText().toString(),
                        adboffice.getText().toString(),
                        projectname.getText().toString(),
                        suffer.getText().toString(),
                        representative.getText().toString(),
                        emailid.getText().toString(),
                        telephone1.getText().toString(),
                        confidential.isOn() ? "Yes" : "No",
                        mailbeans,
                        projectbeans,
                        new ArrayList<>(),
                        signbeans);

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
                date.setFocusable(false);
                date.setText(todaydate);
                adboffice.setText(complaintLetterbean.adboffice);
                adboffice.setFocusable(false);
                projectname.setText(complaintLetterbean.projectname);
                suffer.setText(complaintLetterbean.suffer);
                representative.setText(complaintLetterbean.representative);
                if (complaintLetterbean.confidential.equals("Yes")) {
                    confidential.setOn(true);
                } else {
                    confidential.setOn(false);
                }
                emailid.setText(complaintLetterbean.emailid);
                telephone1.setText(complaintLetterbean.telephone1);
                mailbeans = complaintLetterbean.mailbeans;
                mailAdapter.notifyData(mailbeans);
                projectbeans = complaintLetterbean.projectbeans;
                projectAdapter.notifyData(projectbeans);
//                representativebeans = complaintLetterbean.representativebeans;
//                representativeAdapter.notifyData(representativebeans);
                signbeans = complaintLetterbean.signbeans;
                signAdapter.notifyData(complaintLetterbean.signbeans);
                if (complaintLetterbean.adboffice.equals("Level 1 - Commune")) {
                    Toaddress = new String[]{
                            "Level 2 - Province", "Level 3 - National"
                    };
                    toaddressAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_dropdown_item_1line, Toaddress);
                    adboffice.setAdapter(toaddressAdapter);
                } else if (complaintLetterbean.adboffice.equals("Level 2 - Province")) {
                    Toaddress = new String[]{
                            "Level 3 - National"
                    };
                    toaddressAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_dropdown_item_1line, Toaddress);
                    adboffice.setAdapter(toaddressAdapter);
                } else {
                    Toaddress = new String[]{
                    };
                    toaddressAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_dropdown_item_1line, Toaddress);
                    adboffice.setAdapter(toaddressAdapter);
                }
            }
        } catch (Exception e) {
            Log.e("xxxxxx", "Something went wrong");
        }

    }

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

//    public void showProjectDialog(final int position) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Complaintletter.this);
//        LayoutInflater inflater = Complaintletter.this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.projectdetail, null);
//
//        dialogBuilder.setView(dialogView);
//        final AlertDialog b = dialogBuilder.create();
//
//        detaildialog = (EditText) dialogView.findViewById(R.id.detail);
//        final TextView cancelBtn = (TextView) dialogView.findViewById(R.id.cancelBtn);
//        final TextView submitBtn = (TextView) dialogView.findViewById(R.id.submitBtn);
//        final ImageView signImg = (ImageView) dialogView.findViewById(R.id.signImg);
//        FloatingActionButton addImage = (FloatingActionButton) dialogView.findViewById(R.id.addImage);
//        addImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                imageutils.imagepicker(1);
//                imageutils.setImageAttachmentListener(new Imageutils.ImageAttachmentListener() {
//                    @Override
//                    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
//                        String path = Environment.getExternalStorageDirectory() + File.separator
//                                + "ImageAttach" + File.separator;
//                        if (filename != null) {
//                            imageutils.createImage(file, filename, path, false);
//                        }
//                        GlideApp.with(Complaintletter.this).load(imageutils.getPath(uri))
//                                .dontAnimate()
//                                .thumbnail(0.5f)
//                                .placeholder(R.drawable.file)
//                                .into(signImg);
//                        projectImage = imageutils.getPath(uri);
//                    }
//                });
//            }
//        });
//
//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position == -1) {
//                    Projectbean bean = new Projectbean(projectImage,
//                            detaildialog.getText().toString());
//                    projectbeans.add(bean);
//                } else {
//                    projectbeans.get(position).setAttachment(projectImage);
//                    projectbeans.get(position).setDetail(detaildialog.getText().toString());
//
//                }
//                projectAdapter.notifyData(projectbeans);
//                b.cancel();
//
//            }
//        });
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                b.dismiss();
//            }
//        });
//        if (position != -1) {
//            submit.setText("Update");
//            Projectbean bean = projectbeans.get(position);
//            detaildialog.setText(bean.detail);
//            GlideApp.with(Complaintletter.this).load(bean.attachment)
//                    .dontAnimate()
//                    .thumbnail(0.5f)
//                    .placeholder(R.drawable.file)
//                    .into(signImg);
//        }
//
//        b.show();
//
//
//    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {

    }
    public void addImage(final int position) {
        imagePath = "";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Complaintletter.this);
        LayoutInflater inflater = Complaintletter.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.addimage_popup, null);
        //final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        final CustomFontEditText description = (CustomFontEditText) dialogView.findViewById(R.id.description);
        //final LinearLayout imagelin = (LinearLayout) dialogView.findViewById(R.id.imglin);
        final ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        final CustomFontEditText geotag = (CustomFontEditText) dialogView.findViewById(R.id.geotag);
        final ImageView georefresh = (ImageView) dialogView.findViewById(R.id.refresh);



         TextView submit = null;
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
//        itemclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                b.cancel();
//            }
//        });
        //final TextView itemtittle = (TextView) dialogView.findViewById(R.id.itemtittle);
        //     itemtittle.setText(tittle);
        if (position != -1) {
            Projectbean projectbean = projectbeans.get(position);

            geotag.setText(projectbean.getGeotag());
            description.setText(projectbean.getDetail());
            GlideApp.with(Complaintletter.this).load(projectbean.getAttachment())
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.file)
                    .into(image);
            imagePath = projectbean.getAttachment();


        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description.getText().toString().length() <= 0
                        || geotag.getText().toString().length() <= 0
                        || description.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), AppConfig.convertKhmer("Enter all fileds", getApplicationContext()), Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("geotag", geotag.getText().toString());
                        jsonObject.put("description", description.getText().toString());
                        jsonObject.put("image", imagePath);

                        if (position == -1) {
                            Projectbean projectbean = new Projectbean("", imagePath,
                                    description.getText().toString(), geotag.getText().toString());
                            projectbeans.add(projectbean);
                            projectAdapter.notifyData(projectbeans);
                        } else {
                            projectbeans.get(position).setGeotag(geotag.getText().toString());
                            projectbeans.get(position).setDetail(description.getText().toString());
                            projectbeans.get(position).setAttachment(imagePath);
                            projectAdapter.notifyData(projectbeans);
                        }
                    } catch (JSONException e) {
                        Log.d(AppConfig.convertKhmer("HistoricalTimelinePhoto", getApplicationContext()), e.toString());
                    }
                }
                b.cancel();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectimage = image;
                imageutils.imagepicker(1);
                imageutils.setImageAttachmentListener(new Imageutils.ImageAttachmentListener() {
                    @Override
                    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
                        String path = Environment.getExternalStorageDirectory() + File.separator
                                + "ImageAttach" + File.separator;
                        if (filename != null) {
                            imageutils.createImage(file, filename, path, false);
                        }
                        showDialog();
                        new UploadFileToServer().execute(imageutils.getPath(uri));
                    }
                });
            }
        });


        georefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if GPS enabled
                locationTrack = new LocationTrack(Complaintletter.this);
                if (locationTrack.canGetLocation()) {

                    double latitude = locationTrack.getLatitude();
                    double longitude = locationTrack.getLongitude();

                    geotag.setText(latitude + "," + longitude);
                    // \n is for new line
                    //       Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    locationTrack.showSettingsAlert();
                }
            }
        });
        // check if GPS enabled
        if (locationTrack.canGetLocation()) {
            locationTrack = new LocationTrack(Complaintletter.this);
            double latitude = locationTrack.getLatitude();
            double longitude = locationTrack.getLongitude();

            geotag.setText(latitude + "," + longitude);
            // \n is for new line
            //       Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            locationTrack.showSettingsAlert();
        }
        b.show();
    }

    public void showMailaddressDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Complaintletter.this);
        LayoutInflater inflater = Complaintletter.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mailaddress, null);

//        final  ImageView relation =(ImageView)dialogView.findViewById(R.id.relation);
        final CheckBox useRegister = (CheckBox) dialogView.findViewById(R.id.useRegister);
     //   final TextView mailsubmit = (TextView) dialogView.findViewById(R.id.mailsubmit);
        final EditText name = (EditText) dialogView.findViewById(R.id.name);
        final EditText doornumber = (EditText) dialogView.findViewById(R.id.doornumber);
        final EditText salutation = (EditText) dialogView.findViewById(R.id.salutation);
        if (position != -1) {
            salutation.setFocusable(false);
        }
        final EditText surname = (EditText) dialogView.findViewById(R.id.surname);
        final EditText parentname = (EditText) dialogView.findViewById(R.id.parentname);
        final TextInputLayout parentnameText = (TextInputLayout) dialogView.findViewById(R.id.parentnameText);
        final RadioButton sonofRadio = dialogView.findViewById(R.id.sonofRadio);
        final RadioButton motofRadio = dialogView.findViewById(R.id.motofRadio);
        final RadioButton datofRadio = dialogView.findViewById(R.id.datofRadio);

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
        final EditText mobile = (EditText) dialogView.findViewById(R.id.mobile);
        final EditText email = (EditText) dialogView.findViewById(R.id.email);

        Provincetemp = (MaterialBetterSpinner) dialogView.findViewById(R.id.Province);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PROVINCEtemp);
        Provincetemp.setAdapter(provinceAdapter);
        Provincetemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatatemp("province", PROVINCEtemp[position], "district", position);
            }
        });


        districttemp = (MaterialBetterSpinner) dialogView.findViewById(R.id.district);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DISTRICTtemp);
        districttemp.setAdapter(districtAdapter);
        districttemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatatemp("district", DISTRICTtemp[position], "commune", position);

            }
        });

        communetemp = (MaterialBetterSpinner) dialogView.findViewById(R.id.commune);
        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COMMUNEtemp);
        communetemp.setAdapter(communeAdapter);
        communetemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatatemp("commune", COMMUNEtemp[position], "village", position);

            }
        });

        villagetemp = (MaterialBetterSpinner) dialogView.findViewById(R.id.village);
        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, VILLAGEtemp);
        villagetemp.setAdapter(villageAdapter);
        fetchProviceDatatemp("province", "all", "province", position);


        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();

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

        if (position != -1) {
            submit.setText("Update");
            Mailbean bean = mailbeans.get(position);
            name.setText(bean.name);
            surname.setText(bean.surname);
            parentname.setText(bean.parentname);
            mobile.setText(bean.mobile);
            salutation.setText(bean.salutation);
            email.setText(bean.email);
            Phonedial.setCountryForNameCode(bean.country);
            doornumber.setText(bean.doornumber);
            Provincetemp.setText(bean.Province);
            districttemp.setText(bean.district);
            communetemp.setText(bean.commune);
            villagetemp.setText(bean.village);

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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    if ( position != k ) {
                        Mailbean tempBean = mailbeans.get(k);
                        if(bean.equals(tempBean)){
                            isNotMatch=false;
                            break;
                        }
                    }
                }
                if (isNotMatch) {
                    if (position == -1) {
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
                        mailbeans.add(bean);
                        signbeans.add(new Signbean("", name.getText().toString()));
                        signAdapter.notifyData(signbeans);
                    } else {
                        mailbeans.get(position).setName(name.getText().toString());
                        mailbeans.get(position).setSurname(surname.getText().toString());
                        mailbeans.get(position).setParentname(parentname.getText().toString());
                        mailbeans.get(position).setMobile(mobile.getText().toString());
                        mailbeans.get(position).setEmail(email.getText().toString());
                        mailbeans.get(position).setSalutation(salutation.getText().toString());
                        mailbeans.get(position).setCountry(Phonedial.getSelectedCountryCode());
                        mailbeans.get(position).setDoornumber(doornumber.getText().toString());
                        mailbeans.get(position).setProvince(Provincetemp.getText().toString());
                        mailbeans.get(position).setDistrict(districttemp.getText().toString());
                        mailbeans.get(position).setCommune(communetemp.getText().toString());
                        mailbeans.get(position).setVillage(villagetemp.getText().toString());
                        mailbeans.get(position).setRelation(relationVal);
                    }

                    mailAdapter.notifyData(mailbeans);
                    b.cancel();
                }else{
                    Toast.makeText(getApplicationContext(),"Please Enter different address",Toast.LENGTH_LONG).show();
                }
            }
        });

        b.show();


    }


    public void showRepresentativeDialog(final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Complaintletter.this);
        LayoutInflater inflater = Complaintletter.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.representative, null);

      //  final TextView mailsubmit = (TextView) dialogView.findViewById(R.id.mailsubmit);
        final EditText name = (EditText) dialogView.findViewById(R.id.name);
        final EditText doornumber = (EditText) dialogView.findViewById(R.id.doornumber);

        final EditText salutation = (EditText) dialogView.findViewById(R.id.salutation);
        if (position != -1) {
            salutation.setFocusable(false);
        }

        CheckBox useRegister = dialogView.findViewById(R.id.useRegister);

        final EditText surname = (EditText) dialogView.findViewById(R.id.surname);
        final EditText parentname = (EditText) dialogView.findViewById(R.id.parentname);
        final TextInputLayout parentnameText = (TextInputLayout) dialogView.findViewById(R.id.parentnameText);
        final RadioButton sonofRadio = dialogView.findViewById(R.id.sonofRadio);
        final RadioButton motofRadio = dialogView.findViewById(R.id.motofRadio);
        final RadioButton datofRadio = dialogView.findViewById(R.id.datofRadio);

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

        final CountryCodePicker Phonedial = (CountryCodePicker) dialogView.findViewById(R.id.phonedial);
        final EditText mobile = (EditText) dialogView.findViewById(R.id.mobile);
        final EditText email = (EditText) dialogView.findViewById(R.id.email);
        Provincerep = (MaterialBetterSpinner) dialogView.findViewById(R.id.Province);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PROVINCErep);
        Provincerep.setAdapter(provinceAdapter);
        Provincerep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatarep("province", PROVINCErep[position], "district", position);
            }
        });


        districtrep = (MaterialBetterSpinner) dialogView.findViewById(R.id.district);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DISTRICTrep);
        districtrep.setAdapter(districtAdapter);
        districtrep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatarep("district", DISTRICTrep[position], "commune", position);

            }
        });

        communerep = (MaterialBetterSpinner) dialogView.findViewById(R.id.commune);
        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COMMUNErep);
        communerep.setAdapter(communeAdapter);
        communerep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceDatarep("commune", COMMUNErep[position], "village", position);

            }
        });

        villagerep = (MaterialBetterSpinner) dialogView.findViewById(R.id.village);
        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, VILLAGErep);
        villagerep.setAdapter(villageAdapter);


        fetchProviceDatarep("province", "all", "province", position);


        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();

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

        if (position != -1) {
            submit.setText("Update");
            Representativebean bean = representativebeans.get(position);
            name.setText(bean.name);
            surname.setText(bean.surname);
            parentname.setText(bean.parentname);
            mobile.setText(bean.mobile);
            salutation.setText(bean.salutation);
            email.setText(bean.email);
            Phonedial.setCountryForNameCode(bean.country);

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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String relationVal = "sonof";
                if (datofRadio.isChecked()) {
                    relationVal = "datof";
                } else if (motofRadio.isChecked()) {
                    relationVal = "wifeof";
                }
                Representativebean bean = new Representativebean(
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
                    if (position != k) {
                        Representativebean tempBean = representativebeans.get(k);
                        if (bean.equals(tempBean)) {
                            isNotMatched = false;
                            break;
                        }
                    }
                }

                if (isNotMatched) {
                    if (position == -1) {
                        bean = new Representativebean(
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
                        representativebeans.add(bean);
                    } else {
                        representativebeans.get(position).setName(name.getText().toString());
                        representativebeans.get(position).setSurname(surname.getText().toString());
                        representativebeans.get(position).setParentname(parentname.getText().toString());
                        representativebeans.get(position).setMobile(mobile.getText().toString());
                        representativebeans.get(position).setEmail(email.getText().toString());
                        representativebeans.get(position).setSalutation(salutation.getText().toString());
                        representativebeans.get(position).setCountry(Phonedial.getSelectedCountryCode());
                        representativebeans.get(position).setDoornumber(doornumber.getText().toString());
                        representativebeans.get(position).setProvince(Provincerep.getText().toString());
                        representativebeans.get(position).setDistrict(districtrep.getText().toString());
                        representativebeans.get(position).setCommune(communerep.getText().toString());
                        representativebeans.get(position).setVillage(villagerep.getText().toString());
                        mailbeans.get(position).setRelation(relationVal);

                    }
                    representativeAdapter.notifyData(representativebeans);
                    b.cancel();
                }else{
                    Toast.makeText(getApplicationContext(),"Please Enter different representative",Toast.LENGTH_LONG).show();
                }
            }
        });

        b.show();

    }

    public void showWebviewDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("ADB Projects");

        WebView wv = new WebView(this);
        wv.loadUrl("https://www.adb.org/projects");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();

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
                            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Complaintletter.this,
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

                        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(Complaintletter.this,
                                android.R.layout.simple_dropdown_item_1line, PROVINCErep);
                        Provincerep.setAdapter(provinceAdapter);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Complaintletter.this,
                                android.R.layout.simple_dropdown_item_1line, DISTRICTrep);
                        districtrep.setAdapter(districtAdapter);
                        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(Complaintletter.this,
                                android.R.layout.simple_dropdown_item_1line, COMMUNErep);
                        communerep.setAdapter(communeAdapter);
                        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(Complaintletter.this,
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
                            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Complaintletter.this,
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

                        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(Complaintletter.this,
                                android.R.layout.simple_dropdown_item_1line, PROVINCEtemp);
                        Provincetemp.setAdapter(provinceAdapter);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Complaintletter.this,
                                android.R.layout.simple_dropdown_item_1line, DISTRICTtemp);
                        districttemp.setAdapter(districtAdapter);
                        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(Complaintletter.this,
                                android.R.layout.simple_dropdown_item_1line, COMMUNEtemp);
                        communetemp.setAdapter(communeAdapter);
                        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(Complaintletter.this,
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

    private void imageAttachment(int from, String filename,
                                 Bitmap file, Uri uri, CircleImageView circleImageView) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        circleImageView.setMfilePath(imageutils.getPath(uri));
        circleImageView.setIsImage("false");
        if (filename != null) {
            circleImageView.setIsImage("true");
            imageutils.createImage(file, filename, path, false);
        }

        GlideApp.with(Complaintletter.this).load(imageutils.getPath(uri))
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.file)
                .into(circleImageView);
    }

    private void imageAttachmenttemp(int from, String filename, Bitmap file, Uri uri, CircleImageView circleImageView) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        circleImageView.setMfilePath(imageutils.getPath(uri));
        circleImageView.setIsImage("false");
        if (filename != null) {
            circleImageView.setIsImage("true");
            imageutils.createImage(file, filename, path, false);
        }

        GlideApp.with(Complaintletter.this).load(imageutils.getPath(uri))
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.file)
                .into(circleImageView);
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
        showRepresentativeDialog(position);
    }

    @Override
    public void itemSignClick(int position) {
        signpicker(position);
    }

    public void signpicker(final int position) {

        final CharSequence[] items;


        items = new CharSequence[1];
        items[0] = "Signpad";


        android.app.AlertDialog.Builder alertdialog =
                new android.app.AlertDialog.Builder(Complaintletter.this);
        alertdialog.setTitle("Add Signature");
        alertdialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Signpad")) {
                    Intent intent = new Intent(Complaintletter.this, MainActivitySignature.class);
                    intent.putExtra("position", String.valueOf(position));
                    startActivityForResult(intent, START_SIGN_CODE);
                }
            }
        });
        alertdialog.show();
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
                    signbeans.get(returnPosition).setFilePath(returnValue);
                    signAdapter.notifyData(signbeans);
                }
                break;
            default:
                imageutils.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_pdf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.local_english) {
            setNewLocale(this, LocaleManager.ENGLISH);
            return true;
        }
        if (id == R.id.local_khmer) {
            setNewLocale(this, LocaleManager.KHMER);
            return true;
        }

        if (id == R.id.action_pdf) {
            CompliantPdfConfig.printFunction(Complaintletter.this, complaintLetterbean);
            return true;
        }

        if (id == R.id.action_view) {
            AppConfig.openPdfFile(Complaintletter.this, "cletter.pdf");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                FINE_LOCATION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_REQUEST) {
            // Received permission result for Location permission.
            Log.i(TAG, "Received response for Location permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i(TAG, "Location permission has now been granted. Now call initLocationUpdate");

            }
        } else if (requestCode == 1) {
            imageutils.request_permission_result(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                    GlideApp.with(Complaintletter.this).load(filepath)
                            .dontAnimate()
                            .thumbnail(0.5f)
                            .placeholder(R.drawable.file)
                            .into(projectimage);
                    imagePath = "http://" + AppConfig.ipcloud + "/uploads/" + imageutils.getfilename_from_path(filepath);
                } else {
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

}