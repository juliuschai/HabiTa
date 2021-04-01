package com.android.habita;

import org.joda.time.LocalTime;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

public class Habit {
    private String name;
    private Set<DayOfWeek> days;
    private LocalTime time;

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
        return time;
    }

    public void setTime(int hour, int minute) {
        this.time = new LocalTime(hour, minute);
    }

}
