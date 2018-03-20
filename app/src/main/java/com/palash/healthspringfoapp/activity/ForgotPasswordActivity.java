package com.palash.healthspringfoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.api.JsonObjectMapper;
import com.palash.healthspringfoapp.api.WebServiceConsumer;
import com.palash.healthspringfoapp.entity.ELForgotPassword;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;
import com.palash.healthspringfoapp.utilities.Mail;
import com.palash.healthspringfoapp.utilities.TransparentProgressDialog;
import com.palash.healthspringfoapp.utilities.Validate;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private OkHttpClient client;

    private ELForgotPassword elForgotPassword;
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
    private Button forgot_password_submit_otp_btn;
    private Button forgot_password_submit_btn;
    private Button forgot_password_cancel_btn;

    private LinearLayout forgot_password_send_otp_layout;
    private LinearLayout forgot_password_otp_layout;
    private LinearLayout forgot_password_new_password_layout;
    private LinearLayout forgot_password_confirm_password_layout;
    private LinearLayout forgot_password_footer_layout;

    private String mEmail = "";
    private String mMobileNo = "";
    private String mMobileOtpUrl;
    private String mEmailOtpUrl;
    private String mGeneratedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initAdapter();
        InitView();
        setData();
    }

    private void setData() {
        elForgotPassword = new ELForgotPassword();
        elForgotPasswordArrayList = new ArrayList<>();
        elForgotPasswordArrayList = (ArrayList<ELForgotPassword>) getIntent().getSerializableExtra("ForgotPasswordArrayList");
        if (elForgotPasswordArrayList != null && elForgotPasswordArrayList.size() > 0) {
            elForgotPassword = elForgotPasswordArrayList.get(0);

            forgot_password_username_edt.setText(elForgotPassword.getLoginName());
            mEmail = elForgotPassword.getEmailId();
            mMobileNo = elForgotPassword.getMobileNo();
            if (elForgotPassword.getClickflag() == null) {
                elForgotPassword.setClickflag("0");
            }
        }
    }

    private void initAdapter() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        forgot_password_submit_otp_btn = (Button) findViewById(R.id.forgot_password_submit_otp_btn);
        forgot_password_submit_btn = (Button) findViewById(R.id.forgot_password_submit_btn);
        forgot_password_cancel_btn = (Button) findViewById(R.id.forgot_password_cancel_btn);

        forgot_password_send_otp_layout = (LinearLayout) findViewById(R.id.forgot_password_send_otp_layout);
        forgot_password_otp_layout = (LinearLayout) findViewById(R.id.forgot_password_otp_layout);
        forgot_password_new_password_layout = (LinearLayout) findViewById(R.id.forgot_password_new_password_layout);
        forgot_password_confirm_password_layout = (LinearLayout) findViewById(R.id.forgot_password_confirm_password_layout);
        forgot_password_footer_layout = (LinearLayout) findViewById(R.id.forgot_password_footer_layout);

        forgot_password_send_otp_layout.setVisibility(View.GONE);
        forgot_password_otp_layout.setVisibility(View.GONE);
        forgot_password_new_password_layout.setVisibility(View.GONE);
        forgot_password_confirm_password_layout.setVisibility(View.GONE);
        forgot_password_footer_layout.setVisibility(View.GONE);

        forgot_password_check_employee_btn.setOnClickListener(this);
        forgot_password_send_otp_btn.setOnClickListener(this);
        forgot_password_submit_otp_btn.setOnClickListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_password_check_employee_btn:
                if (!forgot_password_employee_no_edt.getText().toString().equals("")) {
                    if (forgot_password_employee_no_edt.getText().toString().equals(elForgotPassword.getEmployeeNo())) {
                        forgot_password_send_otp_layout.setVisibility(View.VISIBLE);
                        forgot_password_employee_no_edt.setEnabled(false);
                        forgot_password_check_employee_btn.setVisibility(View.GONE);
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
                if (elForgotPassword.getClickflag() != null && elForgotPassword.getClickflag().equals("3")) {
                    new RequestLockUserTask().execute();
                } else {
                    forgot_password_otp_code_edt.setEnabled(true);
                    forgot_password_otp_code_edt.setText("");
                    forgot_password_otp_layout.setVisibility(View.GONE);
                    forgot_password_new_password_layout.setVisibility(View.GONE);
                    forgot_password_confirm_password_layout.setVisibility(View.GONE);
                    forgot_password_footer_layout.setVisibility(View.GONE);

                    if (forgot_password_by_mobile_radio_btn.isChecked()) {
                        sendSMS();
                    } else {
                        sendEmail();
                    }
                }
                break;
            case R.id.forgot_password_submit_otp_btn:
                if (forgot_password_employee_no_edt.getText().toString().equals("")) {
                    Validate.Msgshow(context, "Please enter OTP number!");
                } else if (forgot_password_employee_no_edt.getText().toString().length() < 4) {
                    Validate.Msgshow(context, "Please enter valid OTP number!");
                } else {
                    new CheckOTPTask().execute();
                }
                break;
            case R.id.forgot_password_submit_btn:
                if (!Validate.hasTextIn(forgot_password_new_pass_edt.getText().toString())) {
                    Validate.Msgshow(context, "Please enter new password.");
                } else if (!Validate.hasMinimumCharInEditText(forgot_password_new_pass_edt.getText().toString())) {
                    Validate.Msgshow(context, "Please enter new password min 8 and max 25 characters.");
                } else if (!Validate.hasTextIn(forgot_password_confirm_pass_edt.getText().toString())) {
                    Validate.Msgshow(context, "Please enter confirm password.");
                } else if (!Validate.hasMinimumCharInEditText(forgot_password_confirm_pass_edt.getText().toString())) {
                    Validate.Msgshow(context, "Please enter confirm password min 8 and max 25 characters.");
                } else if (!Validate.hasBothEditTextSame(forgot_password_new_pass_edt.getText().toString(), forgot_password_confirm_pass_edt.getText().toString())) {
                    Validate.Msgshow(context, "Confirm password not matched.");
                } else {
                    new UpdatePasswordTask().execute();
                }
                break;
            case R.id.forgot_password_cancel_btn:
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                break;
        }
    }

    private int GenerateRandomNo() {
        int randomPIN = 0;
        randomPIN = (int) (Math.random() * 9000) + 1000;
        return randomPIN;
    }

    private void sendSMS() {
        if (mMobileNo != null && mMobileNo.length() == 10) {
            mGeneratedOTP = String.valueOf(GenerateRandomNo());

            String SMStext = "Use " + mGeneratedOTP + " as one time password OTP to login to your palash account. Do not share this OTP to anyone for security reasons. Valid for 15 minutes.";

            mMobileOtpUrl = "http://www.smsgatewaycenter.com/library/send_sms_2.php?UserName=healthspring&Password=873457256&Type=Bulk";
            mMobileOtpUrl += "&To=";
            mMobileOtpUrl += mMobileNo;
            mMobileOtpUrl += "&Mask=";
            mMobileOtpUrl += "HSPRNG";
            mMobileOtpUrl += "&Message=";
            mMobileOtpUrl += SMStext;

            if (localSetting.isNetworkAvailable(context)) {
                new SendMobileOTPTask().execute();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
            }
        } else {
            localSetting.showWarningAlert(context, context.getResources().getString(R.string.opps_alert), "Registered mobile number is invalid.");
        }
    }

    private class SendMobileOTPTask extends AsyncTask<String, String, String> {
        private String responseString = "";
        private String responseMsg = "";
        private int responseCode = 0;
        private Response response = null;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... uri) {
            try {
                URL urls = new URL(mMobileOtpUrl.replace(" ", "%20"));
                Log.d(Constants.TAG + "" + "mMobileOtpUrl:", "" + urls);
                /*try {
                    Request request = new Request.Builder()
                            .cacheControl(new CacheControl.Builder().noCache().build())
                            .url(urls).get()
                            .build();
                    System.setProperty("http.keepAlive", "false");
                    response = client.newCall(request).execute();
                    if (response != null) {
                        responseString = response.body().string();
                        responseCode = response.code();
                        responseMsg = response.message().toString();
                        Log.d(Constants.TAG, "Response code:" + responseCode);
                        Log.d(Constants.TAG, "Response string:" + responseString);
                        Log.d(Constants.TAG, "Response Msg:" + responseMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            super.onPostExecute(result);

            new AddOTPTask().execute();
            forgot_password_otp_layout.setVisibility(View.VISIBLE);
            forgot_password_submit_otp_btn.setVisibility(View.VISIBLE);

            /*if (responseCode == Constants.HTTP_OK_200 && responseMsg.equals("OK")) {
                if (result != null && (result.contains("Success (1)") || result.contains("Fail (0)"))) {
                    forgot_password_otp_layout.setVisibility(View.VISIBLE);
                    forgot_password_submit_otp_btn.setVisibility(View.VISIBLE);
                    forgot_password_send_otp_btn.setText("Resend OTP");

                    new AddOTPTask().execute();
                }
            } else {
                forgot_password_otp_layout.setVisibility(View.GONE);
                forgot_password_submit_otp_btn.setVisibility(View.GONE);
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), "Something wents wrong");
            }*/
        }
    }

    private void sendEmail() {
        if (mEmail != null && mEmail.length() > 0 && Validate.isValidEmail(mEmail)) {
            if (localSetting.isNetworkAvailable(context)) {
                new SendEmailOTPTask().execute();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
            }
        } else {
            localSetting.showWarningAlert(context, context.getResources().getString(R.string.opps_alert), "Registered email address is not valid.");
        }
    }

    private class SendEmailOTPTask extends AsyncTask<String, String, Boolean> {
        private String responseString = "";
        private String responseMsg = "";
        private int responseCode = 0;
        private Response response = null;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean res = false;
            try {
                String Otp = String.valueOf(GenerateRandomNo());

                Mail m = new Mail("it@healthspring.in", "Health$pring");
                String[] toArr = null;
                m.set_from("frontoffice_thane@healthspring.in");
                toArr = new String[]{mEmail};
                m.set_to(toArr);
                m.set_subject("One Time Password for palash account");

                StringBuilder BodyContent = new StringBuilder("");
                BodyContent.append("<br />");
                BodyContent.append("<span class=" + "Normal" + "> Use <b>" + Otp + "</b>" + " as one time password (OTP) to login to your palash account. Do not share this OTP to anyone for security reasons. Valid for 15 minutes. </span>");
                BodyContent.append("<span class=" + "Normal" + "></span>");
                BodyContent.append("<br /><br />");

                //m.set_body(params[0]);
                m.set_body(BodyContent.toString());

                res = m.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            localSetting.hideDialog(progressDialog);
            if (b) {
                new AddOTPTask().execute();
                forgot_password_otp_layout.setVisibility(View.VISIBLE);
                forgot_password_submit_otp_btn.setVisibility(View.VISIBLE);

            /*if (responseCode == Constants.HTTP_OK_200 && responseMsg.equals("OK")) {
                if (result != null && (result.contains("Success (1)") || result.contains("Fail (0)"))) {
                    forgot_password_otp_layout.setVisibility(View.VISIBLE);
                    forgot_password_submit_otp_btn.setVisibility(View.VISIBLE);
                    forgot_password_send_otp_btn.setText("Resend OTP");

                    new AddOTPTask().execute();
                }
            } else {
                forgot_password_otp_layout.setVisibility(View.GONE);
                forgot_password_submit_otp_btn.setVisibility(View.GONE);
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), "Something wents wrong");
            }*/
            } else {
                Toast.makeText(context, "Message not sent.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AddOTPTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String jSonData = "";
        private String responseString = "";
        private Response response = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                elForgotPassword.setOTP(mGeneratedOTP);
                int clickCount = Integer.parseInt(elForgotPassword.getClickflag()) + 1;
                elForgotPassword.setClickflag(String.valueOf(clickCount));

                jsonObjectMapper = new JsonObjectMapper();
                webServiceConsumer = new WebServiceConsumer(context, "", "", "");
                jSonData = jsonObjectMapper.unMap(elForgotPassword);
                response = webServiceConsumer.POST(Constants.ADD_OTP_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response Code :" + responseCode);
                    Log.d(Constants.TAG, "Response String :" + responseString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_CREATED_201) {
                localSetting.showSuccessAlert(context, context.getResources().getString(R.string.app_name), "OTP sent to your registered mobile");
            } else {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            }
            super.onPostExecute(result);
        }
    }

    private class RequestLockUserTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String jSonData = "";
        private String responseString = "";
        private Response response = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                elForgotPassword.setOTP(mGeneratedOTP);
                int clickCount = Integer.parseInt(elForgotPassword.getClickflag()) + 1;
                elForgotPassword.setClickflag(String.valueOf(clickCount));

                jsonObjectMapper = new JsonObjectMapper();
                webServiceConsumer = new WebServiceConsumer(context, "", "", "");
                jSonData = jsonObjectMapper.unMap(elForgotPassword);
                response = webServiceConsumer.POST(Constants.ADD_OTP_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response Code :" + responseCode);
                    Log.d(Constants.TAG, "Response String :" + responseString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_CREATED_201) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.opps_alert))
                        .setContentText("User is locked. You have pass max send OTP attempt.")
                        .setConfirmText("Go to Login")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        })
                        .show();
            } else {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            }
            super.onPostExecute(result);
        }
    }

    private class CheckOTPTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String jSonData = "";
        private String responseString = "";
        private Response response = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                elForgotPassword.setOTP(forgot_password_otp_code_edt.getText().toString());

                jsonObjectMapper = new JsonObjectMapper();
                webServiceConsumer = new WebServiceConsumer(context, "", "", "");
                jSonData = jsonObjectMapper.unMap(elForgotPassword);
                response = webServiceConsumer.POST(Constants.CHECK_OTP_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response Code :" + responseCode);
                    Log.d(Constants.TAG, "Response String :" + responseString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200) {
                try {
                    elForgotPasswordArrayList = jsonObjectMapper.map(responseString, ELForgotPassword.class);
                    if (elForgotPasswordArrayList != null && elForgotPasswordArrayList.size() > 0) {
                        forgot_password_otp_code_edt.setEnabled(false);
                        forgot_password_new_password_layout.setVisibility(View.VISIBLE);
                        forgot_password_confirm_password_layout.setVisibility(View.VISIBLE);
                        forgot_password_submit_otp_btn.setVisibility(View.GONE);
                        forgot_password_footer_layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {

                forgot_password_otp_code_edt.setEnabled(true);
                forgot_password_new_password_layout.setVisibility(View.GONE);
                forgot_password_confirm_password_layout.setVisibility(View.GONE);
                forgot_password_submit_otp_btn.setVisibility(View.VISIBLE);
                forgot_password_footer_layout.setVisibility(View.GONE);
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), "OTP mismatch!");
            } else {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            }
            super.onPostExecute(result);
        }
    }

    private class UpdatePasswordTask extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
        private String jSonData = "";
        private String responseString = "";
        private Response response = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                elForgotPassword.setPassword(forgot_password_new_pass_edt.getText().toString());

                jsonObjectMapper = new JsonObjectMapper();
                webServiceConsumer = new WebServiceConsumer(context, "", "", "");
                jSonData = jsonObjectMapper.unMap(elForgotPassword);
                response = webServiceConsumer.POST(Constants.UPDATE_FORGOT_PASSWORD_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                    responseString = response.body().string();
                    Log.d(Constants.TAG, "Response Code :" + responseCode);
                    Log.d(Constants.TAG, "Response String :" + responseString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200) {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(getResources().getString(R.string.app_name))
                        .setContentText("Password Updated successfully.")
                        .setConfirmText("Go to Login")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        })
                        .show();
            } else {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            }
            super.onPostExecute(result);
        }
    }
}
