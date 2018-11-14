package com.manveerbasra.ontime.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.manveerbasra.ontime.db.Alarm;
import com.manveerbasra.ontime.viewmodel.AlarmViewModel;

/**
 * Class to control alarm scheduling/cancelling
 */
public class AlarmHandler {

    private AlarmViewModel alarmViewModel;
    private Context appContext;
    private View snackbarAnchor;

    public AlarmHandler(Context context, View snackbarAnchor) {
        this.appContext = context;
        this.snackbarAnchor = snackbarAnchor;
    }

    public void scheduleAlarm(Alarm alarm) {
        if (!alarm.isActive()) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);

        long alarmTimeInMillis = alarm.getTimeInMillis() - System.currentTimeMillis();

        Intent intent = new Intent(appContext, AlarmReceiver.class);
        // intent.putExtra() Use to pass alarm data to receiver.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent);

    }
}