package com.palash.healthspringapp.utilities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.palash.healthspringapp.R;
import com.palash.healthspringapp.entity.ELAppointmentStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LocalSetting {
    public static SharedPreferences settings;
    public static String Activityname;
    public static String ID;
    public static String fragment_name;
    public static String bnt_click;

    //progress dialog for background task
    public TransparentProgressDialog showDialog(Context context) {
        TransparentProgressDialog progressDialog = null;
        try {
            progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
            // progressDialog.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    //hide currently showing progress dialog
    public void hideDialog(TransparentProgressDialog progressDialog) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //clean json string removes unnecessary \ from string
    public String cleanResponseString(String result) {
        try {
            if (result != null && !result.equals("")) {
                result = result.substring(1, result.length() - 1);
                result = result.replace("\\", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //returns error message respect to error code
    public String handleError(int responseCode) {
        String msg = null;
        switch (responseCode) {
            case Constants.HTTP_OK_200:
                msg = "Success";
                break;
            case Constants.HTTP_CREATED_201:
                msg = "Record created on server.";
                break;
            case Constants.HTTP_NO_RECORD_FOUND_OK_204:
                msg = "No records found.";
                break;
            case Constants.HTTP_NOT_FOUND_401:
                msg = "Unauthorized Access.";
                break;
            case Constants.HTTP_NOT_OK_404:
                msg = "An error occurred at the server while executing your request. " +
                        "Please try again later. " +
                        "If the problem persists please contact your system administrator.";
                break;
            case Constants.HTTP_AMBIGUOUS_300:
                msg = "Already present.";
                break;
            case Constants.HTTP_NOT_OK_500:
                msg = "An error occurred at the server while executing your request. " +
                        "Please try again later. " +
                        "If the problem persists please contact your system administrator.";
                break;
            case Constants.HTTP_NOT_OK_501:
                msg = "Request not Processed.";
                break;
            default:
                msg = "Network is unreachable. Please check your network and try again.";
                break;
        }
        return msg;
    }

    public boolean isNetworkAvailable(Context context) {
        boolean status = true;
        /*boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }*/
        return status;
    }

    //used to show alert box
    public void alertbox(final Context boxContext, String mymessage, final Boolean isBackPress) {

        new AlertDialog
                .Builder(boxContext)
                .setTitle(boxContext.getResources().getString(R.string.app_name))
                .setMessage(mymessage)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isBackPress != false) {
                        }
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    //encodes string
    public String encodeString(String password) {
        try {
            byte[] data = password.getBytes("UTF-8");
            password = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    //decodes string
    public String decodeString(String result) {
        String value = "";
        try {
            byte[] data = Base64.decode(result, Base64.DEFAULT);
            value = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    @SuppressLint("SimpleDateFormat")
    public String dateToStirng(Date arg0, String formate) {
        // Define the SimpleDateFormat object
        SimpleDateFormat dateFormat = null;
        // Define the String variable
        String result = null;
        try {
            // Create the SimpleDateFromat object
            dateFormat = new SimpleDateFormat(formate);
            result = dateFormat.format(arg0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the result in string format
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date stringToDate(String arg0, String simpleDateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
        Date strDate = null;
        try {
            strDate = sdf.parse(arg0);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // Return the result in date format
        return strDate;
    }

    public static void Init(Context paramContext) {
        settings = paramContext.getSharedPreferences("MyPrefsFile", 0);
    }

    public static void Load() {
        Activityname = settings.getString("Activityname", "");
        ID = settings.getString("ID", "");
        fragment_name = settings.getString("fragment_name", "");
        bnt_click = settings.getString("bnt_click", "");
    }

    public static void Save() {
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putString("Activityname", Activityname);
        localEditor.putString("ID", ID);
        localEditor.putString("fragment_name", fragment_name);
        localEditor.putString("bnt_click", bnt_click);
        localEditor.commit();
    }

    public String formatDate(String date, String FromFormate, String Toformat) {
        String returnDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(FromFormate);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(Toformat);
        try {
            Date dates = dateFormat.parse(date);
            returnDate = dateFormat2.format(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public String dateToString(int date, int month, int year, String format) {
        SimpleDateFormat dateFormat = null;
        String result = null;

        Calendar calendar = null;
        try {
            if (format != null && format.length() > 0 && date > 0 && month >= 0 && year > 0) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, date);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(calendar.getTime());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String calculateExpiryDate(String effectiveDate, String month) {
        String result = null;
        try {
            SimpleDateFormat form = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = form.parse(effectiveDate);
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                // manipulate date
                if (!month.equals("")) {
                    if (month.equals("12")) {
                        c.add(Calendar.YEAR, 1);
                    } else {
                        c.add(Calendar.MONTH, Integer.parseInt(month));
                    }
                }

                // convert calendar to date
                Date currentDatePlusOne = c.getTime();
                SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-mm-dd");
                result = postFormater.format(currentDatePlusOne);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean checkUnitName(String unitID) {
        Boolean isHeadOfficeSelected = false;
        if (unitID.equals("1")) {
            isHeadOfficeSelected = true;
        }
        return isHeadOfficeSelected;
    }

    public String returnPDFUrl(String fromActivity, String UnitID, String PatientID, String PatientUnitID, String VisitID, String mrNo, String VisitType, String L1, String L3) {
        String pdfURL = "";
        if (fromActivity.equals("Summary")) {
            pdfURL = Constants.PATIENT_VISIT_SUMMARY_URL + UnitID + "&VisitID=" + VisitID + "&PatientID=" + PatientID + "&PatientUnitID=" + PatientUnitID + "&TemplateID=0&UserID=0&PDF=1";
        } else if (fromActivity.equals("ICE")) {
            pdfURL = Constants.Patient_ICE_URL + UnitID + "&PatientUnitID=" + PatientUnitID + "&PatientID=" + PatientID + "&Username=" + "" + "&VisitID=" + VisitID + "&IsPrintPDF=1&L1="
                    + L1 + "&L3=" + L3 + "&FtpLink=" + Constants.FTP_Link + mrNo + "&VisitTypeID=" + VisitType + "";
        } else if (fromActivity.equals("ConsolePrescription")) {
            pdfURL = Constants.Patient_PRESCRIPTION_URL + UnitID + "&VisitID=" + VisitID + "&PatientID=" + PatientID + "&PatientUnitID=" + PatientUnitID + "&TemplateID=0&UserID=0&PDF=1";
        }
        Log.d(Constants.TAG + "pdfURL : ", pdfURL);
        return pdfURL;
    }

    public ArrayList<ELAppointmentStatus> returnAppointmentStatus() {
        ArrayList<ELAppointmentStatus> elAppointmentStatusArrayList = new ArrayList<>();
        ELAppointmentStatus elAppointmentStatus1 = new ELAppointmentStatus();
        elAppointmentStatus1.setID("0");
        elAppointmentStatus1.setDescription("New");

        ELAppointmentStatus elAppointmentStatus2 = new ELAppointmentStatus();
        elAppointmentStatus2.setID("1");
        elAppointmentStatus2.setDescription("Reschedule");

        ELAppointmentStatus elAppointmentStatus3 = new ELAppointmentStatus();
        elAppointmentStatus3.setID("2");
        elAppointmentStatus3.setDescription("Cancelled");

        elAppointmentStatusArrayList.add(elAppointmentStatus1);
        elAppointmentStatusArrayList.add(elAppointmentStatus2);
        elAppointmentStatusArrayList.add(elAppointmentStatus3);

        return elAppointmentStatusArrayList;
    }

    public ArrayList<ELAppointmentStatus> returnVisitStatus() {
        ArrayList<ELAppointmentStatus> elAppointmentStatusArrayList = new ArrayList<>();
        ELAppointmentStatus elAppointmentStatus1 = new ELAppointmentStatus();
        elAppointmentStatus1.setID("0");
        elAppointmentStatus1.setDescription("Open");

        ELAppointmentStatus elAppointmentStatus2 = new ELAppointmentStatus();
        elAppointmentStatus2.setID("1");
        elAppointmentStatus2.setDescription("Close");

        elAppointmentStatusArrayList.add(elAppointmentStatus1);
        elAppointmentStatusArrayList.add(elAppointmentStatus2);

        return elAppointmentStatusArrayList;
    }

    public void showErrorAlert(Context context, String title, String subTitle) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(subTitle)
                .show();
    }

    public void showSuccessAlert(Context context, String title, String subTitle) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(subTitle)
                .show();
    }

    public void showWarningAlert(Context context, String title, String subTitle) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(subTitle)
                .show();
    }
}
