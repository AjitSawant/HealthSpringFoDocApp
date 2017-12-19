package com.palash.healthspring.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspring.R;
import com.palash.healthspring.database.DatabaseAdapter;
import com.palash.healthspring.database.DatabaseContract;
import com.palash.healthspring.entity.Vital;
import com.palash.healthspring.entity.VitalsList;
import com.palash.healthspring.utilities.LocalSetting;

import java.util.ArrayList;

public class VitalsListUpdateAdapter extends BaseAdapter {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.VitalAdapter vitalAdapterDB;
    //private DatabaseAdapter.VitalsListAdapter vitalListAdapterDB;
    private DatabaseAdapter.VitalsListLocalAdapter vitalsListLocalAdapterDB;

    private ArrayList<VitalsList> vitalsListArrayList;
    private ArrayList<Vital> vitalArrayList;

    private LayoutInflater inflater;
    private double minvalue = 0;
    private double maxvalue = 0;
    private double value = 0;
    private String VisitID;
    private String PatientID;

    public VitalsListUpdateAdapter(Context context, ArrayList<VitalsList> vitalsListArrayList, String PatientID, String VisitID) {
        this.context = context;
        this.VisitID = VisitID;
        this.PatientID = PatientID;
        this.vitalsListArrayList = vitalsListArrayList;

        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        vitalAdapterDB = databaseAdapter.new VitalAdapter();
        //vitalListAdapterDB = databaseAdapter.new VitalsListAdapter();
        vitalsListLocalAdapterDB = databaseAdapter.new VitalsListLocalAdapter();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vitalsListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return vitalsListArrayList.get(position);
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
                convertView = inflater.inflate(R.layout.row_vitals_update, null);
                holder.row_vitals_tv_name = (TextView) convertView.findViewById(R.id.row_vitals_tv_name);
                holder.row_vitals_edt_value = (EditText) convertView.findViewById(R.id.row_vitals_edt_value);
                holder.row_vitals_tv_unit = (TextView) convertView.findViewById(R.id.row_vitals_tv_unit);
                holder.row_vitals_tv_range = (TextView) convertView.findViewById(R.id.row_vitals_tv_range);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            VitalsList vitalsList = vitalsListArrayList.get(position);

            holder.row_vitals_tv_name.setText(vitalsList.getVitalsDecription());
            holder.row_vitals_tv_unit.setText(vitalsList.getUnit());
            holder.row_vitals_edt_value.setText(vitalsList.getValue());
            vitalArrayList = vitalAdapterDB.listAll(vitalsList.getVitalsDecription());
            if (vitalArrayList != null && vitalArrayList.size() > 0) {
                holder.row_vitals_tv_range.setText(vitalArrayList.get(0).getMinValue() + "-" + vitalArrayList.get(0).getMaxValue());
                try {
                    value = Double.parseDouble(vitalsListArrayList.get(position).getValue());
                    minvalue = Double.parseDouble(vitalArrayList.get(0).getMinValue());
                    maxvalue = Double.parseDouble(vitalArrayList.get(0).getMaxValue());
                    if (value > maxvalue || value < minvalue) {
                        holder.row_vitals_edt_value.setTextColor(Color.RED);
                    } else {
                        holder.row_vitals_edt_value.setTextColor(Color.GREEN);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            /*if (vitalsList.getDate() != null && vitalsList.getDate().length() > 0 && vitalsList.getTime() != null && vitalsList.getTime().length() > 0) {
                holder.row_vitals_list_tv_date.setText(localSetting.formatDate(vitalsList.getDate(), Constants.TIME_FORMAT,Constants.OFFLINE_DATE)
                        + " " + localSetting.formatDate(vitalsList.getTime(),Constants.TIME_FORMAT,Constants.OFFLINE_TIME));
            }*/

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* if (localSetting.fragment_name.equals("PatientQueue")) {
                        if (vitalsListArrayList.get(position).getID() != null && vitalsListArrayList.get(position).getID().length() > 0) {
                            vitalListAdapterDB.updateCurrentNotes(vitalsListArrayList.get(position).getID());
                        } else {
                            vitalListAdapterDB.updateUnSyncCurrentNotes(vitalsListArrayList.get(position).get_ID());
                        }
                        context.startActivity(new Intent(context, VitalsListAddUpdateActivity.class).putExtra("isUpdate", "Yes"));
                    }*/
                }
            });

            holder.row_vitals_edt_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vitalsListArrayList.get(position).getValue() != null && vitalsListArrayList.get(position).getValue().length() > 0) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_edit_vital_value);
                        dialog.show();

                        final EditText edt_vital_value = (EditText) dialog.findViewById(R.id.edt_vital_value);
                        TextView vitals_save_btn = (TextView) dialog.findViewById(R.id.vitals_save_btn);
                        edt_vital_value.setText(vitalsListArrayList.get(position).getValue());
                        edt_vital_value.setSelection(edt_vital_value.getText().length());
                        vitals_save_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (edt_vital_value.getText().toString().trim().length() > 0 && (!edt_vital_value.getText().toString().trim().equals("0"))) {
                                    vitalsListLocalAdapterDB.updateValue(PatientID, VisitID, vitalsListArrayList.get(position).getVitalID(), edt_vital_value.getText().toString());
                                    vitalsListArrayList = vitalsListLocalAdapterDB.listAll(PatientID, VisitID);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Enter Value", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        new AlertDialog
                                .Builder(context)
                                .setTitle(context.getResources().getString(R.string.app_name))
                                .setMessage("Do you really want to delete vitals ?")
                                .setCancelable(true)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (vitalsListArrayList.get(position).getVitalID() != null && vitalsListArrayList.get(position).getVitalID().length() > 0) {
                                            vitalsListLocalAdapterDB.DeleteVitals(PatientID, VisitID, vitalsListArrayList.get(position).getVitalID());
                                            vitalsListArrayList = vitalsListLocalAdapterDB.listAll(PatientID, VisitID);
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(R.mipmap.ic_launcher)
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView row_vitals_tv_name;
        //TextView row_vitals_list_tv_date;
        EditText row_vitals_edt_value;
        TextView row_vitals_tv_unit;
        TextView row_vitals_tv_range;
    }
}

