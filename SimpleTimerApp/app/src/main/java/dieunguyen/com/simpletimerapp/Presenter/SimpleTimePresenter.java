package dieunguyen.com.simpletimerapp.Presenter;

import android.os.CountDownTimer;

import dieunguyen.com.simpletimerapp.Model.TimeModel;
import dieunguyen.com.simpletimerapp.Model.TimeModel.TimeStatus;
import dieunguyen.com.simpletimerapp.View.SimpleTimerInterface;

/**
 * Created by DieuNguyen on 3/6/21.
 */

public class SimpleTimePresenter implements SimpleTimePresenterInterface {
    private SimpleTimerInterface mView;
    private TimeModel mModel;
    private long mCountDownTime;
    private long mLimitTime;
    private CountDownTimer mCountDownTimer;

    public SimpleTimePresenter(SimpleTimerInterface view, TimeModel model) {
        mView = view;
        setTimeModel(model);
        startCountDownTimer();
    }

    @Override
    public void pauseButtonClicked() {
        if (mModel == null) {
            return;
        }
        switch (mModel.getStatus()) {
            case RUNNING:
                mModel.setStatus(TimeStatus.PAUSE);
                mCountDownTimer.cancel();
                break;

            case PAUSE:
                mModel.setStatus(TimeStatus.RUNNING);
                resumeCountDownTimer();
                break;

            case STOP:
                setTimeModel(mModel);
                mModel.setStatus(TimeStatus.RUNNING);
                startCountDownTimer();
                break;

            default:
                //noting to do
                break;
        }

        mView.updatePauseTitleButton(mModel.getStatus());
    }

    @Override
    public void cancelButtonClicked() {
        if (mModel == null) {
            return;
        }
        mModel.setStatus(TimeStatus.STOP);
        mView.updatePauseTitleButton(mModel.getStatus());
        resetTime();
    }

    private void setTimeModel(TimeModel model) {
        mModel = model;
        mLimitTime = convertTimeToSeconds(model.getHours(), model.getMinutes(), model.getSeconds());
        mCountDownTime = mLimitTime;
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
        mCountDownTimer = new CountDownTimer(mLimitTime * 1000, 1000) {

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
        mCountDownTimer = new CountDownTimer(mCountDownTime * 1000, 1000) {

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
        mCountDownTime = millisUntilFinished / 1000;
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
        int mins = (int) ((seconds / 3600) % 60);
        int secs = (int) (seconds % 60);

        String time = String.format("%02d:%02d:%02d", hours, mins, secs);
        mView.updateTimeText(time);

        int percent = 0;
        if (mLimitTime > 0) {
            percent = (int) ((mCountDownTime * 100) / mLimitTime);
        }
        mView.updateProgress(percent);

    }
}
