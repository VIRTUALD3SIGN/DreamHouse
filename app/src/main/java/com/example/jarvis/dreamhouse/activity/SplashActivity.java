package com.example.jarvis.dreamhouse.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.jarvis.dreamhouse.R;

public class SplashActivity extends AppCompatActivity {

    private static boolean check = true;
    private int PERMISSION_ALL = 1;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    check = false;
                    return check;
                }
            }
        }
        return check;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        gotonextscreen();
    }

    public void gotonextscreen()
    {
        Handler mh = new Handler();
        mh.postDelayed(new Runnable() {
            @Override
            public void run() {

                  checkPermissions();
            }
        },2000);
    }

    private void checkPermissions() {
        String[] permission = {Manifest.permission.READ_PHONE_STATE,Manifest.permission.GET_ACCOUNTS};
        if (hasPermissions(this, permission)) {

            Intent in = new Intent(SplashActivity.this,LogIn.class);
            startActivity(in);


            // goToNextScreen();


        } else {
            // Log.e(TAG, "Permission not granted.!");
            PERMISSION_ALL = 1;
            if (!hasPermissions(this, permission)) {
                ActivityCompat.requestPermissions(this, permission, PERMISSION_ALL);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {

                Intent in = new Intent(SplashActivity.this,LogIn.class);
                startActivity(in);

                check = true;
            } else {
                finish();
            }
        }
    }
}
