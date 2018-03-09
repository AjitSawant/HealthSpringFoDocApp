package com.palash.healthspringapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.Appointment;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.palash.healthspringapp.utilities.Validate;
import com.squareup.okhttp.Response;


public class CancelAppointmentActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;

    private EditText cancel_appointment_edt_reason;
    private Button cancel_appointment_bnt_submit;

    private String UnitID;
    private String AppointmentID;

    private Flag flag;
    private Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancle_appointment);
        InitSetting();
        InitView();
    }

    private void InitView() {
        cancel_appointment_edt_reason = (EditText) findViewById(R.id.cancel_appointment_edt_reason);
        cancel_appointment_bnt_submit = (Button) findViewById(R.id.cancel_appointment_bnt_submit);
        UnitID = getIntent().getStringExtra("UnitID");
        AppointmentID = getIntent().getStringExtra("AppointmentID");

        cancel_appointment_bnt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel_appointment_edt_reason.getText().toString().length() != 0) {
                    CancleAppointmentBindView();
                } else {
                    Validate.Msgshow(context, "Please enter appointment cancel reason.");
                }
            }
        });
    }

    private void InitSetting() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            context = this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            flagAdapter = databaseAdapter.new FlagAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CancleAppointmentBindView() {
        try {
            appointment = new Appointment();
            appointment.setUnitID(UnitID);
            appointment.setAppointmentId(AppointmentID);
            appointment.setAppCancelReason(cancel_appointment_edt_reason.getText().toString());
            new CancleAppointmentTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public class CancleAppointmentTask extends AsyncTask<Void, Void, String> {

        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private JsonObjectMapper objMapper = null;
        private WebServiceConsumer serviceConsumer = null;
        private Response response = null;
        private String jSonData = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = "";
            try {
                objMapper = new JsonObjectMapper();
                jSonData = objMapper.unMap(appointment);
                serviceConsumer = new WebServiceConsumer(context, null, null, null);
                response = serviceConsumer.POST(Constants.CANCLE_APPOINTMENT_URL, jSonData);
                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response string:" + responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (responseCode == Constants.HTTP_OK_200) {
                    localSetting.hideDialog(progressDialog);
                    new AlertDialog
                            .Builder(context)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage("Appointment cancel successfully.")
                            .setCancelable(false)
                            .setPositiveButton("Go to Appointment", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //startActivity(new Intent(context, AppointmentListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                } else {
                    localSetting.hideDialog(progressDialog);
                    localSetting.alertbox(context, localSetting.handleError(responseCode), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
