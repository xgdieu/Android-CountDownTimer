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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dieunguyen.com.simpletimerapp.Model.TimeModel.TimeStatus;
import dieunguyen.com.simpletimerapp.Presenter.SimpleTimePresenter;
import dieunguyen.com.simpletimerapp.Presenter.SimpleTimePresenterInterface;
import dieunguyen.com.simpletimerapp.R;

public class SimpleTimerActivity extends AppCompatActivity implements SimpleTimerInterface {

    @BindView(R.id.bt_pause)
    public Button mPauseButton;

    @BindView(R.id.bt_cancel)
    public Button mCancel;

    @BindView(R.id.progress_time)
    public ProgressBar mProgressBar;

    @BindView(R.id.edit_hours)
    public EditText mEditHours;

    @BindView(R.id.edit_minutes)
    public EditText mEditMinutes;

    @BindView(R.id.edit_seconds)
    public EditText mEditSeconds;

    private SimpleTimePresenterInterface mPresenter;

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

        String hours = mEditHours.getText().toString();
        String minutes = mEditMinutes.getText().toString();
        String seconds = mEditSeconds.getText().toString();

        mPresenter.pauseButtonClicked(hours, minutes, seconds);
    }


    @Override
    public void notifyFinishCountDown() {
        displayFinishDialog();
        playSound();
        playVibration();
    }

    @Override
    public void updatePauseTitleButton(TimeStatus status) {
        String textButton = getString(R.string.start_button);

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
    public void updateHoursText(String hours) {
        mEditHours.setText(hours);
    }

    @Override
    public void updateMinutesText(String minutes) {
        mEditMinutes.setText(minutes);
    }

    @Override
    public void updateSecondText(String seconds) {
        mEditSeconds.setText(seconds);
    }

    @Override
    public void updateEnableInputTimeIfNeed(TimeStatus status) {
        switch (status) {
            case STOP:
                setEnableInputTime();
                break;

            default:
                setDisableInputTime();
                break;
        }
    }

    @Override
    public void updateProgress(int percent) {
        mProgressBar.setProgress(percent);
    }

    private void configure() {
        int maxPercent = 100;
        mProgressBar.setMax(maxPercent);
        mProgressBar.setProgress(0);

        mPauseButton.setText(getString(R.string.start_button));

        mPresenter = new SimpleTimePresenter(this);

        configureValidInputData();

        setEnableInputTime();
    }

    private void configureValidInputData() {
        final int maxMinute = 60;
        final int maxSecond = 60;

        mEditMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                if (!validationTimeInput(Integer.parseInt(s.toString()), maxMinute)) {
                    mEditMinutes.setText(maxMinute + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditSeconds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                if (!validationTimeInput(Integer.parseInt(s.toString()), maxSecond)) {
                    mEditSeconds.setText(maxSecond + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validationTimeInput(int input, int limit) {
        if (input > limit) {
            Toast.makeText(this, "Input data invalid, max values is " + limit, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setEnableInputTime() {
        mEditHours.setEnabled(true);
        mEditMinutes.setEnabled(true);
        mEditSeconds.setEnabled(true);
    }

    private void setDisableInputTime() {
        mEditHours.setEnabled(false);
        mEditMinutes.setEnabled(false);
        mEditSeconds.setEnabled(false);
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
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ringtone.play();
    }

    private void playVibration() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);
    }

}
