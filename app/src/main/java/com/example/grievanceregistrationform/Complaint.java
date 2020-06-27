package com.example.grievanceregistrationform;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.grievanceregistrationform.ComplaintForm.Complaintbean;
import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.BaseActivity;
import com.example.grievanceregistrationform.app.GlideApp;
import com.example.grievanceregistrationform.app.Imageutils;
import com.example.grievanceregistrationform.app.LocaleManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.grievanceregistrationform.Complaintletter.mypreference;


public class Complaint extends BaseActivity implements Imageutils.ImageAttachmentListener  {
    private static final int FINE_LOCATION_CODE = 199;
    Imageutils imageutils;

    ProgressDialog pDialog;

    SharedPreferences sharedpreferences;
    private String TAG = getClass().getSimpleName();

    String id;
    RadioButton facilitator;
    RadioButton reviewpanel;
    RadioButton yes1;
    RadioButton no1;

    EditText name;
    EditText description;
    EditText projectcause;
    RadioButton yes;
    RadioButton no;
    EditText desiredoutcome;
    EditText add;
    EditText personname;

    EditText date;
    EditText status;
    EditText descript;

    TextView submit;

    MaterialBetterSpinner Province;
    private String[] PROVINCE = new String[]{
            "Loading",
    };

    MaterialBetterSpinner district;
    private String[] DISTRICT = new String[]{
            "Loading",
    };

    MaterialBetterSpinner commune;
    private String[] COMMUNE = new String[]{
            "Loading",
    };

    MaterialBetterSpinner village;
    private String[] VILLAGE = new String[]{
            "Loading",
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaintform);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        imageutils = new Imageutils(this);

        facilitator = (RadioButton) findViewById(R.id.facilitator);
        reviewpanel = (RadioButton) findViewById(R.id.reviewpanel);
        yes1 = (RadioButton) findViewById(R.id.yes1);
        no1 = (RadioButton) findViewById(R.id.no1);


        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        projectcause = (EditText) findViewById(R.id.projectcause);
        yes = (RadioButton) findViewById(R.id.yes);
        no = (RadioButton) findViewById(R.id.no);
        desiredoutcome = (EditText) findViewById(R.id.desiredoutcome);
        add = (EditText) findViewById(R.id.add);
        personname = (EditText) findViewById(R.id.personname);
         date = (EditText) findViewById(R.id.date);
        status = (EditText) findViewById(R.id.status);
        descript = (EditText) findViewById(R.id.descript);

        Province = (MaterialBetterSpinner) findViewById(R.id.Province);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PROVINCE);
        Province.setAdapter(provinceAdapter);
        Province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fetchProviceData("province", PROVINCE[position], "district");
            }
        });


        district = (MaterialBetterSpinner) findViewById(R.id.district);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DISTRICT);
        district.setAdapter(districtAdapter);
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceData("district", DISTRICT[position], "commune");

            }
        });

        commune = (MaterialBetterSpinner) findViewById(R.id.commune);
        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COMMUNE);
        commune.setAdapter(communeAdapter);
        commune.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchProviceData("commune", COMMUNE[position], "village");

            }
        });

        village = (MaterialBetterSpinner) findViewById(R.id.village);
        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, VILLAGE);
        village.setAdapter(villageAdapter);


    fetchProviceData("province", "all", "province");



        submit = (TextView) findViewById(R.id.submit);
       submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Complaintbean tempComplaintbean = new Complaintbean(

                        facilitator.getText().toString(),
                        reviewpanel.getText().toString(),
                        yes1.getText().toString(),
                        no1.getText().toString(),
                   "",
                      "",
                     "",
                       "",
                        "",
                       "",
                       "",
                       "",
                       "",
                        "",
                        "",
                        "",
                       "",
                        name.getText().toString(),
                        description.getText().toString(),
                        projectcause.getText().toString(),
                        yes.getText().toString(),
                        no.getText().toString(),
                        desiredoutcome.getText().toString(),
                        add.getText().toString(),
                        personname.getText().toString(),
                        "",
                        date.getText().toString()

                        );

                        //step 2
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",tempComplaintbean);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }



        } );



        try {
            Complaintbean complaintbean = (Complaintbean) getIntent().getSerializableExtra("object");
            if (complaintbean != null) {
                facilitator.setText(complaintbean.facilitator);
                reviewpanel.setText(complaintbean.reviewpanel);
                yes1.setText(complaintbean.yes1);
                no1.setText(complaintbean.no1);

                name.setText(complaintbean.name);
                description.setText(complaintbean.description);
                projectcause.setText(complaintbean.projectcause);
                yes.setText(complaintbean.yes);
                no.setText(complaintbean.no);
                desiredoutcome.setText(complaintbean.desiredoutcome);
                add.setText(complaintbean.add);
                personname.setText(complaintbean.personname);
               date.setText(complaintbean.date);
            }
        } catch (Exception e) {
            Log.e("xxxxxx", "Something went wrong");
        }

    }

    private void imageAttachment(int from, String filename, Bitmap file, Uri uri, CircleImageView circleImageView) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        circleImageView.setMfilePath(imageutils.getPath(uri));
        circleImageView.setIsImage("false");
        if (filename != null) {
            circleImageView.setIsImage("true");
            imageutils.createImage(file, filename, path, false);
        }

        GlideApp.with(Complaint.this).load(imageutils.getPath(uri))
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.file)
                .into(circleImageView);
    }

    private void fetchProviceData(final String key, final String val, final String val1) {
        this.pDialog.setMessage("fetching...");
        showDialog();
        StringRequest local16 = new StringRequest(1, "http://climatesmartcity.com/rua/students/ajax/readVillageDetails.php", new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    if (localJSONObject1.getBoolean("success")) {
                        JSONArray jsonArray = localJSONObject1.getJSONArray("data");

                        if (val1.equals("province")) {
                            PROVINCE = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PROVINCE[i] = jsonArray.getString(i);
                            }
                            DISTRICT = new String[1];
                            DISTRICT[0] = "Select Province";
                            COMMUNE = new String[1];
                            COMMUNE[0] = "Select District";
                            VILLAGE = new String[1];
                            VILLAGE[0] = "Select Commune";
                            district.setText("");
                            commune.setText("");
                            village.setText("");
                        } else if (val1.equals("district")) {
                            DISTRICT = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                DISTRICT[i] = jsonArray.getString(i);
                            }
                            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Complaint.this,
                                    android.R.layout.simple_dropdown_item_1line, DISTRICT);
                            district.setAdapter(districtAdapter);
                            COMMUNE = new String[1];
                            COMMUNE[0] = "Select District";
                            VILLAGE = new String[1];
                            VILLAGE[0] = "Select Commune";
                            district.setText("");
                            commune.setText("");
                            village.setText("");

                        } else if (val1.equals("commune")) {
                            COMMUNE = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                COMMUNE[i] = jsonArray.getString(i);
                            }
                            VILLAGE = new String[1];
                            VILLAGE[0] = "Select Commune";
                            commune.setText("");
                            village.setText("");

                        } else if (val1.equals("village")) {
                            VILLAGE = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                VILLAGE[i] = jsonArray.getString(i);
                            }
                            village.setText("");
                        }

                        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(Complaint.this,
                                android.R.layout.simple_dropdown_item_1line, PROVINCE);
                        Province.setAdapter(provinceAdapter);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Complaint.this,
                                android.R.layout.simple_dropdown_item_1line, DISTRICT);
                        district.setAdapter(districtAdapter);
                        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(Complaint.this,
                                android.R.layout.simple_dropdown_item_1line, COMMUNE);
                        commune.setAdapter(communeAdapter);
                        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(Complaint.this,
                                android.R.layout.simple_dropdown_item_1line, VILLAGE);
                        village.setAdapter(villageAdapter);

                        return;
                    }
                    String str = localJSONObject1.getString(AppConfig.convertKhmer("data",getApplicationContext()));
                    Toast.makeText(getApplicationContext(), AppConfig.convertKhmer(str,getApplicationContext()), Toast.LENGTH_SHORT).show();
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

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {

    }

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
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
}