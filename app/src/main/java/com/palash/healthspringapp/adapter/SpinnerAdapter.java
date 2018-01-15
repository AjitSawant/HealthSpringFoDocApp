package com.palash.healthspringapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.entity.AppointmentReason;
import com.palash.healthspringapp.entity.BloodGroup;
import com.palash.healthspringapp.entity.Complaint;
import com.palash.healthspringapp.entity.DaignosisMaster;
import com.palash.healthspringapp.entity.DaignosisTypeMaster;
import com.palash.healthspringapp.entity.Department;
import com.palash.healthspringapp.entity.ELUnitMaster;
import com.palash.healthspringapp.entity.Gender;
import com.palash.healthspringapp.entity.MaritalStatus;
import com.palash.healthspringapp.entity.MedicienFrequency;
import com.palash.healthspringapp.entity.MedicienInstruction;
import com.palash.healthspringapp.entity.MedicienName;
import com.palash.healthspringapp.entity.MedicienRoute;
import com.palash.healthspringapp.entity.Prefix;
import com.palash.healthspringapp.entity.Priority;
import com.palash.healthspringapp.entity.ReferralDoctorPerService;
import com.palash.healthspringapp.entity.ServiceName;
import com.palash.healthspringapp.entity.Vital;

import java.util.ArrayList;

public class SpinnerAdapter {

    public static class GenderAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<Gender> genderslist; // Values to be displayed

        public GenderAdapter(Context context, ArrayList<Gender> genderslist) {
            mContext = context;
            this.genderslist = genderslist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return genderslist.size();
        }

        @Override
        public Gender getItem(int position) {
            return genderslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.row_txt_id.setText(genderslist.get(position).getID());
                holder.row_txt_description.setText(genderslist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class PerfixAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<Prefix> prefixArrayList; // Values to be displayed

        public PerfixAdapter(Context context, ArrayList<Prefix> prefixArrayList) {
            mContext = context;
            this.prefixArrayList = prefixArrayList;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return prefixArrayList.size();
        }

        @Override
        public Prefix getItem(int position) {
            return prefixArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(prefixArrayList.get(position).getID());
                holder.row_txt_description.setText(prefixArrayList.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class MaritalStatusAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<MaritalStatus> maritalStatuseslist; // Values to be displayed

        public MaritalStatusAdapter(Context context, ArrayList<MaritalStatus> maritalStatuseslist) {
            mContext = context;
            this.maritalStatuseslist = maritalStatuseslist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return maritalStatuseslist.size();
        }

        @Override
        public MaritalStatus getItem(int position) {
            return maritalStatuseslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(maritalStatuseslist.get(position).getID());
                holder.row_txt_description.setText(maritalStatuseslist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class AppointmentReasonAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<AppointmentReason> appointmentReasonlist; // Values to be displayed

        public AppointmentReasonAdapter(Context context, ArrayList<AppointmentReason> appointmentReasonlist) {
            mContext = context;
            this.appointmentReasonlist = appointmentReasonlist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return appointmentReasonlist.size();
        }

        @Override
        public AppointmentReason getItem(int position) {
            return appointmentReasonlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(appointmentReasonlist.get(position).getID());
                holder.row_txt_description.setText(appointmentReasonlist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class BloodGroupAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<BloodGroup> bloodGrouplist; // Values to be displayed

        public BloodGroupAdapter(Context context, ArrayList<BloodGroup> bloodGrouplist) {
            mContext = context;
            this.bloodGrouplist = bloodGrouplist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return bloodGrouplist.size();
        }

        @Override
        public BloodGroup getItem(int position) {
            return bloodGrouplist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(bloodGrouplist.get(position).getID());
                holder.row_txt_description.setText(bloodGrouplist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class UnitMasterListAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<ELUnitMaster> elUnitMasterList; // Values to be displayed

        public UnitMasterListAdapter(Context context, ArrayList<ELUnitMaster> elUnitMasterList) {
            mContext = context;
            this.elUnitMasterList = elUnitMasterList;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return elUnitMasterList.size();
        }

        @Override
        public ELUnitMaster getItem(int position) {
            return elUnitMasterList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview_unit_master, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(elUnitMasterList.get(position).getUnitID());
                holder.row_txt_description.setText(elUnitMasterList.get(position).getUnitDesc());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class DepartmentAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<Department> departmentlist; // Values to be displayed

        public DepartmentAdapter(Context context, ArrayList<Department> departmentlist) {
            mContext = context;
            this.departmentlist = departmentlist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return departmentlist.size();
        }

        @Override
        public Department getItem(int position) {
            return departmentlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(departmentlist.get(position).getID());
                holder.row_txt_description.setText(departmentlist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class ComplaintAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<Complaint> complaintlist; // Values to be displayed

        public ComplaintAdapter(Context context, ArrayList<Complaint> complaintlist) {
            mContext = context;
            this.complaintlist = complaintlist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return complaintlist.size();
        }

        @Override
        public Complaint getItem(int position) {
            return complaintlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(complaintlist.get(position).getID());
                holder.row_txt_description.setText(complaintlist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class MedicienNameAdapter extends BaseAdapter implements Filterable {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<MedicienName> medicienNamelist; // Values to be displayed
        private ArrayList<MedicienName> filterMedicienNameArrayList;

        public MedicienNameAdapter(Context context, ArrayList<MedicienName> medicienNamelist) {
            this.mContext = context;
            this.medicienNamelist = medicienNamelist;
            this.filterMedicienNameArrayList = medicienNamelist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return this.medicienNamelist.size();
        }

        @Override
        public String getItem(int position) {
            return medicienNamelist.get(position).getItemName();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                MedicienName medicienName = this.medicienNamelist.get(position);
                holder.row_txt_id.setText(medicienName.getID());
                holder.row_txt_description.setText(medicienName.getItemName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    medicienNamelist = (ArrayList<MedicienName>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<MedicienName> FilteredArrList = new ArrayList<MedicienName>();
                    if (filterMedicienNameArrayList == null) {
                        filterMedicienNameArrayList = new ArrayList<MedicienName>(medicienNamelist);
                    }

                    if (constraint == null || constraint.length() == 0) {
                        results.count = filterMedicienNameArrayList.size();
                        results.values = filterMedicienNameArrayList;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < filterMedicienNameArrayList.size(); i++) {
                            String data = filterMedicienNameArrayList.get(i).getItemName();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(filterMedicienNameArrayList.get(i));
                            }
                        }
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }

    public static class MedicienRouteAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<MedicienRoute> medicienRoutelist; // Values to be displayed

        public MedicienRouteAdapter(Context context, ArrayList<MedicienRoute> medicienRoutelist) {
            mContext = context;
            this.medicienRoutelist = medicienRoutelist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return medicienRoutelist.size();
        }

        @Override
        public MedicienRoute getItem(int position) {
            return medicienRoutelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.row_txt_id.setText(medicienRoutelist.get(position).getID());
                holder.row_txt_description.setText(medicienRoutelist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class MedicienFrequencyAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<MedicienFrequency> medicienFrequencylist; // Values to be displayed

        public MedicienFrequencyAdapter(Context context, ArrayList<MedicienFrequency> medicienFrequencylist) {
            mContext = context;
            this.medicienFrequencylist = medicienFrequencylist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return medicienFrequencylist.size();
        }

        @Override
        public MedicienFrequency getItem(int position) {
            return medicienFrequencylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(medicienFrequencylist.get(position).getID());
                holder.row_txt_description.setText(medicienFrequencylist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class MedicienInstructionAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<MedicienInstruction> medicienInstructionlist; // Values to be displayed

        public MedicienInstructionAdapter(Context context, ArrayList<MedicienInstruction> medicienInstructionlist) {
            mContext = context;
            this.medicienInstructionlist = medicienInstructionlist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return medicienInstructionlist.size();
        }

        @Override
        public MedicienInstruction getItem(int position) {
            return medicienInstructionlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(medicienInstructionlist.get(position).getID());
                holder.row_txt_description.setText(medicienInstructionlist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class DaignosisTypeMasterAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<DaignosisTypeMaster> daignosisTypeMasterlist; // Values to be displayed

        public DaignosisTypeMasterAdapter(Context context, ArrayList<DaignosisTypeMaster> daignosisTypeMasterlist) {
            mContext = context;
            this.daignosisTypeMasterlist = daignosisTypeMasterlist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return daignosisTypeMasterlist.size();
        }

        @Override
        public DaignosisTypeMaster getItem(int position) {
            return daignosisTypeMasterlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(daignosisTypeMasterlist.get(position).getID());
                holder.row_txt_description.setText(daignosisTypeMasterlist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class DaignosisMasterAdapter extends BaseAdapter implements Filterable {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<DaignosisMaster> daignosisMasterlist; // Values to be displayed
        private ArrayList<DaignosisMaster> filterDaignosisMasterArrayList;

        public DaignosisMasterAdapter(Context context, ArrayList<DaignosisMaster> daignosisMasterlist) {
            this.mContext = context;
            this.daignosisMasterlist = daignosisMasterlist;
            this.filterDaignosisMasterArrayList = daignosisMasterlist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return this.daignosisMasterlist.size();
        }

        @Override
        public String getItem(int position) {
            return daignosisMasterlist.get(position).getDiagnosis();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                DaignosisMaster daignosisMaster = this.daignosisMasterlist.get(position);
                holder.row_txt_id.setText(daignosisMaster.getID());
                holder.row_txt_description.setText(daignosisMaster.getDiagnosis());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    daignosisMasterlist = (ArrayList<DaignosisMaster>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<DaignosisMaster> FilteredArrList = new ArrayList<DaignosisMaster>();
                    if (filterDaignosisMasterArrayList == null) {
                        filterDaignosisMasterArrayList = new ArrayList<DaignosisMaster>(daignosisMasterlist);
                    }

                    if (constraint == null || constraint.length() == 0) {
                        // set the Original result to return
                        results.count = filterDaignosisMasterArrayList.size();
                        results.values = filterDaignosisMasterArrayList;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < filterDaignosisMasterArrayList.size(); i++) {
                            String data = filterDaignosisMasterArrayList.get(i).getDiagnosis();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(filterDaignosisMasterArrayList.get(i));
                            }
                        }
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }

    public static class VitalAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<Vital> vitallist; // Values to be displayed

        public VitalAdapter(Context context, ArrayList<Vital> vitallist) {
            mContext = context;
            this.vitallist = vitallist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return vitallist.size();
        }

        @Override
        public Vital getItem(int position) {
            return vitallist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview_unit_master, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(vitallist.get(position).getID());
                holder.row_txt_description.setText(vitallist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class ServiceNameAdapter extends BaseAdapter implements Filterable {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<ServiceName> serviceNamelist; // Values to be displayed
        private ArrayList<ServiceName> filterServiceNameArrayList;

        public ServiceNameAdapter(Context context, ArrayList<ServiceName> serviceNamelist) {
            this.mContext = context;
            this.serviceNamelist = serviceNamelist;
            this.filterServiceNameArrayList = serviceNamelist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return this.serviceNamelist.size();
        }

        @Override
        public String getItem(int position) {
            return serviceNamelist.get(position).getDescription();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                ServiceName serviceName = this.serviceNamelist.get(position);
                holder.row_txt_id.setText(serviceName.getID());
                holder.row_txt_description.setText(serviceName.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    serviceNamelist = (ArrayList<ServiceName>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<ServiceName> FilteredArrList = new ArrayList<ServiceName>();
                    if (filterServiceNameArrayList == null) {
                        filterServiceNameArrayList = new ArrayList<ServiceName>(serviceNamelist);
                    }

                    if (constraint == null || constraint.length() == 0) {
                        results.count = filterServiceNameArrayList.size();
                        results.values = filterServiceNameArrayList;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < filterServiceNameArrayList.size(); i++) {
                            String data = filterServiceNameArrayList.get(i).getDescription();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(filterServiceNameArrayList.get(i));
                            }
                        }
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }

    public static class PriorityAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        private ArrayList<Priority> prioritylist; // Values to be displayed

        public PriorityAdapter(Context context, ArrayList<Priority> prioritylist) {
            mContext = context;
            this.prioritylist = prioritylist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return prioritylist.size();
        }

        @Override
        public Priority getItem(int position) {
            return prioritylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(prioritylist.get(position).getID());
                holder.row_txt_description.setText(prioritylist.get(position).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }

    public static class ReferralDoctorPerServiceAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;
        private ArrayList<ReferralDoctorPerService> referralDoctorPerServiceArrayList; // Values to be displayed

        public ReferralDoctorPerServiceAdapter(Context context, ArrayList<ReferralDoctorPerService> referralDoctorPerServiceArrayList) {
            mContext = context;
            this.referralDoctorPerServiceArrayList = referralDoctorPerServiceArrayList;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return referralDoctorPerServiceArrayList.size();
        }

        @Override
        public ReferralDoctorPerService getItem(int position) {
            return referralDoctorPerServiceArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.row_spinner_textview, null);
                    holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                    holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.row_txt_id.setText(referralDoctorPerServiceArrayList.get(position).getReferralDoctorID());
                holder.row_txt_description.setText(referralDoctorPerServiceArrayList.get(position).getReferralDoctorName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private class ViewHolder {
            TextView row_txt_id;
            TextView row_txt_description;
        }
    }
}



