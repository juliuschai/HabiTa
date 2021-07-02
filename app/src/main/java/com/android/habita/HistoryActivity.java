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
        for (History history : histories.values()) {
            historiesArr[0] = history;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        HistoriesAdapter historiesAdapter = new HistoriesAdapter(historiesArr, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historiesAdapter);
    }

    public void onHistoryItemClicked(int pos) {
        Intent intent = new Intent(this, HistoryView.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", this.historiesArr[pos].name);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    protected static class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.ViewHolder>{

        private final HistoryActivityIListener mListener;
        private final History[] historiesArr;

        public HistoriesAdapter(History[] historiesArr, Context context) {
            this.historiesArr = historiesArr;
            this.mListener = (HistoryActivityIListener) context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item, parent, false);

            return new ViewHolder(view, mListener);
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

            public ViewHolder(View view, HistoryActivityIListener mListener) {
                super(view);
                this.mListener = mListener;
                // Define click listener for the ViewHolder's View

                habitName = view.findViewById(R.id.habitName);
                habitSuccess = view.findViewById(R.id.habitSuccess);
                habitCancel = view.findViewById(R.id.habitCancel);
            }

            public void fillForm(History history) {
                habitName.setText(history.name);
                habitSuccess.setText(history.getSuccess());
                habitCancel.setText(history.getCancel());

            }

            @Override
            public void onClick(View v) {
                mListener.onHistoryItemClicked(getLayoutPosition());
            }
        }
    }

}

interface HistoryActivityIListener {
    void onHistoryItemClicked(int pos);
}

