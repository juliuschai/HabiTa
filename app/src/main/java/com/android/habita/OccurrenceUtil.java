package com.android.habita;

import android.widget.ToggleButton;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OccurrenceUtil {
    public static void toButtons(Set<DayOfWeek> days, List<ToggleButton> buttons) {
        for (ToggleButton button: buttons) {
            button.setChecked(false);
        }

        if (days.contains(DayOfWeek.MONDAY)) {
            buttons.get(0).setChecked(true);
        }
        if (days.contains(DayOfWeek.TUESDAY)) {
            buttons.get(1).setChecked(true);
        }
        if (days.contains(DayOfWeek.WEDNESDAY)) {
            buttons.get(2).setChecked(true);
        }
        if (days.contains(DayOfWeek.THURSDAY)) {
            buttons.get(3).setChecked(true);
        }
        if (days.contains(DayOfWeek.FRIDAY)) {
            buttons.get(4).setChecked(true);
        }
        if (days.contains(DayOfWeek.SATURDAY)) {
            buttons.get(5).setChecked(true);
        }
        if (days.contains(DayOfWeek.SUNDAY)) {
            buttons.get(6).setChecked(true);
        }
    }

    public static Set<DayOfWeek> toSet (List<ToggleButton> buttons) {
        Set<DayOfWeek> days = new HashSet<>();

        if (buttons.get(0).isChecked()) {
            days.add(DayOfWeek.MONDAY);
        }
        if (buttons.get(1).isChecked()) {
            days.add(DayOfWeek.TUESDAY);
        }
        if (buttons.get(2).isChecked()) {
            days.add(DayOfWeek.WEDNESDAY);
        }
        if (buttons.get(3).isChecked()) {
            days.add(DayOfWeek.THURSDAY);
        }
        if (buttons.get(4).isChecked()) {
            days.add(DayOfWeek.FRIDAY);
        }
        if (buttons.get(5).isChecked()) {
            days.add(DayOfWeek.SATURDAY);
        }
        if (buttons.get(6).isChecked()) {
            days.add(DayOfWeek.SUNDAY);
        }

        return days;
    }

    public static List<DayOfWeek> getCollDaily() {
        return Arrays.asList(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        );
    }

    public static List<DayOfWeek> getCollWeekday() {
        return Arrays.asList(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
        );
    }

    public static List<DayOfWeek> getCollWeekend() {
        return Arrays.asList(
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        );
    }
}
