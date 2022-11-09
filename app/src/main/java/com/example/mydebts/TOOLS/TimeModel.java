package com.example.mydebts.TOOLS;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.TimePicker;

import java.util.Locale;

public class TimeModel {
    public static String popTimePicker(View view, Context context) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                int  hour;
                int minute;
                String date;
                hour = selectedHour;
                minute = selectedMinute;
                date= String.format(Locale.getDefault(), "%02d:%02d",hour, minute);
            }
        };
        int  hour;
        int minute;
        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, /*style,*/ onTimeSetListener, 00, 00, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
        return "";
    }
}
