package me.projectx.needaticket.handler;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

public abstract class HandlerState {
    public static void handle(Exception error, Context context){
        FancyToast.makeText(context,error.toString(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
    }
}
