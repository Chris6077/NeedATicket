package me.projectx.needaticket.handler;

import android.content.Context;
import android.widget.Toast;

public abstract class HandlerState {
    public static void handle(Exception error,Context context){
        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
    }
}
