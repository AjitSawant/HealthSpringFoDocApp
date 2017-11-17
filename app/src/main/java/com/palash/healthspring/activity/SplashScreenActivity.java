package com.palash.healthspring.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.palash.healthspring.R;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.Flag;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.Shimmer;
import com.palash.healthspring.utilities.ShimmerTextView;

import java.util.ArrayList;


public class SplashScreenActivity extends RuntimePermissionsActivity {
    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Flag flag;
    private ArrayList<DoctorProfile> listProfile;

    private ShimmerTextView splash_loading;
    private Shimmer shimmer;

    private static final int REQUEST_PERMISSIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        InitView();
    }

    private void InitView() {
        try {
            context = this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
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

    private void displaySplashScreen() {
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
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permissions();
        } else {
            displaySplashScreen();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void Permissions() {
        SplashScreenActivity.super.requestAppPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(final int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
        displaySplashScreen();
    }
}
