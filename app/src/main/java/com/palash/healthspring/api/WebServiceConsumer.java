package com.palash.healthspring.api;


import android.content.Context;
import android.util.Log;

import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class WebServiceConsumer {
    private LocalSetting localSettings;
    private OkHttpClient client;
    private Context context;
    private String userLoginID;
    private String userPassword;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private ArrayList<DoctorProfile> listProfile;

    public WebServiceConsumer(Context contxt, String usrID, String pssword) {
        context = contxt;
        localSettings = new LocalSetting();
        client = new OkHttpClient();
        doctorProfileAdapter = new DatabaseAdapter(new DatabaseContract(contxt)).new DoctorProfileAdapter();
        if (usrID != null && pssword != null) {
            userLoginID = usrID;
            userPassword = pssword;
            userLoginID = localSettings.encodeString(userLoginID);
            userPassword = localSettings.encodeString(userPassword);
        } else {
            listProfile = doctorProfileAdapter.listAll();
            if (listProfile != null && listProfile.size() > 0) {
                userLoginID = listProfile.get(0).getLoginName();
                userPassword = listProfile.get(0).getPassword();
            }
        }
    }

    public Response POST(String url, String jSon) {
        Response response = null;
        try {
            RequestBody formBody = new FormEncodingBuilder().add(
                    Constants.KEY_REQUEST_DATA, jSon).build();
            Request request = new Request.Builder().url(url).post(formBody)
                    .addHeader("username", userLoginID)
                    .addHeader("password", userPassword).build();
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + userLoginID);
            Log.d(Constants.TAG, "Password:" + userPassword);
            Log.d(Constants.TAG, "Json:" + jSon);
            response = client.newCall(request).execute();
            //Log.d(Constants.TAG, "Response:" + response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response GET(String url) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .cacheControl(new CacheControl.Builder().noCache().build())
                    .url(url).get().addHeader("username", userLoginID)
                    .addHeader("password", userPassword).build();
            System.setProperty("http.keepAlive", "false");
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + userLoginID);
            Log.d(Constants.TAG, "Password:" + userPassword);
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response PUT(String url, String jSon) {
        Response response = null;
        try {
            RequestBody formBody = new FormEncodingBuilder().add(
                    Constants.KEY_REQUEST_DATA, jSon).build();
            Request request = new Request.Builder().url(url).put(formBody)
                    .addHeader("username", userLoginID)
                    .addHeader("password", userPassword).build();
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + userLoginID);
            Log.d(Constants.TAG, "Password:" + userPassword);
            Log.d(Constants.TAG, "Json:" + jSon);
            response = client.newCall(request).execute();
            //Log.d(Constants.TAG, "Response:" + response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response DELETE(String url) {
        Response response = null;
        try {
            Request request = new Request.Builder().url(url).delete()
                    .addHeader("username", userLoginID)
                    .addHeader("password", userPassword).build();
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + userLoginID);
            Log.d(Constants.TAG, "Password:" + userPassword);
            response = client.newCall(request).execute();
            //Log.d(Constants.TAG, "Response:" + response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
