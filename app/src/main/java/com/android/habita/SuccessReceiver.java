package com.android.habita;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SuccessReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String name = bundle.getString("name");
            HistoryManager.add(name, true, context);
            Toast.makeText(context, "After " + name + " success!", Toast.LENGTH_LONG).show();
        }
        NotificationHelper.cancelAll(context);
    }
}
