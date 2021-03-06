package dieunguyen.com.simpletimerapp.Presenter;

/**
 * Created by DieuNguyen on 3/6/21.
 */

public interface SimpleTimePresenterInterface {
    void pauseButtonClicked(String hours, String minutes, String seconds);

    void cancelButtonClicked();
}
