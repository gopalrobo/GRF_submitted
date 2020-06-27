package com.example.grievanceregistrationform;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.grievanceregistrationform.ComplaintForm.Complaintbean;
import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    public static final String mypreference = "mypref";
    public static final String buSurveyerId = "buSurveyerIdKey";
    private static final int FINE_LOCATION_CODE = 199;
    ProgressDialog pDialog;
    EditText id;

    CardView householdDate;
    CardView questionnaire;


    TextView submit;
    Mainbean mainbean = null;
    SharedPreferences sharedpreferences;
    ComplaintLetterbean complaintLetterbean;
    Complaintbean complaintbean;

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        householdDate = (CardView) findViewById(R.id.householdDate);
        questionnaire = (CardView) findViewById(R.id.questionnaire);


        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mainbean tempMainbean = new Mainbean(
                        complaintLetterbean,
                        complaintbean);

                String jsonVal = new Gson().toJson(tempMainbean);
                Log.e("xxxxxxxxxxxxx", jsonVal);
                if (mainbean == null) {
                    tempMainbean.setId(String.valueOf(System.currentTimeMillis()));
                } else {
                    tempMainbean.setId(mainbean.id);
                }

                getCreateTest(tempMainbean.id, sharedpreferences.getString(buSurveyerId, ""), jsonVal);
            }
        });
        try {
            mainbean = (Mainbean) getIntent().getSerializableExtra("object");
            if (mainbean != null) {
                complaintLetterbean = mainbean.complaintLetterbean;
                complaintbean = mainbean.complaintbean;

            }
        } catch (Exception e) {
            Log.e("xxxxxx", "Something went wrong");
        }




        questionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Complaint.class);
                //step 1
                if(mainbean!=null&&mainbean.getComplaintbean()!=null) {
                    intent.putExtra("data", mainbean.complaintbean);
                }
                startActivityForResult(intent, 1);
            }
        });

        householdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Complaintletter.class);
                //step 1
                if(mainbean!=null&&mainbean.getComplaintLetterbean()!=null) {
                    intent.putExtra("data", mainbean.complaintLetterbean);
                } startActivityForResult(intent, 7);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //step 3
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                complaintbean = (Complaintbean) data.getSerializableExtra("result");
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                complaintLetterbean = (ComplaintLetterbean) data.getSerializableExtra("result");
            }
        }

    }
    private void getCreateTest(final String mId, final String surveyer, final String data) {
        this.pDialog.setMessage("Creating...");
        showDialog();
        StringRequest local16 = new StringRequest(1, "http://climatesmartcity.com/UBA/create_data.php", new Response.Listener<String>() {
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
                localHashMap.put("formId", mId);
                localHashMap.put("surveyer", surveyer);
                localHashMap.put("data", data);
                localHashMap.put("dbname", "grievanceform");


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
    }}
