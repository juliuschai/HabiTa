package com.android.habita;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

public class Habit {
    // name String: string of habit name
    // days Set<DayOfWeek>: Set of DayOfWeek enums
    // time LocalTime: Set of DayOfWeek enums

    private String name;
    private Set<DayOfWeek> days;
    private String time;
    private int requestCode = 0;

    public Habit() {
        days = new HashSet<>();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DayOfWeek> getDays() {
        return days;
    }

    public void setDays(Set<DayOfWeek> days) {
        this.days = days;
    }

    public LocalTime getTime() {
        if (time == null) {
            return null;
        }
        DateTimeFormatter source = DateTimeFormat.forPattern("HH:mm");
        return source.parseLocalTime(time);
    }

    public void setTime(LocalTime time) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("HH:mm");
        this.time = dtfOut.print(time);
    }

    public int getRequestCode() {
        return requestCode;
    }

//    public void setRequestCode(int requestCode) {
//        this.requestCode = requestCode;
//    }

    public void removeAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) throw new RuntimeException("alarmManager is null!");
        Intent alarmIntent = new Intent(context, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, this.requestCode, alarmIntent, PendingIntent.FLAG_NO_CREATE);

        alarmManager.cancel(pendingIntent);
    }

    public void refreshAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) throw new RuntimeException("alarmManager is null!");
        Intent alarmIntent = new Intent(context, AlarmReciever.class);

        Bundle extras = new Bundle();
        extras.putString("name", this.name);
        alarmIntent.putExtras(extras);

        // If previous alarm exists
        if (this.requestCode != 0) {
            // delete alarm
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, this.requestCode, alarmIntent, PendingIntent.FLAG_NO_CREATE);
            alarmManager.cancel(pendingIntent);

            this.requestCode = HabitManager.newRequestCode(context);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, this.requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final DateTime dateTimeNow = new DateTime();

        // Set the alarm to the nearest time when alarm will trigger
        DateTime triggerDateTime = new DateTime();
        // set datetime to the desired time
        triggerDateTime = triggerDateTime.withHourOfDay(this.getTime().getHourOfDay());
        triggerDateTime = triggerDateTime.withMinuteOfHour(this.getTime().getMinuteOfHour());
        triggerDateTime = triggerDateTime.withSecondOfMinute(0);
        triggerDateTime = triggerDateTime.withMillisOfSecond(0);
        // if time has passed, add 1 day to triggerDateTime
        if (triggerDateTime.getMillis() < dateTimeNow.getMillis())
            triggerDateTime = triggerDateTime.plusDays(1);
        // Iterate over each day to find nearest time when alarm will trigger
        for (int i = 0; i < 7; i++) {
            DayOfWeek curDay = Habit.intToDayOfWeek(triggerDateTime.getDayOfWeek());
            if (this.days.contains(curDay)) break;

            triggerDateTime = triggerDateTime.plusDays(1);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerDateTime.getMillis(), pendingIntent);
    }

    public static DayOfWeek intToDayOfWeek(int day) {
        DayOfWeek[] dayOfWeekLookup = new DayOfWeek[]{
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        return dayOfWeekLookup[day];
    }
}
