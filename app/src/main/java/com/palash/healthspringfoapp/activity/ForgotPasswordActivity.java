package com.palash.healthspringfoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.entity.ELForgotPassword;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.Validate;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private OkHttpClient client;

    private ArrayList<ELForgotPassword> elForgotPasswordArrayList;

    private EditText forgot_password_username_edt;
    private EditText forgot_password_employee_no_edt;
    private EditText forgot_password_otp_code_edt;
    private EditText forgot_password_new_pass_edt;
    private EditText forgot_password_confirm_pass_edt;

    private RadioButton forgot_password_by_mobile_radio_btn;
    private RadioButton forgot_password_by_email_radio_btn;

    private Button forgot_password_check_employee_btn;
    private Button forgot_password_send_otp_btn;
    private Button forgot_password_submit_btn;
    private Button forgot_password_cancel_btn;

    private LinearLayout forgot_password_send_otp_layout;
    private LinearLayout forgot_password_otp_layout;
    private LinearLayout forgot_password_new_password_layout;
    private LinearLayout forgot_password_confirm_password_layout;

    private String LoginName = "";
    private String EmployeeNo = "";
    private String mLoginByFrontOfficeUser = "0";
    private String mEmail = "";
    private String mMobileNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initAdapter();
        InitView();

        setData();

    }

    private void setData() {
        elForgotPasswordArrayList = new ArrayList<>();
        elForgotPasswordArrayList = (ArrayList<ELForgotPassword>) getIntent().getSerializableExtra("ForgotPasswordArrayList");
        if (elForgotPasswordArrayList != null && elForgotPasswordArrayList.size() > 0) {
            forgot_password_username_edt.setText(elForgotPasswordArrayList.get(0).getLoginName());
            forgot_password_username_edt.setEnabled(false);
            EmployeeNo = elForgotPasswordArrayList.get(0).getEmployeeNo();
            mLoginByFrontOfficeUser = elForgotPasswordArrayList.get(0).getIsFontOfficeUser();
        }
    }

    private void initAdapter() {
        context = this;
        localSetting = new LocalSetting();
        client = new OkHttpClient();
    }

    private void InitView() {

        forgot_password_username_edt = (EditText) findViewById(R.id.forgot_password_username_edt);
        forgot_password_employee_no_edt = (EditText) findViewById(R.id.forgot_password_employee_no_edt);
        forgot_password_otp_code_edt = (EditText) findViewById(R.id.forgot_password_otp_code_edt);
        forgot_password_new_pass_edt = (EditText) findViewById(R.id.forgot_password_new_pass_edt);
        forgot_password_confirm_pass_edt = (EditText) findViewById(R.id.forgot_password_confirm_pass_edt);

        forgot_password_by_mobile_radio_btn = (RadioButton) findViewById(R.id.forgot_password_by_mobile_radio_btn);
        forgot_password_by_email_radio_btn = (RadioButton) findViewById(R.id.forgot_password_by_email_radio_btn);

        forgot_password_check_employee_btn = (Button) findViewById(R.id.forgot_password_check_employee_btn);
        forgot_password_send_otp_btn = (Button) findViewById(R.id.forgot_password_send_otp_btn);
        forgot_password_submit_btn = (Button) findViewById(R.id.forgot_password_submit_btn);
        forgot_password_cancel_btn = (Button) findViewById(R.id.forgot_password_cancel_btn);

        forgot_password_send_otp_layout = (LinearLayout) findViewById(R.id.forgot_password_send_otp_layout);
        forgot_password_otp_layout = (LinearLayout) findViewById(R.id.forgot_password_otp_layout);
        forgot_password_new_password_layout = (LinearLayout) findViewById(R.id.forgot_password_new_password_layout);
        forgot_password_confirm_password_layout = (LinearLayout) findViewById(R.id.forgot_password_confirm_password_layout);

        forgot_password_send_otp_layout.setVisibility(View.GONE);
        forgot_password_otp_layout.setVisibility(View.GONE);
        forgot_password_new_password_layout.setVisibility(View.GONE);
        forgot_password_confirm_password_layout.setVisibility(View.GONE);

        forgot_password_check_employee_btn.setOnClickListener(this);
        forgot_password_send_otp_btn.setOnClickListener(this);
        forgot_password_submit_btn.setOnClickListener(this);
        forgot_password_cancel_btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_password_check_employee_btn:
                if (!forgot_password_employee_no_edt.getText().toString().equals("")) {
                    if (forgot_password_employee_no_edt.getText().toString().equals(EmployeeNo)) {
                        forgot_password_send_otp_layout.setVisibility(View.VISIBLE);
                        forgot_password_employee_no_edt.setEnabled(false);
                        forgot_password_check_employee_btn.setVisibility(View.GONE);
                        Toast.makeText(context, "Employee number matched", Toast.LENGTH_SHORT).show();
                    } else {
                        forgot_password_send_otp_layout.setVisibility(View.GONE);
                        forgot_password_check_employee_btn.setVisibility(View.VISIBLE);
                        forgot_password_employee_no_edt.setEnabled(true);
                        forgot_password_check_employee_btn.setEnabled(true);
                        forgot_password_check_employee_btn.setText("Check Employee");
                        localSetting.showWarningAlert(context, context.getResources().getString(R.string.opps_alert), "Employee number is not matched!");
                    }
                } else {
                    Validate.Msgshow(context, "Please enter employee number!");
                }
                break;
            case R.id.forgot_password_send_otp_btn:
                if (forgot_password_by_mobile_radio_btn.isChecked()) {
                    sendSMS();
                } else {
                    sendEmail();
                }
                break;
            case R.id.forgot_password_submit_btn:
                break;
            case R.id.forgot_password_cancel_btn:
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                break;
        }
    }

    private void sendSMS() {
        if (mMobileNo != null && (mMobileNo.length() > 0 && mMobileNo.length() < 10)) {

            //string userName = txtUserName.Text;
            String SMStext = "Use " + "1234" + " as one time password (OTP) to login to your palash account. Do not share this OTP to anyone for security reasons. Valid for 15 minutes.";
            //string msgRecepient = txtPhNumber.Text;

            String url = "http://www.smsgatewaycenter.com/library/send_sms_2.php?UserName=healthspring&Password=873457256&Type=Bulk";
            url += "&To=";
            url += mMobileNo;
            url += "&Mask=";
            url += "HSPRNG";
            url += "&&Message=";
            url += SMStext;

            Response response = null;
            try {
                Request request = new Request.Builder()
                        .cacheControl(new CacheControl.Builder().noCache().build())
                        .url(url).get()
//                        .addHeader("username", userLoginID)
//                        .addHeader("password", userPassword)
//                        .addHeader("logintype", userLoginType)
                        .build();
                System.setProperty("http.keepAlive", "false");
                Log.d(Constants.TAG, "Url:" + url);
                response = client.newCall(request).execute();

                Log.e("Response : ",""+response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            localSetting.showWarningAlert(context, context.getResources().getString(R.string.opps_alert), "Registered mobile number is not valid.");
        }
    }

    private void sendEmail() {
        if (mEmail != null && mEmail.length() > 0 && Validate.isValidEmail(mEmail)) {

        } else {
            localSetting.showWarningAlert(context, context.getResources().getString(R.string.opps_alert), "Registered email address is not valid.");
        }
    }

    private boolean Validate() {
        try {
            LoginName = forgot_password_username_edt.getText().toString();
            EmployeeNo = forgot_password_employee_no_edt.getText().toString();
            if (!Validate.hasTextIn(forgot_password_username_edt.getText().toString())) {
                Validate.Msgshow(context, "Please enter username.");
                return false;
            } else if (!Validate.hasTextIn(forgot_password_employee_no_edt.getText().toString())) {
                Validate.Msgshow(context, "Please enter employee no.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
