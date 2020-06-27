package com.example.grievanceregistrationform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.BaseActivity;
import com.example.grievanceregistrationform.app.CompliantPdfConfig;
import com.example.grievanceregistrationform.app.LocaleManager;
import com.example.grievanceregistrationform.dp.DbFarmer;
import com.example.grievanceregistrationform.maps.CustClusterHouseHoldActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.grievanceregistrationform.app.AppConfig.isLogin;
import static com.example.grievanceregistrationform.app.AppConfig.mypreference;


public class MainActivityAllSurvey extends BaseActivity implements OnSummaryClick{


    public static final String buSurveyerId = "buSurveyerIdKey";
    DbFarmer dbFarmer;
    SharedPreferences sharedpreferences;
    private ProgressDialog pDialog;
    private RecyclerView mastersList;
    private MasterAllSurveyAdapter mRecyclerAdapterMaster;
    private ArrayList<ComplaintLetterbean> itemArrayList = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_all_survey);
        dbFarmer = new DbFarmer(this);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.complist));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        NestedScrollView nestedScroll = findViewById(R.id.nestedScroll);
        ExtendedFloatingActionButton addMaster = findViewById(R.id.addSurvey);
        addMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityAllSurvey.this, CompliantLetterCopy.class);
                startActivity(intent);
            }
        });
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mastersList = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerAdapterMaster = new MasterAllSurveyAdapter(this, itemArrayList,this);
        final LinearLayoutManager thirdManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mastersList.setLayoutManager(thirdManager);
        mastersList.setAdapter(mRecyclerAdapterMaster);

        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY && addMaster.isExtended()) {
                    addMaster.shrink();
                }
                if (scrollY < oldScrollY) {
                    addMaster.extend();
                }
            }
        });

    }

    private void getAllData() {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://climatesmartcity.com/UBA/get_all_grfdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean(AppConfig.convertKhmer("error", getApplicationContext()));
                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray(AppConfig.convertKhmer("datas", getApplicationContext()));
                        itemArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject dataObject = jsonArray.getJSONObject(i);
                                ComplaintLetterbean complaintLetterbean = new Gson().fromJson(dataObject.getString("data"), ComplaintLetterbean.class);
                                complaintLetterbean.setId(dataObject.getString("formId"));
                                complaintLetterbean.setUniId(dataObject.getString("id"));
                                complaintLetterbean.setStatus(dataObject.getString("status"));
                                itemArrayList.add(complaintLetterbean);
                            } catch (Exception e) {
                                Log.e("xxxxxxxxxxx", e.toString());
                                //   Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                            }
                        }
                        mRecyclerAdapterMaster.notifyData(itemArrayList);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("xxxxxxxxxxx", e.toString());

                    //     Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        AppConfig.convertKhmer("Some Network Error.Try after some time", getApplicationContext()), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("key", sharedpreferences.getString(buSurveyerId, ""));
                localHashMap.put("dbname", "grievanceform");
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void hideDialog() {
        if (this.pDialog.isShowing())
            this.pDialog.dismiss();
    }


    private void showDialog() {
        if (!this.pDialog.isShowing())
            this.pDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllData();
    }


    private boolean isValidString(String string) {

        if (string == null) {
            return false;
        } else if (string.length() <= 0) {
            return false;
        } else if (string.startsWith("http")) {
            return false;
        }

        return true;
    }

    public String getfilename_from_path(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_profile) {
            RegMainbean regMainbean = new Gson().fromJson(dbFarmer
                    .getDataByfarmerId(sharedpreferences.getString(AppConfig.farmerId, "")).get(1), RegMainbean.class);

            Intent io = new Intent(MainActivityAllSurvey.this, Registerpage.class);
            io.putExtra("object", regMainbean);
            startActivity(io);
            return true;
        }
        if (id == R.id.action_location) {
            Intent io = new Intent(MainActivityAllSurvey.this, CustClusterHouseHoldActivity.class);
            startActivity(io);
            return true;
        }
        if (id == R.id.action_exit) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(isLogin);
            editor.commit();

            Intent intent = new Intent(MainActivityAllSurvey.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.local_english) {
            setNewLocale(this, LocaleManager.ENGLISH);
            return true;
        }
        if (id == R.id.local_hindi) {
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

    public void showWebviewDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("ADB Projects");

        WebView wv = new WebView(this);
        wv.loadUrl("https://www.adb.org/site/accountability-mechanism/main");
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



    public void printFunction(Context context, ComplaintLetterbean complaintLetterbean) {
        pDialog.setMessage("Preparing...");
        showDialog();

        HandlerThread handlerThread = new HandlerThread("NetworkOperation");
        handlerThread.start();
        Handler requestHandler = new Handler(handlerThread.getLooper());

        final Handler responseHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj.toString().equals("Failure")) {
                    hideDialog();
                    Uri photoURI = FileProvider.getUriForFile(context,
                            context.getPackageName()
                                    + ".provider", new
                                    File(
                                    Environment.getExternalStorageDirectory()
                                            .getAbsolutePath() + "/PDF/"
                                            + " Grievance Registration" + ".pdf"));

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(photoURI
                            , "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
//                    Intent intent=new Intent(context,PdfViewActivity.class);
//                    intent.putExtra("uri",photoURI.toString());
//                    context.startActivity(intent);

                } else {
                    showDialog();
                    pDialog.setMessage(msg.obj.toString());
                }

            }
        };

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();




                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/PDF";
                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();
                    Log.d("PDFCreator", "PDF Path: " + path);
                    File file = new File(dir, " Grievance Registration" + ".pdf");
                    FileOutputStream fOut = new FileOutputStream(file);


                    Document document = new Document();
                    PdfWriter pdfWriter = PdfWriter.getInstance(document, fOut);
                    Rectangle rect = new Rectangle(175, 20, 530, 800);
                    pdfWriter.setBoxSize("art", rect);

//            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cst_pdf);
//            Bitmap bu = BitmapFactory.decodeResource(context.getResources(), R.drawable.cst_pdf);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //           icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    //           bu.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                    byte[] byteArray1 = stream1.toByteArray();

//            HeaderFooterPageEvent event = new HeaderFooterPageEvent(Image.getInstance(byteArray),
//                    Image.getInstance(byteArray1));
//            pdfWriter.setPageEvent(event);

                    document.open();
                    CompliantPdfConfig.addMetaData(document);
                    // AppConfig.addTitlePage(document);
                    CompliantPdfConfig.addContent(document, complaintLetterbean,responseHandler);
                    document.close();


                } catch (Error | Exception e) {
                    e.printStackTrace();
                }

                Uri photoURI = FileProvider.getUriForFile(context,
                        context.getPackageName() + ".provider",
                        new File(Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/PDF/" + "Grievance Registration" + ".pdf"));

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(photoURI
                        , "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


                msg.obj = "Failure";
                responseHandler.sendMessage(msg);

            }
        };
        requestHandler.post(myRunnable);


    }


    @Override
    public void onPrintClick(int position) {
        printFunction(getApplicationContext(),itemArrayList.get(position));
    }
}


