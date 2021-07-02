package com.android.habita;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class HabitFormDialogFragment extends DialogFragment {
    private final Habit habit;
    private TextView habitTimeTxt;

    public Habit getHabit() {
        return habit;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface IListener {
        /* On habit dialog clicked "save" button
        * */
        void onDialogPositiveClicked(HabitFormDialogFragment dialog);
        void onDialogNegativeClicked(HabitFormDialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    IListener mListener;

    public HabitFormDialogFragment() {
        habit = new Habit();
    }

    public HabitFormDialogFragment(Habit habit) {
        this.habit = habit;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Build custom view
        final View view = inflater.inflate(R.layout.dialog_habit_form,null);

        // set values
        TextView habitNameTxt = view.findViewById(R.id.habitNameEditTxt);
        habitNameTxt.setText(habit.getName());

        if (habit.getTime() != null) {
            habitTimeTxt = view.findViewById(R.id.habitTime);
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("HH:mm");
            habitTimeTxt.setText(dtfOut.print(habit.getTime()));
        }
        updateToggleButtons(view);
        updateRadOccurrence(view);

        ((RadioGroup) view.findViewById(R.id.radioGroupOccurrence)).setOnCheckedChangeListener((group, checkedId) -> onOccurrenceRadGroupClicked(group));

        List<Integer> dayBtnIds = Arrays.asList(R.id.monBtn, R.id.tueBtn, R.id.wedBtn,
                R.id.thuBtn, R.id.friBtn, R.id.satBtn, R.id.sunBtn);

        for (Integer dayBtnId: dayBtnIds) {
            view.findViewById(dayBtnId).setOnClickListener(v -> onDaysToggleBtnClicked((View) v.getParent()));
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.saveBtn, (dialog, id) -> {
                    // Set habit.name
                    habit.setName(((TextView) view.findViewById(R.id.habitNameEditTxt)).getText().toString());
                    // Set habit.time
                    String timeStr = ((TextView) view.findViewById(R.id.habitTime)).getText().toString();
                    DateTimeFormatter source = DateTimeFormat.forPattern("HH:mm");
                    habit.setTime(source.parseLocalTime(timeStr));
                    mListener.onDialogPositiveClicked(HabitFormDialogFragment.this);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> mListener.onDialogNegativeClicked(HabitFormDialogFragment.this));
        return builder.create();
    }

    public void onDaysToggleBtnClicked(View view) {
        view = (View) view.getParent();
        List<ToggleButton> buttons = getButtons(view);

        habit.setDays(OccurrenceUtil.toSet(buttons));

        updateRadOccurrence(view);
    }

    private void updateRadOccurrence(View view) {
        if (habit.getDays().containsAll(OccurrenceUtil.getCollDaily())) {
            RadioButton radioButton = view.findViewById(R.id.radioDaily);
            radioButton.setChecked(true);
        } else if (habit.getDays().containsAll(OccurrenceUtil.getCollWeekday())) {
            RadioButton radioButton = view.findViewById(R.id.radioWeekday);
            radioButton.setChecked(true);
        } else if (habit.getDays().containsAll(OccurrenceUtil.getCollWeekend())) {
            RadioButton radioButton = view.findViewById(R.id.radioWeekend);
            radioButton.setChecked(true);
        }
    }

    public void onOccurrenceRadGroupClicked(View view) {
        view = (View) view.getParent();

        RadioGroup radioGroup = view.findViewById(R.id.radioGroupOccurrence);
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radioDaily) {
            habit.setDays(new HashSet<>(OccurrenceUtil.getCollDaily()));
        } else if (selectedId == R.id.radioWeekday) {
            habit.setDays(new HashSet<>(OccurrenceUtil.getCollWeekday()));
        } else if (selectedId == R.id.radioWeekend) {
            habit.setDays(new HashSet<>(OccurrenceUtil.getCollWeekend()));
        }

        updateToggleButtons(view);
    }

    private void updateToggleButtons(View view) {
        OccurrenceUtil.toButtons(habit.getDays(), getButtons(view));
    }

    public List<ToggleButton> getButtons(View view) {
        List<ToggleButton> buttons = new ArrayList<>();

        buttons.add(view.findViewById(R.id.monBtn));
        buttons.add(view.findViewById(R.id.tueBtn));
        buttons.add(view.findViewById(R.id.wedBtn));
        buttons.add(view.findViewById(R.id.thuBtn));
        buttons.add(view.findViewById(R.id.friBtn));
        buttons.add(view.findViewById(R.id.satBtn));
        buttons.add(view.findViewById(R.id.sunBtn));

        return buttons;
    }

    // Override the Fragment.onAttach() method to instantiate the HabitFormDialogListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the HabitFormDialogListener so we can send events to the host
            mListener = (IListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(this.toString()
                    + " must implement HabitFormDialogListener");
        }
    }

}
