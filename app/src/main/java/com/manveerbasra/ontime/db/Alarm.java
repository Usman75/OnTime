package com.manveerbasra.ontime.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.util.Log;

import com.manveerbasra.ontime.db.converter.BooleanArrayConverter;
import com.manveerbasra.ontime.db.converter.DateConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Entity(tableName = "alarms")
@TypeConverters({DateConverter.class, BooleanArrayConverter.class})
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="alarm_id")
    public int id;

    @ColumnInfo(name = "alarm_time")
    public Date time;

    @ColumnInfo(name = "alarm_active")
    public boolean active;

    @ColumnInfo(name = "alarm_active_days")
    public boolean[] activeDays; // must have length 7

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean[] getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(boolean[] activeDays) {
        this.activeDays = activeDays;
    }

    public Alarm() {
    }

    // Ignored Members

    @Ignore
    public static final String[] daysOfWeek =
            {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Ignore
    public Alarm(Date time, boolean active, boolean[] activeDays) {
        this.time = time;
        this.active = active;
        this.activeDays = activeDays;
    }

    @Ignore
    public boolean isRepeat() {
        return (activeDays != null && activeDays.length > 0);
    }

    @Ignore
    public String getStringTime() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aa");
        return dateFormatter.format(this.time);
    }

    @Ignore
    public String getStringOfActiveDays() {
        // Build string based on which indices are true in activeDays
        StringBuilder builder = new StringBuilder();
        int activeCount = 0;
        for (int i = 0; i < 7; i++) {
            if (activeDays[i]) {
                String formattedDay = daysOfWeek[i].substring(0, 3) + ", ";
                builder.append(formattedDay);
                activeCount++;
            }
        }

        if (activeCount == 7) {
            return "everyday";
        } else if (activeCount == 0) {
            return "never";
        }

        boolean satInArray = activeDays[6]; // "Saturday" in activeDays
        boolean sunInArray = activeDays[0]; // "Sunday" in activeDays

        if (satInArray && sunInArray && activeCount == 2) {
            return "weekends";
        } else if (!satInArray && !sunInArray && activeCount == 5) {
            return "weekdays";
        }

        if (builder.length() > 1) {
            builder.setLength(builder.length() - 2);
        }

        return builder.toString();
    }

    @Ignore
    public long getTimeToRing() {
        String[] timeString = new SimpleDateFormat("HH:mm").format(this.time).split(":");
        int hour = Integer.parseInt(timeString[0]);
        int minute = Integer.parseInt(timeString[1]);

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.i("Alarm.java", calendar.getTime().toString());
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            // alarm time has passed for today
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTimeInMillis();
    }
}
