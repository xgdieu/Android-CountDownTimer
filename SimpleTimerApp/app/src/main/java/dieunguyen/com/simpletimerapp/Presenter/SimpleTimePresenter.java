package dieunguyen.com.simpletimerapp.Presenter;

import android.os.CountDownTimer;
import android.util.Log;

import dieunguyen.com.simpletimerapp.Model.TimeModel;
import dieunguyen.com.simpletimerapp.Model.TimeModel.TimeStatus;
import dieunguyen.com.simpletimerapp.View.SimpleTimerInterface;

import static android.content.ContentValues.TAG;

/**
 * Created by DieuNguyen on 3/6/21.
 */

public class SimpleTimePresenter implements SimpleTimePresenterInterface {
    private SimpleTimerInterface mView;
    private TimeModel mModel;
    private long mCountDownTime;
    private long mLimitTime;
    private CountDownTimer mCountDownTimer;
    private final int MILLISECOND = 1000;

    public SimpleTimePresenter(SimpleTimerInterface view) {
        mView = view;
    }

    @Override
    public void pauseButtonClicked(String hours, String minutes, String seconds) {
        if (mModel == null || mModel.getStatus() == TimeStatus.STOP) {
            try {
                TimeModel model = new TimeModel(Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds));
                setTimeModel(model);
            } catch (Exception e) {
                Log.e(TAG, "NumberFormatException");
                return;
            }
        }


        switch (mModel.getStatus()) {
            case RUNNING:
                if (mModel == null) {
                    return;
                }
                mModel.setStatus(TimeStatus.PAUSE);
                mCountDownTimer.cancel();
                break;

            case PAUSE:
                if (mModel == null) {
                    return;
                }
                mModel.setStatus(TimeStatus.RUNNING);
                resumeCountDownTimer();
                break;

            case STOP:
                mModel.setStatus(TimeStatus.RUNNING);
                startCountDownTimer();
                break;

            default:
                //noting to do
                break;
        }

        TimeStatus status = mModel.getStatus();
        mView.updatePauseTitleButton(status);
        mView.updateEnableInputTimeIfNeed(status);
    }

    @Override
    public void cancelButtonClicked() {
        if (mModel == null) {
            return;
        }

        TimeStatus status = TimeStatus.STOP;

        mModel.setStatus(status);
        mView.updatePauseTitleButton(status);
        mView.updateEnableInputTimeIfNeed(status);
        resetTime();
    }

    private void setTimeModel(TimeModel model) {
        mModel = model;
        mLimitTime = convertTimeToSeconds(model.getHours(), model.getMinutes(), model.getSeconds());
        mCountDownTime = mLimitTime;
        if (mModel != null) {
            mView.updatePauseTitleButton(model.getStatus());
        }
    }

    private void resetTime() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mLimitTime = 0;
        mCountDownTime = 0;

        updateView(0);
    }

    private void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mLimitTime * MILLISECOND, MILLISECOND) {

            public void onTick(long millisUntilFinished) {
                onTickCountDownTimer(millisUntilFinished);
            }

            public void onFinish() {
                onFinishCountDownTimer();
            }
        };

        runningCountDownTimer();
    }

    private void resumeCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mCountDownTime * MILLISECOND, MILLISECOND) {

            public void onTick(long millisUntilFinished) {
                onTickCountDownTimer(millisUntilFinished);
            }

            public void onFinish() {
                onFinishCountDownTimer();
            }
        };

        runningCountDownTimer();
    }

    private void runningCountDownTimer() {
        if (mModel == null || mCountDownTimer == null) {
            return;
        }
        mModel.setStatus(TimeStatus.RUNNING);
        mCountDownTimer.start();
    }

    private void onTickCountDownTimer(long millisUntilFinished) {
        mCountDownTime = millisUntilFinished / MILLISECOND;
        updateView(mCountDownTime);
    }

    private void onFinishCountDownTimer() {
        if (mModel == null || mView == null) {
            return;
        }

        mModel.setStatus(TimeStatus.STOP);
        mView.notifyFinishCountDown();
        mView.updatePauseTitleButton(mModel.getStatus());
    }

    private long convertTimeToSeconds(int hours, int minutes, int seconds) {
        return (seconds + (60 * minutes) + (3600 * hours));
    }

    private void updateView(long seconds) {
        int hours = (int) (seconds / 3600);
        int mins = (int) ((seconds / 60) % 60);
        int secs = (int) (seconds % 60);

        mView.updateHoursText(String.format("%02d", hours));
        mView.updateMinutesText(String.format("%02d", mins));
        mView.updateSecondText(String.format("%02d", secs));

        int maxPercent = 100;
        int percent = 0;
        if (mLimitTime > 0) {
            percent = (int) ((mCountDownTime * maxPercent) / mLimitTime);
        }
        mView.updateProgress(percent);

    }
}
