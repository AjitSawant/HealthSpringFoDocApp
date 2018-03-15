package com.palash.healthspringfoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palash.healthspringfoapp.R;
import com.palash.healthspringfoapp.database.DatabaseAdapter;
import com.palash.healthspringfoapp.database.DatabaseContract;
import com.palash.healthspringfoapp.entity.BookAppointment;
import com.palash.healthspringfoapp.entity.PatientConsole;
import com.palash.healthspringfoapp.utilities.Constants;
import com.palash.healthspringfoapp.utilities.LocalSetting;

import java.util.ArrayList;

public class PatientConsoleListAdapter extends BaseAdapter {

    private Context context;
    private LocalSetting localSetting;
    private LayoutInflater inflater;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.BookAppointmentAdapter bookAppointmentAdapter;

    private ArrayList<BookAppointment> bookAppointmentArrayList;
    private ArrayList<PatientConsole> patientConsoleArrayList;

    private BookAppointment bookAppointment;

    public PatientConsoleListAdapter(Context context, ArrayList<PatientConsole> patientConsoleArrayList) {
        this.context = context;
        localSetting = new LocalSetting();
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

            if (elPatientConsole.getFilePath() != null && elPatientConsole.getFilePath().length() > 0) {
                holder.row_patient_console_img_visit_document.setImageResource(R.drawable.ic_selected);
            } else {
                holder.row_patient_console_img_visit_document.setImageResource(R.drawable.ic_cancle);
            }

            holder.row_patient_console_layout_visit_prescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("called", "presc");
                    if (patientConsoleArrayList.get(position).getVisitID() != null && patientConsoleArrayList.get(position).getVisitID().length() > 0
                            && patientConsoleArrayList.get(position).getPrescription().equals("True")) {
                        String url = localSetting.returnPDFUrl("ConsolePrescription", bookAppointment.getDoctorUnitID(), bookAppointment.getPatientID(),
                                bookAppointment.getUnitID(), patientConsoleArrayList.get(position).getVisitID(), "", "", "", "");
                        //context.startActivity(new Intent(context, ViewPDFActivity.class).putExtra("url", url));
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                }
            });

            holder.row_patient_console_layout_visit_emr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (patientConsoleArrayList.get(position).getVisitID() != null && patientConsoleArrayList.get(position).getVisitID().length() > 0
                            && patientConsoleArrayList.get(position).getEMR().equals("True")) {
                        String url = localSetting.returnPDFUrl("Summary", bookAppointment.getDoctorUnitID(), bookAppointment.getPatientID(),
                                bookAppointment.getUnitID(), patientConsoleArrayList.get(position).getVisitID(), "", "", "", "");
                        //context.startActivity(new Intent(context, ViewPDFActivity.class).putExtra("url", url));
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                }
            });

            holder.row_patient_console_layout_visit_document.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (patientConsoleArrayList.get(position).getVisitID() != null && patientConsoleArrayList.get(position).getVisitID().length() > 0
                            && patientConsoleArrayList.get(position).getFilePath() != null && patientConsoleArrayList.get(position).getFilePath().length() > 0) {
                        String url = Constants.PATIENT_CONSOLE_DOCUMENT_FTP_PATH_URL + patientConsoleArrayList.get(position).getFilePath().replace("//", "/");
                        //context.startActivity(new Intent(context, ViewPDFActivity.class).putExtra("url", url));
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                        //context.startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(url), "vnd.android.cursor.dir/lysesoft.andftp.uri"));
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (patientConsoleArrayList.get(position).getVisitID() != null && patientConsoleArrayList.get(position).getVisitID().length() > 0) {
                      /*  String url = localSetting.returnPDFUrl("ConsolePrescription", bookAppointment.getDoctorUnitID(), bookAppointment.getPatientID(),
                                bookAppointment.getUnitID(), patientConsoleArrayList.get(position).getVisitID(),"","");
                        //context.startActivity(new Intent(context, ViewPDFActivity.class).putExtra("url", url));
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));*/
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



