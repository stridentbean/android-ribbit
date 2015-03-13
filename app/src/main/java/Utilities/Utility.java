package Utilities;

import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

import models.BasicInformation;

/**
 * Created by michaelborglin on 2/12/15.
 */
public class Utility {
    public static final String TAG = Utility.class.getSimpleName();

    public static String getMultiplierText(int averageDaysOnMarket) {
        return ((int)(getMultiplier(averageDaysOnMarket)*100)) +"%";
    }

    private static double getMultiplier(int averageDaysOnMarket) {

        if (averageDaysOnMarket >= 90) {
            return 0.70;
        } else if (averageDaysOnMarket >= 30) {
            return 0.80;
        }
        return 0.85;
    }

    public static int getMultiplierValue(int averageDaysOnMarket, int ARV) {
        return (int) (getMultiplier(averageDaysOnMarket) * ARV);
    }

    /**
     *
     * @param editable
     * Editable value that needs to be formatted
     * @return
     * A US currency formatted String
     */
    private static String getFormattedText(Editable editable) {
        return getFormattedText(false, editable.toString());
    }

    /**
     *
     * @param string
     * string value that needs to be formatted
     * @return
     * A US currency formatted String
     */
    public static String getFormattedText(boolean chopChange, String string) {
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        int tmp = Integer.valueOf(string.replaceAll("\\$|\\.|,", "")).intValue();

        String toRet= defaultFormat.format((double) tmp / 100);
        if(chopChange) {
            return toRet.substring(0, toRet.length()-3);
        } else {
            return toRet;
        }
    }

    /**
     *
     * @param textView
     * TextView that will be altered
     * @param editable
     */
    public static void formatCurrencyTextField(TextView textView, Editable editable) {
        formatCurrencyTextField(textView, editable.toString());
    }

    /**
     *
     * @param textView
     * TextView that will be altered
     * @param string
     * Editable value that needs to be formatted
     */

    public static void formatCurrencyTextField(TextView textView, String string) {
        if(string.equals("")){
            //string "" are not spannable. This prevents error
            string = "0";
        }
        String formattedText = Utility.getFormattedText(false, string);
        textView.setTextKeepState(formattedText);
        if(textView instanceof EditText) {
            ((EditText)textView).setSelection(formattedText.length());
        }
    }

    public static int getIntValueFromCurrencyTextField(TextView textView) {
        if(textView.getText().toString().equals("")) {
            return -1;
        }
        return Integer.valueOf(textView.getText().toString().replaceAll("\\$|\\.|,", "")).intValue();
    }

    public static void formatCurrencyTextField(TextView textView, int value) {
        formatCurrencyTextField(textView, value + "");
    }

    /**
     *
     * @param values
     * List of values to search through
     * @param toFind
     * The value searching for
     * @return
     * Index of toFind in values
     */
    public static int getIndexOfValue(String[] values, String toFind) {
        for(int i=0; i<values.length; i++) {
            if(values[i].equals(toFind)){
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOfValue(String[] values, int toFind) {
        for(int i=0; i<values.length; i++) {
            if(Integer.valueOf(values[i]).intValue() == toFind){
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOfValue(String[] values, double key) {
        for(int i=0; i<values.length; i++) {
            if(Double.valueOf(values[i]).doubleValue() == key){
                return i;
            }
        }
        return -1;
    }

    public static int getIntValueFromCurrencyText(String stringValue) {
        if(stringValue.equals("")) {
            return -1;
        }
        return Integer.valueOf(stringValue.replaceAll("\\$|\\.|,", "")).intValue();
    }

    public static String generateAssignmentPDF(BasicInformation basicInformation) {
        return null;
    }

    public static int findIndexOf(String key, String[] valuesList) {
        for(int i=0; i<valuesList.length; i++) {
            if(valuesList[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }


}
