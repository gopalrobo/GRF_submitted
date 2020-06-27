package com.example.grievanceregistrationform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.grievanceregistrationform.app.AppConfig.isLogin;
import static com.example.grievanceregistrationform.app.AppConfig.mypreference;

public class SplashScreen extends AppCompatActivity {


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_survey);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                } finally {
                    if (sharedpreferences.contains(isLogin) && sharedpreferences.getString(isLogin, "").equals("yes")) {
                        SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivityAllSurvey.class));
                    } else {
                        SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    }   finish();
                }
            }
        };
        logoTimer.start();

    }

}
