package com.example.jarvis.dreamhouse.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.jarvis.dreamhouse.AppController;
import com.example.jarvis.dreamhouse.R;
import com.example.jarvis.dreamhouse.api.ApiList;
import com.example.jarvis.dreamhouse.api.RequestCode;
import com.example.jarvis.dreamhouse.api.RequestListener;
import com.example.jarvis.dreamhouse.api.RestClient;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarvis.dreamhouse.AppController.mAppOpenData;



public class LogIn extends AppCompatActivity  implements View.OnClickListener,RequestListener{


    private static final int REQUEST_CODE_EMAIL = 1;
    private static boolean check = true;
    TextView tv_fogotpass,tv_Signup;
    TextInputEditText ted_user;
    Button btn_login;
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
        setContentView(R.layout.activity_log_in);
     /*   tv_Signup=(TextView)findViewById(R.id.tv_Signup);
        tv_Signup.setPaintFlags(tv_Signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        */initresourcr();
        AppController.getInstance().setAppOpenData();
       getemail();

    }

    public void initresourcr(){
        /*tv_fogotpass=(TextView)findViewById(R.id.tv_fogotpass);
        tv_Signup=(TextView)findViewById(R.id.tv_Signup);

        tv_Signup.setPaintFlags(tv_Signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    */
        ted_user=(TextInputEditText)findViewById(R.id.ted_user);


        btn_login=(Button)findViewById(R.id.btn_login);

    //    tv_fogotpass.setOnClickListener(this);
      //  tv_Signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
         /*   case R.id.tv_fogotpass:
                Intent i=new Intent(LogIn.this,ForgotPassword.class);
                startActivity(i);
                break;

            case R.id.tv_Signup:
                Intent i2 = new Intent(LogIn.this,Signup.class);
                startActivity(i2);
                break;

            */

            case R.id.btn_login:

                if(isValid()){

                    dologin();

                   // Toast.makeText(LogIn.this,"correct value",Toast.LENGTH_SHORT).show();

                }



                break;

        }
    }

    public  boolean isValid(){

        if(!ted_user.getText().toString().trim().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {

            ted_user.requestFocus();
            ted_user.setError("Please Enter valid email");
            return false;

        }



        /*else if(ted_password.getText().toString().trim().length()==0){


            ted_password.requestFocus();
            ted_password.setError("Please Enter Password");
            return false;

        }
*/


        return true;
    }




    public void getemail() {

        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // TODO
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            ted_user.setText(accountName);
        }
    }


    public void dologin(){

        try{

            JSONObject param=new JSONObject();

            param.put(ApiList.KEY_EMAIL,ted_user.getText().toString().trim());
            param.put(ApiList.KEY_DEVICE_ID,mAppOpenData.getAndroidId());
            param.put(ApiList.KEY_DEVICE_TYPE, mAppOpenData.getmDeviceType());
            param.put(ApiList.KEY_DEVICE_BRAND_NAME, mAppOpenData.getmBrand());
            param.put(ApiList.KEY_MODEL_NO_NAME, mAppOpenData.getmModel());
            param.put(ApiList.KEY_DEVICE_IMEI_NO, mAppOpenData.getmDeviceId());
            param.put(ApiList.KEY_INTERNET_CONNECTION, mAppOpenData.getmInterNetConnection());
            param.put(ApiList.KEY_DEVICE_OS_VERSION, mAppOpenData.getmOsVersion());


            RestClient.getInstance().postRequest(LogIn.this, Request.Method.POST,ApiList.APIs.login.getUrl(),param,this, RequestCode.Login,true,true,"Please wait");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(RequestCode requestCode, Object object, String processCompleted, String status) {

        switch (requestCode){

            case Login:


                Intent i=new Intent(LogIn.this,DashboardActivity.class);
                startActivity(i);


                break;


        }


    }

    @Override
    public void onException(String error, String status, RequestCode requestCode) {

        switch (requestCode){

            case Login:

                Toast.makeText(LogIn.this,error,Toast.LENGTH_SHORT).show();


                break;


        }
    }
}


