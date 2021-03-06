package dieunguyen.com.simpletimerapp.View;

import dieunguyen.com.simpletimerapp.Model.TimeModel.TimeStatus;

/**
 * Created by DieuNguyen on 3/6/21.
 */

public interface SimpleTimerInterface {
    void notifyFinishCountDown();

    void updatePauseTitleButton(TimeStatus status);

    void updateProgress(int percent);

    void updateHoursText(String hours);

    void updateMinutesText(String minutes);

    void updateSecondText(String seconds);

    void updateEnableInputTimeIfNeed(TimeStatus status);
}
