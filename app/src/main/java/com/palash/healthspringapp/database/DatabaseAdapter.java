package com.palash.healthspringapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.palash.healthspringapp.entity.Appointment;
import com.palash.healthspringapp.entity.AppointmentReason;
import com.palash.healthspringapp.entity.BloodGroup;
import com.palash.healthspringapp.entity.BookAppointment;
import com.palash.healthspringapp.entity.CPOEPrescription;
import com.palash.healthspringapp.entity.CPOEService;
import com.palash.healthspringapp.entity.Complaint;
import com.palash.healthspringapp.entity.ComplaintsList;
import com.palash.healthspringapp.entity.DaignosisTypeMaster;
import com.palash.healthspringapp.entity.Department;
import com.palash.healthspringapp.entity.DiagnosisList;
import com.palash.healthspringapp.entity.DoctorProfile;
import com.palash.healthspringapp.entity.DoctorType;
import com.palash.healthspringapp.entity.ELCityMaster;
import com.palash.healthspringapp.entity.ELCompanyName;
import com.palash.healthspringapp.entity.ELCountryMaster;
import com.palash.healthspringapp.entity.ELDoctorMaster;
import com.palash.healthspringapp.entity.ELFollowUp;
import com.palash.healthspringapp.entity.ELHealthspringReferral;
import com.palash.healthspringapp.entity.ELPatientCategory;
import com.palash.healthspringapp.entity.ELRegionMaster;
import com.palash.healthspringapp.entity.ELStateMaster;
import com.palash.healthspringapp.entity.ELSynchOfflineData;
import com.palash.healthspringapp.entity.ELUnitMaster;
import com.palash.healthspringapp.entity.ELVisitType;
import com.palash.healthspringapp.entity.Flag;
import com.palash.healthspringapp.entity.Gender;
import com.palash.healthspringapp.entity.MaritalStatus;
import com.palash.healthspringapp.entity.MedicienFrequency;
import com.palash.healthspringapp.entity.MedicienInstruction;
import com.palash.healthspringapp.entity.MedicienRoute;
import com.palash.healthspringapp.entity.Patient;
import com.palash.healthspringapp.entity.PatientQueue;
import com.palash.healthspringapp.entity.Prefix;
import com.palash.healthspringapp.entity.Priority;
import com.palash.healthspringapp.entity.ReferralDoctorPerService;
import com.palash.healthspringapp.entity.Specialization;
import com.palash.healthspringapp.entity.VisitList;
import com.palash.healthspringapp.entity.Vital;
import com.palash.healthspringapp.entity.VitalsList;
import com.palash.healthspringapp.utilities.Constants;
import com.palash.healthspringapp.utilities.LocalSetting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseAdapter {

    private LocalSetting localSetting;
    private DatabaseContract databaseContract = null;

    public DatabaseAdapter(DatabaseContract contract) {
        localSetting = new LocalSetting();
        databaseContract = contract;
    }

    public void close() {
        if (databaseContract != null) {
            databaseContract.close();
        }
    }

    public class FlagAdapter {

        String[] projection = {
                DatabaseContract.Flag._ID,
                DatabaseContract.Flag.COLUMN_NAME_FLAG,
                DatabaseContract.Flag.COLUMN_NAME_MSG
        };

        private ArrayList<Flag> CursorToFlagList(Cursor result) {
            ArrayList<Flag> flagArrayList = null;
            try {
                if (result != null) {
                    flagArrayList = new ArrayList<Flag>();
                    while (result.moveToNext()) {
                        Flag flag = new Flag();
                        flag.setID(result.getString((result.getColumnIndex(DatabaseContract.Flag._ID))));
                        flag.setMsg(result.getString((result.getColumnIndex(DatabaseContract.Flag.COLUMN_NAME_MSG))));
                        flag.setFlag(result.getInt(result.getColumnIndex(DatabaseContract.Flag.COLUMN_NAME_FLAG)));
                        flagArrayList.add(flag);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flagArrayList;
        }

        private ContentValues FlagToContentValues(Flag flag) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Flag._ID, flag.getID());
                values.put(DatabaseContract.Flag.COLUMN_NAME_FLAG, flag.getFlag());
                values.put(DatabaseContract.Flag.COLUMN_NAME_MSG, flag.getMsg());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        public long create(Flag flag) {
            long rowId = -1;
            try {
                ContentValues values = FlagToContentValues(flag);
                if (values != null) {
                    if (Count(flag.getID()) == 1) {
                        String whereClause = DatabaseContract.Flag._ID + " = '" + flag.getID() + "'";
                        rowId = databaseContract.open().update(DatabaseContract.Flag.TABLE_NAME, values, whereClause, null);
                    } else {
                        if (flag.getDateTime() != null && flag.getDateTime().length() > 0) {
                            rowId = databaseContract.open().insert(DatabaseContract.Flag.TABLE_NAME, null, values);
                        } else {
                            values.put(DatabaseContract.Flag.COLUMN_NAME_FLAG, flag.getFlag());
                            rowId = databaseContract.open().insert(DatabaseContract.Flag.TABLE_NAME, null, values);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        // old code
        /*public long create(Flag flag) {
            long rowId = -1;
            try {
                ContentValues values = FlagToContentValues(flag);
                if (values != null) {
                    if (Count(flag.getID()) == 1) {
                        String whereClause = DatabaseContract.Flag._ID + " = '" + flag.getID() + "'";
                        rowId = databaseContract.open().update(DatabaseContract.Flag.TABLE_NAME, values, whereClause, null);
                    } else {
                        rowId = databaseContract.open().insert(DatabaseContract.Flag.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }*/

        public long updateFalg(Flag flag) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Flag.COLUMN_NAME_FLAG, flag.getFlag());
                rowId = databaseContract.open().update(
                        DatabaseContract.Flag.TABLE_NAME, values,
                        DatabaseContract.Flag._ID + "=1",
                        null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Flag> listAll() {
            ArrayList<Flag> flagArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;

                result = db.query(DatabaseContract.Flag.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Flag.DEFAULT_SORT_ORDER);
                flagArrayList = CursorToFlagList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return flagArrayList;
        }

        public ArrayList<Flag> listLast() {
            ArrayList<Flag> flagArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.Flag._ID + "= 1";
                result = db.query(DatabaseContract.Flag.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Flag.DEFAULT_SORT_ORDER);
                flagArrayList = CursorToFlagList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return flagArrayList;
        }

        public Flag listCurrent() {
            ArrayList<Flag> flagArrayList = null;
            Cursor result = null;
            Flag flag = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.Flag._ID + "= 1";
                result = db.query(DatabaseContract.Flag.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Flag.DEFAULT_SORT_ORDER);
                flagArrayList = CursorToFlagList(result);
                if (flagArrayList != null && flagArrayList.size() > 0) {
                    flag = flagArrayList.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return flag;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.Flag._ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.Flag.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Flag.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }
    }

    public class MasterFlagAdapter {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        String date = df.format(c.getTime());
        String[] projection = {
                DatabaseContract.MasterFlag._ID,
                DatabaseContract.MasterFlag.COLUMN_NAME_MASTERFLAG,
                DatabaseContract.MasterFlag.COLUMN_NAME_MSG,
                DatabaseContract.MasterFlag.COLUMN_NAME_DATETIME
        };

        private ContentValues FlagToContentValues(Flag flag) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.MasterFlag._ID, flag.getID());
                values.put(DatabaseContract.MasterFlag.COLUMN_NAME_MASTERFLAG, flag.getFlag());
                values.put(DatabaseContract.MasterFlag.COLUMN_NAME_MSG, flag.getMsg());
                values.put(DatabaseContract.MasterFlag.COLUMN_NAME_DATETIME, date);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;

        }

        private ArrayList<Flag> CursorToMasterFlagList(Cursor result) {
            ArrayList<Flag> flagArrayList = null;
            try {
                if (result != null) {
                    flagArrayList = new ArrayList<Flag>();
                    while (result.moveToNext()) {
                        Flag flag = new Flag();
                        flag.setID(result.getString((result.getColumnIndex(DatabaseContract.MasterFlag._ID))));
                        flag.setFlag(result.getInt(result.getColumnIndex(DatabaseContract.MasterFlag.COLUMN_NAME_MASTERFLAG)));
                        flag.setMsg(result.getString(result.getColumnIndex(DatabaseContract.MasterFlag.COLUMN_NAME_MSG)));
                        flag.setDateTime(result.getString(result.getColumnIndex(DatabaseContract.MasterFlag.COLUMN_NAME_DATETIME)));
                        flagArrayList.add(flag);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flagArrayList;
        }

        public long create(Flag flag) {
            long rowId = -1;
            try {
                ContentValues values = FlagToContentValues(flag);
                if (values != null) {
                    if (Count(flag.getID()) == 1) {
                        String whereClause = DatabaseContract.MasterFlag._ID + " = '" + flag.getID() + "'";
                        rowId = databaseContract.open().update(DatabaseContract.MasterFlag.TABLE_NAME, values, whereClause, null);
                    } else {
                        if (flag.getDateTime() != null && flag.getDateTime().length() > 0) {
                            rowId = databaseContract.open().insert(DatabaseContract.MasterFlag.TABLE_NAME, null, values);
                        } else {
                            values.put(DatabaseContract.MasterFlag.COLUMN_NAME_MASTERFLAG, flag.getFlag());
                            values.put(DatabaseContract.MasterFlag.COLUMN_NAME_DATETIME, "No synchronization");
                            rowId = databaseContract.open().insert(DatabaseContract.MasterFlag.TABLE_NAME, null, values);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateFalg(Flag flag) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.MasterFlag.COLUMN_NAME_MASTERFLAG, flag.getFlag());

                rowId = databaseContract.open().update(
                        DatabaseContract.MasterFlag.TABLE_NAME, values,
                        DatabaseContract.MasterFlag._ID + "=1",
                        null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateMSG(Flag flag) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                if (Count(flag.getID()) == 1) {
                    values.put(DatabaseContract.MasterFlag.COLUMN_NAME_MSG, flag.getMsg());
                    rowId = databaseContract.open().update(
                            DatabaseContract.MasterFlag.TABLE_NAME, values,
                            DatabaseContract.MasterFlag._ID + "=1",
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public int MaxID() {
            int MaxId = 0;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String[] projection = {
                        DatabaseContract.MasterFlag._ID,
                };
                result = db.query(DatabaseContract.MasterFlag.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MasterFlag.DEFAULT_SORT_ORDER);
                if (result != null && result.moveToFirst()) {
                    MaxId = result.getInt(result.getColumnIndex(DatabaseContract.MasterFlag._ID));
                }
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return MaxId;
        }

        public ArrayList<Flag> listAll() {
            ArrayList<Flag> flagArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;

                result = db.query(DatabaseContract.MasterFlag.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.MasterFlag.DEFAULT_SORT_ORDER);
                flagArrayList = CursorToMasterFlagList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return flagArrayList;
        }

        public ArrayList<Flag> listLast() {
            ArrayList<Flag> flagArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.MasterFlag._ID + "= 1";
                result = db.query(DatabaseContract.MasterFlag.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.MasterFlag.DEFAULT_SORT_ORDER);
                flagArrayList = CursorToMasterFlagList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return flagArrayList;
        }

        public Flag listCurrent() {
            ArrayList<Flag> flagArrayList = null;
            Flag flag = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.MasterFlag._ID + "= 1";
                result = db.query(DatabaseContract.MasterFlag.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.MasterFlag.DEFAULT_SORT_ORDER);
                flagArrayList = CursorToMasterFlagList(result);
                if (flagArrayList != null && flagArrayList.size() > 0) {
                    flag = flagArrayList.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return flag;
        }


        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.MasterFlag._ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.MasterFlag.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.MasterFlag.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

    }

    public class SynchOfflineDataAdapter {

        String[] projection = {
                DatabaseContract.SynchOfflineData.COLUMN_NAME_ID,
                DatabaseContract.SynchOfflineData.COLUMN_NAME_OFFLINE_DATE,
                DatabaseContract.SynchOfflineData.COLUMN_NAME_VERSION_CODE,
                DatabaseContract.SynchOfflineData.COLUMN_NAME_VERSION_NAME,
                DatabaseContract.SynchOfflineData.COLUMN_NAME_OFFLINE_STATUS
        };

        private ContentValues SynchOfflineContentValues(ELSynchOfflineData elSynchOfflineData) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.SynchOfflineData.COLUMN_NAME_ID, elSynchOfflineData.getID());
                values.put(DatabaseContract.SynchOfflineData.COLUMN_NAME_OFFLINE_DATE, elSynchOfflineData.getOfflineLastDate());
                values.put(DatabaseContract.SynchOfflineData.COLUMN_NAME_VERSION_CODE, elSynchOfflineData.getVersionCode());
                values.put(DatabaseContract.SynchOfflineData.COLUMN_NAME_VERSION_NAME, elSynchOfflineData.getVersionName());
                values.put(DatabaseContract.SynchOfflineData.COLUMN_NAME_OFFLINE_STATUS, elSynchOfflineData.getOfflineStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELSynchOfflineData> CursorToArrayList(Cursor result) {
            ArrayList<ELSynchOfflineData> listSynchOfflineData = null;
            try {
                if (result != null) {
                    listSynchOfflineData = new ArrayList<ELSynchOfflineData>();
                    while (result.moveToNext()) {
                        ELSynchOfflineData elSynchOfflineData = new ELSynchOfflineData();
                        elSynchOfflineData.setID(result.getString(result.getColumnIndex(DatabaseContract.SynchOfflineData.COLUMN_NAME_ID)));
                        elSynchOfflineData.setOfflineLastDate(result.getString(result.getColumnIndex(DatabaseContract.SynchOfflineData.COLUMN_NAME_OFFLINE_DATE)));
                        elSynchOfflineData.setVersionCode(result.getString(result.getColumnIndex(DatabaseContract.SynchOfflineData.COLUMN_NAME_VERSION_CODE)));
                        elSynchOfflineData.setVersionName(result.getString(result.getColumnIndex(DatabaseContract.SynchOfflineData.COLUMN_NAME_VERSION_NAME)));
                        elSynchOfflineData.setOfflineStatus(result.getString(result.getColumnIndex(DatabaseContract.SynchOfflineData.COLUMN_NAME_OFFLINE_STATUS)));
                        listSynchOfflineData.add(elSynchOfflineData);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listSynchOfflineData;
        }

        public long create(ELSynchOfflineData elSynchOfflineData) {
            long rowId = -1;
            try {
                if (Count(elSynchOfflineData.getID()) == 0) {
                    ContentValues values = SynchOfflineContentValues(elSynchOfflineData);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.SynchOfflineData.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELSynchOfflineData> listAll() {
            ArrayList<ELSynchOfflineData> listSynchOfflineData = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.SynchOfflineData.TABLE_NAME,
                        projection, null, null, null, null, null);
                listSynchOfflineData = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listSynchOfflineData;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.SynchOfflineData.TABLE_NAME,
                        projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.SynchOfflineData.COLUMN_NAME_ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.SynchOfflineData.TABLE_NAME,
                            projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.SynchOfflineData.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public long updateDate() {
            long rowId = -1;
            try {
                String todayDate = new SimpleDateFormat(Constants.OFFLINE_DATE, Locale.getDefault()).format(new Date());
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.SynchOfflineData.COLUMN_NAME_OFFLINE_DATE, todayDate);
                String whereClause = null;
                rowId = databaseContract.open().update(
                        DatabaseContract.SynchOfflineData.TABLE_NAME, values, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void DeleteOfflineEMR() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.DiagnosisList.TABLE_NAME, DatabaseContract.DiagnosisList.COLUMN_NAME_IS_SYNC + "='1'", null);
                db.delete(DatabaseContract.VitalsList.TABLE_NAME, DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC + "='1'", null);
                db.delete(DatabaseContract.CPOEMedicine.TABLE_NAME, DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_SYNC + "='1'", null);
                db.delete(DatabaseContract.CPOEService.TABLE_NAME, DatabaseContract.CPOEService.COLUMN_NAME_IS_SYNC + "='1'", null);
                db.delete(DatabaseContract.ComplaintsList.TABLE_NAME, DatabaseContract.ComplaintsList.COLUMN_NAME_IS_SYNC + "='1'", null);
                db.delete(DatabaseContract.ReferralServiceList.TABLE_NAME, DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_SYNC + "='1'", null);
                databaseContract.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class DoctorProfileAdapter {

        String[] projection = {
                DatabaseContract.DoctorProfile.COLUMN_NAME_DOCTOR_ID,
                DatabaseContract.DoctorProfile.COLUMN_NAME_LOGIN_NAME,
                DatabaseContract.DoctorProfile.COLUMN_NAME_PASSWORD,
                DatabaseContract.DoctorProfile.COLUMN_NAME_ID,
                DatabaseContract.DoctorProfile.COLUMN_NAME_UNITID,
                DatabaseContract.DoctorProfile.COLUMN_NAME_DEPARTMENTID,
                DatabaseContract.DoctorProfile.COLUMN_NAME_UNITNAME,
                DatabaseContract.DoctorProfile.COLUMN_NAME_FIRST_NAME,
                DatabaseContract.DoctorProfile.COLUMN_NAME_MIDDLE_NAME,
                DatabaseContract.DoctorProfile.COLUMN_NAME_LAST_NAME,
                DatabaseContract.DoctorProfile.COLUMN_NAME_GENDER,
                DatabaseContract.DoctorProfile.COLUMN_NAME_DOB,
                DatabaseContract.DoctorProfile.COLUMN_NAME_EDUCATION,
                DatabaseContract.DoctorProfile.COLUMN_NAME_SPECIALIZTION,
                DatabaseContract.DoctorProfile.COLUMN_NAME_EMAIL_ID,
                DatabaseContract.DoctorProfile.COLUMN_NAME_PFNumber,
                DatabaseContract.DoctorProfile.COLUMN_NAME_LOGIN_STATUS,
                DatabaseContract.DoctorProfile.COLUMN_NAME_IsFrontOfficeUser,
                DatabaseContract.DoctorProfile.COLUMN_NAME_REMEMBER_ME
        };

        private ContentValues ProfileToContentValues(DoctorProfile doctorProfile) {
            ContentValues values = null;
            try {
                if (doctorProfile != null) {
                    values = new ContentValues();
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_DOCTOR_ID, doctorProfile.getDoctorID());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_LOGIN_NAME, doctorProfile.getLoginName());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_PASSWORD, doctorProfile.getPassword());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_ID, doctorProfile.getID());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_UNITID, doctorProfile.getUnitID());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_DEPARTMENTID, doctorProfile.getDepartmentID());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_UNITNAME, doctorProfile.getUnitName());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_FIRST_NAME, doctorProfile.getFirstName());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_MIDDLE_NAME, doctorProfile.getMiddleName());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_LAST_NAME, doctorProfile.getLastName());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_GENDER, doctorProfile.getGender());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_DOB, doctorProfile.getDOB());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_EDUCATION, doctorProfile.getEducation());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_SPECIALIZTION, doctorProfile.getSpecialization());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_EMAIL_ID, doctorProfile.getEmailId());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_PFNumber, doctorProfile.getPFNumber());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_REMEMBER_ME, doctorProfile.getRememberMe());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_LOGIN_STATUS, doctorProfile.getLoginStatus());
                    values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_IsFrontOfficeUser, doctorProfile.getIsFrontOfficeUser());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<DoctorProfile> CursorToArrayList(Cursor result) {
            ArrayList<DoctorProfile> listDoctorProfile = null;
            try {
                if (result != null) {
                    listDoctorProfile = new ArrayList<DoctorProfile>();
                    while (result.moveToNext()) {
                        com.palash.healthspringapp.entity.DoctorProfile doctorProfile = new com.palash.healthspringapp.entity.DoctorProfile();
                        doctorProfile.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_DOCTOR_ID)));
                        doctorProfile.setLoginName(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_LOGIN_NAME)));
                        doctorProfile.setPassword(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_PASSWORD)));
                        doctorProfile.setID(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_ID)));
                        doctorProfile.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_UNITID)));
                        doctorProfile.setDepartmentID(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_DEPARTMENTID)));
                        doctorProfile.setUnitName(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_UNITNAME)));
                        doctorProfile.setFirstName(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_FIRST_NAME)));
                        doctorProfile.setMiddleName(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_MIDDLE_NAME)));
                        doctorProfile.setLastName(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_LAST_NAME)));
                        doctorProfile.setGender(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_GENDER)));
                        doctorProfile.setDOB(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_DOB)));
                        doctorProfile.setEducation(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_EDUCATION)));
                        doctorProfile.setSpecialization(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_SPECIALIZTION)));
                        doctorProfile.setEmailId(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_EMAIL_ID)));
                        doctorProfile.setPFNumber(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_PFNumber)));
                        doctorProfile.setLoginStatus(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_LOGIN_STATUS)));
                        doctorProfile.setRememberMe(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_REMEMBER_ME)));
                        doctorProfile.setIsFrontOfficeUser(result.getString(result.getColumnIndex(DatabaseContract.DoctorProfile.COLUMN_NAME_IsFrontOfficeUser)));
                        listDoctorProfile.add(doctorProfile);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listDoctorProfile;
        }

        public long create(DoctorProfile doctorProfile) {
            long rowId = -1;
            try {
                CheckLogin();
                ContentValues values = ProfileToContentValues(doctorProfile);
                if (values != null) {
                    rowId = databaseContract.open().insert(
                            DatabaseContract.DoctorProfile.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        private void CheckLogin() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.DoctorProfile.TABLE_NAME, null, null);
                db.delete(DatabaseContract.UnitMaster.TABLE_NAME, null, null);
                db.delete(DatabaseContract.BookAppointment.TABLE_NAME, null, null);
                db.delete(DatabaseContract.PatientQueue.TABLE_NAME, null, null);
                db.delete(DatabaseContract.VisitList.TABLE_NAME, null, null);
                db.delete(DatabaseContract.DiagnosisList.TABLE_NAME, null, null);
                db.delete(DatabaseContract.VitalsList.TABLE_NAME, null, null);
                db.delete(DatabaseContract.CPOEMedicine.TABLE_NAME, null, null);
                db.delete(DatabaseContract.CPOEService.TABLE_NAME, null, null);
                db.delete(DatabaseContract.ComplaintsList.TABLE_NAME, null, null);
                db.delete(DatabaseContract.ReferralServiceList.TABLE_NAME, null, null);
                databaseContract.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public long updatePassword(DoctorProfile doctorProfile) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_PASSWORD, new LocalSetting().encodeString(doctorProfile.getPassword()));
                String whereClause = null;
                whereClause = DatabaseContract.DoctorProfile.COLUMN_NAME_DOCTOR_ID + " = " + doctorProfile.getDoctorID();
                rowId = databaseContract.open().update(
                        DatabaseContract.DoctorProfile.TABLE_NAME, values, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long LogOut(String DoctorID, String Status) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.DoctorProfile.COLUMN_NAME_LOGIN_STATUS, Status);
                String whereClause = null;
                whereClause = DatabaseContract.DoctorProfile.COLUMN_NAME_DOCTOR_ID + " = '" + DoctorID + "'";
                rowId = databaseContract.open().update(DatabaseContract.DoctorProfile.TABLE_NAME, values, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<DoctorProfile> listAll() {
            ArrayList<DoctorProfile> listDoctorProfile = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DoctorProfile.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.DoctorProfile.DEFAULT_SORT_ORDER);
                listDoctorProfile = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }

            return listDoctorProfile;
        }

        public long update(DoctorProfile doctorProfile) {
            long rowId = -1;
            try {
                ContentValues values = ProfileToContentValues(doctorProfile);
                String whereClause = null;
                whereClause = DatabaseContract.DoctorProfile.COLUMN_NAME_DOCTOR_ID + " = " + doctorProfile.getDoctorID();
                rowId = databaseContract.open().update(
                        DatabaseContract.DoctorProfile.TABLE_NAME, values, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.DoctorProfile.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = DatabaseContract.DoctorProfile.COLUMN_NAME_DOCTOR_ID + " ='" + ID + "'";
                result = db.query(DatabaseContract.DoctorProfile.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.DoctorProfile.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }
    }

    public class DoctorTypeAdapter {

        String[] projection = {
                DatabaseContract.DoctorType.COLUMN_NAME_ID,
                DatabaseContract.DoctorType.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues DoctorTypeToContentValues(DoctorType doctorType) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.DoctorType.COLUMN_NAME_ID, doctorType.getID());
                values.put(DatabaseContract.DoctorType.COLUMN_NAME_DESCRIPTION, doctorType.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<DoctorType> CursorToArrayList(Cursor result) {
            ArrayList<DoctorType> listDoctorType = null;
            try {
                if (result != null) {
                    listDoctorType = new ArrayList<DoctorType>();
                    while (result.moveToNext()) {
                        DoctorType doctorType = new DoctorType();
                        doctorType.setID(result.getString(result.getColumnIndex(DatabaseContract.DoctorType.COLUMN_NAME_ID)));
                        doctorType.setDescription(result.getString(result.getColumnIndex(DatabaseContract.DoctorType.COLUMN_NAME_DESCRIPTION)));
                        listDoctorType.add(doctorType);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listDoctorType;
        }

        public long create(DoctorType doctorType) {
            long rowId = -1;
            try {
                if (Count(doctorType.getID()) == 0) {
                    ContentValues values = DoctorTypeToContentValues(doctorType);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.DoctorType.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<DoctorType> listAll() {
            ArrayList<DoctorType> listDoctorType = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DoctorType.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.DoctorType.DEFAULT_SORT_ORDER);
                listDoctorType = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listDoctorType;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DoctorType.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.DoctorType.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.DoctorType.COLUMN_NAME_ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.DoctorType.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.DoctorType.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.DoctorType.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class UnitMasterAdapter {

        String[] projection = {
                DatabaseContract.UnitMaster.COLUMN_NAME_UNITID,
                DatabaseContract.UnitMaster.COLUMN_NAME_UNIT_CODE,
                DatabaseContract.UnitMaster.COLUMN_NAME_UNIT_DESC,
                DatabaseContract.UnitMaster.COLUMN_NAME_IsDefault
        };

        private ContentValues UnitMasterToContentValues(ELUnitMaster elUnitMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.UnitMaster.COLUMN_NAME_UNITID, elUnitMaster.getUnitID());
                values.put(DatabaseContract.UnitMaster.COLUMN_NAME_UNIT_CODE, elUnitMaster.getUnitCode());
                values.put(DatabaseContract.UnitMaster.COLUMN_NAME_UNIT_DESC, elUnitMaster.getUnitDesc());
                values.put(DatabaseContract.UnitMaster.COLUMN_NAME_IsDefault, elUnitMaster.getIsDefault());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELUnitMaster> CursorToArrayList(Cursor result) {
            ArrayList<ELUnitMaster> listELUnitMaster = null;
            try {
                if (result != null) {
                    listELUnitMaster = new ArrayList<ELUnitMaster>();
                    while (result.moveToNext()) {
                        ELUnitMaster elUnitMaster = new ELUnitMaster();
                        elUnitMaster.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.UnitMaster.COLUMN_NAME_UNITID)));
                        elUnitMaster.setUnitCode(result.getString(result.getColumnIndex(DatabaseContract.UnitMaster.COLUMN_NAME_UNIT_CODE)));
                        elUnitMaster.setUnitDesc(result.getString(result.getColumnIndex(DatabaseContract.UnitMaster.COLUMN_NAME_UNIT_DESC)));
                        elUnitMaster.setIsDefault(result.getString(result.getColumnIndex(DatabaseContract.UnitMaster.COLUMN_NAME_IsDefault)));
                        listELUnitMaster.add(elUnitMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELUnitMaster;
        }

        public long create(ELUnitMaster elUnitMaster) {
            long rowId = -1;
            try {
                if (Count(elUnitMaster.getUnitID()) == 0) {
                    ContentValues values = UnitMasterToContentValues(elUnitMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.UnitMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELUnitMaster> listAll() {
            ArrayList<ELUnitMaster> listELUnitMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.UnitMaster.TABLE_NAME,
                        projection, null,
                        null, null, null, DatabaseContract.UnitMaster.DEFAULT_SORT_ORDER);
                listELUnitMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listELUnitMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.UnitMaster.TABLE_NAME,
                        projection, null,
                        null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.UnitMaster.COLUMN_NAME_UNITID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.UnitMaster.TABLE_NAME,
                            projection, whereClause,
                            null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.UnitMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class CountryMasterAdapter {

        String[] projection = {
                DatabaseContract.CountryMaster.COLUMN_NAME_ID,
                DatabaseContract.CountryMaster.COLUMN_NAME_COUNTRY_NAME
        };

        private ContentValues ELCountryMasterToContentValues(ELCountryMaster elCountryMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.CountryMaster.COLUMN_NAME_ID, elCountryMaster.getID());
                values.put(DatabaseContract.CountryMaster.COLUMN_NAME_COUNTRY_NAME, elCountryMaster.getCountryName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELCountryMaster> CursorToArrayList(Cursor result) {
            ArrayList<ELCountryMaster> listELCountryMaster = null;
            try {
                if (result != null) {
                    listELCountryMaster = new ArrayList<ELCountryMaster>();
                    while (result.moveToNext()) {
                        ELCountryMaster elCountryMaster = new ELCountryMaster();
                        elCountryMaster.setID(result.getString(result.getColumnIndex(DatabaseContract.CountryMaster.COLUMN_NAME_ID)));
                        elCountryMaster.setCountryName(result.getString(result.getColumnIndex(DatabaseContract.CountryMaster.COLUMN_NAME_COUNTRY_NAME)));
                        listELCountryMaster.add(elCountryMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELCountryMaster;
        }

        public long create(ELCountryMaster elCountryMaster) {
            long rowId = -1;
            try {
                if (Count(elCountryMaster.getID()) == 0) {
                    ContentValues values = ELCountryMasterToContentValues(elCountryMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(DatabaseContract.CountryMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELCountryMaster> listAll() {
            ArrayList<ELCountryMaster> listELCountryMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CountryMaster.TABLE_NAME, projection, null, null, null, null, null);
                listELCountryMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listELCountryMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CountryMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.CountryMaster.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.CountryMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.CountryMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class RegionMasterAdapter {

        String[] projection = {
                DatabaseContract.RegionMaster.COLUMN_NAME_ID,
                DatabaseContract.RegionMaster.COLUMN_NAME_COUNTRY_ID,
                DatabaseContract.RegionMaster.COLUMN_NAME_REGION_ID,
                DatabaseContract.RegionMaster.COLUMN_NAME_REGION_NAME
        };

        private ContentValues ELRegionMasterToContentValues(ELRegionMaster elRegionMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.RegionMaster.COLUMN_NAME_ID, elRegionMaster.getID());
                values.put(DatabaseContract.RegionMaster.COLUMN_NAME_COUNTRY_ID, elRegionMaster.getCountryID());
                values.put(DatabaseContract.RegionMaster.COLUMN_NAME_REGION_ID, elRegionMaster.getRegionID());
                values.put(DatabaseContract.RegionMaster.COLUMN_NAME_REGION_NAME, elRegionMaster.getRegionName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELRegionMaster> CursorToArrayList(Cursor result) {
            ArrayList<ELRegionMaster> listELRegionMaster = null;
            try {
                if (result != null) {
                    listELRegionMaster = new ArrayList<ELRegionMaster>();
                    while (result.moveToNext()) {
                        ELRegionMaster elRegionMaster = new ELRegionMaster();
                        elRegionMaster.setID(result.getString(result.getColumnIndex(DatabaseContract.RegionMaster.COLUMN_NAME_ID)));
                        elRegionMaster.setCountryID(result.getString(result.getColumnIndex(DatabaseContract.RegionMaster.COLUMN_NAME_COUNTRY_ID)));
                        elRegionMaster.setRegionID(result.getString(result.getColumnIndex(DatabaseContract.RegionMaster.COLUMN_NAME_REGION_ID)));
                        elRegionMaster.setRegionName(result.getString(result.getColumnIndex(DatabaseContract.RegionMaster.COLUMN_NAME_REGION_NAME)));
                        listELRegionMaster.add(elRegionMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELRegionMaster;
        }

        public long create(ELRegionMaster elRegionMaster) {
            long rowId = -1;
            try {
                if (Count(elRegionMaster.getID()) == 0) {
                    ContentValues values = ELRegionMasterToContentValues(elRegionMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(DatabaseContract.RegionMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELRegionMaster> listAll(String countryID) {
            ArrayList<ELRegionMaster> listELRegionMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (countryID != null && countryID.length() > 0 && countryID.equals("1")) {
                    whereClause = DatabaseContract.RegionMaster.COLUMN_NAME_COUNTRY_ID + "='" + countryID + "'";
                } else if (countryID != null && countryID.length() > 0) {
                    whereClause = DatabaseContract.RegionMaster.COLUMN_NAME_COUNTRY_ID + "='" + countryID + "'";
                }
                result = db.query(DatabaseContract.RegionMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                listELRegionMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listELRegionMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.RegionMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.RegionMaster.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.RegionMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.RegionMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class StateMasterAdapter {

        String[] projection = {
                DatabaseContract.StateMaster.COLUMN_NAME_ID,
                DatabaseContract.StateMaster.COLUMN_NAME_STATE_NAME,
                DatabaseContract.StateMaster.COLUMN_NAME_REGION_ID
        };

        private ContentValues ELStateMasterToContentValues(ELStateMaster elStateMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.StateMaster.COLUMN_NAME_ID, elStateMaster.getID());
                values.put(DatabaseContract.StateMaster.COLUMN_NAME_STATE_NAME, elStateMaster.getStateName());
                values.put(DatabaseContract.StateMaster.COLUMN_NAME_REGION_ID, elStateMaster.getRegionID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELStateMaster> CursorToArrayList(Cursor result) {
            ArrayList<ELStateMaster> listELStateMaster = null;
            try {
                if (result != null) {
                    listELStateMaster = new ArrayList<ELStateMaster>();
                    while (result.moveToNext()) {
                        ELStateMaster elStateMaster = new ELStateMaster();
                        elStateMaster.setID(result.getString(result.getColumnIndex(DatabaseContract.StateMaster.COLUMN_NAME_ID)));
                        elStateMaster.setStateName(result.getString(result.getColumnIndex(DatabaseContract.StateMaster.COLUMN_NAME_STATE_NAME)));
                        elStateMaster.setRegionID(result.getString(result.getColumnIndex(DatabaseContract.StateMaster.COLUMN_NAME_REGION_ID)));
                        listELStateMaster.add(elStateMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELStateMaster;
        }

        public long create(ELStateMaster elStateMaster) {
            long rowId = -1;
            try {
                if (Count(elStateMaster.getID(), elStateMaster.getRegionID()) == 0) {
                    ContentValues values = ELStateMasterToContentValues(elStateMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(DatabaseContract.StateMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELStateMaster> listAll(String countryID, String regionID) {
            ArrayList<ELStateMaster> listELStateMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (regionID != null && regionID.length() > 0 && regionID.equals("1")) {
                    whereClause = DatabaseContract.StateMaster.COLUMN_NAME_REGION_ID + "='" + regionID + "'";
                } else if (regionID != null && regionID.length() > 0) {
                    whereClause = //DatabaseContract.StateMaster.COLUMN_NAME_COUNTRY_ID + "='" + countryID + "' AND " +
                            DatabaseContract.StateMaster.COLUMN_NAME_REGION_ID + "='" + regionID + "'";
                }
                result = db.query(DatabaseContract.StateMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                listELStateMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listELStateMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.StateMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID, String RegionID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.StateMaster.COLUMN_NAME_ID + "='" + ID + "' AND "
                            + DatabaseContract.StateMaster.COLUMN_NAME_REGION_ID + "='" + RegionID + "'";
                    result = db.query(DatabaseContract.StateMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.StateMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class CityMasterAdapter {

        String[] projection = {
                DatabaseContract.CityMaster.COLUMN_NAME_ID,
                DatabaseContract.CityMaster.COLUMN_NAME_CITY_NAME,
                DatabaseContract.CityMaster.COLUMN_NAME_STATE_ID
        };

        private ContentValues ELCityMasterToContentValues(ELCityMaster elCityMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.CityMaster.COLUMN_NAME_ID, elCityMaster.getID());
                values.put(DatabaseContract.CityMaster.COLUMN_NAME_CITY_NAME, elCityMaster.getCityName());
                values.put(DatabaseContract.CityMaster.COLUMN_NAME_STATE_ID, elCityMaster.getStateID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELCityMaster> CursorToArrayList(Cursor result) {
            ArrayList<ELCityMaster> listELCityMaster = null;
            try {
                if (result != null) {
                    listELCityMaster = new ArrayList<ELCityMaster>();
                    while (result.moveToNext()) {
                        ELCityMaster elCityMaster = new ELCityMaster();
                        elCityMaster.setID(result.getString(result.getColumnIndex(DatabaseContract.CityMaster.COLUMN_NAME_ID)));
                        elCityMaster.setCityName(result.getString(result.getColumnIndex(DatabaseContract.CityMaster.COLUMN_NAME_CITY_NAME)));
                        elCityMaster.setStateID(result.getString(result.getColumnIndex(DatabaseContract.CityMaster.COLUMN_NAME_STATE_ID)));
                        listELCityMaster.add(elCityMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELCityMaster;
        }

        public long create(ELCityMaster elCityMaster) {
            long rowId = -1;
            try {
                if (Count(elCityMaster.getID()) == 0) {
                    ContentValues values = ELCityMasterToContentValues(elCityMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(DatabaseContract.CityMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELCityMaster> listAll(String countryID, String regionID, String stateID) {
            ArrayList<ELCityMaster> listELCityMaster = null;
            Cursor result = null;
            try {
                String whereClause = null;
                if (stateID != null && stateID.length() > 0 && stateID.equals("1")) {
                    whereClause = DatabaseContract.CityMaster.COLUMN_NAME_STATE_ID + "='" + stateID + "'";
                } else if (stateID != null && stateID.length() > 0) {
                    whereClause = //DatabaseContract.CityMaster.COLUMN_NAME_COUNTRY_ID + "='" + countryID + "' AND "
                            // + DatabaseContract.CityMaster.COLUMN_NAME_COUNTRY_ID + "='" + countryID + "' AND " +
                            DatabaseContract.CityMaster.COLUMN_NAME_STATE_ID + "='" + stateID + "'";
                }
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CityMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                listELCityMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listELCityMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CityMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.CityMaster.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.CityMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.CityMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class HealthSpringReferralMasterAdapter {

        String[] projection = {
                DatabaseContract.HealthspringReferralMaster.COLUMN_NAME_ID,
                DatabaseContract.HealthspringReferralMaster.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues ELHealthspringReferralToContentValues(ELHealthspringReferral elHealthspringReferral) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.HealthspringReferralMaster.COLUMN_NAME_ID, elHealthspringReferral.getID());
                values.put(DatabaseContract.HealthspringReferralMaster.COLUMN_NAME_DESCRIPTION, elHealthspringReferral.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELHealthspringReferral> CursorToArrayList(Cursor result) {
            ArrayList<ELHealthspringReferral> listELHealthspringReferral = null;
            try {
                if (result != null) {
                    listELHealthspringReferral = new ArrayList<ELHealthspringReferral>();
                    while (result.moveToNext()) {
                        ELHealthspringReferral elHealthspringReferral = new ELHealthspringReferral();
                        elHealthspringReferral.setID(result.getString(result.getColumnIndex(DatabaseContract.HealthspringReferralMaster.COLUMN_NAME_ID)));
                        elHealthspringReferral.setDescription(result.getString(result.getColumnIndex(DatabaseContract.HealthspringReferralMaster.COLUMN_NAME_DESCRIPTION)));
                        listELHealthspringReferral.add(elHealthspringReferral);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELHealthspringReferral;
        }

        public long create(ELHealthspringReferral elHealthspringReferral) {
            long rowId = -1;
            try {
                if (Count(elHealthspringReferral.getID()) == 0) {
                    ContentValues values = ELHealthspringReferralToContentValues(elHealthspringReferral);
                    if (values != null) {
                        rowId = databaseContract.open().insert(DatabaseContract.HealthspringReferralMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELHealthspringReferral> listAll() {
            ArrayList<ELHealthspringReferral> listELHealthspringReferral = null;
            Cursor result = null;
            try {
                String whereClause = null;
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.HealthspringReferralMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                listELHealthspringReferral = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listELHealthspringReferral;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.HealthspringReferralMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.HealthspringReferralMaster.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.HealthspringReferralMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.HealthspringReferralMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class SpecializationAdapter {

        String[] projection = {
                DatabaseContract.Specialization.COLUMN_NAME_ID,
                DatabaseContract.Specialization.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues SpecializationToContentValues(Specialization specialization) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Specialization.COLUMN_NAME_ID, specialization.getID());
                values.put(DatabaseContract.Specialization.COLUMN_NAME_DESCRIPTION, specialization.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<Specialization> CursorToArrayList(Cursor result) {
            ArrayList<Specialization> listSpecialization = null;
            try {
                if (result != null) {
                    listSpecialization = new ArrayList<Specialization>();
                    while (result.moveToNext()) {
                        Specialization specialization = new Specialization();
                        specialization.setID(result.getString(result.getColumnIndex(DatabaseContract.Specialization.COLUMN_NAME_ID)));
                        specialization.setDescription(result.getString(result.getColumnIndex(DatabaseContract.Specialization.COLUMN_NAME_DESCRIPTION)));
                        listSpecialization.add(specialization);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listSpecialization;
        }

        public long create(Specialization specialization) {
            long rowId = -1;
            try {
                if (Count(specialization.getID()) == 0) {
                    ContentValues values = SpecializationToContentValues(specialization);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Specialization.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Specialization> listAll() {
            ArrayList<Specialization> listSpecialization = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Specialization.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Specialization.DEFAULT_SORT_ORDER);
                listSpecialization = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listSpecialization;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Specialization.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Specialization.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.Specialization.COLUMN_NAME_ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.Specialization.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Specialization.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Specialization.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class AppointmentReasonAdapter {

        String[] projection = {
                DatabaseContract.AppointmentReason.COLUMN_NAME_ID,
                DatabaseContract.AppointmentReason.COLUMN_NAME_DESCRIPTION,
                DatabaseContract.AppointmentReason.COLUMN_NAME_COLORCODE,
                DatabaseContract.AppointmentReason.COLUMN_NAME_SERVICEID
        };

        private ContentValues AppointmentReasonToContentValues(AppointmentReason appointmentReason) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.AppointmentReason.COLUMN_NAME_ID, appointmentReason.getID());
                values.put(DatabaseContract.AppointmentReason.COLUMN_NAME_DESCRIPTION, appointmentReason.getDescription());
                values.put(DatabaseContract.AppointmentReason.COLUMN_NAME_COLORCODE, appointmentReason.getColorCode());
                values.put(DatabaseContract.AppointmentReason.COLUMN_NAME_SERVICEID, appointmentReason.getServiceID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<AppointmentReason> CursorToArrayList(Cursor result) {
            ArrayList<AppointmentReason> listAppointmentReason = null;
            try {
                if (result != null) {
                    listAppointmentReason = new ArrayList<AppointmentReason>();
                    while (result.moveToNext()) {
                        AppointmentReason getAppointmentReason = new AppointmentReason();
                        getAppointmentReason.setID(result.getString(result.getColumnIndex(DatabaseContract.AppointmentReason.COLUMN_NAME_ID)));
                        getAppointmentReason.setDescription(result.getString(result.getColumnIndex(DatabaseContract.AppointmentReason.COLUMN_NAME_DESCRIPTION)));
                        getAppointmentReason.setColorCode(result.getString(result.getColumnIndex(DatabaseContract.AppointmentReason.COLUMN_NAME_COLORCODE)));
                        getAppointmentReason.setServiceID(result.getString(result.getColumnIndex(DatabaseContract.AppointmentReason.COLUMN_NAME_SERVICEID)));
                        listAppointmentReason.add(getAppointmentReason);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listAppointmentReason;
        }

        public long create(AppointmentReason appointmentReason) {
            long rowId = -1;
            try {
                if (Count(appointmentReason.getID()) == 0) {
                    ContentValues values = AppointmentReasonToContentValues(appointmentReason);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.AppointmentReason.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<AppointmentReason> listAll() {
            ArrayList<AppointmentReason> listAppointmentReason = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.AppointmentReason.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.AppointmentReason.DEFAULT_SORT_ORDER);
                listAppointmentReason = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listAppointmentReason;
        }

        public ArrayList<AppointmentReason> listVisitTypeServiceID(String reasonID) {
            ArrayList<AppointmentReason> listAppointmentReason = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;

                if (reasonID != null) {
                    whereClause = DatabaseContract.AppointmentReason.COLUMN_NAME_ID + "='" + reasonID + "'";
                }
                result = db.query(DatabaseContract.AppointmentReason.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.AppointmentReason.DEFAULT_SORT_ORDER);
                listAppointmentReason = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listAppointmentReason;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.AppointmentReason.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.AppointmentReason.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.AppointmentReason.COLUMN_NAME_ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.AppointmentReason.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.AppointmentReason.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.AppointmentReason.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class DepartmentAdapter {

        String[] projection = {
                DatabaseContract.Department.COLUMN_NAME_ID,
                DatabaseContract.Department.COLUMN_NAME_UnitID,
                DatabaseContract.Department.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues DepartmentToContentValues(Department department) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Department.COLUMN_NAME_ID, department.getID());
                values.put(DatabaseContract.Department.COLUMN_NAME_UnitID, department.getUnitID());
                String description = department.getDescription();
                description = description.replace("#", "");
                description = description.replace("*", "");
                description = description.replace("$", "");
                values.put(DatabaseContract.Department.COLUMN_NAME_DESCRIPTION, description);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<Department> CursorToArrayList(Cursor result) {
            ArrayList<Department> listDepartment = null;
            try {
                if (result != null) {
                    listDepartment = new ArrayList<Department>();
                    while (result.moveToNext()) {
                        Department department = new Department();
                        department.setID(result.getString(result.getColumnIndex(DatabaseContract.Department.COLUMN_NAME_ID)));
                        department.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.Department.COLUMN_NAME_UnitID)));
                        department.setDescription(result.getString(result.getColumnIndex(DatabaseContract.Department.COLUMN_NAME_DESCRIPTION)));
                        listDepartment.add(department);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listDepartment;
        }

        public long create(Department department) {
            long rowId = -1;
            try {
                if (Count(department.getID()) == 0) {
                    ContentValues values = DepartmentToContentValues(department);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Department.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Department> listAll() {
            ArrayList<Department> listDepartment = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Department.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Department.DEFAULT_SORT_ORDER);
                listDepartment = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listDepartment;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Department.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Department.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.Department.COLUMN_NAME_ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.Department.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Department.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Department.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class ComplaintAdapter {

        String[] projection = {
                DatabaseContract.Complaint.COLUMN_NAME_ID,
                DatabaseContract.Complaint.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues ComplaintToContentValues(Complaint complaint) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Complaint.COLUMN_NAME_ID, complaint.getID());
                values.put(DatabaseContract.Complaint.COLUMN_NAME_DESCRIPTION, complaint.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<Complaint> CursorToArrayList(Cursor result) {
            ArrayList<Complaint> listComplaint = null;
            try {
                if (result != null) {
                    listComplaint = new ArrayList<Complaint>();
                    while (result.moveToNext()) {
                        Complaint complaint = new Complaint();
                        complaint.setID(result.getString(result.getColumnIndex(DatabaseContract.Complaint.COLUMN_NAME_ID)));
                        complaint.setDescription(result.getString(result.getColumnIndex(DatabaseContract.Complaint.COLUMN_NAME_DESCRIPTION)));
                        listComplaint.add(complaint);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listComplaint;
        }

        public long create(Complaint complaint) {
            long rowId = -1;
            try {
                if (Count(complaint.getID()) == 0) {
                    ContentValues values = ComplaintToContentValues(complaint);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Complaint.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Complaint> listAll() {
            ArrayList<Complaint> listComplaint = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Complaint.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Complaint.DEFAULT_SORT_ORDER);
                listComplaint = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listComplaint;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Complaint.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Complaint.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.Complaint.COLUMN_NAME_ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.Complaint.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Complaint.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Complaint.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class BookAppointmentAdapter {

        String[] projection = {
                DatabaseContract.BookAppointment.COLUMN_NAME_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_UNIT_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_PATIENT_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_FIRST_NAME,
                DatabaseContract.BookAppointment.COLUMN_NAME_MIDDLE_NAME,
                DatabaseContract.BookAppointment.COLUMN_NAME_LAST_NAME,
                DatabaseContract.BookAppointment.COLUMN_NAME_GENDER_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_DOB,
                DatabaseContract.BookAppointment.COLUMN_NAME_AGE,
                DatabaseContract.BookAppointment.COLUMN_NAME_MRNo,
                DatabaseContract.BookAppointment.COLUMN_NAME_REGISTRATION_DATE,
                DatabaseContract.BookAppointment.COLUMN_NAME_CLINIC_NAME,
                DatabaseContract.BookAppointment.COLUMN_NAME_MATRITAL_STATUS,
                DatabaseContract.BookAppointment.COLUMN_NAME_MATRITAL_STATUS_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_CONTACT1,
                DatabaseContract.BookAppointment.COLUMN_NAME_EMAIL_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_DEPARTMENT_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_UNIT_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_NAME,
                DatabaseContract.BookAppointment.COLUMN_NAME_SPECIALIZATION,
                DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_EDUCATION,
                DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_MOBILE_NO,
                DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENT_REASON_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENT_DATE,
                DatabaseContract.BookAppointment.COLUMN_NAME_FROM_TIME,
                DatabaseContract.BookAppointment.COLUMN_NAME_TO_TIME,
                DatabaseContract.BookAppointment.COLUMN_NAME_REMARK,
                DatabaseContract.BookAppointment.COLUMN_NAME_COMPLAINT_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_BLOOD_GROUP_ID,
                DatabaseContract.BookAppointment.COLUMN_NAME_RESCHEDULINGREASON,
                DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENTID,
                DatabaseContract.BookAppointment.COLUMN_NAME_VISITID,
                DatabaseContract.BookAppointment.COLUMN_NAME_VISIT_TYPEID
        };

        private ContentValues BookAppointmentToContentValues(BookAppointment bookAppointment) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_ID, bookAppointment.getID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_UNIT_ID, bookAppointment.getUnitID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_PATIENT_ID, bookAppointment.getPatientID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_MRNo, bookAppointment.getMRNo());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_AGE, bookAppointment.getAge());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_REGISTRATION_DATE, bookAppointment.getRegistrationDate());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_CLINIC_NAME, bookAppointment.getClinicName());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_FIRST_NAME, bookAppointment.getFirstName());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_MIDDLE_NAME, bookAppointment.getMiddleName());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_LAST_NAME, bookAppointment.getLastName());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_GENDER_ID, bookAppointment.getGenderID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_DOB, bookAppointment.getDOB());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_MATRITAL_STATUS, bookAppointment.getMaritalStatus());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_MATRITAL_STATUS_ID, bookAppointment.getMaritalStatusID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_CONTACT1, bookAppointment.getContact1());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_EMAIL_ID, bookAppointment.getEmailId());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_BLOOD_GROUP_ID, bookAppointment.getBloodGroupID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENTID, bookAppointment.getAppointmentId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<BookAppointment> CursorToBookAppointmentList(Cursor result) {
            ArrayList<BookAppointment> bookAppointmentArrayList = null;
            try {
                if (result != null) {
                    bookAppointmentArrayList = new ArrayList<BookAppointment>();
                    while (result.moveToNext()) {
                        BookAppointment bookAppointment = new BookAppointment();
                        bookAppointment.setID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_ID)));
                        bookAppointment.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_UNIT_ID)));
                        bookAppointment.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_PATIENT_ID)));
                        bookAppointment.setMRNo(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_MRNo)));
                        bookAppointment.setAge(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_AGE)));
                        bookAppointment.setRegistrationDate(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_REGISTRATION_DATE)));
                        bookAppointment.setClinicName(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_CLINIC_NAME)));
                        bookAppointment.setFirstName(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_FIRST_NAME)));
                        bookAppointment.setMiddleName(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_MIDDLE_NAME)));
                        bookAppointment.setLastName(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_LAST_NAME)));
                        bookAppointment.setGenderID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_GENDER_ID)));
                        bookAppointment.setDOB(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_DOB)));
                        bookAppointment.setMaritalStatus(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_MATRITAL_STATUS)));
                        bookAppointment.setMaritalStatusID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_MATRITAL_STATUS_ID)));
                        bookAppointment.setContact1(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_CONTACT1)));
                        bookAppointment.setEmailId(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_EMAIL_ID)));
                        bookAppointment.setDepartmentID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_DEPARTMENT_ID)));
                        bookAppointment.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_ID)));
                        bookAppointment.setDoctorUnitID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_UNIT_ID)));
                        bookAppointment.setDoctorName(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_NAME)));
                        bookAppointment.setDoctorEducation(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_EDUCATION)));
                        bookAppointment.setDoctorMobileNo(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_MOBILE_NO)));
                        bookAppointment.setSpecialization(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_SPECIALIZATION)));
                        bookAppointment.setAppointmentReasonID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENT_REASON_ID)));
                        bookAppointment.setAppointmentDate(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENT_DATE)));
                        bookAppointment.setFromTime(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_FROM_TIME)));
                        bookAppointment.setToTime(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_TO_TIME)));
                        bookAppointment.setComplaintId(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_COMPLAINT_ID)));
                        bookAppointment.setRemark(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_REMARK)));
                        bookAppointment.setBloodGroupID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_BLOOD_GROUP_ID)));
                        bookAppointment.setAppointmentId(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENTID)));
                        bookAppointment.setReSchedulingReason(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_RESCHEDULINGREASON)));
                        bookAppointment.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_VISITID)));
                        bookAppointment.setVisitTypeID(result.getString(result.getColumnIndex(DatabaseContract.BookAppointment.COLUMN_NAME_VISIT_TYPEID)));
                        bookAppointmentArrayList.add(bookAppointment);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bookAppointmentArrayList;
        }

        public long create(BookAppointment bookAppointment) {
            long rowId = -1;
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.BookAppointment.TABLE_NAME, null, null);
                databaseContract.close();

                ContentValues values = BookAppointmentToContentValues(bookAppointment);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.BookAppointment.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateDoctor(BookAppointment bookAppointment) {
            long rowId = -1;
            try {
                int MaxId = MaxID();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_ID, bookAppointment.getDoctorID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_UNIT_ID, bookAppointment.getDoctorUnitID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_NAME, bookAppointment.getDoctorName());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_SPECIALIZATION, bookAppointment.getSpecialization());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_EDUCATION, bookAppointment.getDoctorEducation());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_DOCTOR_MOBILE_NO, bookAppointment.getDoctorMobileNo());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_VISIT_TYPEID, bookAppointment.getVisitTypeID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_VISITID, bookAppointment.getVisitID());
                rowId = databaseContract.open().update(
                        DatabaseContract.BookAppointment.TABLE_NAME, values,
                        DatabaseContract.BookAppointment._ID + "=" + MaxId,
                        null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateTimeSlot(BookAppointment bookAppointment) {
            long rowId = -1;
            try {
                int MaxId = MaxID();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENT_DATE, bookAppointment.getAppointmentDate());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_FROM_TIME, bookAppointment.getFromTime());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_TO_TIME, bookAppointment.getToTime());
                rowId = databaseContract.open().update(
                        DatabaseContract.BookAppointment.TABLE_NAME, values,
                        DatabaseContract.BookAppointment._ID + "= " + MaxId,
                        null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateAppointment(BookAppointment bookAppointment) {
            long rowId = -1;
            try {
                int MaxId = MaxID();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_DEPARTMENT_ID, bookAppointment.getDepartmentID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_APPOINTMENT_REASON_ID, bookAppointment.getAppointmentReasonID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_COMPLAINT_ID, bookAppointment.getComplaintId());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_REMARK, bookAppointment.getRemark());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_RESCHEDULINGREASON, bookAppointment.getReSchedulingReason());
                rowId = databaseContract.open().update(
                        DatabaseContract.BookAppointment.TABLE_NAME, values,
                        DatabaseContract.BookAppointment._ID + "= " + MaxId,
                        null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateVisitID(BookAppointment bookAppointment) {
            long rowId = -1;
            try {
                int MaxId = MaxID();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_VISITID, bookAppointment.getVisitID());
                values.put(DatabaseContract.BookAppointment.COLUMN_NAME_VISIT_TYPEID, bookAppointment.getVisitTypeID());
                rowId = databaseContract.open().update(
                        DatabaseContract.BookAppointment.TABLE_NAME, values,
                        DatabaseContract.BookAppointment._ID + "= " + MaxId,
                        null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public int MaxID() {
            int MaxId = 0;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String[] projection = {
                        DatabaseContract.BookAppointment._ID,
                };
                result = db.query(DatabaseContract.BookAppointment.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.BookAppointment.DEFAULT_SORT_ORDER);
                if (result != null && result.moveToFirst()) {
                    MaxId = result.getInt(result.getColumnIndex(DatabaseContract.BookAppointment._ID));
                }
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return MaxId;
        }

        public ArrayList<BookAppointment> listAll() {
            ArrayList<BookAppointment> bookAppointmentArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;

                result = db.query(DatabaseContract.BookAppointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.BookAppointment.DEFAULT_SORT_ORDER);
                bookAppointmentArrayList = CursorToBookAppointmentList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return bookAppointmentArrayList;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                result = db.query(DatabaseContract.BookAppointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.BookAppointment.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.BookAppointment._ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.BookAppointment.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.BookAppointment.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public ArrayList<BookAppointment> listLast() {
            ArrayList<BookAppointment> bookAppointmentArrayList = null;
            Cursor result = null;
            try {
                int MaxId = MaxID();
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.BookAppointment._ID + "= " + MaxId;
                result = db.query(DatabaseContract.BookAppointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.BookAppointment.DEFAULT_SORT_ORDER);
                bookAppointmentArrayList = CursorToBookAppointmentList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return bookAppointmentArrayList;
        }
    }

    public class AppointmentAdapter {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String[] projection = {
                DatabaseContract.Appointment.COLUMN_NAME_ID,
                DatabaseContract.Appointment.COLUMN_NAME_UNIT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_UNIT_NAME,
                DatabaseContract.Appointment.COLUMN_NAME_MRNo,
                DatabaseContract.Appointment.COLUMN_NAME_PATIENT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_PATIENT_UNIT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_VISIT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_FIRST_NAME,
                DatabaseContract.Appointment.COLUMN_NAME_MIDDLE_NAME,
                DatabaseContract.Appointment.COLUMN_NAME_LAST_NAME,
                DatabaseContract.Appointment.COLUMN_NAME_DOB,
                DatabaseContract.Appointment.COLUMN_NAME_GENDER,
                DatabaseContract.Appointment.COLUMN_NAME_BLOOD_GROUP,
                DatabaseContract.Appointment.COLUMN_NAME_MATRITAL_STATUS,
                DatabaseContract.Appointment.COLUMN_NAME_GENDER_ID,
                DatabaseContract.Appointment.COLUMN_NAME_BLOOD_GROUP_ID,
                DatabaseContract.Appointment.COLUMN_NAME_MARITAL_STATUS_ID,
                DatabaseContract.Appointment.COLUMN_NAME_CONTACT1,
                DatabaseContract.Appointment.COLUMN_NAME_EMAIL_ID,
                DatabaseContract.Appointment.COLUMN_NAME_DEPARTMENT,
                DatabaseContract.Appointment.COLUMN_NAME_DEPARTMENT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_DOCTOR_ID,
                DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_REASON,
                DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_REASON_ID,
                DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE,
                DatabaseContract.Appointment.COLUMN_NAME_FROM_TIME,
                DatabaseContract.Appointment.COLUMN_NAME_TO_TIME,
                DatabaseContract.Appointment.COLUMN_NAME_SEARCH_APPOINTMENT_DATE,
                DatabaseContract.Appointment.COLUMN_NAME_REMARK,
                DatabaseContract.Appointment.COLUMN_NAME_APP_CANCEL_REASON,
                DatabaseContract.Appointment.COLUMN_NAME_STATUS,
                DatabaseContract.Appointment.COLUMN_NAME_CREATED_UNIT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK,
                DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS,
                DatabaseContract.Appointment.COLUMN_NAME_PARENT_APPOINT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_PARENT_APPOINT_UNIT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_RESCHEDUlING_REASON,
                DatabaseContract.Appointment.COLUMN_NAME_COMPLAINT,
                DatabaseContract.Appointment.COLUMN_NAME_COMPLAINT_ID,
                DatabaseContract.Appointment.COLUMN_NAME_SORTINGDATETIME,
                DatabaseContract.Appointment.COLUMN_NAME_DrName
        };

        private ContentValues AppointmentToContentValues(Appointment appointment) {
            ContentValues values = null;
            if (appointment != null) {
                values = new ContentValues();
                values.put(DatabaseContract.Appointment.COLUMN_NAME_ID, appointment.getID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_UNIT_ID, appointment.getUnitID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_UNIT_NAME, appointment.getUnitName());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_MRNo, appointment.getMRNo());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_PATIENT_ID, appointment.getPatientID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_PATIENT_UNIT_ID, appointment.getPatientUnitID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_VISIT_ID, appointment.getVisitID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_FIRST_NAME, appointment.getFirstName());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_MIDDLE_NAME, appointment.getMiddleName());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_LAST_NAME, appointment.getLastName());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_DOB, appointment.getDOB());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_GENDER, appointment.getGender());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_BLOOD_GROUP, appointment.getBloodGroup());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_MATRITAL_STATUS, appointment.getMaritalStatus());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_GENDER_ID, appointment.getGenderID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_BLOOD_GROUP_ID, appointment.getBloodGroupID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_MARITAL_STATUS_ID, appointment.getMaritalStatusID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_CONTACT1, appointment.getContact1());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_EMAIL_ID, appointment.getEmailId());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_DEPARTMENT, appointment.getDepartment());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_DEPARTMENT_ID, appointment.getDepartmentID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_DOCTOR_ID, appointment.getDoctorID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_REASON, appointment.getAppointmentReason());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_REASON_ID, appointment.getAppointmentReasonID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE, appointment.getAppointmentDate());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_FROM_TIME, appointment.getFromTime());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_TO_TIME, appointment.getToTime());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_SEARCH_APPOINTMENT_DATE, appointment.getSearchAppointmentDate());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_REMARK, appointment.getRemark());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_APP_CANCEL_REASON, appointment.getAppCancelReason());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_STATUS, appointment.getStatus());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_CREATED_UNIT_ID, appointment.getCreatedUnitID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK, appointment.getVisitMark());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS, appointment.getAppointmentStatus());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_PARENT_APPOINT_ID, appointment.getParentappointID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_PARENT_APPOINT_UNIT_ID, appointment.getParentappointUnitID());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_RESCHEDUlING_REASON, appointment.getReSchedulingReason());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_COMPLAINT, appointment.getComplaint());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_COMPLAINT_ID, appointment.getComplaintId());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_SORTINGDATETIME, appointment.getSortingDateTime());
                values.put(DatabaseContract.Appointment.COLUMN_NAME_DrName, appointment.getDrName());
            }
            return values;
        }

        private ArrayList<Appointment> CursorToAppointmentArrayList(Cursor result) {
            ArrayList<Appointment> appointmentArrayList = null;
            if (result != null) {
                appointmentArrayList = new ArrayList<Appointment>();
                while (result.moveToNext()) {
                    Appointment appointment = new Appointment();
                    appointment.setID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_ID)));
                    appointment.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_UNIT_ID)));
                    appointment.setUnitName(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_UNIT_NAME)));
                    appointment.setMRNo(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_MRNo)));
                    appointment.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_PATIENT_ID)));
                    appointment.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_PATIENT_UNIT_ID)));
                    appointment.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_VISIT_ID)));
                    appointment.setFirstName(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_FIRST_NAME)));
                    appointment.setMiddleName(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_MIDDLE_NAME)));
                    appointment.setLastName(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_LAST_NAME)));
                    appointment.setDOB(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_DOB)));
                    appointment.setGender(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_GENDER)));
                    appointment.setBloodGroup(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_BLOOD_GROUP)));
                    appointment.setMaritalStatus(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_MATRITAL_STATUS)));
                    appointment.setGenderID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_GENDER_ID)));
                    appointment.setBloodGroupID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_BLOOD_GROUP_ID)));
                    appointment.setMaritalStatusID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_MARITAL_STATUS_ID)));
                    appointment.setContact1(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_CONTACT1)));
                    appointment.setEmailId(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_EMAIL_ID)));
                    appointment.setDepartment(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_DEPARTMENT)));
                    appointment.setDepartmentID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_DEPARTMENT_ID)));
                    appointment.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_DOCTOR_ID)));
                    appointment.setAppointmentReason(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_REASON)));
                    appointment.setAppointmentReasonID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_REASON_ID)));
                    appointment.setAppointmentDate(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE)));
                    appointment.setFromTime(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_FROM_TIME)));
                    appointment.setToTime(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_TO_TIME)));
                    appointment.setSearchAppointmentDate(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_SEARCH_APPOINTMENT_DATE)));
                    appointment.setRemark(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_REMARK)));
                    appointment.setAppCancelReason(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_APP_CANCEL_REASON)));
                    appointment.setStatus(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_STATUS)));
                    appointment.setCreatedUnitID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_CREATED_UNIT_ID)));
                    appointment.setVisitMark(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK)));
                    appointment.setAppointmentStatus(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS)));
                    appointment.setParentappointID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_PARENT_APPOINT_ID)));
                    appointment.setParentappointUnitID(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_PARENT_APPOINT_UNIT_ID)));
                    appointment.setReSchedulingReason(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_RESCHEDUlING_REASON)));
                    appointment.setComplaint(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_COMPLAINT)));
                    appointment.setComplaintId(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_COMPLAINT_ID)));
                    appointment.setSortingDateTime(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_SORTINGDATETIME)));
                    appointment.setDrName(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_DrName)));
                    appointmentArrayList.add(appointment);
                }
                result.close();
            }
            return appointmentArrayList;
        }

        public long create(Appointment appointment) {
            long rowId = -1;
            try {
                ArrayList<Appointment> listPatient = listAllID(appointment.getID());
                if (listPatient.size() == 0) {
                    if (Count(appointment.getID()) == 0) {
                        ContentValues values = AppointmentToContentValues(appointment);
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Appointment.TABLE_NAME, null, values);
                    }
                } else {
                    update(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Appointment> listAll() {
            ArrayList<Appointment> appointmentArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                result = db.query(DatabaseContract.Appointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER);
                appointmentArrayList = CursorToAppointmentArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return appointmentArrayList;
        }

        public ArrayList<Appointment> listAllID(String ID) {
            ArrayList<Appointment> appointmentArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null && ID.length() > 0) {
                    whereClause = DatabaseContract.Appointment.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.Appointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER);
                appointmentArrayList = CursorToAppointmentArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return appointmentArrayList;
        }

        public ArrayList<Appointment> listAllWithDate(String Date) {
            ArrayList<Appointment> appointmentArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (Date != null && Date.length() > 0) {
                    whereClause = DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE + "='" + Date + "'";
                }
                result = db.query(DatabaseContract.Appointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER);
                appointmentArrayList = CursorToAppointmentArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return appointmentArrayList;
        }

        public ArrayList<Appointment> listAll(String date, String patientName) {
            ArrayList<Appointment> appointmentArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (patientName != null && patientName.trim().length() > 0 && date != null && date.trim().length() > 0) {
                    whereClause = DatabaseContract.Appointment.COLUMN_NAME_FIRST_NAME + " LIKE '" + patientName + "%' AND "
                            + DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE + " = '" + date + "'  AND  " +
                            DatabaseContract.Appointment.COLUMN_NAME_STATUS + " = 'True'  AND  " +
                            DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK + "= 'False' AND " +
                            "(" + DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 0 OR  " +
                            DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 2 )";
                } else {
                    whereClause =
                            DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE + " = '" + date + "'  AND  " +
                                    DatabaseContract.Appointment.COLUMN_NAME_STATUS + " = 'True' AND  " +
                                    DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK + "= 'False'";//AND " +
                    //"(" + DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 0 OR  " +
                    //DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 2 )";

                }
                result = db.query(DatabaseContract.Appointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER);
                appointmentArrayList = CursorToAppointmentArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return appointmentArrayList;
        }

        public int listAllCount(String date, String patientName) {
            int headerCount = 0;
            ArrayList<Appointment> listAppointmentchild = null;
            try {
                listAppointmentchild = listAll(date, patientName);
                if (listAppointmentchild != null && listAppointmentchild.size() > 0) {
                    headerCount = listAppointmentchild.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return headerCount;
        }

        public ArrayList<String> listHeader(String patientName, String FromDate, String ToDate) {

            ArrayList<String> headerList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                String date = localSetting.dateToStirng(new Date(), Constants.SEARCH_DATE_FORMAT);
                String CurrentDate = dateFormat.format(new Date());
                //String toFormate = "MM-dd-yyyy";
                if (patientName != null && patientName.trim().length() > 0 && FromDate != null && FromDate.trim().length() > 0 && ToDate != null && ToDate.trim().length() > 0) {

                    //FromDate = localSetting.formatDate(FromDate, Constants.SEARCH_DATE_FORMAT, toFormate);
                    // ToDate = localSetting.formatDate(ToDate, Constants.SEARCH_DATE_FORMAT, toFormate);

                    whereClause = DatabaseContract.Appointment.COLUMN_NAME_FIRST_NAME + " LIKE '" + patientName + "%' AND " +
                            DatabaseContract.Appointment.COLUMN_NAME_SEARCH_APPOINTMENT_DATE + " BETWEEN DATE('" + FromDate + "')  AND  DATE('" + ToDate + "') AND " +
                            DatabaseContract.Appointment.COLUMN_NAME_STATUS + " = 'True'  AND  " +
                            DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK + "= 'False' AND " +
                            "(" + DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 0 OR  " +
                            DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 2 )";
                } else if (FromDate != null && FromDate.trim().length() > 0 && ToDate != null && ToDate.trim().length() > 0) {

                    // FromDate = localSetting.formatDate(FromDate, Constants.SEARCH_DATE_FORMAT, toFormate);
                    //ToDate = localSetting.formatDate(ToDate, Constants.SEARCH_DATE_FORMAT, toFormate);

                    whereClause =
                            DatabaseContract.Appointment.COLUMN_NAME_SEARCH_APPOINTMENT_DATE + " BETWEEN DATE('" + FromDate + "')  AND  DATE('" + ToDate + "') AND  " +
                                    DatabaseContract.Appointment.COLUMN_NAME_STATUS + " = 'True'  AND  " +
                                    DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK + "= 'False'";// AND " +
                    //"(" + DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 0 OR  " +
                    //DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 2 )";
                } else if (patientName != null && patientName.trim().length() > 0 && FromDate != null && FromDate.trim().length() > 0) {
                    //FromDate = localSetting.formatDate(FromDate, Constants.SEARCH_DATE_FORMAT, toFormate);

                    whereClause = DatabaseContract.Appointment.COLUMN_NAME_FIRST_NAME + " LIKE '" + patientName + "%' AND " +
                            "DATE(" + DatabaseContract.Appointment.COLUMN_NAME_SEARCH_APPOINTMENT_DATE + ")>= DATE('" + FromDate + "')" + " AND  " +
                            DatabaseContract.Appointment.COLUMN_NAME_STATUS + " = 'True'  AND  " +
                            DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK + "= 'False' AND " +
                            "(" + DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 0 OR  " +
                            DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 2 )";
                } else if (FromDate != null && FromDate.trim().length() > 0) {
                    // FromDate = localSetting.formatDate(FromDate, Constants.SEARCH_DATE_FORMAT, toFormate);

                    whereClause = "DATE(" + DatabaseContract.Appointment.COLUMN_NAME_SEARCH_APPOINTMENT_DATE + ")>= DATE('" + FromDate + "')" + " AND  " +
                            DatabaseContract.Appointment.COLUMN_NAME_STATUS + " = 'True'  AND  " +
                            DatabaseContract.Appointment.COLUMN_NAME_VISIT_MARK + "= 'False' AND " +
                            "(" + DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 0 OR  " +
                            DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_STATUS + "= 2 )";
                }
                result = db.query(true, DatabaseContract.Appointment.TABLE_NAME,
                        new String[]{DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE}, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER, null);
                if (result != null) {
                    headerList = new ArrayList<String>();
                    while (result.moveToNext()) {
                        headerList.add(result.getString(result.getColumnIndex(DatabaseContract.Appointment.COLUMN_NAME_APPOINTMENT_DATE)));
                    }
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return headerList;
        }

        public ArrayList<Appointment> listAllAppointmentWithUnit(String UnitID) {
            ArrayList<Appointment> appointmentArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (UnitID != null && UnitID.equals("1")) {
                    whereClause = null;
                } else {
                    whereClause = DatabaseContract.Appointment.COLUMN_NAME_UNIT_ID + " = '" + UnitID + "' ";
                }
                result = db.query(DatabaseContract.Appointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER);
                appointmentArrayList = CursorToAppointmentArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return appointmentArrayList;
        }

        public int HeaderCount(String patientID, String FromDate, String ToDate) {
            int headerCount = 0;
            ArrayList<String> listAppointmentHeader = null;
            try {
                listAppointmentHeader = listHeader(patientID, FromDate, ToDate);
                if (listAppointmentHeader != null && listAppointmentHeader.size() > 0) {
                    headerCount = listAppointmentHeader.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return headerCount;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = DatabaseContract.Appointment.COLUMN_NAME_ID + " ='" + ID + "'";
                result = db.query(DatabaseContract.Appointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                result = db.query(DatabaseContract.Appointment.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Appointment.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public long update(Appointment appointment) {
            long rowId = -1;
            try {
                ContentValues values = AppointmentToContentValues(appointment);
                String WhereClause = null;
                WhereClause = DatabaseContract.Appointment.COLUMN_NAME_ID + " ='" + appointment.getID() + "'";
                rowId = databaseContract.open().update(
                        DatabaseContract.Appointment.TABLE_NAME, values, WhereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete() {
            try {
                String whereClause = null;
                databaseContract.open().delete(DatabaseContract.Appointment.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
        }
    }

    public class PatientAdapter {

        String[] projection = {
                DatabaseContract.Patient.COLUMN_NAME_ID,
                DatabaseContract.Patient.COLUMN_NAME_UNITID,
                DatabaseContract.Patient.COLUMN_NAME_MRNO,
                DatabaseContract.Patient.COLUMN_NAME_CLINIC,
                DatabaseContract.Patient.COLUMN_NAME_REGISTRATIONDATE,
                DatabaseContract.Patient.COLUMN_NAME_LAST_NAME,
                DatabaseContract.Patient.COLUMN_NAME_FIRST_NAME,
                DatabaseContract.Patient.COLUMN_NAME_MIDDLE_NAME,
                DatabaseContract.Patient.COLUMN_NAME_FIMLIY_NAME,
                DatabaseContract.Patient.COLUMN_NAME_GENDER,
                DatabaseContract.Patient.COLUMN_NAME_DATEOFBIRTH,
                DatabaseContract.Patient.COLUMN_NAME_EDUCATION,
                DatabaseContract.Patient.COLUMN_NAME_MARITALSTATUS,
                DatabaseContract.Patient.COLUMN_NAME_CONTACTNO1,
                DatabaseContract.Patient.COLUMN_NAME_EMAIL,
                DatabaseContract.Patient.COLUMN_NAME_AGE,
                DatabaseContract.Patient.COLUMN_NAME_GENDERID,
                DatabaseContract.Patient.COLUMN_NAME_MARITALSTATUSID,
                DatabaseContract.Patient.COLUMN_NAME_BLOOD_GROUP,
                DatabaseContract.Patient.COLUMN_NAME_BLOOD_GROUP_ID
        };

        private ContentValues PatientToContentValues(Patient patient) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Patient.COLUMN_NAME_ID, patient.getID());
                values.put(DatabaseContract.Patient.COLUMN_NAME_UNITID, patient.getUnitID());
                values.put(DatabaseContract.Patient.COLUMN_NAME_CLINIC, patient.getClinicName());
                values.put(DatabaseContract.Patient.COLUMN_NAME_MRNO, patient.getMRNo());
                values.put(DatabaseContract.Patient.COLUMN_NAME_REGISTRATIONDATE, patient.getRegistrationDate());
                values.put(DatabaseContract.Patient.COLUMN_NAME_LAST_NAME, patient.getLastName());
                values.put(DatabaseContract.Patient.COLUMN_NAME_FIRST_NAME, patient.getFirstName());
                values.put(DatabaseContract.Patient.COLUMN_NAME_MIDDLE_NAME, patient.getMiddleName());
                values.put(DatabaseContract.Patient.COLUMN_NAME_FIMLIY_NAME, patient.getFamilyName());
                values.put(DatabaseContract.Patient.COLUMN_NAME_GENDER, patient.getGender());
                values.put(DatabaseContract.Patient.COLUMN_NAME_DATEOFBIRTH, patient.getDateOfBirth());
                values.put(DatabaseContract.Patient.COLUMN_NAME_MARITALSTATUS, patient.getMaritalStatus());
                values.put(DatabaseContract.Patient.COLUMN_NAME_CONTACTNO1, patient.getContactNo1());
                values.put(DatabaseContract.Patient.COLUMN_NAME_EMAIL, patient.getEmail());
                values.put(DatabaseContract.Patient.COLUMN_NAME_AGE, patient.getAge());
                values.put(DatabaseContract.Patient.COLUMN_NAME_GENDERID, patient.getGenderID());
                values.put(DatabaseContract.Patient.COLUMN_NAME_MARITALSTATUSID, patient.getMaritalStatusID());
                values.put(DatabaseContract.Patient.COLUMN_NAME_BLOOD_GROUP, patient.getBloodGroup());
                values.put(DatabaseContract.Patient.COLUMN_NAME_BLOOD_GROUP_ID, patient.getBloodGroupID());
            } catch (Exception e) {
                Log.e(Constants.TAG, "" + e);
            }
            return values;
        }

        private ArrayList<Patient> CursorToArraylist(Cursor result) {
            ArrayList<Patient> listPatient = null;
            try {
                if (result != null) {
                    listPatient = new ArrayList<Patient>();
                    while (result.moveToNext()) {
                        Patient patient = new Patient();
                        patient.setID(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_ID)));
                        patient.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_UNITID)));
                        patient.setMRNo(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_MRNO)));
                        patient.setClinicName(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_CLINIC)));
                        patient.setRegistrationDate(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_REGISTRATIONDATE)));
                        patient.setLastName(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_LAST_NAME)));
                        patient.setFirstName(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_FIRST_NAME)));
                        patient.setMiddleName(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_MIDDLE_NAME)));
                        patient.setFamilyName(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_FIMLIY_NAME)));
                        patient.setGender(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_GENDER)));
                        patient.setDateOfBirth(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_DATEOFBIRTH)));
                        patient.setMaritalStatus(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_MARITALSTATUS)));
                        patient.setContactNo1(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_CONTACTNO1)));
                        patient.setEmail(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_EMAIL)));
                        patient.setAge(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_AGE)));
                        patient.setGenderID(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_GENDERID)));
                        patient.setMaritalStatusID(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_MARITALSTATUSID)));
                        patient.setBloodGroup(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_BLOOD_GROUP)));
                        patient.setBloodGroupID(result.getString(result.getColumnIndex(DatabaseContract.Patient.COLUMN_NAME_BLOOD_GROUP_ID)));
                        listPatient.add(patient);
                    }
                    result.close();
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "" + e);
            }
            return listPatient;
        }

        public long create(Patient patient) {
            long rowId = -1;
            try {
                ContentValues values = PatientToContentValues(patient);
                if (values != null) {
                    rowId = databaseContract.open().insert(
                            DatabaseContract.Patient.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(Patient patient) {
            long rowId = -1;
            try {
                ContentValues values = PatientToContentValues(patient);
                String whereClause = null;
                whereClause = DatabaseContract.Patient.COLUMN_NAME_ID + "='" + patient.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.Patient.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public int Count(String UnitID, String PatientName) {
            int Count = 0;
            ArrayList<Patient> listPatient = listPatient(UnitID, PatientName);
            if (listPatient.size() > 0) {
                Count = listPatient.size();
            }
            return Count;
        }

        public ArrayList<Patient> listPatient(String UnitID, String PatientName) {
            ArrayList<Patient> listPatient = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (UnitID.equals("1")) {   // For head office
                    if (PatientName != null && PatientName.length() > 0) {
                        whereClause = //DatabaseContract.Patient.COLUMN_NAME_UNITID + "='" + UnitID + "' AND"+
                                DatabaseContract.Patient.COLUMN_NAME_FIRST_NAME + " LIKE '" + PatientName + "%'";
                    } else {
                        //whereClause = DatabaseContract.Patient.COLUMN_NAME_UNITID + "='" + UnitID + "'";
                    }
                } else {
                    if (PatientName != null && PatientName.length() > 0) {
                        whereClause = DatabaseContract.Patient.COLUMN_NAME_UNITID + "='" + UnitID + "' AND"
                                + DatabaseContract.Patient.COLUMN_NAME_FIRST_NAME + " LIKE '" + PatientName + "%'";
                    } else {
                        whereClause = DatabaseContract.Patient.COLUMN_NAME_UNITID + "='" + UnitID + "'";
                    }
                }
                result = db.query(DatabaseContract.Patient.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Patient.DEFAULT_SORT_ORDER);
                listPatient = CursorToArraylist(result);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, e.getMessage());
            } finally {
                databaseContract.close();
                result.close();
            }
            return listPatient;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Patient.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Patient.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, e.getMessage());
            } finally {
                databaseContract.close();

            }
            return Count;
        }

        public ArrayList<Patient> listAll() {
            ArrayList<Patient> listPatient = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                result = db.query(DatabaseContract.Patient.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Patient.DEFAULT_SORT_ORDER);
                listPatient = CursorToArraylist(result);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, e.getMessage());
            } finally {
                databaseContract.close();
                result.close();
            }
            return listPatient;
        }

        public void delete(String UnitID) {
            try {
                String whereClause = null;
                if (UnitID.equals("1")) {  // For head office

                } else {
                    whereClause = DatabaseContract.Patient.COLUMN_NAME_UNITID + "='" + UnitID + "'";
                }
                databaseContract.open().delete(DatabaseContract.Patient.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
        }
    }

    public class PatientQueueAdapter {

        String[] projection = {
                DatabaseContract.PatientQueue.COLUMN_NAME_ID,
                DatabaseContract.PatientQueue.COLUMN_NAME_UNITID,
                DatabaseContract.PatientQueue.COLUMN_NAME_UnitName,
                DatabaseContract.PatientQueue.COLUMN_NAME_DATE,
                DatabaseContract.PatientQueue.COLUMN_NAME_FROMTIME,
                DatabaseContract.PatientQueue.COLUMN_NAME_TOTIME,
                DatabaseContract.PatientQueue.COLUMN_NAME_QUEUETIME,
                DatabaseContract.PatientQueue.COLUMN_NAME_VISITID,
                DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTID,
                DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTUNITID,
                DatabaseContract.PatientQueue.COLUMN_NAME_OPDNO,
                DatabaseContract.PatientQueue.COLUMN_NAME_VISITTYPEID,
                DatabaseContract.PatientQueue.COLUMN_NAME_VISITDESCRIPTION,
                DatabaseContract.PatientQueue.COLUMN_NAME_MRNO,
                DatabaseContract.PatientQueue.COLUMN_NAME_FIRSTNAME,
                DatabaseContract.PatientQueue.COLUMN_NAME_MIDDLENAME,
                DatabaseContract.PatientQueue.COLUMN_NAME_LASTNAME,
                DatabaseContract.PatientQueue.COLUMN_NAME_DATEOFBIRTH,
                DatabaseContract.PatientQueue.COLUMN_NAME_EMAIL,
                DatabaseContract.PatientQueue.COLUMN_NAME_CONTACTNO1,
                DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUSID,
                DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUS,
                DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUPID,
                DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUP,
                DatabaseContract.PatientQueue.COLUMN_NAME_GENDERID,
                DatabaseContract.PatientQueue.COLUMN_NAME_GENDER,
                DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTSID,
                DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTS,
                DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENTID,
                DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENT,
                DatabaseContract.PatientQueue.COLUMN_NAME_DOCTORID,
                DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTORID,
                DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTOR,
                DatabaseContract.PatientQueue.COLUMN_NAME_ISBILLED_SUBMITID,
                DatabaseContract.PatientQueue.COLUMN_NAME_ISSTATUS,
                DatabaseContract.PatientQueue.COLUMN_NAME_DrName

        };

        private ContentValues PatientQueueToContentValues(PatientQueue patientQueue) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_ID, patientQueue.getID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_UNITID, patientQueue.getUnitId());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_UnitName, patientQueue.getUnitName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DATE, patientQueue.getDate());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_FROMTIME, patientQueue.getFromTime());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_TOTIME, patientQueue.getToTime());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_QUEUETIME, patientQueue.getQueuetime());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_VISITID, patientQueue.getVisitID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTID, patientQueue.getPatientId());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTUNITID, patientQueue.getPatientUnitID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_OPDNO, patientQueue.getOPDNO());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_VISITTYPEID, patientQueue.getVisitTypeID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_VISITDESCRIPTION, patientQueue.getVisitDescription());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MRNO, patientQueue.getMRNo());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_FIRSTNAME, patientQueue.getFirstName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MIDDLENAME, patientQueue.getMiddleName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_LASTNAME, patientQueue.getLastName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DATEOFBIRTH, patientQueue.getDateOfBirth());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_EMAIL, patientQueue.getEmail());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_CONTACTNO1, patientQueue.getContactNo1());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUSID, patientQueue.getMaritalStatusID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUS, patientQueue.getMaritalStatus());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUP, patientQueue.getBloodGroup());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUPID, patientQueue.getBloodGroupID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_GENDERID, patientQueue.getGenderID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_GENDER, patientQueue.getGender());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTSID, patientQueue.getComplaintsID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTS, patientQueue.getComplaints());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENTID, patientQueue.getDepartmentID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENT, patientQueue.getDepartment());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DOCTORID, patientQueue.getDoctorID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTORID, patientQueue.getReferredDoctorID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTOR, patientQueue.getReferredDoctor());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_ISBILLED_SUBMITID, patientQueue.getIsBilledSubmitID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_ISSTATUS, "1");
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DrName, patientQueue.getDrName());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ContentValues PatientQueueToContentValuesUPdate(PatientQueue patientQueue) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_ID, patientQueue.getID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_UNITID, patientQueue.getUnitId());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_UnitName, patientQueue.getUnitName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DATE, patientQueue.getDate());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_FROMTIME, patientQueue.getFromTime());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_TOTIME, patientQueue.getToTime());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_QUEUETIME, patientQueue.getQueuetime());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_VISITID, patientQueue.getVisitID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTID, patientQueue.getPatientId());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTUNITID, patientQueue.getPatientUnitID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_OPDNO, patientQueue.getOPDNO());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_VISITTYPEID, patientQueue.getVisitTypeID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_VISITDESCRIPTION, patientQueue.getVisitDescription());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MRNO, patientQueue.getMRNo());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_FIRSTNAME, patientQueue.getFirstName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MIDDLENAME, patientQueue.getMiddleName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_LASTNAME, patientQueue.getLastName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DATEOFBIRTH, patientQueue.getDateOfBirth());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_EMAIL, patientQueue.getEmail());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_CONTACTNO1, patientQueue.getContactNo1());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUSID, patientQueue.getMaritalStatusID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUS, patientQueue.getMaritalStatus());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUP, patientQueue.getBloodGroup());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUPID, patientQueue.getBloodGroupID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_GENDERID, patientQueue.getGenderID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_GENDER, patientQueue.getGender());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTSID, patientQueue.getComplaintsID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTS, patientQueue.getComplaints());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENTID, patientQueue.getDepartmentID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENT, patientQueue.getDepartment());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DOCTORID, patientQueue.getDoctorID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTORID, patientQueue.getReferredDoctorID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTOR, patientQueue.getReferredDoctor());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_ISBILLED_SUBMITID, patientQueue.getIsBilledSubmitID());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_DrName, patientQueue.getDrName());
                values.put(DatabaseContract.PatientQueue.COLUMN_NAME_ISSTATUS, "0");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<PatientQueue> CursorToArrayList(Cursor result) {
            ArrayList<PatientQueue> listPatientQueue = null;
            try {
                if (result != null) {
                    listPatientQueue = new ArrayList<PatientQueue>();
                    while (result.moveToNext()) {
                        PatientQueue patientQueue = new PatientQueue();
                        patientQueue.setID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_ID)));
                        patientQueue.setUnitId(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_UNITID)));
                        patientQueue.setUnitName(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_UnitName)));
                        patientQueue.setDate(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_DATE)));
                        patientQueue.setFromTime(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_FROMTIME)));
                        patientQueue.setToTime(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_TOTIME)));
                        patientQueue.setQueuetime(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_QUEUETIME)));
                        patientQueue.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_VISITID)));
                        patientQueue.setPatientId(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTID)));
                        patientQueue.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_PATIENTUNITID)));
                        patientQueue.setOPDNO(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_OPDNO)));
                        patientQueue.setVisitTypeID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_VISITTYPEID)));
                        patientQueue.setVisitDescription(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_VISITDESCRIPTION)));
                        patientQueue.setMRNo(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_MRNO)));
                        patientQueue.setFirstName(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_FIRSTNAME)));
                        patientQueue.setMiddleName(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_MIDDLENAME)));
                        patientQueue.setLastName(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_LASTNAME)));
                        patientQueue.setDateOfBirth(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_DATEOFBIRTH)));
                        patientQueue.setEmail(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_EMAIL)));
                        patientQueue.setContactNo1(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_CONTACTNO1)));
                        patientQueue.setMaritalStatusID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUSID)));
                        patientQueue.setMaritalStatus(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_MARITALSTATUS)));
                        patientQueue.setBloodGroupID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUPID)));
                        patientQueue.setBloodGroup(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_BLOODGROUP)));
                        patientQueue.setGenderID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_GENDERID)));
                        patientQueue.setGender(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_GENDER)));
                        patientQueue.setComplaintsID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTSID)));
                        patientQueue.setComplaints(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_COMPLAINTS)));
                        patientQueue.setDepartmentID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENTID)));
                        patientQueue.setDepartment(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_DEPARTMENT)));
                        patientQueue.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_DOCTORID)));
                        patientQueue.setReferredDoctorID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTORID)));
                        patientQueue.setReferredDoctor(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_REFERREDDOCTOR)));
                        patientQueue.setIsBilledSubmitID(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_ISBILLED_SUBMITID)));
                        patientQueue.setDrName(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_DrName)));
                        patientQueue.setISStatus(result.getString(result.getColumnIndex(DatabaseContract.PatientQueue.COLUMN_NAME_ISSTATUS)));

                        listPatientQueue.add(patientQueue);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listPatientQueue;
        }

        public long create(PatientQueue patientQueue) {
            long rowId = -1;
            try {
                //ArrayList<PatientQueue> listPatient = listAll(patientQueue.getID());
                //if (listPatient.size() == 0) {
                if (Count(patientQueue.getID()) == 0) {
                    ContentValues values = PatientQueueToContentValues(patientQueue);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.PatientQueue.TABLE_NAME, null, values);
                    }
                } else {
                    update(patientQueue);
                /*} else {
                    update(patientQueue);
                    delete();*/
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(PatientQueue patientQueue) {
            long rowId = -1;
            try {
                ContentValues values = PatientQueueToContentValues(patientQueue);
                String whereClause = null;
                whereClause = DatabaseContract.PatientQueue.COLUMN_NAME_ID + "='" + patientQueue.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.PatientQueue.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<PatientQueue> listAll(String ID) {
            ArrayList<PatientQueue> listPatientQueue = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.PatientQueue.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.PatientQueue.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.PatientQueue.DEFAULT_SORT_ORDER);
                listPatientQueue = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listPatientQueue;
        }

        public ArrayList<PatientQueue> listAll() {
            ArrayList<PatientQueue> listPatientQueue = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                result = db.query(DatabaseContract.PatientQueue.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.PatientQueue.DEFAULT_SORT_ORDER);

                listPatientQueue = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listPatientQueue;
        }

        public ArrayList<PatientQueue> listToday(String UnitID, String PatientName) {
            ArrayList<PatientQueue> patientQueueArrayList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                String CurrentDate = new SimpleDateFormat(Constants.PATIENT_QUEUE_DATE, Locale.getDefault()).format(new Date());
                if (UnitID.equals("1")) {
                    if (PatientName != null && CurrentDate != null) {
                        whereClause =
                                //DatabaseContract.CPOEMedicine.COLUMN_NAME_UNITID + "='" + UnitID + "' AND " +
                                DatabaseContract.PatientQueue.COLUMN_NAME_FIRSTNAME + " LIKE '" + PatientName + "%' AND " +
                                        DatabaseContract.PatientQueue.COLUMN_NAME_DATE + "='" + CurrentDate + "'";
                    } else if (CurrentDate != null) {
                        whereClause =
                                //DatabaseContract.CPOEMedicine.COLUMN_NAME_UNITID + "='" + UnitID + "' AND " +
                                DatabaseContract.PatientQueue.COLUMN_NAME_DATE + "='" + CurrentDate + "'";
                    }
                } else {
                    if (PatientName != null && CurrentDate != null) {
                        whereClause =
                                DatabaseContract.CPOEMedicine.COLUMN_NAME_UNITID + "='" + UnitID + "' AND " +
                                        DatabaseContract.PatientQueue.COLUMN_NAME_FIRSTNAME + " LIKE '" + PatientName + "%' AND " +
                                        DatabaseContract.PatientQueue.COLUMN_NAME_DATE + "='" + CurrentDate + "'";
                    } else if (CurrentDate != null) {
                        whereClause =
                                DatabaseContract.CPOEMedicine.COLUMN_NAME_UNITID + "='" + UnitID + "' AND " +
                                        DatabaseContract.PatientQueue.COLUMN_NAME_DATE + "='" + CurrentDate + "'";
                    }
                }
                result = db.query(DatabaseContract.PatientQueue.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.PatientQueue.DEFAULT_SORT_ORDER);
                patientQueueArrayList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return patientQueueArrayList;
        }

        public int CountToday(String UnitID, String PatientName) {
            int Count = 0;
            ArrayList<PatientQueue> patientQueueArrayList = null;
            try {
                patientQueueArrayList = listToday(UnitID, PatientName);
                if (patientQueueArrayList != null && patientQueueArrayList.size() > 0) {
                    Count = patientQueueArrayList.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.PatientQueue.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.PatientQueue.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.PatientQueue.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.PatientQueue.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.PatientQueue.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public long updateISStatus(PatientQueue patientQueue) {
            long rowId = -1;
            try {
                ContentValues values = PatientQueueToContentValuesUPdate(patientQueue);
                String whereClause = null;
                whereClause = DatabaseContract.PatientQueue.COLUMN_NAME_ID + "='" + patientQueue.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.PatientQueue.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String UnitID) {
            try {
                String whereClause = null;
                //whereClause = DatabaseContract.PatientQueue.COLUMN_NAME_UNITID + "='" + UnitID + "'";
                databaseContract.open().delete(DatabaseContract.PatientQueue.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
        }
    }

    public class VisitListAdapter {

        String[] projection = {
                DatabaseContract.VisitList.COLUMN_NAME_ID,
                DatabaseContract.VisitList.COLUMN_NAME_UNITID,
                DatabaseContract.VisitList.COLUMN_NAME_DATE,
                DatabaseContract.VisitList.COLUMN_NAME_TIME,
                DatabaseContract.VisitList.COLUMN_NAME_APPOINTMENTDATE,
                DatabaseContract.VisitList.COLUMN_NAME_FROMTIME,
                DatabaseContract.VisitList.COLUMN_NAME_TOTIME,
                DatabaseContract.VisitList.COLUMN_NAME_PATIENTID,
                DatabaseContract.VisitList.COLUMN_NAME_PATIENTUNITID,
                DatabaseContract.VisitList.COLUMN_NAME_FIRSTNAME,
                DatabaseContract.VisitList.COLUMN_NAME_MIDDLENAME,
                DatabaseContract.VisitList.COLUMN_NAME_LASTNAME,
                DatabaseContract.VisitList.COLUMN_NAME_OPDNO,
                DatabaseContract.VisitList.COLUMN_NAME_VISITTYPEID,
                DatabaseContract.VisitList.COLUMN_NAME_VISITDESCRIPTION,
                DatabaseContract.VisitList.COLUMN_NAME_COMPLAINTSID,
                DatabaseContract.VisitList.COLUMN_NAME_COMPLAINTS,
                DatabaseContract.VisitList.COLUMN_NAME_DEPARTMENTID,
                DatabaseContract.VisitList.COLUMN_NAME_DEPARTMENT,
                DatabaseContract.VisitList.COLUMN_NAME_DOCTORID,
                DatabaseContract.VisitList.COLUMN_NAME_REFERREDDOCTORID,
                DatabaseContract.VisitList.COLUMN_NAME_REFERREDDOCTOR,
                DatabaseContract.VisitList.COLUMN_NAME_VISITTYPESERVICEID,
                DatabaseContract.VisitList.COLUMN_NAME_ISEMR,
                DatabaseContract.VisitList.COLUMN_NAME_ISBASICEMR,
                DatabaseContract.VisitList.COLUMN_NAME_ISLAB,
                DatabaseContract.VisitList.COLUMN_NAME_ISRADIOLOG,
                DatabaseContract.VisitList.COLUMN_NAME_ISPROCEDURE,
                DatabaseContract.VisitList.COLUMN_NAME_ISOTHERSERVIC,
                DatabaseContract.VisitList.COLUMN_NAME_ISCURRENTMEDICATION,
                DatabaseContract.VisitList.COLUMN_NAME_ISPRESCRIPTION,
                DatabaseContract.VisitList.COLUMN_NAME_ISREFERRAL,
                DatabaseContract.VisitList.COLUMN_NAME_REFENTITYTYPE,
                DatabaseContract.VisitList.COLUMN_NAME_REFENTITYID,
                DatabaseContract.VisitList.COLUMN_NAME_VISITDATETIME,
                DatabaseContract.VisitList.COLUMN_NAME_STATUS,
                DatabaseContract.VisitList.COLUMN_NAME_VISITSTATUS,
                DatabaseContract.VisitList.COLUMN_NAME_CURRENTVISITSTATUS,
                DatabaseContract.VisitList.COLUMN_NAME_UnitName,
                DatabaseContract.VisitList.COLUMN_NAME_DrName
        };

        private ContentValues VisitListToContentValues(VisitList visitList) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ID, visitList.getID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_UNITID, visitList.getUnitId());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_DATE, visitList.getDate());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_TIME, visitList.getTime());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_APPOINTMENTDATE, visitList.getAppointmentDate());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_FROMTIME, visitList.getFromTime());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_TOTIME, visitList.getToTime());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_PATIENTID, visitList.getPatientId());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_PATIENTUNITID, visitList.getPatientUnitId());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_FIRSTNAME, visitList.getFirstName());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_MIDDLENAME, visitList.getMiddleName());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_LASTNAME, visitList.getLastName());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_OPDNO, visitList.getOPDNO());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_VISITTYPEID, visitList.getVisitTypeID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_VISITDESCRIPTION, visitList.getVisitDescription());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_COMPLAINTSID, visitList.getComplaintsID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_COMPLAINTS, visitList.getComplaints());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_DEPARTMENTID, visitList.getDepartmentID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_DEPARTMENT, visitList.getDepartment());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_DOCTORID, visitList.getDoctorID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_REFERREDDOCTORID, visitList.getReferredDoctorID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_REFERREDDOCTOR, visitList.getReferredDoctor());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_VISITTYPESERVICEID, visitList.getVisitTypeServiceID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISEMR, visitList.getIsEMR());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISBASICEMR, visitList.getIsBasicEMR());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISLAB, visitList.getIsLab());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISRADIOLOG, visitList.getIsRadiology());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISPROCEDURE, visitList.getIsProcedure());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISOTHERSERVIC, visitList.getIsOtherService());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISCURRENTMEDICATION, visitList.getIsCurrentMedication());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISPRESCRIPTION, visitList.getIsPrescription());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_ISREFERRAL, visitList.getIsReferral());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_REFENTITYTYPE, visitList.getRefEntityType());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_REFENTITYID, visitList.getRefEntityID());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_VISITDATETIME, visitList.getVisitDateTime());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_STATUS, visitList.getStatus());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_VISITSTATUS, visitList.getVisitStatus());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_CURRENTVISITSTATUS, visitList.getCurrentVisitStatus());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_UnitName, visitList.getUnitName());
                values.put(DatabaseContract.VisitList.COLUMN_NAME_DrName, visitList.getDrName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<VisitList> CursorToArrayList(Cursor result) {
            ArrayList<VisitList> listVisitList = null;
            try {
                if (result != null) {
                    listVisitList = new ArrayList<VisitList>();
                    while (result.moveToNext()) {
                        VisitList visitList = new VisitList();
                        visitList.setID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ID)));
                        visitList.setUnitId(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_UNITID)));
                        visitList.setDate(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_DATE)));
                        visitList.setTime(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_TIME)));
                        visitList.setAppointmentDate(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_APPOINTMENTDATE)));
                        visitList.setFromTime(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_FROMTIME)));
                        visitList.setToTime(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_TOTIME)));
                        visitList.setPatientId(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_PATIENTID)));
                        visitList.setPatientUnitId(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_PATIENTUNITID)));
                        visitList.setFirstName(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_FIRSTNAME)));
                        visitList.setMiddleName(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_MIDDLENAME)));
                        visitList.setLastName(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_LASTNAME)));
                        visitList.setOPDNO(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_OPDNO)));
                        visitList.setVisitTypeID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_VISITTYPEID)));
                        visitList.setVisitDescription(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_VISITDESCRIPTION)));
                        visitList.setComplaintsID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_COMPLAINTSID)));
                        visitList.setComplaints(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_COMPLAINTS)));
                        visitList.setDepartmentID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_DEPARTMENTID)));
                        visitList.setDepartment(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_DEPARTMENT)));
                        visitList.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_DOCTORID)));
                        visitList.setReferredDoctorID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_REFERREDDOCTORID)));
                        visitList.setReferredDoctor(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_REFERREDDOCTOR)));
                        visitList.setVisitTypeServiceID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_VISITTYPESERVICEID)));
                        visitList.setIsEMR(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISEMR)));
                        visitList.setIsBasicEMR(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISBASICEMR)));
                        visitList.setIsLab(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISLAB)));
                        visitList.setIsRadiology(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISRADIOLOG)));
                        visitList.setIsProcedure(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISPROCEDURE)));
                        visitList.setIsOtherService(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISOTHERSERVIC)));
                        visitList.setIsCurrentMedication(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISCURRENTMEDICATION)));
                        visitList.setIsPrescription(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISPRESCRIPTION)));
                        visitList.setIsReferral(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_ISREFERRAL)));
                        visitList.setRefEntityType(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_REFENTITYTYPE)));
                        visitList.setRefEntityID(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_REFENTITYID)));
                        visitList.setVisitDateTime(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_VISITDATETIME)));
                        visitList.setStatus(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_STATUS)));
                        visitList.setVisitStatus(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_VISITSTATUS)));
                        visitList.setCurrentVisitStatus(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_CURRENTVISITSTATUS)));
                        visitList.setUnitName(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_UnitName)));
                        visitList.setDrName(result.getString(result.getColumnIndex(DatabaseContract.VisitList.COLUMN_NAME_DrName)));
                        listVisitList.add(visitList);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listVisitList;
        }

        public long create(VisitList visitList) {
            long rowId = -1;
            try {
                ArrayList<VisitList> listVisitList = listAll(visitList.getID());
                if (listVisitList.size() == 0) {
                    if (Count(visitList.getID()) == 0) {
                        ContentValues values = VisitListToContentValues(visitList);
                        if (values != null) {
                            rowId = databaseContract.open().insert(
                                    DatabaseContract.VisitList.TABLE_NAME, null, values);
                        }
                    }
                } else {
                    update(visitList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(VisitList visitList) {
            long rowId = -1;
            try {
                ContentValues values = VisitListToContentValues(visitList);
                String whereClause = null;
                whereClause = DatabaseContract.VisitList.COLUMN_NAME_ID + "='" + visitList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.VisitList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<VisitList> listAll(String ID) {
            ArrayList<VisitList> listVisitList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.VisitList.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.VisitList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VisitList.DEFAULT_SORT_ORDER);
                listVisitList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVisitList;
        }

        public ArrayList<VisitList> PatientIDList(String PatientID, String FromDate, String ToDate) {
            ArrayList<VisitList> listVisitList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (PatientID != null && FromDate != null && ToDate != null) {
                    whereClause = DatabaseContract.VisitList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' " + "  AND  " +
                            DatabaseContract.VisitList.COLUMN_NAME_DATE + " BETWEEN DATE('" + FromDate + "')  AND  DATE('" + ToDate + "')";
                } else if (PatientID != null) {
                    whereClause = DatabaseContract.VisitList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' ";
                }
                result = db.query(DatabaseContract.VisitList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VisitList.DEFAULT_SORT_ORDER);
                listVisitList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVisitList;
        }

        public int PatientIDCount(String PatientID, String FromDate, String ToDate) {
            int headerCount = 0;
            ArrayList<VisitList> visitListArrayList = null;
            try {
                visitListArrayList = PatientIDList(PatientID, FromDate, ToDate);
                if (visitListArrayList != null && visitListArrayList.size() > 0) {
                    headerCount = visitListArrayList.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return headerCount;
        }

        public ArrayList<VisitList> listAll() {
            ArrayList<VisitList> listVisitList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                result = db.query(DatabaseContract.VisitList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VisitList.DEFAULT_SORT_ORDER);
                listVisitList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVisitList;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.VisitList.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.VisitList.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.VisitList.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.VisitList.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.VisitList.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                String whereClause = null;
                databaseContract.open().delete(DatabaseContract.VisitList.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
        }
    }

    public class MedicienRouteAdapter {

        String[] projection = {
                DatabaseContract.MedicienRoute.COLUMN_NAME_ID,
                DatabaseContract.MedicienRoute.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues MedicienRouteToContentValues(MedicienRoute medicienRoute) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.MedicienRoute.COLUMN_NAME_ID, medicienRoute.getID());
                values.put(DatabaseContract.MedicienRoute.COLUMN_NAME_DESCRIPTION, medicienRoute.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<MedicienRoute> CursorToArrayList(Cursor result) {
            ArrayList<MedicienRoute> listMedicienRoute = null;
            try {
                if (result != null) {
                    listMedicienRoute = new ArrayList<MedicienRoute>();
                    while (result.moveToNext()) {
                        MedicienRoute medicienRoute = new MedicienRoute();
                        medicienRoute.setID(result.getString(result.getColumnIndex(DatabaseContract.MedicienRoute.COLUMN_NAME_ID)));
                        medicienRoute.setDescription(result.getString(result.getColumnIndex(DatabaseContract.MedicienRoute.COLUMN_NAME_DESCRIPTION)));
                        listMedicienRoute.add(medicienRoute);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listMedicienRoute;
        }

        public long create(MedicienRoute medicienRoute) {
            long rowId = -1;
            try {
                if (Count(medicienRoute.getID()) == 0) {
                    ContentValues values = MedicienRouteToContentValues(medicienRoute);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.MedicienRoute.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<MedicienRoute> listAll() {
            ArrayList<MedicienRoute> listMedicienRoute = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MedicienRoute.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MedicienRoute.DEFAULT_SORT_ORDER);
                listMedicienRoute = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listMedicienRoute;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MedicienRoute.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MedicienRoute.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.MedicienRoute.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.MedicienRoute.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.MedicienRoute.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.MedicienRoute.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class MedicienFrequencyAdapter {

        String[] projection = {
                DatabaseContract.MedicienFrequency.COLUMN_NAME_ID,
                DatabaseContract.MedicienFrequency.COLUMN_NAME_DESCRIPTION,
                DatabaseContract.MedicienFrequency.COLUMN_NAME_QUNTITYPERDAY
        };

        private ContentValues MedicienFrequencyToContentValues(MedicienFrequency medicienFrequency) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.MedicienFrequency.COLUMN_NAME_ID, medicienFrequency.getID());
                values.put(DatabaseContract.MedicienFrequency.COLUMN_NAME_DESCRIPTION, medicienFrequency.getDescription());
                values.put(DatabaseContract.MedicienFrequency.COLUMN_NAME_QUNTITYPERDAY, medicienFrequency.getQuntityPerDay());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<MedicienFrequency> CursorToArrayList(Cursor result) {
            ArrayList<MedicienFrequency> listMedicienFrequency = null;
            try {
                if (result != null) {
                    listMedicienFrequency = new ArrayList<MedicienFrequency>();
                    while (result.moveToNext()) {
                        MedicienFrequency medicienFrequency = new MedicienFrequency();
                        medicienFrequency.setID(result.getString(result.getColumnIndex(DatabaseContract.MedicienFrequency.COLUMN_NAME_ID)));
                        medicienFrequency.setDescription(result.getString(result.getColumnIndex(DatabaseContract.MedicienFrequency.COLUMN_NAME_DESCRIPTION)));
                        medicienFrequency.setQuntityPerDay(result.getString(result.getColumnIndex(DatabaseContract.MedicienFrequency.COLUMN_NAME_QUNTITYPERDAY)));
                        listMedicienFrequency.add(medicienFrequency);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listMedicienFrequency;
        }

        public long create(MedicienFrequency medicienFrequency) {
            long rowId = -1;
            try {
                if (Count(medicienFrequency.getID()) == 0) {
                    ContentValues values = MedicienFrequencyToContentValues(medicienFrequency);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.MedicienFrequency.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<MedicienFrequency> listAll() {
            ArrayList<MedicienFrequency> listMedicienFrequency = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MedicienFrequency.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MedicienFrequency.DEFAULT_SORT_ORDER);
                listMedicienFrequency = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listMedicienFrequency;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MedicienFrequency.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MedicienFrequency.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.MedicienFrequency.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.MedicienFrequency.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.MedicienFrequency.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.MedicienFrequency.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class MedicienInstructionAdapter {

        String[] projection = {
                DatabaseContract.MedicienInstruction.COLUMN_NAME_ID,
                DatabaseContract.MedicienInstruction.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues MedicienInstructionToContentValues(MedicienInstruction medicienInstruction) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.MedicienInstruction.COLUMN_NAME_ID, medicienInstruction.getID());
                values.put(DatabaseContract.MedicienInstruction.COLUMN_NAME_DESCRIPTION, medicienInstruction.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<MedicienInstruction> CursorToArrayList(Cursor result) {
            ArrayList<MedicienInstruction> listMedicienInstruction = null;
            try {
                if (result != null) {
                    listMedicienInstruction = new ArrayList<MedicienInstruction>();
                    while (result.moveToNext()) {
                        MedicienInstruction medicienInstruction = new MedicienInstruction();
                        medicienInstruction.setID(result.getString(result.getColumnIndex(DatabaseContract.MedicienInstruction.COLUMN_NAME_ID)));
                        medicienInstruction.setDescription(result.getString(result.getColumnIndex(DatabaseContract.MedicienInstruction.COLUMN_NAME_DESCRIPTION)));
                        listMedicienInstruction.add(medicienInstruction);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listMedicienInstruction;
        }

        public long create(MedicienInstruction medicienInstruction) {
            long rowId = -1;
            try {
                if (Count(medicienInstruction.getID()) == 0) {
                    ContentValues values = MedicienInstructionToContentValues(medicienInstruction);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.MedicienInstruction.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<MedicienInstruction> listAll() {
            ArrayList<MedicienInstruction> listMedicienInstruction = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MedicienInstruction.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MedicienInstruction.DEFAULT_SORT_ORDER);
                listMedicienInstruction = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listMedicienInstruction;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MedicienInstruction.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MedicienInstruction.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.MedicienInstruction.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.MedicienInstruction.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.MedicienInstruction.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.MedicienInstruction.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class VitalAdapter {

        String[] projection = {
                DatabaseContract.Vital.COLUMN_NAME_ID,
                DatabaseContract.Vital.COLUMN_NAME_DESCRIPTION,
                DatabaseContract.Vital.COLUMN_NAME_UNIT,
                DatabaseContract.Vital.COLUMN_NAME_MINVALUE,
                DatabaseContract.Vital.COLUMN_NAME_MAXVALUE
        };

        private ContentValues VitalToContentValues(Vital vital) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Vital.COLUMN_NAME_ID, vital.getID());
                values.put(DatabaseContract.Vital.COLUMN_NAME_DESCRIPTION, vital.getDescription());
                values.put(DatabaseContract.Vital.COLUMN_NAME_UNIT, vital.getUnit());
                values.put(DatabaseContract.Vital.COLUMN_NAME_MINVALUE, vital.getMinValue());
                values.put(DatabaseContract.Vital.COLUMN_NAME_MAXVALUE, vital.getMaxValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<Vital> CursorToArrayList(Cursor result) {
            ArrayList<Vital> listVital = null;
            try {
                if (result != null) {
                    listVital = new ArrayList<Vital>();
                    while (result.moveToNext()) {
                        Vital vital = new Vital();
                        vital.setID(result.getString(result.getColumnIndex(DatabaseContract.Vital.COLUMN_NAME_ID)));
                        vital.setDescription(result.getString(result.getColumnIndex(DatabaseContract.Vital.COLUMN_NAME_DESCRIPTION)));
                        vital.setUnit(result.getString(result.getColumnIndex(DatabaseContract.Vital.COLUMN_NAME_UNIT)));
                        vital.setMinValue(result.getString(result.getColumnIndex(DatabaseContract.Vital.COLUMN_NAME_MINVALUE)));
                        vital.setMaxValue(result.getString(result.getColumnIndex(DatabaseContract.Vital.COLUMN_NAME_MAXVALUE)));
                        listVital.add(vital);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listVital;
        }

        public long create(Vital vital) {
            long rowId = -1;
            try {
                if (Count(vital.getID()) == 0) {
                    ContentValues values = VitalToContentValues(vital);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Vital.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Vital> listAll() {
            ArrayList<Vital> listVital = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Vital.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Vital.DEFAULT_SORT_ORDER);
                listVital = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVital;
        }

        public ArrayList<Vital> listAll(String DESCRIPTION) {
            ArrayList<Vital> listVital = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (DESCRIPTION != null) {
                    whereClause = DatabaseContract.Vital.COLUMN_NAME_DESCRIPTION + "='" + DESCRIPTION + "'";
                }
                result = db.query(DatabaseContract.Vital.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.Vital.DEFAULT_SORT_ORDER);
                listVital = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVital;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Vital.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Vital.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.Vital.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.Vital.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Vital.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Vital.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class DaignosisTypeMasterAdapter {

        String[] projection = {
                DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_ID,
                DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues DaignosisTypeMasterToContentValues(DaignosisTypeMaster daignosisTypeMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_ID, daignosisTypeMaster.getID());
                values.put(DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_DESCRIPTION, daignosisTypeMaster.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<DaignosisTypeMaster> CursorToArrayList(Cursor result) {
            ArrayList<DaignosisTypeMaster> listDaignosisTypeMaster = null;
            try {
                if (result != null) {
                    listDaignosisTypeMaster = new ArrayList<DaignosisTypeMaster>();
                    while (result.moveToNext()) {
                        DaignosisTypeMaster daignosisTypeMaster = new DaignosisTypeMaster();
                        daignosisTypeMaster.setID(result.getString(result.getColumnIndex(DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_ID)));
                        daignosisTypeMaster.setDescription(result.getString(result.getColumnIndex(DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_DESCRIPTION)));
                        listDaignosisTypeMaster.add(daignosisTypeMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listDaignosisTypeMaster;
        }

        public long create(DaignosisTypeMaster daignosisTypeMaster) {
            long rowId = -1;
            try {
                if (Count(daignosisTypeMaster.getID()) == 0) {
                    ContentValues values = DaignosisTypeMasterToContentValues(daignosisTypeMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.DaignosisTypeMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<DaignosisTypeMaster> listAll() {
            ArrayList<DaignosisTypeMaster> listDaignosisTypeMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DaignosisTypeMaster.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.DaignosisTypeMaster.DEFAULT_SORT_ORDER);
                listDaignosisTypeMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listDaignosisTypeMaster;
        }

        public ArrayList<DaignosisTypeMaster> listAll(String name) {
            ArrayList<DaignosisTypeMaster> listDaignosisTypeMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (name != null) {
                    whereClause = DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_DESCRIPTION + "='" + name + "'";
                }
                result = db.query(DatabaseContract.DaignosisTypeMaster.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.DaignosisTypeMaster.DEFAULT_SORT_ORDER);
                listDaignosisTypeMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listDaignosisTypeMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DaignosisTypeMaster.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.DaignosisTypeMaster.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.DaignosisTypeMaster.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.DaignosisTypeMaster.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.DaignosisTypeMaster.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.DaignosisTypeMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class PriorityAdapter {

        String[] projection = {
                DatabaseContract.Priority.COLUMN_NAME_ID,
                DatabaseContract.Priority.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues PriorityToContentValues(Priority priority) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Priority.COLUMN_NAME_ID, priority.getID());
                values.put(DatabaseContract.Priority.COLUMN_NAME_DESCRIPTION, priority.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<Priority> CursorToArrayList(Cursor result) {
            ArrayList<Priority> listPriority = null;
            try {
                if (result != null) {
                    listPriority = new ArrayList<Priority>();
                    while (result.moveToNext()) {
                        Priority priority = new Priority();
                        priority.setID(result.getString(result.getColumnIndex(DatabaseContract.Priority.COLUMN_NAME_ID)));
                        priority.setDescription(result.getString(result.getColumnIndex(DatabaseContract.Priority.COLUMN_NAME_DESCRIPTION)));
                        listPriority.add(priority);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listPriority;
        }

        public long create(Priority priority) {
            long rowId = -1;
            try {
                if (Count(priority.getID()) == 0) {
                    ContentValues values = PriorityToContentValues(priority);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Priority.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Priority> listAll() {
            ArrayList<Priority> listPriority = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Priority.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Priority.DEFAULT_SORT_ORDER);
                listPriority = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listPriority;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Priority.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Priority.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.Priority.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.Priority.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Priority.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Priority.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class DiagnosisListAdapter {

        String[] projection = {
                DatabaseContract.DiagnosisList._ID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_ID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_UNITID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTUNITID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_VISITID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_CODE,
                DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISNAME,
                DatabaseContract.DiagnosisList.COLUMN_NAME_DATE,
                DatabaseContract.DiagnosisList.COLUMN_NAME_ICDID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_PRIMARYDIAGNOSIS,
                DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISTYPEID,
                DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISTYPE,
                DatabaseContract.DiagnosisList.COLUMN_NAME_REMARK,
                DatabaseContract.DiagnosisList.COLUMN_NAME_ADDEDBY,
                DatabaseContract.DiagnosisList.COLUMN_NAME_STATUS,
                DatabaseContract.DiagnosisList.COLUMN_NAME_ISSTATUS,
                DatabaseContract.DiagnosisList.COLUMN_NAME_IS_SYNC,
                DatabaseContract.DiagnosisList.COLUMN_NAME_IS_UPDATE

        };

        private ContentValues DiagnosisListToContentValues(DiagnosisList elDiagnosisList) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_ID, elDiagnosisList.getID());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_UNITID, elDiagnosisList.getUnitID());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTID, elDiagnosisList.getPatientID());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTUNITID, elDiagnosisList.getPatientUnitID());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_VISITID, elDiagnosisList.getVisitId());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISID, elDiagnosisList.getDiagnosisID());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_CODE, elDiagnosisList.getCode());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISNAME, elDiagnosisList.getDiagnosisName());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_DATE, elDiagnosisList.getDate());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_ICDID, elDiagnosisList.getICDId());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_PRIMARYDIAGNOSIS, elDiagnosisList.getPrimaryDiagnosis());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISTYPEID, elDiagnosisList.getDiagnosisTypeID());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISTYPE, elDiagnosisList.getDiagnosisType());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_REMARK, elDiagnosisList.getRemark());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_ADDEDBY, elDiagnosisList.getAddedBy());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_STATUS, elDiagnosisList.getStatus());
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_ISSTATUS, "1");
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_IS_SYNC, elDiagnosisList.getIsSync());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<DiagnosisList> CursorToArrayList(Cursor result) {
            ArrayList<DiagnosisList> listDiagnosisList = null;
            try {
                if (result != null) {
                    listDiagnosisList = new ArrayList<DiagnosisList>();
                    while (result.moveToNext()) {
                        DiagnosisList DiagnosisList = new DiagnosisList();
                        DiagnosisList.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.DiagnosisList._ID)));
                        DiagnosisList.setID(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_ID)));
                        DiagnosisList.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_UNITID)));
                        DiagnosisList.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTID)));
                        DiagnosisList.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTUNITID)));
                        DiagnosisList.setVisitId(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_VISITID)));
                        DiagnosisList.setDiagnosisID(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISID)));
                        DiagnosisList.setCode(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_CODE)));
                        DiagnosisList.setDiagnosisName(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISNAME)));
                        DiagnosisList.setDate(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_DATE)));
                        DiagnosisList.setICDId(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_ICDID)));
                        DiagnosisList.setPrimaryDiagnosis(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_PRIMARYDIAGNOSIS)));
                        DiagnosisList.setDiagnosisTypeID(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISTYPEID)));
                        DiagnosisList.setDiagnosisType(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_DIAGNOSISTYPE)));
                        DiagnosisList.setRemark(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_REMARK)));
                        DiagnosisList.setAddedBy(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_ADDEDBY)));
                        DiagnosisList.setStatus(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_STATUS)));
                        DiagnosisList.setISStatus(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_ISSTATUS)));
                        DiagnosisList.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.DiagnosisList.COLUMN_NAME_IS_SYNC)));
                        listDiagnosisList.add(DiagnosisList);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listDiagnosisList;
        }

        public long create(DiagnosisList DiagnosisList) {
            long rowId = -1;
            try {

                if (Count(DiagnosisList.getID()) == 0) {
                    ContentValues values = DiagnosisListToContentValues(DiagnosisList);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.DiagnosisList.TABLE_NAME, null, values);
                    }
                } else {
                    update(DiagnosisList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(DiagnosisList DiagnosisList) {
            long rowId = -1;
            try {
                ContentValues values = DiagnosisListToContentValues(DiagnosisList);
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_ID + "='" + DiagnosisList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<DiagnosisList> listAll() {
            ArrayList<DiagnosisList> listDiagnosisList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DiagnosisList.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.DiagnosisList.DEFAULT_SORT_ORDER);
                listDiagnosisList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listDiagnosisList;
        }

        public ArrayList<DiagnosisList> listAll(String ID) {
            ArrayList<DiagnosisList> listDiagnosisList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.DiagnosisList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.DiagnosisList.DEFAULT_SORT_ORDER);
                listDiagnosisList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listDiagnosisList;
        }

        public ArrayList<DiagnosisList> listAll(String PatientID, String VisitID) {
            ArrayList<DiagnosisList> listDiagnosisList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTID + "='" + PatientID + "'";
                // AND " +DatabaseContract.DiagnosisList.COLUMN_NAME_STATUS + "= 'True'";
                result = db.query(DatabaseContract.DiagnosisList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.DiagnosisList.DEFAULT_SORT_ORDER);
                listDiagnosisList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listDiagnosisList;
        }

        public long updateISStatus(DiagnosisList DiagnosisList) {
            long rowId = -1;
            try {
                ContentValues values = DiagnosisListToContentValues(DiagnosisList);
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_ID + "='" + DiagnosisList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.DiagnosisList.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_ISSTATUS + "='" + 0 + "' AND " +
                        DatabaseContract.DiagnosisList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.DiagnosisList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.DiagnosisList.COLUMN_NAME_STATUS + "= 'False'";
                databaseContract.open().delete(DatabaseContract.DiagnosisList.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
        }

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<DiagnosisList> DiagnosisLists = null;
            try {
                DiagnosisLists = listAll(PatientID, VisitID);
                if (DiagnosisLists != null && DiagnosisLists.size() > 0) {
                    Count = DiagnosisLists.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.DiagnosisList.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.DiagnosisList.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public long updateCurrentNotes(String ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_ID + "='" + ID + "'";
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long removeCurrentUpdateNotes() {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(
                            DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<DiagnosisList> CurrentUpdateNotes() {
            ArrayList<DiagnosisList> listDiagnosisList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_IS_UPDATE + "='" + "1" + "'";
                result = db.query(DatabaseContract.DiagnosisList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listDiagnosisList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listDiagnosisList;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<DiagnosisList> listAllUnSync() {
            ArrayList<DiagnosisList> listDiagnosisList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList.COLUMN_NAME_IS_SYNC + "='1'";
                result = db.query(DatabaseContract.DiagnosisList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.DiagnosisList.DEFAULT_SORT_ORDER);
                listDiagnosisList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listDiagnosisList;
        }

        public long createUnSync(DiagnosisList elDiagnosisList) {
            long rowId = -1;
            try {
                ContentValues values = DiagnosisListToContentValues(elDiagnosisList);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.DiagnosisList.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSync(DiagnosisList elDiagnosisList) {
            long rowId = -1;
            try {
                ContentValues values = DiagnosisListToContentValues(elDiagnosisList);
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList._ID + "='" + elDiagnosisList.get_ID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSyncCurrentNotes(int ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.DiagnosisList._ID + "='" + ID + "'";
                values.put(DatabaseContract.DiagnosisList.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateSyncLocalItem(int _ID, DiagnosisList elDiagnosisList) {
            long rowId = -1;
            try {
                elDiagnosisList.setIsSync("0");
                ContentValues values = DiagnosisListToContentValues(elDiagnosisList);
                String whereClause = null;
                whereClause = DatabaseContract.DiagnosisList._ID + "='" + _ID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.DiagnosisList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public String CheckDiagnosis(DiagnosisList elDiagnosisList) {
            String returnValue = "";
            ArrayList<DiagnosisList> elDiagnosisArrayList = listAll(elDiagnosisList.getPatientID(), elDiagnosisList.getVisitId());
            Boolean matchPrimary = false;
            for (int i = 0; i < elDiagnosisArrayList.size(); i++) {
                if (elDiagnosisArrayList.get(i).getPrimaryDiagnosis().equals("True")) {
                    matchPrimary = true;
                }
            }

            if (elDiagnosisList.getPrimaryDiagnosis().equals("1")) {
                if (matchPrimary == false) {
                    //create(elDiagnosisList);
                    returnValue = "1";
                    return returnValue;
                } else if (matchPrimary == true) {
                    returnValue = "4";
                    return returnValue;
                }
            } else if (elDiagnosisList.getPrimaryDiagnosis().equals("0")) {
                if (matchPrimary == false) {
                    returnValue = "3";
                    return returnValue;
                } else if (matchPrimary == true) {
                    returnValue = "1";
                    return returnValue;
                }
            }
            return returnValue;
        }
    }

    public class VitalsListAdapter {

        String[] projection = {
                DatabaseContract.VitalsList._ID,
                DatabaseContract.VitalsList.COLUMN_NAME_ID,
                DatabaseContract.VitalsList.COLUMN_NAME_UNITID,
                DatabaseContract.VitalsList.COLUMN_NAME_PATIENTID,
                DatabaseContract.VitalsList.COLUMN_NAME_PATIENTUNITID,
                DatabaseContract.VitalsList.COLUMN_NAME_VISITID,
                DatabaseContract.VitalsList.COLUMN_NAME_TEMPLATEID,
                DatabaseContract.VitalsList.COLUMN_NAME_DOCTORID,
                DatabaseContract.VitalsList.COLUMN_NAME_DATE,
                DatabaseContract.VitalsList.COLUMN_NAME_TIME,
                DatabaseContract.VitalsList.COLUMN_NAME_VITALID,
                DatabaseContract.VitalsList.COLUMN_NAME_VITALSDECRIPTION,
                DatabaseContract.VitalsList.COLUMN_NAME_VALUE,
                DatabaseContract.VitalsList.COLUMN_NAME_UNIT,
                DatabaseContract.VitalsList.COLUMN_NAME_ADDEDBY,
                DatabaseContract.VitalsList.COLUMN_NAME_STATUS,
                DatabaseContract.VitalsList.COLUMN_NAME_ISSTATUS,
                DatabaseContract.VitalsList.COLUMN_NAME_ISLOCAL,
                DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC,
                DatabaseContract.VitalsList.COLUMN_NAME_IS_UPDATE
        };

        private ContentValues VitalsListToContentValues(VitalsList vitalsList) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_ID, vitalsList.getID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_UNITID, vitalsList.getUnitID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_PATIENTID, vitalsList.getPatientID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_PATIENTUNITID, vitalsList.getPatientUnitID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_VISITID, vitalsList.getVisitID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_TEMPLATEID, vitalsList.getTemplateID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_DOCTORID, vitalsList.getDoctorID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_DATE, vitalsList.getDate());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_TIME, vitalsList.getTime());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_VITALID, vitalsList.getVitalID());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_VITALSDECRIPTION, vitalsList.getVitalsDecription());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_VALUE, vitalsList.getValue());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_UNIT, vitalsList.getUnit());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_ADDEDBY, vitalsList.getAddedBy());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_STATUS, vitalsList.getStatus());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_ISSTATUS, "1");
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_ISLOCAL, vitalsList.getISLocal());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC, vitalsList.getIsSync());
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_IS_UPDATE, vitalsList.getIsUpdate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<VitalsList> CursorToArrayList(Cursor result) {
            ArrayList<VitalsList> listVitalsList = null;
            try {
                if (result != null) {
                    listVitalsList = new ArrayList<VitalsList>();
                    while (result.moveToNext()) {
                        VitalsList vitalsList = new VitalsList();
                        vitalsList.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.VitalsList._ID)));
                        vitalsList.setID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_ID)));
                        vitalsList.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_UNITID)));
                        vitalsList.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_PATIENTID)));
                        vitalsList.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_PATIENTUNITID)));
                        vitalsList.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_VISITID)));
                        vitalsList.setTemplateID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_TEMPLATEID)));
                        vitalsList.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_DOCTORID)));
                        vitalsList.setDate(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_DATE)));
                        vitalsList.setTime(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_TIME)));
                        vitalsList.setVitalID(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_VITALID)));
                        vitalsList.setVitalsDecription(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_VITALSDECRIPTION)));
                        vitalsList.setValue(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_VALUE)));
                        vitalsList.setUnit(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_UNIT)));
                        vitalsList.setAddedBy(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_ADDEDBY)));
                        vitalsList.setStatus(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_STATUS)));
                        vitalsList.setISStatus(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_ISSTATUS)));
                        vitalsList.setISLocal(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_ISLOCAL)));
                        vitalsList.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC)));
                        vitalsList.setIsUpdate(result.getString(result.getColumnIndex(DatabaseContract.VitalsList.COLUMN_NAME_IS_UPDATE)));
                        listVitalsList.add(vitalsList);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listVitalsList;
        }

        public long create(VitalsList vitalsList) {
            long rowId = -1;
            try {

                if (Count(vitalsList.getID()) == 0) {
                    ContentValues values = VitalsListToContentValues(vitalsList);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.VitalsList.TABLE_NAME, null, values);
                    }
                } else {
                    update(vitalsList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long createLocal(VitalsList vitalsList) {
            long rowId = -1;
            try {
                ContentValues values = VitalsListToContentValues(vitalsList);
                if (values != null) {
                    rowId = databaseContract.open().insert(
                            DatabaseContract.VitalsList.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(VitalsList vitalsList) {
            long rowId = -1;
            try {
                ContentValues values = VitalsListToContentValues(vitalsList);
                String whereClause = null;
                whereClause = DatabaseContract.VitalsList.COLUMN_NAME_ID + "='" + vitalsList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.VitalsList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<VitalsList> listAll() {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.VitalsList.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.VitalsList.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }

        public ArrayList<VitalsList> listAll(String ID) {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.VitalsList.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.VitalsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsList.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }

        public Boolean IsVitalsExist(String VitalID, String PatientID, String VisitID) {
            ArrayList<VitalsList> listVitalsList = null;
            Boolean isExist = false;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (VitalID != null) {
                    whereClause = DatabaseContract.VitalsList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                            DatabaseContract.VitalsList.COLUMN_NAME_VITALID + "='" + VitalID + "'";
                }
                result = db.query(DatabaseContract.VitalsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsList.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
                if (listVitalsList != null && listVitalsList.size() > 0) {
                    isExist = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return isExist;
        }

        /*public ArrayList<VitalsList> listAllVitals(String PatientID, String VisitID) {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause =
                        DatabaseContract.VitalsList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                                DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC + "='1' AND " +
                                DatabaseContract.VitalsList.COLUMN_NAME_ISLOCAL + "= '1'";
                result = db.query(DatabaseContract.VitalsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsList.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }*/

        public ArrayList<VitalsList> listAll(String PatientID, String VisitID) {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause =
                        DatabaseContract.VitalsList.COLUMN_NAME_VISITID + "='" + VisitID + "'";
                /*AND " +
                        DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsList.COLUMN_NAME_ISLOCAL + "= 'True'";*/
                result = db.query(DatabaseContract.VitalsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsList.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<VitalsList> vitalsLists = null;
            try {
                vitalsLists = listAll(PatientID, VisitID);
                if (vitalsLists != null && vitalsLists.size() > 0) {
                    Count = vitalsLists.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public long updateISStatus(VitalsList vitalsList) {
            long rowId = -1;
            try {
                ContentValues values = VitalsListToContentValues(vitalsList);
                String whereClause = null;
                whereClause = DatabaseContract.VitalsList.COLUMN_NAME_ID + "='" + vitalsList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.VitalsList.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateValue(String PatientID, String VisitID, String VitalID, String Value) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.VitalsList.COLUMN_NAME_VALUE, Value);
                String whereClause = null;
                whereClause = DatabaseContract.VitalsList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.VitalsList.COLUMN_NAME_VITALID + "='" + VitalID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(DatabaseContract.VitalsList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long DeleteVitals(String PatientID, String VisitID, String VitalID) {
            long rowId = -1;
            try {
                String whereClause = null;
                whereClause = DatabaseContract.VitalsList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.VitalsList.COLUMN_NAME_VITALID + "='" + VitalID + "'";
                databaseContract.open().delete(DatabaseContract.VitalsList.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.VitalsList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsList.COLUMN_NAME_VISITID + "='" + VisitID + "'";
                //AND " + DatabaseContract.VitalsList.COLUMN_NAME_STATUS + "= 'False'";
                databaseContract.open().delete(DatabaseContract.VitalsList.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.VitalsList.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.VitalsList.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.VitalsList.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<VitalsList> listAllUnSync(String VisitID) {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (VisitID.equals("")) {
                    whereClause = DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC + "='1'";
                } else {
                    whereClause = DatabaseContract.VitalsList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                            DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC + "='1'";
                }
                result = db.query(DatabaseContract.VitalsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.VitalsList.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listVitalsList;
        }

        public ArrayList<VitalsList> listAllVisitUnSync() {
            ArrayList<VitalsList> listVitalsList = null;
            boolean distinct = true;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.VitalsList.COLUMN_NAME_IS_SYNC + "='1'";
                result = db.query(distinct, DatabaseContract.VitalsList.TABLE_NAME,
                        projection, whereClause,
                        null, DatabaseContract.VitalsList.COLUMN_NAME_VISITID, null, null, null);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listVitalsList;
        }

        public long createUnSync(VitalsList elVitalsList, String currentDate) {
            long rowId = -1;
            try {
                elVitalsList.setIsSync("1");
                elVitalsList.setDate(currentDate);
                elVitalsList.setTime(currentDate);
                ContentValues values = VitalsListToContentValues(elVitalsList);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.VitalsList.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateSyncLocalItem(VitalsList elVitalsList) {
            long rowId = -1;
            try {
                elVitalsList.setIsSync("0");
                ContentValues values = VitalsListToContentValues(elVitalsList);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.VitalsList.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }


    }

    public class VitalsListLocalAdapter {

        String[] projection = {
                DatabaseContract.VitalsListLocal._ID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_ID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_UNITID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTUNITID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_TEMPLATEID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_DOCTORID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_DATE,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_TIME,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALID,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALSDECRIPTION,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_VALUE,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_UNIT,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_ADDEDBY,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_STATUS,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_ISSTATUS,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_ISLOCAL,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_SYNC,
                DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_UPDATE
        };

        private ContentValues VitalsListToContentValues(VitalsList vitalsList) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_ID, vitalsList.getID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_UNITID, vitalsList.getUnitID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTID, vitalsList.getPatientID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTUNITID, vitalsList.getPatientUnitID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID, vitalsList.getVisitID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_TEMPLATEID, vitalsList.getTemplateID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_DOCTORID, vitalsList.getDoctorID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_DATE, vitalsList.getDate());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_TIME, vitalsList.getTime());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALID, vitalsList.getVitalID());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALSDECRIPTION, vitalsList.getVitalsDecription());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_VALUE, vitalsList.getValue());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_UNIT, vitalsList.getUnit());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_ADDEDBY, vitalsList.getAddedBy());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_STATUS, vitalsList.getStatus());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_ISSTATUS, "1");
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_ISLOCAL, vitalsList.getISLocal());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_SYNC, vitalsList.getIsSync());
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_UPDATE, vitalsList.getIsUpdate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<VitalsList> CursorToArrayList(Cursor result) {
            ArrayList<VitalsList> listVitalsList = null;
            try {
                if (result != null) {
                    listVitalsList = new ArrayList<VitalsList>();
                    while (result.moveToNext()) {
                        VitalsList vitalsList = new VitalsList();
                        vitalsList.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.VitalsListLocal._ID)));
                        vitalsList.setID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_ID)));
                        vitalsList.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_UNITID)));
                        vitalsList.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTID)));
                        vitalsList.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTUNITID)));
                        vitalsList.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID)));
                        vitalsList.setTemplateID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_TEMPLATEID)));
                        vitalsList.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_DOCTORID)));
                        vitalsList.setDate(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_DATE)));
                        vitalsList.setTime(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_TIME)));
                        vitalsList.setVitalID(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALID)));
                        vitalsList.setVitalsDecription(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALSDECRIPTION)));
                        vitalsList.setValue(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_VALUE)));
                        vitalsList.setUnit(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_UNIT)));
                        vitalsList.setAddedBy(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_ADDEDBY)));
                        vitalsList.setStatus(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_STATUS)));
                        vitalsList.setISStatus(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_ISSTATUS)));
                        vitalsList.setISLocal(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_ISLOCAL)));
                        vitalsList.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_SYNC)));
                        vitalsList.setIsUpdate(result.getString(result.getColumnIndex(DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_UPDATE)));
                        listVitalsList.add(vitalsList);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listVitalsList;
        }

        public long create(VitalsList vitalsList) {
            long rowId = -1;
            try {

                if (Count(vitalsList.getID()) == 0) {
                    ContentValues values = VitalsListToContentValues(vitalsList);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.VitalsListLocal.TABLE_NAME, null, values);
                    }
                } else {
                    update(vitalsList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long createLocal(VitalsList vitalsList) {
            long rowId = -1;
            try {
                ContentValues values = VitalsListToContentValues(vitalsList);
                if (values != null) {
                    rowId = databaseContract.open().insert(
                            DatabaseContract.VitalsListLocal.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(VitalsList vitalsList) {
            long rowId = -1;
            try {
                ContentValues values = VitalsListToContentValues(vitalsList);
                String whereClause = null;
                whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_ID + "='" + vitalsList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.VitalsListLocal.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<VitalsList> listAll() {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.VitalsListLocal.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.VitalsListLocal.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }

        public ArrayList<VitalsList> listAll(String ID) {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.VitalsListLocal.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsListLocal.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }

        public Boolean IsVitalsExist(String VitalID, String PatientID, String VisitID) {
            ArrayList<VitalsList> listVitalsList = null;
            Boolean isExist = false;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (VitalID != null) {
                    whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                            DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALID + "='" + VitalID + "'";
                }
                result = db.query(DatabaseContract.VitalsListLocal.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsListLocal.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
                if (listVitalsList != null && listVitalsList.size() > 0) {
                    isExist = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return isExist;
        }

        /*public ArrayList<VitalsList> listAllVitals(String PatientID, String VisitID) {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause =
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                                DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_SYNC + "='1' AND " +
                                DatabaseContract.VitalsListLocal.COLUMN_NAME_ISLOCAL + "= '1'";
                result = db.query(DatabaseContract.VitalsListLocal.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsListLocal.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }*/

        public ArrayList<VitalsList> listAll(String PatientID, String VisitID) {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause =
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID + "='" + VisitID + "'";
                /*AND " +
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_SYNC + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_ISLOCAL + "= 'True'";*/
                result = db.query(DatabaseContract.VitalsListLocal.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.VitalsListLocal.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listVitalsList;
        }

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<VitalsList> vitalsLists = null;
            try {
                vitalsLists = listAll(PatientID, VisitID);
                if (vitalsLists != null && vitalsLists.size() > 0) {
                    Count = vitalsLists.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public long updateISStatus(VitalsList vitalsList) {
            long rowId = -1;
            try {
                ContentValues values = VitalsListToContentValues(vitalsList);
                String whereClause = null;
                whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_ID + "='" + vitalsList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.VitalsListLocal.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateValue(String PatientID, String VisitID, String VitalID, String Value) {
            long rowId = -1;
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_VALUE, Value);
                String whereClause = null;
                whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALID + "='" + VitalID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(DatabaseContract.VitalsListLocal.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long DeleteVitals(String PatientID, String VisitID, String VitalID) {
            long rowId = -1;
            try {
                String whereClause = null;
                whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_VITALID + "='" + VitalID + "'";
                databaseContract.open().delete(DatabaseContract.VitalsListLocal.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.VitalsListLocal.COLUMN_NAME_VISITID + "='" + VisitID + "'";
                //AND " + DatabaseContract.VitalsListLocal.COLUMN_NAME_STATUS + "= 'False'";
                databaseContract.open().delete(DatabaseContract.VitalsListLocal.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.VitalsListLocal.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.VitalsListLocal.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<VitalsList> listAllUnSync() {
            ArrayList<VitalsList> listVitalsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_SYNC + "='1'";
                result = db.query(DatabaseContract.VitalsListLocal.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.VitalsListLocal.DEFAULT_SORT_ORDER);
                listVitalsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listVitalsList;
        }

        public long createUnSync(VitalsList elVitalsList) {
            long rowId = -1;
            try {
                elVitalsList.setIsSync("1");
                ContentValues values = VitalsListToContentValues(elVitalsList);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.VitalsListLocal.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSync(VitalsList elVitalsList) {
            long rowId = -1;
            try {
                ContentValues values = VitalsListToContentValues(elVitalsList);
                String whereClause = null;
                whereClause = DatabaseContract.VitalsListLocal._ID + "='" + elVitalsList.get_ID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.VitalsListLocal.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSyncCurrentNotes(int ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.VitalsListLocal.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.VitalsListLocal._ID + "='" + ID + "'";
                values.put(DatabaseContract.VitalsListLocal.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.VitalsListLocal.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }
    }

    public class CPOEServiceAdapter {

        String[] projection = {
                DatabaseContract.CPOEService._ID,
                DatabaseContract.CPOEService.COLUMN_NAME_ID,
                DatabaseContract.CPOEService.COLUMN_NAME_UNITID,
                DatabaseContract.CPOEService.COLUMN_NAME_PATIENTID,
                DatabaseContract.CPOEService.COLUMN_NAME_PATIENTUNITID,
                DatabaseContract.CPOEService.COLUMN_NAME_VISITID,
                DatabaseContract.CPOEService.COLUMN_NAME_TEMPLATEID,
                DatabaseContract.CPOEService.COLUMN_NAME_DOCTORID,
                DatabaseContract.CPOEService.COLUMN_NAME_SERVICEID,
                DatabaseContract.CPOEService.COLUMN_NAME_SERVICENAME,
                DatabaseContract.CPOEService.COLUMN_NAME_REASON,
                DatabaseContract.CPOEService.COLUMN_NAME_RATE,
                DatabaseContract.CPOEService.COLUMN_NAME_PRIORITY,
                DatabaseContract.CPOEService.COLUMN_NAME_TEMPLATEID,
                DatabaseContract.CPOEService.COLUMN_NAME_ADVICE,
                DatabaseContract.CPOEService.COLUMN_NAME_PRESCRIPTIONID,
                DatabaseContract.CPOEService.COLUMN_NAME_PRIORITYDESCRIPTION,
                DatabaseContract.CPOEService.COLUMN_NAME_BILLEDDATE,
                DatabaseContract.CPOEService.COLUMN_NAME_ADDEDBY,
                DatabaseContract.CPOEService.COLUMN_NAME_STATUS,
                DatabaseContract.CPOEService.COLUMN_NAME_ISSTATUS,
                DatabaseContract.CPOEService.COLUMN_NAME_IS_SYNC,
                DatabaseContract.CPOEService.COLUMN_NAME_IS_UPDATE

        };

        private ContentValues CPOEServiceToContentValues(CPOEService cpoeService) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_ID, cpoeService.getID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_UNITID, cpoeService.getUnitID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_PATIENTID, cpoeService.getPatientID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_PATIENTUNITID, cpoeService.getPatientUnitID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_VISITID, cpoeService.getVisitID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_TEMPLATEID, cpoeService.getTemplateID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_DOCTORID, cpoeService.getDoctorID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_SERVICEID, cpoeService.getServiceID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_SERVICENAME, cpoeService.getServiceName());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_REASON, cpoeService.getReason());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_RATE, cpoeService.getRate());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_PRIORITY, cpoeService.getPriority());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_TEMPLATEID, cpoeService.getTemplateID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_ADVICE, cpoeService.getAdvice());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_PRESCRIPTIONID, cpoeService.getPrescriptionID());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_PRIORITYDESCRIPTION, cpoeService.getPriorityDescription());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_BILLEDDATE, cpoeService.getBilledDate());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_ADDEDBY, cpoeService.getAddedBy());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_STATUS, cpoeService.getStatus());
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_ISSTATUS, "1");
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_IS_SYNC, cpoeService.getIsSync());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<CPOEService> CursorToArrayList(Cursor result) {
            ArrayList<CPOEService> listCPOEService = null;
            try {
                if (result != null) {
                    listCPOEService = new ArrayList<CPOEService>();
                    while (result.moveToNext()) {
                        CPOEService cpoeService = new CPOEService();
                        cpoeService.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.CPOEService._ID)));
                        cpoeService.setID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_ID)));
                        cpoeService.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_UNITID)));
                        cpoeService.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_PATIENTID)));
                        cpoeService.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_PATIENTUNITID)));
                        cpoeService.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_VISITID)));
                        cpoeService.setTemplateID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_TEMPLATEID)));
                        cpoeService.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_DOCTORID)));
                        cpoeService.setServiceID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_SERVICEID)));
                        cpoeService.setServiceName(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_SERVICENAME)));
                        cpoeService.setReason(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_REASON)));
                        cpoeService.setRate(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_RATE)));
                        cpoeService.setPriority(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_PRIORITY)));
                        cpoeService.setTemplateID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_TEMPLATEID)));
                        cpoeService.setAdvice(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_ADVICE)));
                        cpoeService.setPrescriptionID(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_PRESCRIPTIONID)));
                        cpoeService.setPriorityDescription(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_PRIORITYDESCRIPTION)));
                        cpoeService.setBilledDate(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_BILLEDDATE)));
                        cpoeService.setAddedBy(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_ADDEDBY)));
                        cpoeService.setStatus(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_STATUS)));
                        cpoeService.setISStatus(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_ISSTATUS)));
                        cpoeService.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.CPOEService.COLUMN_NAME_IS_SYNC)));

                        listCPOEService.add(cpoeService);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listCPOEService;
        }

        public long create(CPOEService cpoeService) {
            long rowId = -1;
            try {

                if (Count(cpoeService.getID()) == 0) {
                    ContentValues values = CPOEServiceToContentValues(cpoeService);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.CPOEService.TABLE_NAME, null, values);
                    }
                } else {
                    update(cpoeService);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(CPOEService cpoeService) {
            long rowId = -1;
            try {
                ContentValues values = CPOEServiceToContentValues(cpoeService);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEService.COLUMN_NAME_ID + "='" + cpoeService.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<CPOEService> listAll() {
            ArrayList<CPOEService> listCPOEService = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CPOEService.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.CPOEService.DEFAULT_SORT_ORDER);
                listCPOEService = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listCPOEService;
        }

        public ArrayList<CPOEService> listAll(String ID) {
            ArrayList<CPOEService> listCPOEService = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.CPOEService.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.CPOEService.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.CPOEService.DEFAULT_SORT_ORDER);
                listCPOEService = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listCPOEService;
        }

        public ArrayList<CPOEService> listAll(String PatientID, String VisitID) {
            ArrayList<CPOEService> listCPOEService = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause =
                        DatabaseContract.CPOEService.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                                DatabaseContract.CPOEService.COLUMN_NAME_PATIENTID + "='" + PatientID + "'";
                // AND " + DatabaseContract.CPOEService.COLUMN_NAME_STATUS + "= 'True'";
                result = db.query(DatabaseContract.CPOEService.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.CPOEService.DEFAULT_SORT_ORDER);
                listCPOEService = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listCPOEService;
        }

        public long updateISStatus(CPOEService cpoeService) {
            long rowId = -1;
            try {
                ContentValues values = CPOEServiceToContentValues(cpoeService);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEService.COLUMN_NAME_ID + "='" + cpoeService.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEService.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.CPOEService.COLUMN_NAME_ISSTATUS + "='" + 0 + "' AND " +
                        DatabaseContract.CPOEService.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.CPOEService.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.CPOEService.COLUMN_NAME_STATUS + "= 'True'";
                databaseContract.open().delete(DatabaseContract.CPOEService.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
        }

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<CPOEService> cpoeServices = null;
            try {
                cpoeServices = listAll(PatientID, VisitID);
                if (cpoeServices != null && cpoeServices.size() > 0) {
                    Count = cpoeServices.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.CPOEService.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.CPOEService.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.CPOEService.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public long updateCurrentNotes(String ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.CPOEService.COLUMN_NAME_ID + "='" + ID + "'";
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long removeCurrentUpdateNotes() {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(
                            DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<CPOEService> CurrentUpdateNotes() {
            ArrayList<CPOEService> listCPOEService = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.CPOEService.COLUMN_NAME_IS_UPDATE + "='" + "1" + "'";
                result = db.query(DatabaseContract.CPOEService.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listCPOEService = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listCPOEService;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<CPOEService> listAllUnSync() {
            ArrayList<CPOEService> listCPOEServiceList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.CPOEService.COLUMN_NAME_IS_SYNC + "='1'";
                result = db.query(DatabaseContract.CPOEService.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.CPOEService.DEFAULT_SORT_ORDER);
                listCPOEServiceList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listCPOEServiceList;
        }

        public long createUnSync(CPOEService elCPOEService) {
            long rowId = -1;
            try {
                ContentValues values = CPOEServiceToContentValues(elCPOEService);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.CPOEService.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSync(CPOEService elCPOEService) {
            long rowId = -1;
            try {
                ContentValues values = CPOEServiceToContentValues(elCPOEService);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEService._ID + "='" + elCPOEService.get_ID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSyncCurrentNotes(int ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.CPOEService._ID + "='" + ID + "'";
                values.put(DatabaseContract.CPOEService.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateSyncLocalItem(int _ID, CPOEService elCPOEService) {
            long rowId = -1;
            try {
                elCPOEService.setIsSync("0");
                ContentValues values = CPOEServiceToContentValues(elCPOEService);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEService._ID + "='" + _ID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEService.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }
    }

    public class CPOEMedicineAdapter {

        String[] projection = {
                DatabaseContract.CPOEMedicine._ID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_UNITID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTUNITID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_VISITID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_DOCTORID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_TEMPLATEID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_PRESCRIPTIONID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ISOTHER,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_FROMHISTORY,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_BILLEDDATE,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_GENERALINSTRUCTION,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ISDESPENCE,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_STATUS,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ADDEDBY,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_UPDATEDDATETIME,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_DRUGID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_DOSE,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ROUTE,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_FREQUENCY,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_DAYS,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_QUANTITY,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ITEMNAME,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_REASON,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_RATE,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_DATE,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ROUTEID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_FREQUENCYID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_REASONID,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_ISSTATUS,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_SYNC,
                DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_UPDATE
        };

        private ContentValues CPOEMedicineToContentValues(CPOEPrescription cpoeMedicine) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ID, cpoeMedicine.getID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_UNITID, cpoeMedicine.getUnitID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTID, cpoeMedicine.getPatientID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTUNITID, cpoeMedicine.getPatientUnitID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_VISITID, cpoeMedicine.getVisitID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_DRUGID, cpoeMedicine.getDrugID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_DOSE, cpoeMedicine.getDose());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ROUTE, cpoeMedicine.getRoute());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_FREQUENCY, cpoeMedicine.getFrequency());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_DAYS, cpoeMedicine.getDays());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_QUANTITY, cpoeMedicine.getQuantity());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ITEMNAME, cpoeMedicine.getItemName());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_REASON, cpoeMedicine.getReason());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_RATE, cpoeMedicine.getRate());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_DATE, cpoeMedicine.getDate());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ROUTEID, cpoeMedicine.getRouteID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_FREQUENCYID, cpoeMedicine.getFrequencyID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_REASONID, cpoeMedicine.getReasonID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_DOCTORID, cpoeMedicine.getDoctorID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_TEMPLATEID, cpoeMedicine.getTemplateID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_PRESCRIPTIONID, cpoeMedicine.getPrescriptionID());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ISOTHER, cpoeMedicine.getIsOther());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_FROMHISTORY, cpoeMedicine.getFromHistory());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_BILLEDDATE, cpoeMedicine.getBilledDate());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_GENERALINSTRUCTION, cpoeMedicine.getGeneralInstruction());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ISDESPENCE, cpoeMedicine.getIsDespence());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_STATUS, cpoeMedicine.getStatus());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ADDEDBY, cpoeMedicine.getAddedBy());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_UPDATEDDATETIME, cpoeMedicine.getUpdatedDateTime());
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_ISSTATUS, "1");
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_SYNC, cpoeMedicine.getIsSync());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<CPOEPrescription> CursorToArrayList(Cursor result) {
            ArrayList<CPOEPrescription> listCPOEMedicine = null;
            try {
                if (result != null) {
                    listCPOEMedicine = new ArrayList<CPOEPrescription>();
                    while (result.moveToNext()) {
                        CPOEPrescription cpoeMedicine = new CPOEPrescription();
                        cpoeMedicine.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.CPOEMedicine._ID)));
                        cpoeMedicine.setID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ID)));
                        cpoeMedicine.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_UNITID)));
                        cpoeMedicine.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTID)));
                        cpoeMedicine.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTUNITID)));
                        cpoeMedicine.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_VISITID)));
                        cpoeMedicine.setDrugID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_DRUGID)));
                        cpoeMedicine.setDose(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_DOSE)));
                        cpoeMedicine.setRoute(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ROUTE)));
                        cpoeMedicine.setFrequency(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_FREQUENCY)));
                        cpoeMedicine.setDays(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_DAYS)));
                        cpoeMedicine.setQuantity(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_QUANTITY)));
                        cpoeMedicine.setItemName(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ITEMNAME)));
                        cpoeMedicine.setReason(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_REASON)));
                        cpoeMedicine.setRate(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_RATE)));
                        cpoeMedicine.setDate(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_DATE)));
                        cpoeMedicine.setRouteID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ROUTEID)));
                        cpoeMedicine.setFrequencyID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_FREQUENCYID)));
                        cpoeMedicine.setReasonID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_REASONID)));
                        cpoeMedicine.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_DOCTORID)));
                        cpoeMedicine.setTemplateID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_TEMPLATEID)));
                        cpoeMedicine.setPrescriptionID(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_PRESCRIPTIONID)));
                        cpoeMedicine.setIsOther(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ISOTHER)));
                        cpoeMedicine.setFromHistory(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_FROMHISTORY)));
                        cpoeMedicine.setBilledDate(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_BILLEDDATE)));
                        cpoeMedicine.setGeneralInstruction(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_GENERALINSTRUCTION)));
                        cpoeMedicine.setIsDespence(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ISDESPENCE)));
                        cpoeMedicine.setStatus(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_STATUS)));
                        cpoeMedicine.setAddedBy(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ADDEDBY)));
                        cpoeMedicine.setUpdatedDateTime(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_UPDATEDDATETIME)));
                        cpoeMedicine.setISStatus(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_ISSTATUS)));
                        cpoeMedicine.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_SYNC)));
                        listCPOEMedicine.add(cpoeMedicine);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listCPOEMedicine;
        }

        public long create(CPOEPrescription cpoeMedicine) {
            long rowId = -1;
            try {
                if (Count(cpoeMedicine.getID()) == 0) {
                    ContentValues values = CPOEMedicineToContentValues(cpoeMedicine);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.CPOEMedicine.TABLE_NAME, null, values);
                    }
                } else {
                    update(cpoeMedicine);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(CPOEPrescription cpoeMedicine) {
            long rowId = -1;
            try {
                ContentValues values = CPOEMedicineToContentValues(cpoeMedicine);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_ID + "='" + cpoeMedicine.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<CPOEPrescription> listAll() {
            ArrayList<CPOEPrescription> listCPOEMedicine = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CPOEMedicine.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.CPOEMedicine.DEFAULT_SORT_ORDER);
                listCPOEMedicine = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listCPOEMedicine;
        }

        public ArrayList<CPOEPrescription> listAll(String ID) {
            ArrayList<CPOEPrescription> listCPOEMedicine = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.CPOEMedicine.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.CPOEMedicine.DEFAULT_SORT_ORDER);
                listCPOEMedicine = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listCPOEMedicine;
        }

        public ArrayList<CPOEPrescription> listAll(String PatientID, String VisitID) {
            ArrayList<CPOEPrescription> listCPOEMedicine = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTID + "='" + PatientID + "'";
                // DatabaseContract.CPOEMedicine.COLUMN_NAME_STATUS + "= 'True'";
                result = db.query(DatabaseContract.CPOEMedicine.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.CPOEMedicine.DEFAULT_SORT_ORDER);
                listCPOEMedicine = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listCPOEMedicine;
        }

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<CPOEPrescription> cpoeMedicines = null;
            try {
                cpoeMedicines = listAll(PatientID, VisitID);
                if (cpoeMedicines != null && cpoeMedicines.size() > 0) {
                    Count = cpoeMedicines.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public long updateISStatus(CPOEPrescription cpoeMedicine) {
            long rowId = -1;
            try {
                ContentValues values = CPOEMedicineToContentValues(cpoeMedicine);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_ID + "='" + cpoeMedicine.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEMedicine.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_ISSTATUS + "='" + 0 + "' AND " + DatabaseContract.CPOEMedicine.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.CPOEMedicine.COLUMN_NAME_VISITID + "='" + VisitID + "'";
                databaseContract.open().delete(DatabaseContract.CPOEMedicine.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.CPOEMedicine.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.CPOEMedicine.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public long updateCurrentNotes(String ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_ID + "='" + ID + "'";
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long removeCurrentUpdateNotes() {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(
                            DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<CPOEPrescription> CurrentUpdateNotes() {
            ArrayList<CPOEPrescription> listCPOEPrescription = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_UPDATE + "='" + "1" + "'";
                result = db.query(DatabaseContract.CPOEMedicine.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listCPOEPrescription = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listCPOEPrescription;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<CPOEPrescription> listAllUnSync() {
            ArrayList<CPOEPrescription> listCPOEPrescriptionList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_SYNC + "='1'";
                result = db.query(DatabaseContract.CPOEMedicine.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.CPOEMedicine.DEFAULT_SORT_ORDER);
                listCPOEPrescriptionList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listCPOEPrescriptionList;
        }

        public long createUnSync(CPOEPrescription elCPOEPrescription) {
            long rowId = -1;
            try {
                ContentValues values = CPOEMedicineToContentValues(elCPOEPrescription);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.CPOEMedicine.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSync(CPOEPrescription elCPOEPrescription) {
            long rowId = -1;
            try {
                ContentValues values = CPOEMedicineToContentValues(elCPOEPrescription);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine._ID + "='" + elCPOEPrescription.get_ID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSyncCurrentNotes(int ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.CPOEMedicine._ID + "='" + ID + "'";
                values.put(DatabaseContract.CPOEMedicine.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long RemoveSyncItem(int _ID) {
            long rowId = -1;
            try {
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine._ID + "='" + _ID + "'";
                rowId = databaseContract.open().delete(DatabaseContract.CPOEMedicine.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateSyncLocalItem(int _ID, CPOEPrescription elCPOEPrescription) {
            long rowId = -1;
            try {
                elCPOEPrescription.setIsSync("0");
                ContentValues values = CPOEMedicineToContentValues(elCPOEPrescription);
                String whereClause = null;
                whereClause = DatabaseContract.CPOEMedicine._ID + "='" + _ID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.CPOEMedicine.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }
    }

    public class ComplaintsListDBAdapter {

        String[] projection = {
                DatabaseContract.ComplaintsList._ID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_ID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_UNITID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_VISITID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_VISIT_UNITID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENTID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENT_UNITID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_DOCTORID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_CHIEF_COMPLAINTS,
                DatabaseContract.ComplaintsList.COLUMN_NAME_ASSOCIATE_COMPLAINTS,
                DatabaseContract.ComplaintsList.COLUMN_NAME_REMARK,
                DatabaseContract.ComplaintsList.COLUMN_NAME_STATUS,
                DatabaseContract.ComplaintsList.COLUMN_NAME_CREATED_UNITID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATED_UNITID,
                DatabaseContract.ComplaintsList.COLUMN_NAME_ADDEDBY,
                DatabaseContract.ComplaintsList.COLUMN_NAME_ADDED_DATETIME,
                DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATEDBY,
                DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATED_DATETIME,
                DatabaseContract.ComplaintsList.COLUMN_NAME_IS_SYNC,
                DatabaseContract.ComplaintsList.COLUMN_NAME_IS_UPDATE
        };

        private ContentValues ComplaintsListToContentValues(ComplaintsList elComplaintsList) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_ID, elComplaintsList.getID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_UNITID, elComplaintsList.getUnitID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_VISITID, elComplaintsList.getVisitID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_VISIT_UNITID, elComplaintsList.getVisitUnitID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENTID, elComplaintsList.getPatientID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENT_UNITID, elComplaintsList.getPatientUnitID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_DOCTORID, elComplaintsList.getDoctorID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_CHIEF_COMPLAINTS, elComplaintsList.getChiefComplaints());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_ASSOCIATE_COMPLAINTS, elComplaintsList.getAssosciateComplaints());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_REMARK, elComplaintsList.getRemark());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_STATUS, elComplaintsList.getStatus());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_CREATED_UNITID, elComplaintsList.getCreatedUnitID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATED_UNITID, elComplaintsList.getUpdatedUnitID());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_ADDEDBY, elComplaintsList.getAddedBy());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_ADDED_DATETIME, elComplaintsList.getAddedDateTime());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATEDBY, elComplaintsList.getUpdatedBy());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATED_DATETIME, elComplaintsList.getUpdatedDateTime());
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_IS_SYNC, elComplaintsList.getIsSync());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ComplaintsList> CursorToArrayList(Cursor result) {
            ArrayList<ComplaintsList> listComplaintsList = null;
            try {
                if (result != null) {
                    listComplaintsList = new ArrayList<ComplaintsList>();
                    while (result.moveToNext()) {
                        ComplaintsList complaintsList = new ComplaintsList();
                        complaintsList.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.ComplaintsList._ID)));
                        complaintsList.setID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_ID)));
                        complaintsList.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_UNITID)));
                        complaintsList.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_VISITID)));
                        complaintsList.setVisitUnitID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_VISIT_UNITID)));
                        complaintsList.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENTID)));
                        complaintsList.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENT_UNITID)));
                        complaintsList.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_DOCTORID)));
                        complaintsList.setChiefComplaints(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_CHIEF_COMPLAINTS)));
                        complaintsList.setAssosciateComplaints(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_ASSOCIATE_COMPLAINTS)));
                        complaintsList.setRemark(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_REMARK)));
                        complaintsList.setStatus(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_STATUS)));
                        complaintsList.setCreatedUnitID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_CREATED_UNITID)));
                        complaintsList.setUpdatedUnitID(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATED_UNITID)));
                        complaintsList.setAddedBy(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_ADDEDBY)));
                        complaintsList.setAddedDateTime(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_ADDED_DATETIME)));
                        complaintsList.setUpdatedBy(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATEDBY)));
                        complaintsList.setUpdatedDateTime(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_UPDATED_DATETIME)));
                        complaintsList.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.ComplaintsList.COLUMN_NAME_IS_SYNC)));
                        listComplaintsList.add(complaintsList);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listComplaintsList;
        }

        public long create(ComplaintsList complaintsList) {
            long rowId = -1;
            try {

                if (Count(complaintsList.getID()) == 0) {
                    ContentValues values = ComplaintsListToContentValues(complaintsList);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.ComplaintsList.TABLE_NAME, null, values);
                    }
                } else {
                    update(complaintsList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(ComplaintsList complaintsList) {
            long rowId = -1;
            try {
                ContentValues values = ComplaintsListToContentValues(complaintsList);
                String whereClause = null;
                whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_ID + "='" + complaintsList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.ComplaintsList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ComplaintsList> listAll() {
            ArrayList<ComplaintsList> listComplaintsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.ComplaintsList.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.ComplaintsList.DEFAULT_SORT_ORDER);
                listComplaintsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listComplaintsList;
        }

        public ArrayList<ComplaintsList> listAll(String ID) {
            ArrayList<ComplaintsList> listComplaintsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.ComplaintsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.ComplaintsList.DEFAULT_SORT_ORDER);
                listComplaintsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listComplaintsList;
        }

        public ArrayList<ComplaintsList> listAll(String PatientID, String VisitID) {
            ArrayList<ComplaintsList> listComplaintsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENTID + "='" + PatientID + "'";
                // AND " +DatabaseContract.ComplaintsList.COLUMN_NAME_STATUS + "= 'True'";
                result = db.query(DatabaseContract.ComplaintsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.ComplaintsList.DEFAULT_SORT_ORDER);
                listComplaintsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listComplaintsList;
        }

        public long updateISStatus(ComplaintsList complaintsList) {
            long rowId = -1;
            try {
                ContentValues values = ComplaintsListToContentValues(complaintsList);
                String whereClause = null;
                whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_ID + "='" + complaintsList.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.ComplaintsList.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.ComplaintsList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.ComplaintsList.COLUMN_NAME_STATUS + "= 'False'";
                databaseContract.open().delete(DatabaseContract.ComplaintsList.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
        }

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<ComplaintsList> DiagnosisLists = null;
            try {
                DiagnosisLists = listAll(PatientID, VisitID);
                if (DiagnosisLists != null && DiagnosisLists.size() > 0) {
                    Count = DiagnosisLists.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.ComplaintsList.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.ComplaintsList.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public long updateCurrentNotes(String ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.ComplaintsList.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_ID + "='" + ID + "'";
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.ComplaintsList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long removeCurrentUpdateNotes() {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.ComplaintsList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(
                            DatabaseContract.ComplaintsList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ComplaintsList> CurrentUpdateNotes() {
            ArrayList<ComplaintsList> listComplaintsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_IS_UPDATE + "='" + "1" + "'";
                result = db.query(DatabaseContract.ComplaintsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listComplaintsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listComplaintsList;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<ComplaintsList> listAllUnSync() {
            ArrayList<ComplaintsList> listComplaintsList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.ComplaintsList.COLUMN_NAME_IS_SYNC + "='1'";
                result = db.query(DatabaseContract.ComplaintsList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.ComplaintsList.DEFAULT_SORT_ORDER);
                listComplaintsList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listComplaintsList;
        }

        public long createUnSync(ComplaintsList elComplaintsList) {
            long rowId = -1;
            try {
                ContentValues values = ComplaintsListToContentValues(elComplaintsList);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.ComplaintsList.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateSyncLocalItem(int _ID, ComplaintsList elComplaintsList) {
            long rowId = -1;
            try {
                elComplaintsList.setIsSync("0");
                ContentValues values = ComplaintsListToContentValues(elComplaintsList);
                String whereClause = null;
                whereClause = DatabaseContract.ComplaintsList._ID + "='" + _ID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.ComplaintsList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }
    }

    public class ReferralServiceListDBAdapter {

        String[] projection = {
                DatabaseContract.ReferralServiceList._ID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_ID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_UNITID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENTID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENT_UNITID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_DOCTORID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_VISITID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_DEPTID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_SERVICEID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_SERVICE_NAME,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_REFERRAL_DOCTORID,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_REFERRAL_DOCTORNAME,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_RATE,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_SYNC,
                DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_UPDATE
        };

        private ContentValues ReferralServiceListToContentValues(ReferralDoctorPerService elReferralDoctorPerService) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_ID, elReferralDoctorPerService.getID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_UNITID, elReferralDoctorPerService.getUnitID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENTID, elReferralDoctorPerService.getPatientID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENT_UNITID, elReferralDoctorPerService.getPatientUnitID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_DOCTORID, elReferralDoctorPerService.getDoctorID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_VISITID, elReferralDoctorPerService.getVisitId());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_DEPTID, elReferralDoctorPerService.getDepartmentID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_SERVICEID, elReferralDoctorPerService.getServiceID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_SERVICE_NAME, elReferralDoctorPerService.getServiceName());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_REFERRAL_DOCTORID, elReferralDoctorPerService.getReferralDoctorID());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_REFERRAL_DOCTORNAME, elReferralDoctorPerService.getReferralDoctorName());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_RATE, elReferralDoctorPerService.getRate());
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_SYNC, elReferralDoctorPerService.getIsSync());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ReferralDoctorPerService> CursorToArrayList(Cursor result) {
            ArrayList<ReferralDoctorPerService> listReferralServiceList = null;
            try {
                if (result != null) {
                    listReferralServiceList = new ArrayList<ReferralDoctorPerService>();
                    while (result.moveToNext()) {
                        ReferralDoctorPerService elReferralDoctorPerService = new ReferralDoctorPerService();
                        elReferralDoctorPerService.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.ReferralServiceList._ID)));
                        elReferralDoctorPerService.setID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_ID)));
                        elReferralDoctorPerService.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_UNITID)));
                        elReferralDoctorPerService.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENTID)));
                        elReferralDoctorPerService.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENT_UNITID)));
                        elReferralDoctorPerService.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_DOCTORID)));
                        elReferralDoctorPerService.setVisitId(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_VISITID)));
                        elReferralDoctorPerService.setDepartmentID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_DEPTID)));
                        elReferralDoctorPerService.setServiceID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_SERVICEID)));
                        elReferralDoctorPerService.setServiceName(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_SERVICE_NAME)));
                        elReferralDoctorPerService.setReferralDoctorID(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_REFERRAL_DOCTORID)));
                        elReferralDoctorPerService.setReferralDoctorName(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_REFERRAL_DOCTORNAME)));
                        elReferralDoctorPerService.setRate(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_RATE)));
                        elReferralDoctorPerService.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_SYNC)));
                        listReferralServiceList.add(elReferralDoctorPerService);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listReferralServiceList;
        }

        public long create(ReferralDoctorPerService elReferralDoctorPerService) {
            long rowId = -1;
            try {

                if (Count(elReferralDoctorPerService.getID()) == 0) {
                    ContentValues values = ReferralServiceListToContentValues(elReferralDoctorPerService);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.ReferralServiceList.TABLE_NAME, null, values);
                    }
                } else {
                    update(elReferralDoctorPerService);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(ReferralDoctorPerService elReferralDoctorPerService) {
            long rowId = -1;
            try {
                ContentValues values = ReferralServiceListToContentValues(elReferralDoctorPerService);
                String whereClause = null;
                whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_ID + "='" + elReferralDoctorPerService.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ReferralDoctorPerService> listAll() {
            ArrayList<ReferralDoctorPerService> listReferralServiceList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.ReferralServiceList.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.ReferralServiceList.DEFAULT_SORT_ORDER);
                listReferralServiceList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listReferralServiceList;
        }

        public ArrayList<ReferralDoctorPerService> listAll(String ID) {
            ArrayList<ReferralDoctorPerService> listReferralServiceList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.ReferralServiceList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.ReferralServiceList.DEFAULT_SORT_ORDER);
                listReferralServiceList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listReferralServiceList;
        }

        public ArrayList<ReferralDoctorPerService> listAll(String PatientID, String VisitID) {
            ArrayList<ReferralDoctorPerService> listReferralServiceList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_VISITID + "='" + VisitID + "' AND " +
                        DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENTID + "='" + PatientID + "'";
                // AND " +DatabaseContract.ReferralServiceList.COLUMN_NAME_STATUS + "= 'True'";
                result = db.query(DatabaseContract.ReferralServiceList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null,
                        DatabaseContract.ReferralServiceList.DEFAULT_SORT_ORDER);
                listReferralServiceList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listReferralServiceList;
        }

        /*public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_PATIENTID + "='" + PatientID + "' AND " +
                        DatabaseContract.ReferralServiceList.COLUMN_NAME_VISITID + "='" + VisitID + "'";
                databaseContract.open().delete(DatabaseContract.ReferralServiceList.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
        }*/

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<ReferralDoctorPerService> elReferralDoctorPerServices = null;
            try {
                elReferralDoctorPerServices = listAll(PatientID, VisitID);
                if (elReferralDoctorPerServices != null && elReferralDoctorPerServices.size() > 0) {
                    Count = elReferralDoctorPerServices.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.ReferralServiceList.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.ReferralServiceList.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public long updateCurrentNotes(String ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_ID + "='" + ID + "'";
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long removeCurrentUpdateNotes() {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(
                            DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ReferralDoctorPerService> CurrentUpdateNotes() {
            ArrayList<ReferralDoctorPerService> listReferralServiceList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_UPDATE + "='" + "1" + "'";
                result = db.query(DatabaseContract.ReferralServiceList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listReferralServiceList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listReferralServiceList;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<ReferralDoctorPerService> listAllUnSync() {
            ArrayList<ReferralDoctorPerService> listReferralServiceList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_SYNC + "='1'";
                result = db.query(DatabaseContract.ReferralServiceList.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, DatabaseContract.ReferralServiceList.DEFAULT_SORT_ORDER);
                listReferralServiceList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listReferralServiceList;
        }

        public long createUnSync(ReferralDoctorPerService elReferralServiceList) {
            long rowId = -1;
            try {
                ContentValues values = ReferralServiceListToContentValues(elReferralServiceList);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.ReferralServiceList.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSync(ReferralDoctorPerService elReferralServiceList) {
            long rowId = -1;
            try {
                ContentValues values = ReferralServiceListToContentValues(elReferralServiceList);
                String whereClause = null;
                whereClause = DatabaseContract.ReferralServiceList._ID + "='" + elReferralServiceList.get_ID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long updateUnSyncCurrentNotes(int ID) {
            long rowId = -1;
            SQLiteDatabase sqLiteDatabase = null;
            try {
                sqLiteDatabase = databaseContract.open();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_UPDATE, "0");
                String whereClause = null;
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
                whereClause = DatabaseContract.ReferralServiceList._ID + "='" + ID + "'";
                values.put(DatabaseContract.ReferralServiceList.COLUMN_NAME_IS_UPDATE, "1");
                if (values != null) {
                    rowId = sqLiteDatabase.update(DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateSyncLocalItem(int _ID, ReferralDoctorPerService elReferralDoctorPerService) {
            long rowId = -1;
            try {
                elReferralDoctorPerService.setIsSync("0");
                ContentValues values = ReferralServiceListToContentValues(elReferralDoctorPerService);
                String whereClause = null;
                whereClause = DatabaseContract.ReferralServiceList._ID + "='" + _ID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.ReferralServiceList.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }
    }

    public class PatientFollowUpAdapter {

        String[] projection = {
                DatabaseContract.PatientFollowUp._ID,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_ID,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_UnitID,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientID,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientUnitID,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_VisitID,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_DoctorID,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_Date,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_Advice,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_FollowUpRemarks,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_FollowUpDate,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_IsSync,
                DatabaseContract.PatientFollowUp.COLUMN_NAME_IsUpdate
        };

        private ContentValues PatientFollowUpToContentValues(ELFollowUp elPatientFollowUp) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_ID, elPatientFollowUp.getID());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_UnitID, elPatientFollowUp.getUnitID());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientID, elPatientFollowUp.getPatientID());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientUnitID, elPatientFollowUp.getPatientUnitID());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_VisitID, elPatientFollowUp.getVisitID());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_DoctorID, elPatientFollowUp.getDoctorID());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_Date, elPatientFollowUp.getDate());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_Advice, elPatientFollowUp.getAdvice());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_FollowUpRemarks, elPatientFollowUp.getFollowUpRemarks());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_FollowUpDate, elPatientFollowUp.getFollowUpDate());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_IsSync, elPatientFollowUp.getIsSync());
                values.put(DatabaseContract.PatientFollowUp.COLUMN_NAME_IsUpdate, elPatientFollowUp.getIsUpdate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELFollowUp> CursorToArrayList(Cursor result) {
            ArrayList<ELFollowUp> listELFollowUpList = null;
            try {
                if (result != null) {
                    listELFollowUpList = new ArrayList<ELFollowUp>();
                    while (result.moveToNext()) {
                        ELFollowUp elPatientFollowUp = new ELFollowUp();
                        elPatientFollowUp.set_ID(result.getInt(result.getColumnIndex(DatabaseContract.PatientFollowUp._ID)));
                        elPatientFollowUp.setID(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_ID)));
                        elPatientFollowUp.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_UnitID)));
                        elPatientFollowUp.setPatientID(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientID)));
                        elPatientFollowUp.setPatientUnitID(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientUnitID)));
                        elPatientFollowUp.setVisitID(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_VisitID)));
                        elPatientFollowUp.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_DoctorID)));
                        elPatientFollowUp.setDate(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_Date)));
                        elPatientFollowUp.setAdvice(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_Advice)));
                        elPatientFollowUp.setFollowUpRemarks(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_FollowUpRemarks)));
                        elPatientFollowUp.setFollowUpDate(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_FollowUpDate)));
                        elPatientFollowUp.setIsSync(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_IsSync)));
                        elPatientFollowUp.setIsUpdate(result.getString(result.getColumnIndex(DatabaseContract.PatientFollowUp.COLUMN_NAME_IsUpdate)));
                        listELFollowUpList.add(elPatientFollowUp);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELFollowUpList;
        }

        public long create(ELFollowUp elPatientFollowUp) {
            long rowId = -1;
            try {
                if (Count(elPatientFollowUp.getID()) == 0) {
                    ContentValues values = PatientFollowUpToContentValues(elPatientFollowUp);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.PatientFollowUp.TABLE_NAME, null, values);
                    }
                } else {
                    update(elPatientFollowUp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long update(ELFollowUp elPatientFollowUp) {
            long rowId = -1;
            try {
                ContentValues values = PatientFollowUpToContentValues(elPatientFollowUp);
                String whereClause = null;
                whereClause = DatabaseContract.PatientFollowUp.COLUMN_NAME_ID + "='" + elPatientFollowUp.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.PatientFollowUp.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELFollowUp> listAll() {
            ArrayList<ELFollowUp> listELFollowUpList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.PatientFollowUp.TABLE_NAME,
                        projection, null,
                        null, null, null, null);
                listELFollowUpList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listELFollowUpList;
        }

        public ArrayList<ELFollowUp> listAll(String ID) {
            ArrayList<ELFollowUp> listELFollowUpList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.PatientFollowUp.COLUMN_NAME_ID + "='" + ID + "'";
                }
                result = db.query(DatabaseContract.PatientFollowUp.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listELFollowUpList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
                result.close();
            }
            return listELFollowUpList;
        }

        public ArrayList<ELFollowUp> listAll(String PatientID, String VisitID) {
            ArrayList<ELFollowUp> listFollowUpList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.PatientFollowUp.COLUMN_NAME_VisitID + "='" + VisitID + "' AND " +
                        DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientID + "='" + PatientID + "'";
                // AND " +DatabaseContract.PatientFollowUp.COLUMN_NAME_STATUS + "= 'True'";
                result = db.query(DatabaseContract.PatientFollowUp.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listFollowUpList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listFollowUpList;
        }

        public long updateISStatus(ELFollowUp elPatientFollowUp) {
            long rowId = -1;
            try {
                ContentValues values = PatientFollowUpToContentValues(elPatientFollowUp);
                String whereClause = null;
                whereClause = DatabaseContract.PatientFollowUp.COLUMN_NAME_ID + "='" + elPatientFollowUp.getID() + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.PatientFollowUp.TABLE_NAME, values,
                            whereClause,
                            null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public void delete(String PatientID, String VisitID) {
            try {
                String whereClause = null;
                whereClause = DatabaseContract.PatientFollowUp.COLUMN_NAME_PatientID + "='" + PatientID + "' AND " +
                        DatabaseContract.PatientFollowUp.COLUMN_NAME_VisitID + "='" + VisitID + "'";
                //+ " AND " + DatabaseContract.PatientFollowUp.COLUMN_NAME_STATUS + "= 'False'";
                databaseContract.open().delete(DatabaseContract.PatientFollowUp.TABLE_NAME, whereClause, null);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
        }

        public int CountID(String PatientID, String VisitID) {
            int Count = 0;
            ArrayList<ELFollowUp> elPatientFollowUpLists = null;
            try {
                elPatientFollowUpLists = listAll(PatientID, VisitID);
                if (elPatientFollowUpLists != null && elPatientFollowUpLists.size() > 0) {
                    Count = elPatientFollowUpLists.size();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.PatientFollowUp.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.PatientFollowUp.TABLE_NAME,
                            projection, whereClause,
                            null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        //======================== methods to sync offline data ======================//
        public ArrayList<ELFollowUp> listAllUnSync() {
            ArrayList<ELFollowUp> listELFollowUpList = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                whereClause = DatabaseContract.PatientFollowUp.COLUMN_NAME_IsSync + "='1'";
                result = db.query(DatabaseContract.PatientFollowUp.TABLE_NAME,
                        projection, whereClause,
                        null, null, null, null);
                listELFollowUpList = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listELFollowUpList;
        }

        public long createUnSync(ELFollowUp elFollowUp) {
            long rowId = -1;
            try {
                ContentValues values = PatientFollowUpToContentValues(elFollowUp);
                if (values != null) {
                    rowId = databaseContract.open().insert(DatabaseContract.PatientFollowUp.TABLE_NAME, null, values);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public long UpdateSyncLocalItem(int _ID, ELFollowUp elFollowUp) {
            long rowId = -1;
            try {
                elFollowUp.setIsSync("0");
                ContentValues values = PatientFollowUpToContentValues(elFollowUp);
                String whereClause = null;
                whereClause = DatabaseContract.PatientFollowUp._ID + "='" + _ID + "'";
                if (values != null) {
                    rowId = databaseContract.open().update(
                            DatabaseContract.PatientFollowUp.TABLE_NAME, values, whereClause, null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }
    }

    public class PrefixAdapter {

        String[] projection = {
                DatabaseContract.Prefix.COLUMN_NAME_ID,
                DatabaseContract.Prefix.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues PrefixToContentValues(Prefix bloodGroup) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Prefix.COLUMN_NAME_ID, bloodGroup.getID());
                values.put(DatabaseContract.Prefix.COLUMN_NAME_DESCRIPTION, bloodGroup.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<Prefix> CursorToArrayList(Cursor result) {
            ArrayList<Prefix> listPrefix = null;
            try {
                if (result != null) {
                    listPrefix = new ArrayList<Prefix>();
                    while (result.moveToNext()) {
                        Prefix bloodGroup = new Prefix();
                        bloodGroup.setID(result.getString(result.getColumnIndex(DatabaseContract.Prefix.COLUMN_NAME_ID)));
                        bloodGroup.setDescription(result.getString(result.getColumnIndex(DatabaseContract.Prefix.COLUMN_NAME_DESCRIPTION)));
                        listPrefix.add(bloodGroup);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listPrefix;
        }

        public long create(Prefix bloodGroup) {
            long rowId = -1;
            try {
                if (Count(bloodGroup.getID()) == 0) {
                    ContentValues values = PrefixToContentValues(bloodGroup);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Prefix.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<Prefix> listAll() {
            ArrayList<Prefix> listPrefix = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Prefix.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Prefix.DEFAULT_SORT_ORDER);
                listPrefix = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listPrefix;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Prefix.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Prefix.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.Prefix.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.Prefix.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Prefix.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Prefix.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class GenderAdapter {

        String[] projection = {
                DatabaseContract.Gender.COLUMN_NAME_ID,
                DatabaseContract.Gender.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues GenderToContentValues(Gender gender) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.Gender.COLUMN_NAME_ID, gender.getID());
                values.put(DatabaseContract.Gender.COLUMN_NAME_DESCRIPTION, gender.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<Gender> CursorToArrayList(Cursor result) {
            ArrayList<Gender> listGender = null;
            try {
                if (result != null) {
                    listGender = new ArrayList<Gender>();
                    while (result.moveToNext()) {
                        Gender gender = new Gender();
                        gender.setID(result.getString(result.getColumnIndex(DatabaseContract.Gender.COLUMN_NAME_ID)));
                        gender.setDescription(result.getString(result.getColumnIndex(DatabaseContract.Gender.COLUMN_NAME_DESCRIPTION)));
                        listGender.add(gender);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listGender;
        }

        public long create(Gender gender) {
            long rowId = -1;
            try {
                if (Count(gender.getID()) == 0) {
                    ContentValues values = GenderToContentValues(gender);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.Gender.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Gender.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Gender.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.Gender.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.Gender.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.Gender.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public ArrayList<Gender> listAll() {
            ArrayList<Gender> listGender = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.Gender.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.Gender.DEFAULT_SORT_ORDER);
                listGender = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listGender;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.Gender.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class MaritalStatusAdapter {

        String[] projection = {
                DatabaseContract.MaritalStatus.COLUMN_NAME_ID,
                DatabaseContract.MaritalStatus.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues MaritalStatusToContentValues(MaritalStatus maritalStatus) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.MaritalStatus.COLUMN_NAME_ID, maritalStatus.getID());
                values.put(DatabaseContract.MaritalStatus.COLUMN_NAME_DESCRIPTION, maritalStatus.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<MaritalStatus> CursorToArrayList(Cursor result) {
            ArrayList<MaritalStatus> listMaritalStatus = null;
            try {
                if (result != null) {
                    listMaritalStatus = new ArrayList<MaritalStatus>();
                    while (result.moveToNext()) {
                        MaritalStatus maritalStatus = new MaritalStatus();
                        maritalStatus.setID(result.getString(result.getColumnIndex(DatabaseContract.MaritalStatus.COLUMN_NAME_ID)));
                        maritalStatus.setDescription(result.getString(result.getColumnIndex(DatabaseContract.MaritalStatus.COLUMN_NAME_DESCRIPTION)));
                        listMaritalStatus.add(maritalStatus);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listMaritalStatus;
        }

        public long create(MaritalStatus maritalStatus) {
            long rowId = -1;
            try {
                if (Count(maritalStatus.getID()) == 0) {
                    ContentValues values = MaritalStatusToContentValues(maritalStatus);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.MaritalStatus.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<MaritalStatus> listAll() {
            ArrayList<MaritalStatus> listMaritalStatus = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MaritalStatus.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MaritalStatus.DEFAULT_SORT_ORDER);
                listMaritalStatus = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listMaritalStatus;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.MaritalStatus.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.MaritalStatus.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                if (ID != null) {
                    String whereClause = DatabaseContract.MaritalStatus.COLUMN_NAME_ID + "='" + ID + "'";
                    SQLiteDatabase db = databaseContract.open();
                    result = db.query(DatabaseContract.MaritalStatus.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.MaritalStatus.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.MaritalStatus.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class BloodGroupAdapter {

        String[] projection = {
                DatabaseContract.BloodGroup.COLUMN_NAME_ID,
                DatabaseContract.BloodGroup.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues BloodGroupToContentValues(BloodGroup bloodGroup) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.BloodGroup.COLUMN_NAME_ID, bloodGroup.getID());
                values.put(DatabaseContract.BloodGroup.COLUMN_NAME_DESCRIPTION, bloodGroup.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<BloodGroup> CursorToArrayList(Cursor result) {
            ArrayList<BloodGroup> listBloodGroup = null;
            try {
                if (result != null) {
                    listBloodGroup = new ArrayList<BloodGroup>();
                    while (result.moveToNext()) {
                        BloodGroup bloodGroup = new BloodGroup();
                        bloodGroup.setID(result.getString(result.getColumnIndex(DatabaseContract.BloodGroup.COLUMN_NAME_ID)));
                        bloodGroup.setDescription(result.getString(result.getColumnIndex(DatabaseContract.BloodGroup.COLUMN_NAME_DESCRIPTION)));
                        listBloodGroup.add(bloodGroup);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listBloodGroup;
        }

        public long create(BloodGroup bloodGroup) {
            long rowId = -1;
            try {
                if (Count(bloodGroup.getID()) == 0) {
                    ContentValues values = BloodGroupToContentValues(bloodGroup);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.BloodGroup.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<BloodGroup> listAll() {
            ArrayList<BloodGroup> listBloodGroup = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.BloodGroup.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.BloodGroup.DEFAULT_SORT_ORDER);
                listBloodGroup = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listBloodGroup;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.BloodGroup.TABLE_NAME,
                        projection, null,
                        null, null, null,
                        DatabaseContract.BloodGroup.DEFAULT_SORT_ORDER);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.BloodGroup.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.BloodGroup.TABLE_NAME,
                            projection, whereClause,
                            null, null, null,
                            DatabaseContract.BloodGroup.DEFAULT_SORT_ORDER);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.BloodGroup.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class VisitTypeMasterAdapter {

        String[] projection = {
                DatabaseContract.VisitTypeMaster.COLUMN_NAME_ID,
                DatabaseContract.VisitTypeMaster.COLUMN_NAME_ServiceID,
                DatabaseContract.VisitTypeMaster.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues VisitTypeMasterToContentValues(ELVisitType elVisitType) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.VisitTypeMaster.COLUMN_NAME_ID, elVisitType.getID());
                values.put(DatabaseContract.VisitTypeMaster.COLUMN_NAME_DESCRIPTION, elVisitType.getDescription());
                values.put(DatabaseContract.VisitTypeMaster.COLUMN_NAME_ServiceID, elVisitType.getServiceID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELVisitType> CursorToArrayList(Cursor result) {
            ArrayList<ELVisitType> listVisitTypeMaster = null;
            try {
                if (result != null) {
                    listVisitTypeMaster = new ArrayList<ELVisitType>();
                    while (result.moveToNext()) {
                        ELVisitType elVisitType = new ELVisitType();
                        elVisitType.setID(result.getString(result.getColumnIndex(DatabaseContract.VisitTypeMaster.COLUMN_NAME_ID)));
                        elVisitType.setDescription(result.getString(result.getColumnIndex(DatabaseContract.VisitTypeMaster.COLUMN_NAME_DESCRIPTION)));
                        elVisitType.setServiceID(result.getString(result.getColumnIndex(DatabaseContract.VisitTypeMaster.COLUMN_NAME_ServiceID)));
                        listVisitTypeMaster.add(elVisitType);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listVisitTypeMaster;
        }

        public long create(ELVisitType elVisitType) {
            long rowId = -1;
            try {
                if (Count(elVisitType.getID()) == 0) {
                    ContentValues values = VisitTypeMasterToContentValues(elVisitType);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.VisitTypeMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELVisitType> listAll() {
            ArrayList<ELVisitType> listVisitTypeMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.VisitTypeMaster.TABLE_NAME, projection, null, null, null, null, null);
                listVisitTypeMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listVisitTypeMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.VisitTypeMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.VisitTypeMaster.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.VisitTypeMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.VisitTypeMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class PatientCategoryL1Adapter {

        String[] projection = {
                DatabaseContract.PatientCategoryL1.COLUMN_NAME_ID,
                DatabaseContract.PatientCategoryL1.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues PatientCategoryL1ToContentValues(ELPatientCategory elPatientCategory) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.PatientCategoryL1.COLUMN_NAME_ID, elPatientCategory.getID());
                values.put(DatabaseContract.PatientCategoryL1.COLUMN_NAME_DESCRIPTION, elPatientCategory.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELPatientCategory> CursorToArrayList(Cursor result) {
            ArrayList<ELPatientCategory> listPatientCategoryL1 = null;
            try {
                if (result != null) {
                    listPatientCategoryL1 = new ArrayList<ELPatientCategory>();
                    while (result.moveToNext()) {
                        ELPatientCategory elPatientCategory = new ELPatientCategory();
                        elPatientCategory.setID(result.getString(result.getColumnIndex(DatabaseContract.PatientCategoryL1.COLUMN_NAME_ID)));
                        elPatientCategory.setDescription(result.getString(result.getColumnIndex(DatabaseContract.PatientCategoryL1.COLUMN_NAME_DESCRIPTION)));
                        listPatientCategoryL1.add(elPatientCategory);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listPatientCategoryL1;
        }

        public long create(ELPatientCategory elPatientCategory) {
            long rowId = -1;
            try {
                if (Count(elPatientCategory.getID()) == 0) {
                    ContentValues values = PatientCategoryL1ToContentValues(elPatientCategory);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.PatientCategoryL1.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELPatientCategory> listAll() {
            ArrayList<ELPatientCategory> listPatientCategoryL1 = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.PatientCategoryL1.TABLE_NAME, projection, null, null, null, null, null);
                listPatientCategoryL1 = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listPatientCategoryL1;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.PatientCategoryL1.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.PatientCategoryL1.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.PatientCategoryL1.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.PatientCategoryL1.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class DoctorNameMasterAdapter {

        String[] projection = {
                DatabaseContract.DoctorNameMaster.COLUMN_NAME_DOCTOR_ID,
                DatabaseContract.DoctorNameMaster.COLUMN_NAME_DOCTOR_NAME
        };

        private ContentValues ELDoctorMasterToContentValues(ELDoctorMaster elDoctorMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.DoctorNameMaster.COLUMN_NAME_DOCTOR_ID, elDoctorMaster.getDoctorID());
                values.put(DatabaseContract.DoctorNameMaster.COLUMN_NAME_DOCTOR_NAME, elDoctorMaster.getDoctorName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELDoctorMaster> CursorToArrayList(Cursor result) {
            ArrayList<ELDoctorMaster> listELDoctorMaster = null;
            try {
                if (result != null) {
                    listELDoctorMaster = new ArrayList<ELDoctorMaster>();
                    while (result.moveToNext()) {
                        ELDoctorMaster elDoctorMaster = new ELDoctorMaster();
                        elDoctorMaster.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.DoctorNameMaster.COLUMN_NAME_DOCTOR_ID)));
                        elDoctorMaster.setDoctorName(result.getString(result.getColumnIndex(DatabaseContract.DoctorNameMaster.COLUMN_NAME_DOCTOR_NAME)));
                        listELDoctorMaster.add(elDoctorMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELDoctorMaster;
        }

        public long create(ELDoctorMaster elDoctorMaster) {
            long rowId = -1;
            try {
                if (Count(elDoctorMaster.getDoctorID()) == 0) {
                    ContentValues values = ELDoctorMasterToContentValues(elDoctorMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.DoctorNameMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELDoctorMaster> listAll() {
            ArrayList<ELDoctorMaster> listELDoctorMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DoctorNameMaster.TABLE_NAME, projection, null, null, null, null, null);
                listELDoctorMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listELDoctorMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.DoctorNameMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.DoctorNameMaster.COLUMN_NAME_DOCTOR_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.DoctorNameMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.DoctorNameMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class PCPDoctorMasterAdapter {

        String[] projection = {
                DatabaseContract.PCPDoctorMster.COLUMN_NAME_DOCTOR_ID,
                DatabaseContract.PCPDoctorMster.COLUMN_NAME_UNIT_ID,
                DatabaseContract.PCPDoctorMster.COLUMN_NAME_DOCTOR_NAME,
                DatabaseContract.PCPDoctorMster.COLUMN_NAME_SPEC_ID
        };

        private ContentValues ELDoctorMasterToContentValues(ELDoctorMaster elDoctorMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.PCPDoctorMster.COLUMN_NAME_DOCTOR_ID, elDoctorMaster.getDoctorID());
                values.put(DatabaseContract.PCPDoctorMster.COLUMN_NAME_UNIT_ID, elDoctorMaster.getUnitID());
                values.put(DatabaseContract.PCPDoctorMster.COLUMN_NAME_DOCTOR_NAME, elDoctorMaster.getDoctorName());
                values.put(DatabaseContract.PCPDoctorMster.COLUMN_NAME_SPEC_ID, elDoctorMaster.getSpecializationID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELDoctorMaster> CursorToArrayList(Cursor result) {
            ArrayList<ELDoctorMaster> listELDoctorMaster = null;
            try {
                if (result != null) {
                    listELDoctorMaster = new ArrayList<ELDoctorMaster>();
                    while (result.moveToNext()) {
                        ELDoctorMaster elDoctorMaster = new ELDoctorMaster();
                        elDoctorMaster.setDoctorID(result.getString(result.getColumnIndex(DatabaseContract.PCPDoctorMster.COLUMN_NAME_DOCTOR_ID)));
                        elDoctorMaster.setUnitID(result.getString(result.getColumnIndex(DatabaseContract.PCPDoctorMster.COLUMN_NAME_UNIT_ID)));
                        elDoctorMaster.setDoctorName(result.getString(result.getColumnIndex(DatabaseContract.PCPDoctorMster.COLUMN_NAME_DOCTOR_NAME)));
                        elDoctorMaster.setSpecializationID(result.getString(result.getColumnIndex(DatabaseContract.PCPDoctorMster.COLUMN_NAME_SPEC_ID)));
                        listELDoctorMaster.add(elDoctorMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELDoctorMaster;
        }

        public long create(ELDoctorMaster elDoctorMaster) {
            long rowId = -1;
            try {
                if (Count(elDoctorMaster.getDoctorID()) == 0) {
                    ContentValues values = ELDoctorMasterToContentValues(elDoctorMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(DatabaseContract.PCPDoctorMster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELDoctorMaster> listAll(String UnitID) {
            ArrayList<ELDoctorMaster> listELDoctorMaster = null;
            Cursor result = null;
            String whereClause = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                whereClause = DatabaseContract.PCPDoctorMster.COLUMN_NAME_UNIT_ID + "='" + UnitID + "'";
                result = db.query(DatabaseContract.PCPDoctorMster.TABLE_NAME, projection, whereClause, null, null, null, null);
                listELDoctorMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listELDoctorMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.PCPDoctorMster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.PCPDoctorMster.COLUMN_NAME_DOCTOR_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.PCPDoctorMster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.PCPDoctorMster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class CompanyMasterAdapter {

        String[] projection = {
                DatabaseContract.CompanyNameMaster.COLUMN_NAME_ID,
                DatabaseContract.CompanyNameMaster.COLUMN_NAME_DESCRIPTION
        };

        private ContentValues ELCompanyNameMasterToContentValues(ELCompanyName elCompanyNameMaster) {
            ContentValues values = null;
            try {
                values = new ContentValues();
                values.put(DatabaseContract.CompanyNameMaster.COLUMN_NAME_ID, elCompanyNameMaster.getID());
                values.put(DatabaseContract.CompanyNameMaster.COLUMN_NAME_DESCRIPTION, elCompanyNameMaster.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return values;
        }

        private ArrayList<ELCompanyName> CursorToArrayList(Cursor result) {
            ArrayList<ELCompanyName> listELCompanyNameMaster = null;
            try {
                if (result != null) {
                    listELCompanyNameMaster = new ArrayList<ELCompanyName>();
                    while (result.moveToNext()) {
                        ELCompanyName elCompanyNameMaster = new ELCompanyName();
                        elCompanyNameMaster.setID(result.getString(result.getColumnIndex(DatabaseContract.CompanyNameMaster.COLUMN_NAME_ID)));
                        elCompanyNameMaster.setDescription(result.getString(result.getColumnIndex(DatabaseContract.CompanyNameMaster.COLUMN_NAME_DESCRIPTION)));
                        listELCompanyNameMaster.add(elCompanyNameMaster);
                    }
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listELCompanyNameMaster;
        }

        public long create(ELCompanyName elCompanyNameMaster) {
            long rowId = -1;
            try {
                if (Count(elCompanyNameMaster.getID()) == 0) {
                    ContentValues values = ELCompanyNameMasterToContentValues(elCompanyNameMaster);
                    if (values != null) {
                        rowId = databaseContract.open().insert(
                                DatabaseContract.CompanyNameMaster.TABLE_NAME, null, values);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                databaseContract.close();
            }
            return rowId;
        }

        public ArrayList<ELCompanyName> listAll() {
            ArrayList<ELCompanyName> listELCompanyNameMaster = null;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CompanyNameMaster.TABLE_NAME, projection, null, null, null, null, null);
                listELCompanyNameMaster = CursorToArrayList(result);
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
                result.close();
            }
            return listELCompanyNameMaster;
        }

        public int TotalCount() {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                result = db.query(DatabaseContract.CompanyNameMaster.TABLE_NAME, projection, null, null, null, null, null);
                if (result != null) {
                    Count = result.getCount();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public int Count(String ID) {
            int Count = -1;
            Cursor result = null;
            try {
                SQLiteDatabase db = databaseContract.open();
                String whereClause = null;
                if (ID != null) {
                    whereClause = DatabaseContract.CompanyNameMaster.COLUMN_NAME_ID + "='" + ID + "'";
                    result = db.query(DatabaseContract.CompanyNameMaster.TABLE_NAME, projection, whereClause, null, null, null, null);
                    if (result != null) {
                        Count = result.getCount();
                        result.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                databaseContract.close();
            }
            return Count;
        }

        public void delete() {
            try {
                SQLiteDatabase db = databaseContract.open();
                db.delete(DatabaseContract.CompanyNameMaster.TABLE_NAME, null, null);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}