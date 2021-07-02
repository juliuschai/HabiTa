package com.android.habita;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class HistoryView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) throw new RuntimeException("Bundle for HistoryView is null!");

        String historyName = this.getIntent().getExtras().getString("name");
        Map<String, History> histories = HistoryManager.readFromJSON(this);
        History history = histories.get(historyName);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        TimestampsAdapter historiesAdapter = new TimestampsAdapter(history.timestamps, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historiesAdapter);
    }

    static class TimestampsAdapter extends RecyclerView.Adapter<TimestampsAdapter.ViewHolder> {

        private final List<History.Time> timestamps;

        public TimestampsAdapter(List<History.Time> timestamps, Context context) {
            this.timestamps = timestamps;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_view_timestamp_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.fillForm(timestamps.get(position));
        }

        @Override
        public int getItemCount() {
            return this.timestamps.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {


            private final TextView timestamp;
            private final TextView timestampStatus;

            public ViewHolder(View view) {
                super(view);
                timestamp = view.findViewById(R.id.timestamp);
                timestampStatus = view.findViewById(R.id.timestampStatus);
            }

            public void fillForm(History.Time time) {
                this.timestamp.setText(time.getLocalTime().toString());

                if (time.success) {
                    this.timestampStatus.setTextColor(Color.parseColor("#4CAF50"));
                    this.timestampStatus.setText("Success");
                } else {
                    this.timestampStatus.setTextColor(Color.parseColor("##F44336"));
                    this.timestampStatus.setText("Cancelled");
                }
            }
        }
    }
}