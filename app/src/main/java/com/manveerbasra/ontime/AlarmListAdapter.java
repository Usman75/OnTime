package com.manveerbasra.ontime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.manveerbasra.ontime.db.Alarm;

public class AlarmListAdapter extends ArrayAdapter<Alarm> {

    public AlarmListAdapter(@NonNull Context context, @NonNull Alarm[] alarms) {
        super(context, 0, alarms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Alarm alarm = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }

        if (alarm == null) { // Ensures dereference isn't null
            return convertView;
        }

        // Get views to populate data
        TextView timeTextView = convertView.findViewById(R.id.alarm_time_text);
        TextView repetitionTextView = convertView.findViewById(R.id.alarm_repetition_text);
        Switch activeSwitch = convertView.findViewById(R.id.alarm_active_switch);

        // Populate data into views
        timeTextView.setText(AlarmDataManager.convertToStringTime(alarm.hour, alarm.minute, alarm.meridian));

        if (alarm.repeat) {
            String repetitionText = alarm.activeDays;
            repetitionTextView.setText(repetitionText);
        } else {
            repetitionTextView.setText(getContext().getString(R.string.no_repeat));
        }

        if (alarm.active) {
            activeSwitch.setChecked(true);
            timeTextView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            repetitionTextView.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
        } else {
            activeSwitch.setChecked(false);
            timeTextView.setTextColor(getContext().getResources().getColor(R.color.colorGrey500));
            repetitionTextView.setTextColor(getContext().getResources().getColor(R.color.colorGrey500));
        }

        return convertView;
    }
}
