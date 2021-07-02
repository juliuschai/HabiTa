package com.android.habita;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class History {
    public String name;
    public List<History.Time> timestamps;
    private int[] successCancelCount;

    public History(String name) {
        this.name = name;
    }

    public int getSuccess() {
        if (successCancelCount == null)
            updateSuccessCancelCount();
        return successCancelCount[0];
    }

    public int getCancel() {
        if (successCancelCount == null)
            updateSuccessCancelCount();
        return successCancelCount[1];
    }

    private void updateSuccessCancelCount() {
        int[] ret = new int[]{0, 0};
        for (Time timestamp : timestamps) {
            if (timestamp.success) ret[0]++;
            else ret[1]++;
        }
        successCancelCount = ret;
    }

    public static class Time {

        public String localTime;
        public boolean success;

        public Time(boolean success) {
            DateTimeFormatter formatter = DateTimeFormat.longDateTime();
            this.localTime = formatter.print(new LocalDateTime());
            this.success = success;
        }

        public LocalDateTime getLocalTime() {
            DateTimeFormatter formatter = DateTimeFormat.longDateTime();
            return formatter.parseLocalDateTime(localTime);
        }

        public void setLocalTime(LocalDateTime localTime) {
            DateTimeFormatter formatter = DateTimeFormat.longDateTime();
            this.localTime = formatter.print(localTime);
        }

    }
}
