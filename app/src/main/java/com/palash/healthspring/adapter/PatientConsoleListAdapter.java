package com.palash.healthspring.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palash.healthspring.R;
import com.palash.healthspring.activity.ViewPDFActivity;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.BookAppointment;
import com.palash.healthspring.entity.PatientConsole;

import java.util.ArrayList;

public class PatientConsoleListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;

    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<PatientConsole> patientConsoleArrayList;

    private BookAppointment bookAppointment;

    public PatientConsoleListAdapter(Context context, ArrayList<PatientConsole> patientConsoleArrayList) {
        this.context = context;
        this.patientConsoleArrayList = patientConsoleArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        bookAppointmentAdapter = databaseAdapter.new BookAppointmentAdapter();

        bookAppointmentArrayList = bookAppointmentAdapter.listAll();
        bookAppointment = new BookAppointment();
        if (bookAppointmentArrayList != null && bookAppointmentArrayList.size() > 0) {
            bookAppointment = bookAppointmentArrayList.get(0);
        }
    }

    @Override
    public int getCount() {
        return patientConsoleArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientConsoleArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_patient_console_list_adapter, null);
                holder.row_patient_console_tv_visit_date = (TextView) convertView.findViewById(R.id.row_patient_console_tv_visit_date);
                holder.row_patient_console_tv_visit_type = (TextView) convertView.findViewById(R.id.row_patient_console_tv_visit_type);
                holder.row_patient_console_tv_clinic_name = (TextView) convertView.findViewById(R.id.row_patient_console_tv_clinic_name);
                holder.row_patient_console_tv_doctor_name = (TextView) convertView.findViewById(R.id.row_patient_console_tv_doctor_name);
                holder.row_patient_console_img_visit_prescription = (ImageView) convertView.findViewById(R.id.row_patient_console_img_visit_prescription);
                holder.row_patient_console_img_visit_radiology = (ImageView) convertView.findViewById(R.id.row_patient_console_img_visit_radiology);
                holder.row_patient_console_img_visit_pathology = (ImageView) convertView.findViewById(R.id.row_patient_console_img_visit_pathology);
                holder.row_patient_console_img_visit_SuggestedService = (ImageView) convertView.findViewById(R.id.row_patient_console_img_visit_SuggestedService);
                holder.row_patient_console_img_visit_referral = (ImageView) convertView.findViewById(R.id.row_patient_console_img_visit_referral);
                holder.row_patient_console_img_visit_emr = (ImageView) convertView.findViewById(R.id.row_patient_console_img_visit_emr);
                holder.row_patient_console_img_visit_document = (ImageView) convertView.findViewById(R.id.row_patient_console_img_visit_document);

                holder.row_patient_console_layout_visit_prescription = (LinearLayout) convertView.findViewById(R.id.row_patient_console_layout_visit_prescription);
                holder.row_patient_console_layout_visit_radiology = (LinearLayout) convertView.findViewById(R.id.row_patient_console_layout_visit_radiology);
                holder.row_patient_console_layout_visit_pathology = (LinearLayout) convertView.findViewById(R.id.row_patient_console_layout_visit_pathology);
                holder.row_patient_console_layout_visit_SuggestedService = (LinearLayout) convertView.findViewById(R.id.row_patient_console_layout_visit_SuggestedService);
                holder.row_patient_console_layout_visit_referral = (LinearLayout) convertView.findViewById(R.id.row_patient_console_layout_visit_referral);
                holder.row_patient_console_layout_visit_emr = (LinearLayout) convertView.findViewById(R.id.row_patient_console_layout_visit_emr);
                holder.row_patient_console_layout_visit_document = (LinearLayout) convertView.findViewById(R.id.row_patient_console_layout_visit_document);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PatientConsole elPatientConsole = patientConsoleArrayList.get(position);

            if (elPatientConsole.getVisitDate() != null && elPatientConsole.getVisitDate().length() > 0) {
                holder.row_patient_console_tv_visit_date.setText(elPatientConsole.getVisitDate());
            } else {
                holder.row_patient_console_tv_visit_date.setText("");
            }

            if (elPatientConsole.getVisitType() != null && elPatientConsole.getVisitType().length() > 0) {
                holder.row_patient_console_tv_visit_type.setText(elPatientConsole.getVisitType());
            } else {
                holder.row_patient_console_tv_visit_type.setText("");
            }

            if (elPatientConsole.getClinic() != null && elPatientConsole.getClinic().length() > 0) {
                holder.row_patient_console_tv_clinic_name.setText(elPatientConsole.getClinic());
            } else {
                holder.row_patient_console_tv_clinic_name.setText("");
            }

            if (elPatientConsole.getVisitDoctor() != null && elPatientConsole.getVisitDoctor().length() > 0) {
                holder.row_patient_console_tv_doctor_name.setText(elPatientConsole.getVisitDoctor());
            } else {
                holder.row_patient_console_tv_doctor_name.setText("");
            }

            if (elPatientConsole.getPrescription() != null && elPatientConsole.getPrescription().length() > 0 && elPatientConsole.getPrescription().equals("True")) {
                holder.row_patient_console_img_visit_prescription.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_prescription.setImageResource(R.drawable.ic_cancle);
            }

            if (elPatientConsole.getRadiology() != null && elPatientConsole.getRadiology().length() > 0 && elPatientConsole.getRadiology().equals("True")) {
                holder.row_patient_console_img_visit_radiology.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_radiology.setImageResource(R.drawable.ic_cancle);
            }

            if (elPatientConsole.getPathology() != null && elPatientConsole.getPathology().length() > 0 && elPatientConsole.getPathology().equals("True")) {
                holder.row_patient_console_img_visit_pathology.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_pathology.setImageResource(R.drawable.ic_cancle);
            }

            if (elPatientConsole.getSuggestedService() != null && elPatientConsole.getSuggestedService().length() > 0 && elPatientConsole.getSuggestedService().equals("True")) {
                holder.row_patient_console_img_visit_SuggestedService.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_SuggestedService.setImageResource(R.drawable.ic_cancle);
            }

            if (elPatientConsole.getReferal() != null && elPatientConsole.getReferal().length() > 0 && elPatientConsole.getReferal().equals("True")) {
                holder.row_patient_console_img_visit_referral.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_referral.setImageResource(R.drawable.ic_cancle);
            }

            if (elPatientConsole.getEMR() != null && elPatientConsole.getEMR().length() > 0 && elPatientConsole.getEMR().equals("True")) {
                holder.row_patient_console_img_visit_emr.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_emr.setImageResource(R.drawable.ic_cancle);
            }

            if (elPatientConsole.getAttachment() != null && elPatientConsole.getAttachment().length() > 0 && elPatientConsole.getAttachment().equals("True")) {
                holder.row_patient_console_img_visit_document.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_document.setImageResource(R.drawable.ic_cancle);
            }

            holder.row_patient_console_layout_visit_prescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (patientConsoleArrayList.get(position) != null && patientConsoleArrayList.get(position).getVisitID() != null && patientConsoleArrayList.get(position).getVisitID().length() > 0) {
                        String url = "https://192.168.1.70/HealthSpringAndroid/Reports/Patient/EMRPatientPrescription.aspx?UnitID=" + bookAppointment.getDoctorUnitID()
                                + "&VisitID=" + patientConsoleArrayList.get(position).getVisitID() + "&PatientID=" + bookAppointment.getPatientID() + "&PatientUnitID=" + bookAppointment.getUnitID()
                                + "&TemplateID=0&UserID=0";

                        //context.startActivity(new Intent(context, ViewPDFActivity.class).putExtra("url", url));
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView row_patient_console_tv_visit_date;
        TextView row_patient_console_tv_visit_type;
        TextView row_patient_console_tv_clinic_name;
        TextView row_patient_console_tv_doctor_name;
        ImageView row_patient_console_img_visit_prescription;
        ImageView row_patient_console_img_visit_radiology;
        ImageView row_patient_console_img_visit_pathology;
        ImageView row_patient_console_img_visit_SuggestedService;
        ImageView row_patient_console_img_visit_referral;
        ImageView row_patient_console_img_visit_emr;
        ImageView row_patient_console_img_visit_document;

        LinearLayout row_patient_console_layout_visit_prescription;
        LinearLayout row_patient_console_layout_visit_radiology;
        LinearLayout row_patient_console_layout_visit_pathology;
        LinearLayout row_patient_console_layout_visit_SuggestedService;
        LinearLayout row_patient_console_layout_visit_referral;
        LinearLayout row_patient_console_layout_visit_emr;
        LinearLayout row_patient_console_layout_visit_document;
    }
}



