package dieunguyen.com.simpletimerapp.Model;

/**
 * Created by DieuNguyen on 3/6/21.
 */

public class TimeModel {
    public enum TimeStatus {
        RUNNING,
        STOP,
        PAUSE
    }

    private int mHours;
    private int mMinutes;
    private int mSeconds;
    private TimeStatus mStatus = TimeStatus.STOP;

    public TimeModel(int time, int minutes, int seconds) {
        mHours = time;
        mMinutes = minutes;
        mSeconds = seconds;
    }

    public void setHours(int hours) {
        mHours = hours;
    }

    public int getHours() {
        return mHours;
    }

    public void setMinutess(int minutes) {
        mMinutes = minutes;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public int getSeconds() {
        return mSeconds;
    }

    public void setSeconds(int seconds) {
        mSeconds = seconds;
    }

    public TimeStatus getStatus() {
        return mStatus;
    }

    public void setStatus(TimeStatus status) {
        mStatus = status;
    }
}
