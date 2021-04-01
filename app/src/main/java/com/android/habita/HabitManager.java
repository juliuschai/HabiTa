package com.android.habita;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HabitManager {
    private static HabitManager INSTANCE = null;
    List<Habit> habits;

    private HabitManager() { }

    public static HabitManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HabitManager();
        }
        return(INSTANCE);
    }

    private void read() {
        List<Habit> result = new ArrayList<>();

        Habit habit1 = new Habit();
        habit1.setName("Test");
        habit1.setTime(7, 0);
        Set<DayOfWeek> days1 = new HashSet<>();
        days1.add(DayOfWeek.MONDAY);
        habit1.setDays(days1);
        result.add(habit1);

        habits = result;

    }

    public List<Habit> get() {
        if (habits == null) { read(); }

        return habits;
    }

    public void write() {
        // TODO:
    }

    public List<Habit> add(Habit habit) {
        habits.add(habit);

        return habits;
    }

    public static void toFile(List<Habit> habits) {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("habits.json"), habits);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Habit> fromFile() {
        final ObjectMapper objectMapper = new ObjectMapper();

        List<Habit> habits = new ArrayList<>();
        try {
            Habit[] datas =  objectMapper.readValue(new File("habits.json"), Habit[].class);
            habits = Arrays.asList(datas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return habits;
    }
}
