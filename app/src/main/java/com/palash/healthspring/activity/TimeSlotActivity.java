package com.palash.healthspring.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.adapter.TimeSlotAdapter;
import com.palash.healthspring.api.JsonObjectMapper;
import com.palash.healthspring.api.WebServiceConsumer;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.entity.TimeSlot;
import com.palash.healthspring.utilities.Constants;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by manishas on 6/2/2016.
 */
public class TimeSlotActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;

    private BookAppointment bookAppointment;
    private TimeSlot timeSlot = null;
    private ArrayList<TimeSlot> timeSlotArrayList = null;
    private ArrayList<DoctorProfile> listProfile;
    private ArrayList<Date> dates = null;

    private TimeSlotAdapter timeSlotAdapter;

    private CalendarPickerView time_slot_calendar_view;
    private TextView time_slot_tv_empty;
    private GridView time_slot_schedule_list;

    private final Calendar nextThreeMonths = Calendar.getInstance();
    private final Calendar today = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);
        InitSetting();
        InitView();
    }

    private void InitSetting() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            context = this;
            localSetting = new LocalSetting();
            localSetting.Init(context);
            localSetting.Load();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            bookAppointment = new BookAppointment();
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();
            listProfile = doctorProfileAdapter.listAll();
            bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            timeSlot = new TimeSlot();
            time_slot_calendar_view = (CalendarPickerView) findViewById(R.id.time_slot_calendar_view);
            time_slot_schedule_list = (GridView) findViewById(R.id.time_slot_schedule_list);
            time_slot_tv_empty = (TextView) findViewById(R.id.time_slot_tv_empty);
            today.add(Calendar.DAY_OF_MONTH, 0);
            nextThreeMonths.add(Calendar.MONTH, 3);
            dates = new ArrayList<Date>();
            dates.add(today.getTime());
            time_slot_calendar_view.init(new Date(), nextThreeMonths.getTime()).withSelectedDates(dates);
            time_slot_calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                @Override
                public void onDateSelected(Date date) {
                    GetTimeSlot(date);
                }

                @Override
                public void onDateUnselected(Date date) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindView(final ArrayList<TimeSlot> listData) {
        try {
            listData.remove(listData.size() - 1);
            if (listData != null && listData.size() > 0) {
                timeSlotAdapter = new TimeSlotAdapter(context, listData);
                time_slot_schedule_list.setAdapter(timeSlotAdapter);
                time_slot_schedule_list.setVisibility(View.VISIBLE);
                time_slot_tv_empty.setVisibility(View.GONE);
                time_slot_schedule_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        bookAppointment.setFromTime(listData.get(position).getFromSlot());
                        bookAppointment.setToTime(listData.get(position).getToSlot());
                        bookAppointment.setAppointmentDate(
                                new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT,
                                        Locale.getDefault()).format(time_slot_calendar_view.getSelectedDate()));
                        bookAppointmentAdapter.updateTimeSlot(bookAppointment);
                        if (localSetting.Activityname.equals("PatientList")) {
                            localSetting.Activityname = "PatientList";
                            localSetting.Save();
                            Intent intent_appointment = new Intent(context, BookAppointmentActivity.class);
                            startActivity(intent_appointment);
                            intent_appointment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                        } else if (localSetting.Activityname.equals("RescheduleAppointment")) {
                            localSetting.Activityname = "RescheduleAppointment";
                            localSetting.Save();
                            Intent intent_appointment = new Intent(context, BookAppointmentActivity.class);
                            startActivity(intent_appointment);
                            intent_appointment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                        }
                    }
                });
            } else {
                time_slot_schedule_list.setVisibility(View.GONE);
                time_slot_tv_empty.setVisibility(View.VISIBLE);
            }
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

    @Override
    protected void onResume() {
        GetTimeSlot(new Date());
        super.onResume();
    }

    private void GetTimeSlot(Date date) {
        try {
            timeSlot.setDoctorId(bookAppointmentAdapter.listLast().get(0).getDoctorID());
            timeSlot.setUnitId(listProfile.get(0).getUnitID());
            timeSlot.setDateTime(new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT, Locale.getDefault()).format(date));
            new GetTimeSlotTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GetTimeSlotTask extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        private int responseCode = 0;
        private TransparentProgressDialog progressDialog = null;
        private String jSonData;
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                WebServiceConsumer serviceConsumer = new WebServiceConsumer(context, null, null);
                objectMapper = new JsonObjectMapper();
                jSonData = objectMapper.unMap(timeSlot);
                Response response = serviceConsumer.POST(Constants.TIME_SLOT_URL, jSonData);
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
                    objectMapper = new JsonObjectMapper();
                    timeSlotArrayList = objectMapper.map(responseString, TimeSlot.class);
                    bindView(timeSlotArrayList);
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
