package dieunguyen.com.simpletimerapp.Presenter;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

/**
 * Created by DieuNguyen on 3/7/21.
 */

public class TimerService extends Service {
    public static final String COUNTDOWN_BR = "dieunguyen.com.simpletimerapp.TimerService";
    Intent mIntent = new Intent(COUNTDOWN_BR);

    CountDownTimer mCountDownTimer = null;

    private final IBinder mBinder = new MyBinder();

    @Override
    public void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public void setTime(long time) {

        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                sendBroadcast(mIntent);
            }
        };

        mCountDownTimer.start();
    }

    public class MyBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }
}

