package com.palash.healthspringfoapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.Flag;
import com.palash.healthspringfoapp.task.MasterTask;
import com.palash.healthspringfoapp.task.SynchronizationTask;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;

import java.util.ArrayList;

public class SynchronizationActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.FlagAdapter flagAdapter;
    private DatabaseAdapter.MasterFlagAdapter masterFlagAdapter;

    private Chronometer setting_chronometer;
    private TextView sych_txt_msg;

    private Flag masterflag;
    private Flag flag;
    private ArrayList<Flag> masterflagArrayList;
    private ArrayList<Flag> flagArrayList;
    private String reason = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        reason = getIntent().getStringExtra("reason");
        InitView();
        InitSetting();
        MasterFlagTask();
    }

    private void InitSetting() {
        context = SynchronizationActivity.this;
        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        flagAdapter = databaseAdapter.new FlagAdapter();
        masterFlagAdapter = databaseAdapter.new MasterFlagAdapter();
    }

    private void InitView() {
        sych_txt_msg = (TextView) findViewById(R.id.txt_msg);
        setting_chronometer = (Chronometer) findViewById(R.id.setting_chronometer);
        setting_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                ShowMSG();
            }
        });
    }

    private void ShowMSG() {
        if (reason.equals("master data")) {
            masterflagArrayList = masterFlagAdapter.listLast();
            if (masterflagArrayList != null && masterflagArrayList.size() > 0) {
                if (masterflagArrayList.get(0).getFlag() == Constants.STOP_TASK) {
                    finish();
                    Toast.makeText(context, "Synchronizing Completed.", Toast.LENGTH_SHORT).show();
                } else {
                    sych_txt_msg.setText(masterflagArrayList.get(0).getMsg());
                }
            }
        } else {
            flagArrayList = flagAdapter.listLast();
            if (flagArrayList != null && flagArrayList.size() > 0) {
                if (flagArrayList.get(0).getFlag() == Constants.STOP_TASK) {
                    finish();
                    Toast.makeText(context, "Synchronizing Completed.", Toast.LENGTH_SHORT).show();
                } else {
                    sych_txt_msg.setText(flagArrayList.get(0).getMsg());
                }
            }
        }
    }

    private void MasterFlagTask() {
        if (reason.equals("master data")) {
            masterflag = masterFlagAdapter.listCurrent();
            masterflag.setFlag(Constants.EMR_TASK);
            masterflag.setMsg("Synchronization...");
            masterFlagAdapter.create(masterflag);
            SchedulerManager.getInstance().runNow(context, MasterTask.class, 1);
        } else {
            flag = flagAdapter.listCurrent();
            flag.setFlag(Constants.ONLINE_SYNC);
            flag.setMsg("Synchronization...");
            flagAdapter.create(flag);
            SchedulerManager.getInstance().runNow(context, SynchronizationTask.class, 1);
        }
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
}
