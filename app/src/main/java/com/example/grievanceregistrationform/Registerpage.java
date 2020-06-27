package com.example.grievanceregistrationform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.grievanceregistrationform.app.BaseActivity;
import com.example.grievanceregistrationform.app.LocaleManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registerpage extends BaseActivity {
    public static final String mypreference = "mypref";
    public static final String buSurveyerId = "buSurveyerIdKey";
    private static final int FINE_LOCATION_CODE = 199;
    ProgressDialog pDialog;
    EditText id;
    EditText name, surname, parentname;
    TextInputLayout parentnameText;
    CountryCodePicker Phonedial;
    EditText mobile;
    EditText doornumber;
    EditText email;
    EditText password;
    EditText confirmpassword;
    RadioButton sonofRadio;
    RadioButton wifeofRadio;
    RadioButton datofRadio;

    BetterSpinner Province;
    BetterSpinner district;
    BetterSpinner commune;
    BetterSpinner village;

    TextInputLayout salutationlayout;
    BetterSpinner salutation;
    TextInputLayout namelayout;
    TextInputLayout surnamelayout;
    TextInputLayout mobilelayout;
    TextInputLayout doornumberlayout;
    TextInputLayout Provincelayout;
    TextInputLayout districtlayout;
    TextInputLayout communelayout;
    TextInputLayout villagelayout;
    TextInputLayout email_auto_complete;
    TextInputLayout passwordlayout;
    TextInputLayout confirmpasswordlayout;

    RegMainbean RegMainbean = null;
    SharedPreferences sharedpreferences;
    private String[] SALUTATION = new String[]{
            "Mr","Ms","Mrs","Dr",
    };
    private String[] PROVINCE = new String[]{
            "Loading",
    };
    private String[] DISTRICT = new String[]{
            "Loading",
    };
    private String[] COMMUNE = new String[]{
            "Loading",
    };
    private String[] VILLAGE = new String[]{
            "Loading",
    };
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setSubtitle("Affected Person Registration");
        salutation = (BetterSpinner) findViewById(R.id.salutation);
        salutationlayout = (TextInputLayout) findViewById(R.id.salutationlayout);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        parentname = (EditText) findViewById(R.id.parentname);
        parentnameText = (TextInputLayout) findViewById(R.id.parentnameText);
        sonofRadio = findViewById(R.id.sonofRadio);
        wifeofRadio = findViewById(R.id.wifeofRadio);
        datofRadio = findViewById(R.id.datofRadio);
        doornumber = (EditText) findViewById(R.id.doornumber);
        ExtendedFloatingActionButton submit = findViewById(R.id.submit);
        mobile = (EditText) findViewById(R.id.mobile);

        namelayout = findViewById(R.id.namelayout);
        surnamelayout = findViewById(R.id.surnamelayout);
        mobilelayout = findViewById(R.id.mobilelayout);
        doornumberlayout = findViewById(R.id.doornumberlayout);
        Provincelayout = findViewById(R.id.Provincelayout);
        districtlayout = findViewById(R.id.districtlayout);
        communelayout = findViewById(R.id.communelayout);
        villagelayout = findViewById(R.id.villagelayout);
        email_auto_complete = findViewById(R.id.email_auto_complete);
        passwordlayout = findViewById(R.id.passwordlayout);
        confirmpasswordlayout = findViewById(R.id.confirmpasswordlayout);
        Province = (BetterSpinner) findViewById(R.id.Province);
        district = (BetterSpinner) findViewById(R.id.district);
        commune  = (BetterSpinner) findViewById(R.id.commune);
        village  = (BetterSpinner) findViewById(R.id.village);


        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        confirmpassword =  findViewById(R.id.confirmpassword);


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

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    passwordlayout.setError(null);
                }
            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String passwrd = password.getText().toString();
                if (editable.length() > 0 && passwrd.length() > 0) {
                    if (!confirmpassword.getText().toString().equals(passwrd)) {
                        confirmpasswordlayout.setError("Password not match");
                    }else {
                        confirmpasswordlayout.setError(null);
                    }
                }
            }
        });
        sonofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sonofRadio.setChecked(true);
                    wifeofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                    parentnameText.setHint("Father/Mother Name");
                }else {
                    sonofRadio.setChecked(false);
                    wifeofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                }
            }
        });
        wifeofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sonofRadio.setChecked(false);
                    wifeofRadio.setChecked(true);
                    datofRadio.setChecked(false);
                    parentnameText.setHint("Husband Name");
                }else {
                    sonofRadio.setChecked(false);
                    wifeofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                }
            }
        });
        datofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sonofRadio.setChecked(false);
                    wifeofRadio.setChecked(false);
                    datofRadio.setChecked(true);
                    parentnameText.setHint("Father/Mother Name");
                }else {
                    sonofRadio.setChecked(false);
                    wifeofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                }
            }
        });
    /*    fatofRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sonofRadio.setChecked(false);
                    wifeofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                    fatofRadio.setChecked(true);
                    parentnameText.setHint("Son/Daughter Name");
                }else {
                    sonofRadio.setChecked(false);
                    wifeofRadio.setChecked(false);
                    datofRadio.setChecked(false);
                    fatofRadio.setChecked(false);
                }
            }
        });
    */    Phonedial = (CountryCodePicker) findViewById(R.id.phonedial);
        mobile = (EditText) findViewById(R.id.mobile);
        doornumber = (EditText) findViewById(R.id.doornumber);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PROVINCE);
        Province.setAdapter(provinceAdapter);
        Province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Provincelayout.setError(null);
                fetchProviceData("province", PROVINCE[position], "district");
            }
        });


        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DISTRICT);
        district.setAdapter(districtAdapter);
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                districtlayout.setError(null);
                fetchProviceData("district", DISTRICT[position], "commune");

            }
        });

        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COMMUNE);
        commune.setAdapter(communeAdapter);
        commune.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                communelayout.setError(null);
                fetchProviceData("commune", COMMUNE[position], "village");

            }
        });

        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, VILLAGE);
        village.setAdapter(villageAdapter);
        village.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                villagelayout.setError(null);
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

        fetchProviceData("province", "all", "province");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (salutation.getText().toString().length() <= 0) {
                    salutationlayout.setError("Select the Salutation");
                }else if (name.getText().toString().length() <= 0) {
                    namelayout.setError("Enter the name");
                }else if (surname.getText().toString().length() <= 0) {
                    surnamelayout.setError("Enter the Surname");
                }else if (parentname.getText().toString().length() <= 0) {
                    parentnameText.setError("Enter the Parent name");
                }else if (mobile.getText().toString().length() <= 0 && mobile.getText().toString().length() > 10) {
                    mobilelayout.setError("Enter the Mobile No");
                }else if (doornumber.getText().toString().length() <= 0) {
                    doornumberlayout.setError("Enter the DoorNo..");
                }else if (Province.getText().toString().length() <= 0) {
                    Provincelayout.setError("Select the Province");
                }else if (district.getText().toString().length() <= 0) {
                    districtlayout.setError("Select the District");
                }else if (commune.getText().toString().length() <= 0) {
                    communelayout.setError("Select the Commune");
                }else if (village.getText().toString().length() <= 0) {
                    villagelayout.setError("Select the Village");
                }else if (email.getText().toString().length() <= 0) {
                    email_auto_complete.setError("Enter the Email Id");
                }else if (password.getText().toString().length() <= 0) {
                    passwordlayout.setError("Enter the Password");
                }else if (confirmpassword.getText().toString().length() <= 0) {
                    confirmpasswordlayout.setError("Enter the Confirm password");
                }else if (!confirmpassword.getText().toString().equals(password.getText().toString())) {
                    confirmpasswordlayout.setError("Password not match");
                }
                else {

                String relationVal = "sonof";
                if (datofRadio.isChecked()) {
                    relationVal = "datof";
                } else if (wifeofRadio.isChecked()) {
                    relationVal = "wifeof";
                }
                RegMainbean tempRegMainbean = new RegMainbean(

                        name.getText().toString(),
                        Phonedial.getSelectedCountryCode(),
                        mobile.getText().toString(),
                        doornumber.getText().toString(),
                        Province.getText().toString(),
                        district.getText().toString(),
                        commune.getText().toString(),
                        village.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        confirmpassword.getText().toString(),
                        surname.getText().toString(),
                        parentname.getText().toString(),
                        salutation.getText().toString(),relationVal);


                String jsonVal = new Gson().toJson(tempRegMainbean);
                Log.e("xxxxxxxxxxxxx", jsonVal);
                if (RegMainbean == null) {
                    tempRegMainbean.setId(String.valueOf(System.currentTimeMillis()));
                } else {
                    tempRegMainbean.setId(RegMainbean.id);
                }

                getCreateTest(tempRegMainbean.id, name.getText().toString(), mobile.getText().toString(), doornumber.getText().toString(), Province.getText().toString(), district.getText().toString(), commune.getText().toString(), village.getText().toString(), email.getText().toString(), password.getText().toString(), confirmpassword.getText().toString(),
                        surname.getText().toString(),
                        parentname.getText().toString(),
                        salutation.getText().toString(),
                        relationVal);


                }


            }
        });
        try {
            RegMainbean = (RegMainbean) getIntent().getSerializableExtra("object");
            if (RegMainbean != null) {

                name.setText(RegMainbean.name);
                mobile.setText(RegMainbean.mobile);
                doornumber.setText(RegMainbean.doornumber);
                Province.setText(RegMainbean.Province);
                district.setText(RegMainbean.district);
                commune.setText(RegMainbean.commune);
                village.setText(RegMainbean.village);
                email.setText(RegMainbean.email);
                password.setText(RegMainbean.password);
                confirmpassword.setText(RegMainbean.confirmpassword);
                surname.setText(RegMainbean.surname);
                parentname.setText(RegMainbean.parentname);
                salutation.setText(RegMainbean.salutation);
                if (RegMainbean.relation != null) {
                    if (RegMainbean.relation.equals("sonof")) {
                        sonofRadio.setChecked(true);
                    } else if (RegMainbean.relation.equals("datof")) {
                        datofRadio.setChecked(true);
                    } else if (RegMainbean.relation.equals("wifeof")) {
                        wifeofRadio.setChecked(true);
                    }else{
                        sonofRadio.setChecked(true);
                    }
                }

            }

        } catch (Exception e) {
            Log.e("xxxxxx", "Something went wrong");
        }

        Intent intent = new Intent(Registerpage.this, LoginActivity.class);
    }

    private void getCreateTest(final String mId, final String name, final String mobile, final String doornumber, final String Province, final String district, final String commune, final String village, final String email, final String password, final String confirmpassword,final String surname,final String parentname,final String salutation,final String relation) {
        this.pDialog.setMessage("Creating...");
        showDialog();
        StringRequest local16 = new StringRequest(1, "http://climatesmartcity.com/UBA/grm_reg.php", new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString("message");
                    if (localJSONObject1.getInt("success") == 1) {
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
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
                localHashMap.put("name", name);
                localHashMap.put("mobile", mobile);
                localHashMap.put("doornumber", doornumber);
                localHashMap.put("Province", Province);
                localHashMap.put("district", district);
                localHashMap.put("commune", commune);
                localHashMap.put("village", village);
                localHashMap.put("email", email);
                localHashMap.put("password", password);
                localHashMap.put("confirmpassword", confirmpassword);
                localHashMap.put("surname", surname);
                localHashMap.put("parentname", parentname);
                localHashMap.put("salutation", salutation);
                localHashMap.put("relation", relation);


                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
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

                            try {
                                RegMainbean = (RegMainbean) getIntent().getSerializableExtra("object");
                                if (RegMainbean != null) {

                                    name.setText(RegMainbean.name);
                                    mobile.setText(RegMainbean.mobile);
                                    doornumber.setText(RegMainbean.doornumber);
                                    Province.setText(RegMainbean.Province);
                                    district.setText(RegMainbean.district);
                                    commune.setText(RegMainbean.commune);
                                    village.setText(RegMainbean.village);
                                    email.setText(RegMainbean.email);
                                    password.setText(RegMainbean.password);
                                    confirmpassword.setText(RegMainbean.confirmpassword);
                                    surname.setText(RegMainbean.surname);
                                    parentname.setText(RegMainbean.parentname);
                                    salutation.setText(RegMainbean.salutation);
                                    if (RegMainbean.relation != null) {
                                        if (RegMainbean.relation.equals("sonof")) {
                                            sonofRadio.setChecked(true);
                                        } else if (RegMainbean.relation.equals("datof")) {
                                            datofRadio.setChecked(true);
                                        } else if (RegMainbean.relation.equals("wifeof")) {
                                            wifeofRadio.setChecked(true);
                                        }else{
                                            sonofRadio.setChecked(true);
                                        }
                                    }

                                }

                            } catch (Exception e) {
                                Log.e("xxxxxx", "Something went wrong");
                            }


                        } else if (val1.equals("district")) {
                            DISTRICT = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                DISTRICT[i] = jsonArray.getString(i);
                            }
                            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Registerpage.this,
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

                        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(Registerpage.this,
                                android.R.layout.simple_dropdown_item_1line, PROVINCE);
                        Province.setAdapter(provinceAdapter);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Registerpage.this,
                                android.R.layout.simple_dropdown_item_1line, DISTRICT);
                        district.setAdapter(districtAdapter);
                        ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(Registerpage.this,
                                android.R.layout.simple_dropdown_item_1line, COMMUNE);
                        commune.setAdapter(communeAdapter);
                        ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(Registerpage.this,
                                android.R.layout.simple_dropdown_item_1line, VILLAGE);
                        village.setAdapter(villageAdapter);

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

