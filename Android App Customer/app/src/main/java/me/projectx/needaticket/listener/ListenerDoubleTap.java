package me.projectx.needaticket.listener;
import android.view.View;
public abstract class ListenerDoubleTap implements View.OnClickListener {
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; //milliseconds
    long lastClickTime = 0;
    @Override public void onClick (View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v);
            lastClickTime = 0;
        } else {
            onSingleClick(v);
        }
        lastClickTime = clickTime;
    }
    public abstract void onDoubleClick (View v);
    public abstract void onSingleClick (View v);
}
