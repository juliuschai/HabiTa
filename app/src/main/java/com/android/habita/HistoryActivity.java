package com.android.habita;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements HistoryActivityIListener {

    Map<String, History> histories;
    private History[] historiesArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.histories = HistoryManager.readFromJSON(this);

        this.historiesArr = new History[histories.size()];
        int i = 0;
        for (History history : histories.values()) {
            historiesArr[i] = history;
            i++;
        }

        RecyclerView recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setHasFixedSize(true);

        HistoriesAdapter historiesAdapter = new HistoriesAdapter(historiesArr, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historiesAdapter);
    }

    public void onHistoryItemClicked(Context context, int pos) {
        Intent intent = new Intent(this, HistoryView.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", this.historiesArr[pos].name);
        intent.putExtras(bundle);

        context.startActivity(intent);

    }

    protected static class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.ViewHolder>{

        private final History[] historiesArr;
        private final Context context;

        public HistoriesAdapter(History[] historiesArr, Context context) {
            this.historiesArr = historiesArr;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item, parent, false);

            return new ViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.fillForm(this.historiesArr[position]);
        }

        @Override
        public int getItemCount() {
            return this.historiesArr.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView habitName;
            private final TextView habitSuccess;
            private final TextView habitCancel;
            private final HistoryActivityIListener mListener;
            private final Context context;

            public ViewHolder(View view, Context context) {
                super(view);
                this.context = context;
                this.mListener = (HistoryActivityIListener) context;

                habitName = view.findViewById(R.id.habitName);
                habitSuccess = view.findViewById(R.id.habitSuccess);
                habitCancel = view.findViewById(R.id.habitCancel);

                // Define click listener for the ViewHolder's View
                view.setOnClickListener(this);
            }

            public void fillForm(History history) {
                habitName.setText(history.name);
                habitSuccess.setText(String.valueOf(history.getSuccess()));
                habitCancel.setText(String.valueOf(history.getCancel()));

            }

            @Override
            public void onClick(View v) {
                mListener.onHistoryItemClicked(context, getLayoutPosition());
            }
        }
    }

}

interface HistoryActivityIListener {
    void onHistoryItemClicked(Context context, int pos);
}

