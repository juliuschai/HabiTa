package com.android.habita;

import java.util.Calendar;

public class Notes {

    public void setRepeatingAlarm() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String today = null;
//        Calendar calSet = new Calendar();
        if (day == 2 || day == 3 || day == 4 || day == 5 || day == 6) {

//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                    calSet.getTimeInMillis(), 24 * 60 * 60 * 1000,
//                    pendingIntent);
        }
    }
}
