package com.palash.healthspring.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.DoctorProfile;
import com.palash.healthspring.utilities.LocalSetting;
import com.palash.healthspring.utilities.TextDrawable;
import com.palash.healthspring.utilities.Validate;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.DoctorProfileAdapter doctorProfileAdapter;
    private ArrayList<DoctorProfile> doctorprofilelist = null;

    private DoctorProfile elDoctorProfile;

    private MenuItem editItem, saveItem, cancelItem;
    private TextView txt_profile_name;
    private TextView txt_profile_edu;
    private EditText edt_profile_fname;
    private EditText edt_profile_mname;
    private EditText edt_profile_lname;
    private EditText edt_profile_dob;
    private EditText edt_profile_gender;
    private EditText edt_profile_specialization;
    private EditText edt_profile_education;
    private EditText edt_profile_email;
    private EditText edt_profile_mobile;
    private MaterialSpinner spinner_profile_gender;
    private ImageView profile_image;
    private String[] Gender_ITEMS = {"Select Gender", "Male", "Female", "other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        InitSetting();
        InitView();
        disableEditMode();
    }

    private void InitSetting() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            context = ProfileActivity.this;
            localSetting = new LocalSetting();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            doctorProfileAdapter = databaseAdapter.new DoctorProfileAdapter();

            elDoctorProfile = new DoctorProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InitView() {
        txt_profile_name = (TextView) findViewById(R.id.profile_txt_name);
        txt_profile_edu = (TextView) findViewById(R.id.profile_txt_edu);
        edt_profile_fname = (EditText) findViewById(R.id.profile_edt_fname);
        edt_profile_mname = (EditText) findViewById(R.id.profile_edt_mname);
        edt_profile_lname = (EditText) findViewById(R.id.profile_edt_lname);
        edt_profile_dob = (EditText) findViewById(R.id.profile_edt_dob);
        edt_profile_gender = (EditText) findViewById(R.id.profile_edt_gender);
        edt_profile_specialization = (EditText) findViewById(R.id.profile_edt_specialization);
        edt_profile_education = (EditText) findViewById(R.id.profile_edt_education);
        edt_profile_email = (EditText) findViewById(R.id.profile_edt_email);
        edt_profile_mobile = (EditText) findViewById(R.id.profile_edt_mobile);
        profile_image = (ImageView) findViewById(R.id.profile_image);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_spinner_textview, Gender_ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_profile_gender = (MaterialSpinner) findViewById(R.id.spinner_profile_gender);
        spinner_profile_gender.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadVisit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        editItem = (MenuItem) menu.findItem(R.id.menu_profile_edit);
        saveItem = (MenuItem) menu.findItem(R.id.menu_profile_save);
        cancelItem = (MenuItem) menu.findItem(R.id.menu_profile_cancle);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile_save:
                if (validateControls()) {
                    disableEditMode();
                }
                return true;
            case R.id.menu_profile_edit:
                enableEditMode();
                return true;
            case R.id.menu_profile_cancle:
                LoadVisit();
                disableEditMode();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void disableEditMode() {
        if (editItem != null) {
            editItem.setVisible(true);
            saveItem.setVisible(false);
            cancelItem.setVisible(false);
        }
        edt_profile_fname.setEnabled(false);
        edt_profile_mname.setEnabled(false);
        edt_profile_lname.setEnabled(false);
        edt_profile_dob.setEnabled(false);
        edt_profile_gender.setEnabled(false);
        spinner_profile_gender.setEnabled(false);
        edt_profile_email.setEnabled(false);
        edt_profile_mobile.setEnabled(false);
        edt_profile_specialization.setEnabled(false);
        edt_profile_education.setEnabled(false);
        profile_image.setClickable(false);
    }

    private void enableEditMode() {
        if (editItem != null) {
            editItem.setVisible(false);
            saveItem.setVisible(true);
            cancelItem.setVisible(true);
        }
        edt_profile_fname.setEnabled(true);
        edt_profile_mname.setEnabled(true);
        edt_profile_lname.setEnabled(true);
        edt_profile_education.setEnabled(true);
        edt_profile_mobile.setEnabled(true);
        edt_profile_email.setEnabled(true);
    }

    private void LoadVisit() {
        try {
            doctorprofilelist = doctorProfileAdapter.listAll();
            if (doctorprofilelist != null && doctorprofilelist.size() > 0) {
                elDoctorProfile = doctorprofilelist.get(0);

                if (elDoctorProfile.getFirstName() != null && elDoctorProfile.getFirstName().trim().length() > 0) {
                    TextDrawable drawable = TextDrawable.builder().buildRound(
                            elDoctorProfile.getFirstName().charAt(0) + "".toString()
                                    + elDoctorProfile.getLastName().charAt(0)
                                    + "".toString(), Color.parseColor("#039BE5"));
                    profile_image.setImageDrawable(drawable);

                    edt_profile_fname.setText(elDoctorProfile.getFirstName().trim());
                    edt_profile_fname.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_fname.setVisibility(View.GONE);
                }

                if (elDoctorProfile.getMiddleName() != null && elDoctorProfile.getMiddleName().trim().length() > 0) {
                    edt_profile_mname.setText(elDoctorProfile.getMiddleName().toString().trim());
                    edt_profile_mname.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_mname.setVisibility(View.GONE);
                }

                if (elDoctorProfile.getLastName() != null && elDoctorProfile.getLastName().trim().length() > 0) {
                    edt_profile_lname.setText(elDoctorProfile.getLastName().toString().trim());
                    edt_profile_lname.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_lname.setVisibility(View.GONE);
                }

                if (elDoctorProfile.getDOB() != null && elDoctorProfile.getDOB().trim().length() > 0) {
                    edt_profile_dob.setText(elDoctorProfile.getDOB().toString().trim());
                } else {
                    edt_profile_dob.setText("N/A");
                }

                if (elDoctorProfile.getGender() != null && elDoctorProfile.getGender().trim().length() > 0) {
                    edt_profile_gender.setText(elDoctorProfile.getGender().toString().trim());
                    edt_profile_gender.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_gender.setVisibility(View.GONE);
                }

                if (elDoctorProfile.getSpecialization() != null && elDoctorProfile.getSpecialization().trim().length() > 0) {
                    edt_profile_specialization.setText(elDoctorProfile.getSpecialization().toString().trim());
                    edt_profile_specialization.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_specialization.setVisibility(View.GONE);
                }

                if (elDoctorProfile.getEducation() != null && elDoctorProfile.getEducation().trim().length() > 0) {
                    edt_profile_education.setText(elDoctorProfile.getEducation().toString().trim());
                    edt_profile_education.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_education.setVisibility(View.GONE);
                }

                if (elDoctorProfile.getPFNumber() != null && elDoctorProfile.getPFNumber().trim().length() > 0) {
                    edt_profile_mobile.setText(elDoctorProfile.getPFNumber().toString().trim());
                    edt_profile_mobile.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_mobile.setVisibility(View.VISIBLE);
                    edt_profile_mobile.setText("N/A");
                }

                if (elDoctorProfile.getEmailId() != null && elDoctorProfile.getEmailId().trim().length() > 0) {
                    edt_profile_email.setText(elDoctorProfile.getEmailId().toString().trim());
                    edt_profile_email.setVisibility(View.VISIBLE);
                } else {
                    edt_profile_email.setVisibility(View.VISIBLE);
                    edt_profile_email.setText("N/A");
                }

                txt_profile_name.setText("Dr." + edt_profile_fname.getText().toString().trim() +
                        " " + edt_profile_mname.getText().toString().trim() +
                        " " + edt_profile_lname.getText().toString().trim());
                txt_profile_edu.setText(elDoctorProfile.getEducation().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateControls() {
        if (!Validate.hasTextInEditText(edt_profile_fname))
            return false;
        else if (!Validate.hasTextInEditText(edt_profile_lname))
            return false;
        else if (!Validate.hasTextInEditText(edt_profile_email))
            return false;
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

