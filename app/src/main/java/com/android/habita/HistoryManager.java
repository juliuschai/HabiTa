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
        Map<String, History> histories;
        try {
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
            histories = gson.fromJson(json, new TypeToken<Map<String, History>>(){}.getType());
        } catch (IOException e) {
            histories = null;
        }

        if (histories == null) histories = new HashMap<>();

        return histories;
    }

    public static void saveToJSON(Map<String, History> histories, Context context) {
        try {
            Gson gson = new GsonBuilder().create();
            String s = gson.toJson(histories);
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
//
//            File file = new File(context.getApplicationInfo().dataDir, FILENAME);
//            file.getParentFile().mkdirs();
//            file.createNewFile();
//            FileWriter writer = new FileWriter(file);
//            Gson gson = new GsonBuilder().create();
//            gson.toJson(histories, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
