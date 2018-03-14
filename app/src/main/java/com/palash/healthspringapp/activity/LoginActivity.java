package com.palash.healthspringapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELForgotPassword;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;

    private EditText login_edt_username;
    private EditText login_edt_password;
    private Button login_btn_login;
    private TextView forgot_password_btn;
    private CheckBox login_chkbx_remember;
    private RadioButton login_type_front_office_user_rbtn;
    private RadioButton login_type_doctor_rbtn;

    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<ELForgotPassword> elForgotPasswordArrayList;

    private String savedUserName;
    private String savedUserPassword;
    private boolean RememberMe;
    private boolean isFrontOfficeUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CheckPermission();
        InitSetting();
        InitView();
    }

    private void CheckPermission() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_SETTINGS
                    }, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitSetting() {
        context = this;
        localSetting = new LocalSetting();
        doctorProfileAdapter = new DatabaseAdapter(new DatabaseContract(context)).new DoctorProfileAdapter();
    }

    public void InitView() {
        try {
            login_edt_username = (EditText) findViewById(R.id.login_edt_username);
            login_edt_password = (EditText) findViewById(R.id.login_edt_password);
            login_btn_login = (Button) findViewById(R.id.login_btn_login);
            forgot_password_btn = (TextView) findViewById(R.id.forgot_password_btn);
            login_chkbx_remember = (CheckBox) findViewById(R.id.login_chkbx_remember);
            login_type_front_office_user_rbtn = (RadioButton) findViewById(R.id.login_type_front_office_user_rbtn);
            login_type_doctor_rbtn = (RadioButton) findViewById(R.id.login_type_doctor_rbtn);
            login_btn_login.setOnClickListener(this);
            forgot_password_btn.setOnClickListener(this);

            ShowLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowLogin() {
        try {
            listProfile = doctorProfileAdapter.listAll();
            if (listProfile != null && listProfile.size() > 0 && listProfile.get(0).getRememberMe().equals("true")) {
                login_edt_username.setText(localSetting.decodeString(listProfile.get(0).getLoginName()));
                login_edt_password.setText(localSetting.decodeString(listProfile.get(0).getPassword()));
                login_chkbx_remember.setChecked(true);

                if (listProfile.get(0).getIsFrontOfficeUser() != null && (listProfile.get(0).getIsFrontOfficeUser().equals("True")
                        || listProfile.get(0).getIsFrontOfficeUser().equals("true") || listProfile.get(0).getIsFrontOfficeUser().equals("1"))) {
                    login_type_doctor_rbtn.setChecked(false);
                    login_type_front_office_user_rbtn.setChecked(true);
                } else {
                    login_type_doctor_rbtn.setChecked(true);
                    login_type_front_office_user_rbtn.setChecked(false);
                }
            } else {
                ClearView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClearView() {
        try {
            login_edt_username.setText(null);
            login_edt_password.setText(null);
            login_chkbx_remember.setChecked(false);
            login_type_doctor_rbtn.setChecked(true);
            login_type_front_office_user_rbtn.setChecked(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                savedUserName = login_edt_username.getText().toString();
                savedUserPassword = login_edt_password.getText().toString();
                RememberMe = login_chkbx_remember.isChecked();
                isFrontOfficeUser = login_type_front_office_user_rbtn.isChecked();
                if (Validate()) {
                    if (localSetting.isNetworkAvailable(context)) {
                        new LoginTask().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.forgot_password_btn:
                savedUserName = login_edt_username.getText().toString();
                isFrontOfficeUser = login_type_front_office_user_rbtn.isChecked();
                if (login_edt_username.getText().toString().equals("")) {
                    Validate.Msgshow(context, "Please enter username.");
                } else {
                    if (localSetting.isNetworkAvailable(context)) {
                        new ValidUserTask().execute();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private boolean Validate() {
        try {
            if (!Validate.hasTextIn(login_edt_username.getText().toString())) {
                Validate.Msgshow(context, "Please enter username.");
                return false;
            } else if (!Validate.hasTextIn(login_edt_password.getText().toString())) {
                Validate.Msgshow(context, "Please enter password.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public class LoginTask extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        private ArrayList<DoctorProfile> profileList = new ArrayList<>();
        String responseString = "";
        String responseMsg = "";

        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String mLoginByFrontOfficeUser = "0";
                if (isFrontOfficeUser == true) {
                    mLoginByFrontOfficeUser = "1";
                } else {
                    mLoginByFrontOfficeUser = "0";
                }

                Log.d(Constants.TAG, "savedUserName:" + savedUserName);
                Log.d(Constants.TAG, "savedUserPassword:" + savedUserPassword);
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, savedUserName, savedUserPassword, mLoginByFrontOfficeUser);
                Response response = serviceConsumer.GET(Constants.LOGIN_URL);
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
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200 && responseMsg.equals("OK")) {
                try {
                    objectMapper = new JsonObjectMapper();
                    profileList = objectMapper.map(responseString, DoctorProfile.class);
                    if (profileList != null && profileList.size() > 0) {
                        saveProfile(profileList.get(0));
                    } else {
                        localSetting.alertbox(context, context.getResources().getString(R.string.login_alert), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (responseCode == Constants.HTTP_OK_200 && responseMsg.equals("Please login.")) {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), context.getResources().getString(R.string.network_alert));
            } else {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            }
            super.onPostExecute(result);
        }
    }

    private void saveProfile(DoctorProfile profile) {
        try {
            if (profile != null) {
                profile.setRememberMe(String.valueOf(RememberMe));
                profile.setLoginStatus(Constants.STATUS_LOG_IN);
                doctorProfileAdapter.delete();
                doctorProfileAdapter.create(profile);

                startActivity(new Intent(context, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                Constants.isFromLogin = true;
                Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ValidUserTask extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        String responseString = "";
        String responseMsg = "";
        String jSonData = "";
        private Response response = null;
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, "", "", "");
                objectMapper = new JsonObjectMapper();

                String mLoginByFrontOfficeUser = "0";
                if (isFrontOfficeUser == true) {
                    mLoginByFrontOfficeUser = "1";
                } else {
                    mLoginByFrontOfficeUser = "0";
                }

                ELForgotPassword elForgotPassord = new ELForgotPassword();
                elForgotPassord.setIsFontOfficeUser(mLoginByFrontOfficeUser);
                elForgotPassord.setLoginName(savedUserName);

                jSonData = objectMapper.unMap(elForgotPassord);
                response = serviceConsumer.POST(Constants.CHECK_VALID_USER_URL, jSonData);

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
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            if (responseCode == Constants.HTTP_OK_200 && responseMsg.equals("OK")) {
                try {
                    elForgotPasswordArrayList = objectMapper.map(responseString, ELForgotPassword.class);
                    if (elForgotPasswordArrayList != null && elForgotPasswordArrayList.size() > 0) {
                        startActivity(new Intent(context, ForgotPasswordActivity.class).putExtra("ForgotPasswordArrayList", elForgotPasswordArrayList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), "Username is invalid!");
            } else {
                localSetting.showErrorAlert(context, context.getResources().getString(R.string.opps_alert), localSetting.handleError(responseCode));
            }
            super.onPostExecute(result);
        }
    }
}
