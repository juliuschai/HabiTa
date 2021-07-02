package com.android.habita;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryManager {
    public static String FILENAME = "histories.json";
//    public Map<String, Habit> histories;

    public static void add(String name, boolean success, Context context) {
        // read json
        Map<String, History> histories = HistoryManager.readFromJSON(context);

        // add record
        History curHist = histories.getOrDefault(name, new History(name));

        if (curHist.timestamps == null)
            curHist.timestamps = new ArrayList<>();
        curHist.timestamps.add(0, new History.Time(success));
        histories.put(name, curHist);

        // write json
        HistoryManager.saveToJSON(histories, context);
    }

    public static Map<String, History> readFromJSON(Context context) {
        Gson gson = new Gson();
        JsonReader reader;
        Map<String, History> histories;
        try {
            File file = new File(context.getApplicationInfo().dataDir, FILENAME);
            reader = new JsonReader(new FileReader(file));
            histories = gson.fromJson(reader, new TypeToken<Map<String, History>>(){}.getType());
        } catch (FileNotFoundException e) {
            histories = new HashMap<>();
        }
        return histories;
    }

    public static void saveToJSON(Map<String, History> histories, Context context) {
        try {
            File file = new File(context.getApplicationInfo().dataDir, FILENAME);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            Gson gson = new GsonBuilder().create();
            gson.toJson(histories, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
