package com.palash.healthspringapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringapp.R;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapterDB;

    private EditText edt_set_current_password;
    private EditText edt_set_new_password;
    private EditText edt_set_confirm_password;
    private Button bnt_set_changepassword;
    private Button bnt_synchronization;
    private TextView txt_set_sych;
    private Flag masterflag = null;
    private Chronometer setting_chronometer;

    private DoctorProfile doctorProfile;
    private ArrayList<DoctorProfile> listProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        InitSetting();
        InitView();
    }

    private void InitSetting() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        doctorProfileAdapter = new DatabaseAdapter(new DatabaseContract(context)).new DoctorProfileAdapter();
        listProfile = doctorProfileAdapter.listAll();
        masterFlagAdapterDB = databaseAdapter.new MasterFlagAdapter();
    }

    public void InitView() {
        edt_set_current_password = (EditText) findViewById(R.id.edt_set_current_password);
        edt_set_new_password = (EditText) findViewById(R.id.edt_set_new_password);
        edt_set_confirm_password = (EditText) findViewById(R.id.edt_set_confirm_password);
        bnt_set_changepassword = (Button) findViewById(R.id.bnt_set_changepassword);
        txt_set_sych = (TextView) findViewById(R.id.txt_set_sych);
        bnt_set_changepassword.setOnClickListener(this);

        bnt_synchronization = (Button) findViewById(R.id.bnt_set_synchronize);
        bnt_synchronization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localSetting.isNetworkAvailable(context)) {
                    Intent intent = new Intent(context, SynchronizationActivity.class).putExtra("reason","master data");
                    startActivity(intent);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_alert), Toast.LENGTH_SHORT).show();
                }
            }
        });
        setting_chronometer = (Chronometer) findViewById(R.id.set_chronometer);
        setting_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                ShowMSG();
            }
        });
    }

    @Override
    protected void onResume() {
        setting_chronometer.setBase(SystemClock.elapsedRealtime());
        setting_chronometer.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setting_chronometer.stop();
    }

    private void ShowMSG() {
        masterflag = masterFlagAdapterDB.listCurrent();
        if (masterflag != null && masterflag.getDateTime() != null && masterflag.getDateTime().length() > 0) {
            txt_set_sych.setText("Click on start to synchronize hospital records\nLast synchronization : " + masterflag.getDateTime());
        } else {
            txt_set_sych.setText("Click on start to synchronize hospital records\nLast synchronization : No synchronization");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnt_set_changepassword:
                UpdatePassword();
                break;
        }
    }

    public boolean validateControls() {
        if (!Validate.hasTextIn(edt_set_current_password.getText().toString())) {
            Validate.Msgshow(context, "Please enter current password.");
            return false;
        } else if (!localSetting.decodeString(doctorProfileAdapter.listAll().get(0).getPassword()).equals(edt_set_current_password.getText().toString())) {
            Validate.Msgshow(context, "Current password not matched.");
            return false;
        } else if (!Validate.hasTextIn(edt_set_new_password.getText().toString())) {
            Validate.Msgshow(context, "Please enter new password.");
            return false;
        } else if (edt_set_current_password.getText().toString().equals(edt_set_new_password.getText().toString())) {
            Validate.Msgshow(context, "Current password and new password are same");
            return false;
        } else if (!Validate.hasMinimumCharInEditText(edt_set_new_password.getText().toString())) {
            Validate.Msgshow(context, "Please enter new password min 6 and max 12 characters.");
            return false;
        } else if (!Validate.hasTextIn(edt_set_confirm_password.getText().toString())) {
            Validate.Msgshow(context, "Please enter confirm password.");
            return false;
        } else if (!Validate.hasMinimumCharInEditText(edt_set_confirm_password.getText().toString())) {
            Validate.Msgshow(context, "Please enter confirm password min 6 and max 12 characters.");
            return false;
        } else if (!Validate.hasBothEditTextSame(edt_set_new_password.getText().toString(), edt_set_confirm_password.getText().toString())) {
            Validate.Msgshow(context, "Confirm password not matched.");
            return false;
        }
        return true;
    }

    private void UpdatePassword() {
        try {
            if (validateControls()) {
                doctorProfile = new DoctorProfile();
                doctorProfile.setDoctorID(doctorProfileAdapter.listAll().get(0).getDoctorID());
                doctorProfile.setPassword(edt_set_new_password.getText().toString());
                new ChangePasswordTask().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Clear() {
        try {
            edt_set_current_password.setText(null);
            edt_set_new_password.setText(null);
            edt_set_confirm_password.setText(null);
            edt_set_current_password.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ChangePasswordTask extends AsyncTask<Void, Void, Void> {
        TransparentProgressDialog progressDialog;
        private Response response;
        private JsonObjectMapper objMapper;
        private String jSonData = "";
        private WebServiceConsumer serviceConsumer = null;
        private int responseCode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(doctorProfile);
                response = serviceConsumer.POST(Constants.UPDATE_PASSWORD_URL, jSonData);
                if (response != null) {
                    responseCode = response.code();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            try {
                if (responseCode == Constants.HTTP_OK_200) {
                    doctorProfileAdapter.updatePassword(doctorProfile);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("New password updated successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Clear();
                                    doctorProfileAdapter.LogOut(listProfile.get(0).getDoctorID(), Constants.STATUS_LOG_OUT);
                                    SchedulerManager.getInstance().stopAll(context);
                                    startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher).show();
                } else {
                    localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

