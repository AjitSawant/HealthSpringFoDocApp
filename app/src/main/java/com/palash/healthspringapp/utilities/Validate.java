package com.palash.healthspringapp.utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.palash.healthspringapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validate {

    public static boolean hasTextInEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {
            editText.setError(Constants.REQUIRED_MSG);
            editText.setFocusable(true);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean hasTextIn(String editText) {
        if (editText.length() == 0) {
            return false;
        }
        return true;
    }

    public static void Msgshow(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.border_whitedark);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        v.setPadding(6, 0, 6, 3);
        toast.show();
    }

    public static boolean checkPasswordValidOrNot(EditText editText) {
        boolean isValid = false;
        String text = editText.getText().toString().trim();
        editText.setError(null);
        String expression = "^[a-z0-9_-]{5,20}$";
        CharSequence inputStr = text;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean hasMinimumCharInEditText(String editText) {
        if (editText.length() > 12 || editText.length() < 6) {
            return false;
        }
        return true;
    }

    public static boolean hasBothEditTextSame(String edtFirst, String edtSecond) {
        if (!edtFirst.equals(edtSecond)) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        //String EMAIL_PATTERN = "^([0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidOption(int option) {
        if (option != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkMobileValidOrNot(EditText editText) {
        boolean isValid = false;
        String text = editText.getText().toString().trim();
        editText.setError(null);

        String expression = "^[0-9]{10,10}$";
        CharSequence inputStr = text;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static int validateSearchDate(String fromDate, String toDate) {
        int result = 0;
        try {
            if (fromDate == null) {
                fromDate = "";
            }
            if (toDate == null) {
                toDate = "";
            }
            if (fromDate.equals(Constants.SEARCH_DATE_FORMAT) && toDate.equals(Constants.SEARCH_DATE_FORMAT)) {
                result = 1;
            } else if ((fromDate.equals(Constants.SEARCH_DATE_FORMAT) && !toDate.equals(Constants.SEARCH_DATE_FORMAT))
                    || (!fromDate.equals(Constants.SEARCH_DATE_FORMAT) && toDate.equals(Constants.SEARCH_DATE_FORMAT))) {
                result = 2;
            } else if (!fromDate.equals(Constants.SEARCH_DATE_FORMAT) && !toDate.equals(Constants.SEARCH_DATE_FORMAT)) {
                if (fromDate.equals(toDate)) {
                    result = 1;
                } else {
                    DateFormat df = new SimpleDateFormat(Constants.SEARCH_DATE_FORMAT);
                    Date frmDate = df.parse(fromDate);
                    Date tDate = df.parse(toDate);
                    if (frmDate.before(tDate)) {
                        result = 1;
                    } else {
                        result = 3;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
