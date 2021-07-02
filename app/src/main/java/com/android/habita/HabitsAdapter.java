package com.android.habita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.ViewHolder> {
    List<Habit> habits;
    WeakReference<Context> mContextWeakReference;
    private final IListener mListener;

    public interface IListener {
        void onHabitItemClicked(int caller);
        void onHabitDeleteClicked(int caller);
    }


    public HabitsAdapter(List<Habit> habits, Context context) {
        this.habits = habits;
        this.mContextWeakReference = new WeakReference<Context>(context);
        this.mListener = (IListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = mContextWeakReference.get();

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_item, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillForm(position, habits.get(position));
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final List<ToggleButton> buttons = new ArrayList<>();
        final TextView habitNameTxt;
        private final View itemView;
        public IListener mListener;
        private final Button delBtn;
        public int habitIdx;

        public ViewHolder(View itemView, IListener listener) {
            super(itemView);
            this.mListener = listener;
            this.itemView = itemView;
            habitNameTxt = this.itemView.findViewById(R.id.habitNameTxt);
            delBtn = this.itemView.findViewById(R.id.delBtn);

            buttons.add(this.itemView.findViewById(R.id.monBtn));
            buttons.add(this.itemView.findViewById(R.id.tueBtn));
            buttons.add(this.itemView.findViewById(R.id.wedBtn));
            buttons.add(this.itemView.findViewById(R.id.thuBtn));
            buttons.add(this.itemView.findViewById(R.id.friBtn));
            buttons.add(this.itemView.findViewById(R.id.satBtn));
            buttons.add(this.itemView.findViewById(R.id.sunBtn));

            delBtn.setOnClickListener(v -> {
                mListener.onHabitDeleteClicked(habitIdx);
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onHabitItemClicked(getLayoutPosition());
        }

        public void fillForm(int idx, Habit habit) {
            habitNameTxt.setText(habit.getName());
            habitIdx = idx;
            OccurrenceUtil.toButtons(habit.getDays(), buttons);
        }
    }

}
