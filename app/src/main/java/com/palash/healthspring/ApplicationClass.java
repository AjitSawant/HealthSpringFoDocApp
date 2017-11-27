package com.palash.healthspring;

import android.app.Application;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.healthspring.task.MasterTask;
import com.palash.healthspring.task.SynchronizationTask;

/**
 * Created by Bhushan on 09-Aug-17.
 */
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

   /* private void initImage() {
        *//*------------------- code for downloading image -----------------------------------.*//*
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }*/

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