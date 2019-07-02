package com.bricks.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author DraLastat
 * @Description Contains time related utility methods.
 */

public class Utils_Timer {

    private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private final static SimpleDateFormat TIME_FORMAT_MSEC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    private final static SimpleDateFormat FILE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
    // only static methods, don't allow construction
    private Utils_Timer() {
    }

    /**
     * Return a prettified version of the given elapsed time in milliseconds.
     */
    public static synchronized String formatElapsedTime(long elapsedTimeMs) {
        if (elapsedTimeMs < 1000) {
            return String.format("%d ms", elapsedTimeMs);
        }
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMs) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMs) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMs);
        StringBuilder time = new StringBuilder();
        if (hours > 0) {
            time.append(hours);
            time.append("h ");
        }
        if (minutes > 0) {
            time.append(minutes);
            time.append("m ");
        }
        time.append(seconds);
        time.append("s");

        return time.toString();
    }

    /**
     * Return a readable formatted version of the given epoch time.
     *
     * @param epochTime the epoch time in milliseconds
     * @return a user readable string
     */
    public static synchronized String formatTimeStamp(long epochTime) {
        return TIME_FORMAT.format(new Date(epochTime));
    }
    
    public static String getTimestamp() {
        return formatTimeStamp(System.currentTimeMillis());
    }
    
    public static synchronized String formatTimeStampMsec(long epochTime) {
        return TIME_FORMAT_MSEC.format(new Date(epochTime));
    }
    
    public static String getTimestampMsec() {
        return formatTimeStampMsec(System.currentTimeMillis());
    }
    
    public static synchronized String formatTimeForFile(long epochTime) {
        return FILE_TIME_FORMAT.format(new Date(epochTime));
    }
    
    public static String getTimestampForFile() {
        return formatTimeForFile(System.currentTimeMillis());
    }
    
}