package com.palash.healthspringapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.api.JsonObjectMapper;
import com.palash.healthspringapp.api.WebServiceConsumer;
import com.palash.healthspringapp.database.DatabaseAdapter;
import com.palash.healthspringapp.database.DatabaseContract;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.ELSynchOfflineData;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;
import com.palash.healthspringapp.utilities.Shimmer;
import com.palash.healthspringapp.utilities.ShimmerTextView;
import com.palash.healthspringapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class SplashScreenActivity
        extends AppCompatActivity
        //extends RuntimePermissionsActivity
{
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.SynchOfflineDataAdapter synchOfflineDataAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Flag flag;
    private ArrayList<DoctorProfile> listProfile;
    ArrayList<ELSynchOfflineData> elSynchOfflineDataList;

    private ShimmerTextView splash_loading;
    private Shimmer shimmer;

    private int appVersion = 0;
    private static final int REQUEST_PERMISSIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        InitView();
        FetchAppVesion();
    }

    private void InitView() {
        try {
            context = this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            synchOfflineDataAdapter = databaseAdapter.new SynchOfflineDataAdapter();
            flagAdapter = databaseAdapter.new FlagAdapter();
            masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();

            shimmer = new Shimmer();
            splash_loading = (ShimmerTextView) findViewById(R.id.splash_loading);
            shimmer.start(splash_loading);
            listProfile = doctorProfileAdapter.listAll();

            //set flag
            flag = new Flag();
            flag.setFlag(Constants.ALL_URL_TASK);
            flagAdapter.create(flag);
            flag.setFlag(Constants.ALL_URL_TASK);
            masterFlagAdapter.create(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FetchAppVesion() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionCode;
            Log.d(Constants.TAG, "AppVersione :" + appVersion);
            Log.d(Constants.TAG, "versionName :" + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startActivity(new Intent(context, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        //finish();
        if (localSetting.isNetworkAvailable(context)) {
            new GetAppVesion().execute();
        } else {
            CheckAppUpdate();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*private void Permissions() {
        SplashScreenActivity.super.requestAppPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(final int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
        LauncherScreen();
    }*/

    private class GetAppVesion extends AsyncTask<Void, Void, String> {
        private TransparentProgressDialog progressDialog;
        private JsonObjectMapper jsonObjectMapper;
        private WebServiceConsumer webServiceConsumer;
        private int responseCode = 0;
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
                jsonObjectMapper = new JsonObjectMapper();
                webServiceConsumer = new WebServiceConsumer(context, "", "","");
                response = webServiceConsumer.GET(Constants.APP_VERSION_URL);
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
                elSynchOfflineDataList = jsonObjectMapper.map(responseString, ELSynchOfflineData.class);
                if (elSynchOfflineDataList != null && elSynchOfflineDataList.size() > 0) {
                    synchOfflineDataAdapter.delete();
                    for (int index = 0; index < elSynchOfflineDataList.size(); index++) {
                        synchOfflineDataAdapter.create(elSynchOfflineDataList.get(index));
                    }
                }
            } else {
                Toast.makeText(context, localSetting.handleError(responseCode), Toast.LENGTH_SHORT).show();
            }
            CheckAppUpdate();
            super.onPostExecute(result);
        }
    }

   /* private void displaySplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permissions();
        } else {
            LauncherScreen();
        }
    }*/

    private void CheckAppUpdate() {
        elSynchOfflineDataList = synchOfflineDataAdapter.listAll();
        if (elSynchOfflineDataList != null && elSynchOfflineDataList.size() > 0) {
            String fromDBAppVersion = elSynchOfflineDataList.get(0).getVersionCode();
            if ((fromDBAppVersion != null) && (Integer.parseInt(fromDBAppVersion) < appVersion || Integer.parseInt(fromDBAppVersion) > appVersion)) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_app_update);

                TextView dialogButton = (TextView) dialog.findViewById(R.id.update);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        finish();
                    }
                });
                dialog.show();
            } else {
                LaunchActivity();
            }
        } else {
            LaunchActivity();
        }
    }

    private void LaunchActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listProfile != null && listProfile.size() > 0 && listProfile.get(0).getLoginStatus().equals(Constants.STATUS_LOG_IN)) {
                    startActivity(new Intent(context, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                finish();
            }
        }, 500);
    }
}
