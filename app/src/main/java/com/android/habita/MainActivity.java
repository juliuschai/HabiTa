package com.android.habita;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements HabitFormDialogFragment.IListener, HabitsAdapter.IListener {

    private HabitFormDialogFragment dialog;
    private final HabitManager habitManager = HabitManager.getInstance();
    private List<Habit> habits;
    private HabitsAdapter habitsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationHelper.createNotificationChannel(this);

        habits = habitManager.get(this);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        habitsAdapter = new HabitsAdapter(habits, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(habitsAdapter);
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClicked(HabitFormDialogFragment mDialog) {
        Habit habit = mDialog.getHabit();
        habit.refreshAlarm(this);
        boolean storagePermission = isStoragePermissionGranted();
        this.habits = habitManager.add(habit, this);
        habitsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClicked(HabitFormDialogFragment mDialog) {
        mDialog.dismiss();
    }

    @Override
    public void onHabitItemClicked(int idx) {
        showHabitFormDialog(habits.get(idx));
    }

    @Override
    public void onHabitDeleteClicked(int idx) {
        Habit habit = this.habits.get(idx);
        habit.removeAlarm(this);

        this.habits = habitManager.del(idx, this);
        habitsAdapter.notifyDataSetChanged();
    }

    public void onAddBtnClicked(View view) {
        showHabitFormDialog();
    }

    public void showHabitFormDialog() {
        // Create an instance of the dialog fragment and show it
        dialog = new HabitFormDialogFragment();
        dialog.show(getSupportFragmentManager(), "Habit");
    }

    public void showHabitFormDialog(Habit habit) {
        // Create an instance of the dialog fragment and show it
        dialog = new HabitFormDialogFragment(habit);
        dialog.show(getSupportFragmentManager(), "Habit");
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("file permission","Permission is granted");
                return true;
            } else {

                Log.v("file permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("file permission","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("file permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public void onHistoryBtnClicked(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);

        startActivity(intent);
    }
}
