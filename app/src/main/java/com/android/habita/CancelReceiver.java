package com.android.habita;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CancelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String name = bundle.getString("name");
            HistoryManager.add(name, false, context);
        }
        NotificationHelper.cancelAll(context);
    }
}
