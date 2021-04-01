package com.android.habita;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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

        habits = habitManager.get();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        habitsAdapter = new HabitsAdapter(habits, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(habitsAdapter);
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

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClicked(HabitFormDialogFragment mDialog) {
        Habit habit = mDialog.getHabit();
        this.habits = habitManager.add(habit);
        habitsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClicked(HabitFormDialogFragment mDialog) {
        mDialog.dismiss();
    }

    @Override
    public void onHabitItemClicked(int caller) {
        showHabitFormDialog(habits.get(caller));
    }

    public void onAddBtnClicked(View view) {
        showHabitFormDialog();
    }
}