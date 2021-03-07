package dieunguyen.com.simpletimerapp.Presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;

import dieunguyen.com.simpletimerapp.R;

/**
 * Created by DieuNguyen on 3/7/21.
 */

public class TimeBroadcastReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        displayFinishDialog(context);
        playSound(context);
        playVibration(context);
    }
    private void displayFinishDialog(Context context) {
        AlertDialog.Builder finishDialog = new AlertDialog.Builder(context);
        finishDialog.setMessage(context.getString(R.string.finish_text));
        finishDialog.setTitle(R.string.finish_title);
        finishDialog.setCancelable(true);
        finishDialog.setPositiveButton(
                context.getString(R.string.close_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        finishDialog.create().show();

    }

    private void playSound(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, notification);
        ringtone.play();
    }

    private void playVibration(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);
    }
}
