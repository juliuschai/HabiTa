package com.android.habita;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HabitManager {
    private static HabitManager INSTANCE = null;
    private final String FILENAME = "habits.json";
    List<Habit> habits;

    private HabitManager() { }

    public static HabitManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HabitManager();
        }
        return(INSTANCE);
    }

    public static int newRequestCode(Context context) {
        HabitManager habitManager = INSTANCE;
        List<Habit> habits = habitManager.get(context);
        Set<Integer> requestCodes = new HashSet<>();

        for (int i = 0; i < habits.size(); i++) {
            requestCodes.add(habits.get(i).getRequestCode());
        }

        // randomize a unique 4 digit number from 1000 to 9999
        while (true) {
            int curNum = (int) (Math.random()*9000 + 1000);
            if (!requestCodes.contains(curNum)) {
                return curNum;
            }
        }
    }

    private void read(Context context) {
//        List<Habit> result = new ArrayList<>();
//
//        Habit habit1 = new Habit();
//        habit1.setName("Test");
//        habit1.setTime(LocalTime.parse("07:00:00"));
//        Set<DayOfWeek> days1 = new HashSet<>();
//        days1.add(DayOfWeek.MONDAY);
//        habit1.setDays(days1);
//        result.add(habit1);
//
//new FileReader(new File(context.getApplicationInfo().dataDir, FILENAME)).read()
        try {
//            File file = new File(context.getApplicationInfo().dataDir, FILENAME);
//            JsonReader reader = new JsonReader(new FileReader(file));
            Gson gson = new Gson();
            FileInputStream fis = context.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            this.habits = gson.fromJson(json, new TypeToken<List<Habit>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.habits == null)
            this.habits = new ArrayList<>();
    }

    public List<Habit> get(Context context) {
        if (habits == null) { read(context); }

        return habits;
    }

    public void write(Context context) {
        try {
//            File file = new File(context.getApplicationInfo().dataDir, FILENAME);
//            file.getParentFile().mkdirs();
//            file.createNewFile();
//            FileWriter writer = new FileWriter(file);
            Gson gson = new GsonBuilder().create();
            String s = gson.toJson(this.habits);
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Habit> add(Habit habit, Context context) {
        if (!habits.contains(habit)) {
            habits.add(habit);
        }

        write(context);
        return habits;
    }

    public List<Habit> del(int idx, Context context) {
        habits.remove(idx);
        write(context);
        return habits;
    }

/*    public static void toFile(List<Habit> habits) {
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
    }*/
}
