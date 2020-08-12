package com.developer.chithlal.tracker.Reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.developer.chithlal.tracker.Services.AppService;

public class AppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,AppService.class));
        Toast.makeText(context, "Power connected", Toast.LENGTH_SHORT).show();

    }
}
