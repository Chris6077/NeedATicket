package me.projectx.needaticket.handler;
import android.content.Context;

import com.shashank.sony.fancytoastlib.FancyToast;
public abstract class HandlerState {
    private HandlerState () {
        throw new IllegalStateException("Utility class");
    }
    public static void handle (Exception error, Context context) {
        FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
    }
    public static void handle (Context context){
        FancyToast.makeText(context, "Error: Please check your connection!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
    }
}
