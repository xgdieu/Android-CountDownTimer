package dieunguyen.com.simpletimerapp.View;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dieunguyen.com.simpletimerapp.Model.TimeModel;
import dieunguyen.com.simpletimerapp.Model.TimeModel.TimeStatus;
import dieunguyen.com.simpletimerapp.Presenter.SimpleTimePresenter;
import dieunguyen.com.simpletimerapp.Presenter.SimpleTimePresenterInterface;
import dieunguyen.com.simpletimerapp.R;

public class SimpleTimerActivity extends AppCompatActivity implements SimpleTimerInterface {

    @BindView(R.id.txt_time)
    public TextView mTimeTextView;

    @BindView(R.id.bt_pause)
    public Button mPauseButton;

    @BindView(R.id.bt_cancel)
    public Button mCancel;

    @BindView(R.id.progress_time)
    public ProgressBar mProgressBar;

    private SimpleTimePresenterInterface mPresenter;
    private int mHours = 0;
    private int mMinutes = 0;
    private int mSeconds = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_timer);
        ButterKnife.bind(this);

        configure();
    }

    @OnClick(R.id.bt_cancel)
    public void onCancelClicked() {
        if (mPresenter == null) {
            return;
        }
        mPresenter.cancelButtonClicked();
    }

    @OnClick(R.id.bt_pause)
    public void onPauseClicked() {
        if (mPresenter == null) {
            return;
        }

        mPresenter.pauseButtonClicked();
    }


    @Override
    public void notifyFinishCountDown() {
        displayFinishDialog();
        playSound();
        playVibration();
    }

    @Override
    public void updatePauseTitleButton(TimeStatus status) {
        if (status == null) {
            return;
        }
        String textButton = getString(R.string.pause_button);
        switch (status) {
            case RUNNING:
                textButton = getString(R.string.pause_button);
                break;

            case PAUSE:
                textButton = getString(R.string.resume_button);
                break;

            case STOP:
                textButton = getString(R.string.start_button);
                break;

            default:
                break;
        }
        mPauseButton.setText(textButton);
    }

    @Override
    public void updateTimeText(String strTime) {
        mTimeTextView.setText(strTime);
    }

    @Override
    public void updateProgress(int percent) {
        mProgressBar.setProgress(percent);
    }

    private void configure() {
        int maxPercent = 100;
        mProgressBar.setMax(maxPercent);
        mProgressBar.setProgress(maxPercent);

        TimeModel model = new TimeModel(mHours, mMinutes, mSeconds);
        mPresenter = new SimpleTimePresenter(this, model);
    }

    private void displayFinishDialog() {
        AlertDialog.Builder finishDialog = new AlertDialog.Builder(this);
        finishDialog.setMessage(getString(R.string.finish_text));
        finishDialog.setTitle(R.string.finish_title);
        finishDialog.setCancelable(true);
        finishDialog.setPositiveButton(
                getString(R.string.close_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        finishDialog.create().show();

    }

    private void playSound() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    private void playVibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);
    }
}
