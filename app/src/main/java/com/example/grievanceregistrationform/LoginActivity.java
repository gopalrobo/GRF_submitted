package com.example.grievanceregistrationform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.BaseActivity;
import com.example.grievanceregistrationform.app.LocaleManager;
import com.example.grievanceregistrationform.dp.DbFarmer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.SEND_SMS;
import static com.example.grievanceregistrationform.app.AppConfig.farmerId;
import static com.example.grievanceregistrationform.app.AppConfig.isLogin;
import static com.example.grievanceregistrationform.app.AppConfig.mypreference;


public class LoginActivity extends BaseActivity {
    ProgressDialog pDialog;
    public static final String buSurveyerId = "buSurveyerIdKey";
    SharedPreferences sharedpreferences;
    private String TAG = getClass().getSimpleName();
    DbFarmer dbFarmer;
    TextInputEditText mail;
    TextInputLayout mailInput;
    private static final int FINE_SMS_CODE = 199;
    EditText username;
    EditText password;
    ProgressBar smsProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        dbFarmer = new DbFarmer(this);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.login));

        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        final Button register = findViewById(R.id.register);
        final Button forgotpassword = findViewById(R.id.forgotpassword);
        final Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    getCreateTest(username.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter username and Password/Mobile", Toast.LENGTH_SHORT).show();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent localIntent1 = new Intent(LoginActivity.this, Registerpage.class);
                startActivity(localIntent1);

            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkPermission(new String[]{SEND_SMS, READ_PHONE_STATE})) {
                    requestPermission(new String[]{SEND_SMS, READ_PHONE_STATE}, FINE_SMS_CODE);
                } else {
                    forgotPasswordDialog();
                }


            }
        });

    }

    private void getCreateTest(final String username, final String password) {
        this.pDialog.setMessage("Creating...");
        showDialog();
        StringRequest local16 = new StringRequest(1, "http://climatesmartcity.com/UBA/grm_login.php", new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString("message");
                    if (localJSONObject1.getInt("success") == 1) {
                        dbFarmer.deleteAll();
                        JSONObject jsonObject = localJSONObject1.getJSONArray("grmRegistration").getJSONObject(0);
                        RegMainbean student = new RegMainbean();
                        String id = jsonObject.getString("id");
                        student.setId(id);
                        student.name = jsonObject.getString("name");
                        student.mobile = jsonObject.getString("mobile");
                        student.doornumber = jsonObject.getString("doornumber");
                        student.Province = jsonObject.getString("province");
                        student.district = jsonObject.getString("district");
                        student.commune = jsonObject.getString("commune");
                        student.village = jsonObject.getString("village");
                        student.email = jsonObject.getString("email");
                        student.password = jsonObject.getString("password");
                        student.confirmpassword = jsonObject.getString("confirmpassword");
                        student.surname = jsonObject.getString("surname");
                        student.parentname = jsonObject.getString("parentname");
                        student.salutation = jsonObject.getString("salutation");
                        student.relation = jsonObject.getString("relation");
                        dbFarmer.addData(id, new Gson().toJson(student));

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(buSurveyerId, username + "_" + password);
                        editor.putString(farmerId, id);
                        editor.putString(isLogin, "yes");
                        editor.commit();
                        Intent localIntent1 = new Intent(LoginActivity.this, MainActivityAllSurvey.class);
                        startActivity(localIntent1);
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

                localHashMap.put("mobile", username);
                localHashMap.put("password", password);


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
        if(id== R.id.video_play){
            Intent localIntent2 = new Intent(this, VideoPlay.class);
            startActivity(localIntent2);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    public void forgotPasswordDialog() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.RoundShapeTheme);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.forgot_password, null);
        mail = dialogView.findViewById(R.id.mail);
        mailInput = dialogView.findViewById(R.id.mailInput);
        smsProgress = dialogView.findViewById(R.id.smsProgress);

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mailInput.setError(null);
                }
            }
        });

        dialogBuilder.setTitle(getString(R.string.forgotPassword))
                .setPositiveButton(getString(R.string.submit), null)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
                        if (mail.getText().toString().length() > 8) {
                            getForgotPassword(b);
                        } else {
                            mailInput.setError("Enter valid Mobile");
                        }
                    }
                });
            }
        });

        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
    }


    private void getForgotPassword(final AlertDialog b) {
        smsProgress.setVisibility(View.VISIBLE);
        StringRequest local16 = new StringRequest(1, AppConfig.URL_FORGOT_PASSWORD, new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                smsProgress.setVisibility(View.GONE);
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString(AppConfig.convertKhmer("message", getApplicationContext()));
                    if (localJSONObject1.getInt("success") == 1) {

                        RegMainbean regMainbean = new Gson()
                                .fromJson(localJSONObject1.getString("data"), RegMainbean.class);

                        StringBuilder stringBuilder = new StringBuilder();
                        String name = regMainbean.name;
                        stringBuilder.append("Smart Grievance Compliance\nGRF : \nName : " + name + "\nPassword : " + regMainbean.password);
                        AppConfig.sendSMS(regMainbean.mobile,
                                stringBuilder.toString(), getApplicationContext());
                        Toast.makeText(getApplicationContext(), AppConfig.convertKhmer("Password  has been send you successfully", getApplicationContext()), Toast.LENGTH_SHORT).show();
                        username.setText(mail.getText().toString());
                        password.requestFocus();
                        b.cancel();
                        return;


                    } else {
                        mailInput.setError(AppConfig.convertKhmer(str, getApplicationContext()));
                    }
                    return;
                } catch (JSONException localJSONException) {
                    localJSONException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Log.e("tag", "Fetch Error: " + paramVolleyError.getMessage());
                mailInput.setError(AppConfig.convertKhmer("Server Error", getApplicationContext()));
                smsProgress.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put("mail", mail.getText().toString());
                localHashMap.put("dbname", "grmRegistration");
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }


    private boolean checkPermission(String[] permissions) {
        boolean result = false;
        for (int i = 0; i < permissions.length; i++) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i]) == 0;
            if (!result) {
                return false;
            }
        }
        return result;
    }

    private void requestPermission(String[] permissions, int code) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == FINE_SMS_CODE) {
            if (grantResults.length > 0) {
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean locationAccepted1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted && locationAccepted1) {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access SMS data.", Toast.LENGTH_LONG).show();
                    forgotPasswordDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access SMS data", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                            showMessageOKCancel(getResources().getString(R.string.locationPermissionAlert),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{SEND_SMS, READ_PHONE_STATE},
                                                        FINE_SMS_CODE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
            }
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}