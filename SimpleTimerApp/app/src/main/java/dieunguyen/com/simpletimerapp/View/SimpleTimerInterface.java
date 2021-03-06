package dieunguyen.com.simpletimerapp.View;

import dieunguyen.com.simpletimerapp.Model.TimeModel.TimeStatus;

/**
 * Created by DieuNguyen on 3/6/21.
 */

public interface SimpleTimerInterface {
    void notifyFinishCountDown();

    void updatePauseTitleButton(TimeStatus status);

    void updateTimeText(String strTime);

    void updateProgress(int percent);
}
