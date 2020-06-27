package com.example.grievanceregistrationform.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    public static final String farmerId = "farmerIdKey";
    public static final String isLogin = "isLoginKey";
    public static final String mypreference = "mypref";
    public static DecimalFormat df = new DecimalFormat("0.00");
    public static DecimalFormat dfFour = new DecimalFormat("0.0000");

    public static String ipcloud = "climatesmartcity.com/UBA";
    public static String URL_GET_All_SURVEY = "http://" + ipcloud + "/get_all_data.php";
    public static String URL_GET_TIMELINE = "http://" + ipcloud + "/get_all_time.php";
    public static String URL_FORGOT_PASSWORD = "http://" + ipcloud + "/get_forgot_mail.php";
    public static String URL_CHANGE_STATUS = "http://" + ipcloud + "/change_status.php";

    public static String URL_IMAGE_UPLOAD = "http://" + ipcloud + "/fileUpload.php";

    public static final Map<String, String> khmerEnglish = createMap();

    private static Map<String, String> createMap() {
        Map<String, String> hiEnglish = new HashMap<String, String>();
        hiEnglish.put("Survey successfully created.", "ការស្ទង់មតិត្រូវបានបង្កើតដោយជោគជ័យ។");
        hiEnglish.put("Oops! An error occurred.", "Oops! កំហុស\u200Bមួយ\u200Bបាន\u200Bកើត\u200Bឡើង");
        hiEnglish.put("404 Error", "៤០៤ កំហុស");
        hiEnglish.put("Survey successfully updated.", "ការស្ទង់មតិត្រូវបានធ្វើបច្ចុប្បន្នភាពដោយជោគជ័យ");
        hiEnglish.put("No Staff found", "មិនមានបុគ្គលិក");
        hiEnglish.put("Required field(s) is missing", "ខ្វះជំនាញដែលត្រូវការ");
        hiEnglish.put("Successfully found", "បានរកឃើញ");
        hiEnglish.put("Please enter username and Password/Mobile", "សូមបញ្ចូលឈ្មោះដែប្រើប្រាស់ និងលេខសម្ងាត់/ លេខទូរស័ព្ទ");
        hiEnglish.put("Add all", "បាន្ថែម");
        hiEnglish.put("Invalid Request!", "សំណើមិនត្រឹមត្រូវ!");
        hiEnglish.put("Person not found or Invalid student id", "រកមិនឃើញមនុស្សឬលេខសម្គាល់និស្សិតដែលមិនត្រឹមត្រូវ");
        hiEnglish.put("Password  has been send you successfully", "លេខសម្ងាត់ត្រូវបានផ្ញើរទៅអ្នកដោយជោគជ័យ");
        hiEnglish.put("Something went wrong", "មាន\u200Bអ្វីមួយ\u200Bមិន\u200Bប្រក្រតី");
        hiEnglish.put("No Service Provider is available", "មិនមានអ្នកផ្តល់សេវាកម្មទេ");
        hiEnglish.put("GPS is not Enabled!", "ជីភីអេសមិនត្រូវបានដាក់អោយតំណើការ!");
        hiEnglish.put("Do you want to turn on GPS?", "តើអ្នកចង់បើកប្រព័ន្ធ ជីភីអេស?");
        hiEnglish.put("OnStartSigning", "នៅពេលចុះឈ្មោះ");
        hiEnglish.put("Unable to store the signature", "មិនអាចរក្សាហត្ថលេខាទុកទេ");
        hiEnglish.put("Directory not created", "ថតមិនបានបង្កើត");
        hiEnglish.put("Some Network Error.Try after some time","កំហុសឆ្គងបណ្តាញមួយចំនួន ។ ព្យាយាមពេលក្រោយទៀត");

        hiEnglish.put("Complaint", "ដើមបណ្តឹងតវ៉ា");
        hiEnglish.put("Acknowledgement", "ទទួលស្គាល់");
        hiEnglish.put("Meeting invitation", "ការអញ្ជើញចូលរួមប្រជុំ");
        hiEnglish.put("Field investigation", "នការស៊ើបអង្កេតនៅទីតាំង");
        hiEnglish.put("Meeting Resolution date", "កាលបរិច្ឆេទប្រជុំដោះស្រាយ");
        hiEnglish.put("Feed back date", "កាលបរិច្ឆេទផ្តល់យោបលត្រឡប់");
        hiEnglish.put("Issues resolved or escalated","បញ្ហាបានដោះស្រាយ ឬកាន់តែធ្ងន់ធ្ងរ");

        hiEnglish.put("Permission Call Phone denied", "Permission Call Phone denied");
        hiEnglish.put("Mobile Number not found", "Mobile Number not found");
        hiEnglish.put("You can call the number by clicking on the button", "You can call the number by clicking on the button");
        hiEnglish.put("Network", "Network");
        hiEnglish.put("GPS Enabled", "GPS Enabled");
        hiEnglish.put("GPS is settings", "GPS is settings");
        hiEnglish.put("GPS is not enabled. Do you want to go to settings menu?","GPS is not enabled. Do you want to go to settings menu?");
        hiEnglish.put("Settings","Settings");
        hiEnglish.put("Enter all fileds","Enter all fileds");
        hiEnglish.put("HistoricalTimelinePhoto","HistoricalTimelinePhoto");

        hiEnglish.put("Father/Mother Name","ឈ្មោះឪពុក/ម្តាយ");
        hiEnglish.put("Son/Daughter Name","ឈ្មោះកូនប្រុស/កូនស្រី");


        return hiEnglish;
    }

    public static void openPdfFile(Context context,String name) {
        File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" +name);
        if (!fileBrochure.exists()) {
            CopyAssetsbrochure(context,name);
        }

        /** PDF reader code */
        File file = new File(Environment.getExternalStorageDirectory() + "/" +name);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO Pdf Viewer", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendSMS(String phoneNo, String msg, Context context) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    //method to write the PDFs file to sd card
    private static void CopyAssetsbrochure(Context context,String name) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for (int i = 0; i < files.length; i++) {
            String fStr = files[i];
            if (fStr.equalsIgnoreCase(name)) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(files[i]);
                    out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + files[i]);
                    copyFileNew(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    break;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }

    private static void copyFileNew(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static String convertKhmer(String value, Context context) {

        if (LocaleManager.getLanguagePref(context).equals(LocaleManager.KHMER)) {
            String[] valueStrings = value.split("_");
            if (valueStrings.length > 1) {
                if (khmerEnglish.containsKey(valueStrings[0])) {
                    return khmerEnglish.get(valueStrings[0]) + "_" + valueStrings[1];
                } else {
                    return valueStrings[0] + "_" + valueStrings[1];
                }
            } else {
                if (khmerEnglish.containsKey(valueStrings[0])) {
                    return khmerEnglish.get(valueStrings[0]);
                } else {
                    return valueStrings[0];
                }
            }

        }
        return value;
    }

}
