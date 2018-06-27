package com.palash.healthspringfoapp;

import android.app.Application;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspringfoapp.task.MasterTask;
import com.palash.healthspringfoapp.task.SynchronizationTask;

public class ApplicationClass extends Application {

    private static ApplicationClass context;
    //private static DisplayImageOptions options;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //initImage();
        InitScheduler();
        InitMasterTask();

        /*options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.image_not_available)
                .showImageOnFail(R.drawable.image_not_available)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();*/
    }

    private void InitScheduler() {
        try {
            SchedulerManager.getInstance().saveTask(context, "*/1 * * * *", SynchronizationTask.class);
            SchedulerManager.getInstance().restart(this, SynchronizationTask.class);
            SchedulerManager.getInstance().runNow(this, SynchronizationTask.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitMasterTask() {
        try {
            SchedulerManager.getInstance().saveTask(context, "*/1 * * * *", MasterTask.class);
            SchedulerManager.getInstance().restart(this, MasterTask.class);
            SchedulerManager.getInstance().runNow(this, MasterTask.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
