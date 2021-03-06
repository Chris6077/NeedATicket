package me.projectx.needaticket.customgui;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.NumberPicker;

import me.projectx.needaticket.listener.ListenerNumberPicked;
import me.projectx.needaticket.pojo.Ticket;
public class NumberPickerDialog extends DialogFragment {
    private ListenerNumberPicked valueChangeListener;
    private Ticket t;
    private int maxVal;
    @SuppressLint ("ValidFragment") public NumberPickerDialog (Ticket ticket, int maxVal) {
        this.t = ticket;
        this.maxVal = maxVal;
    }
    public NumberPickerDialog () {
    }
    @Override public Dialog onCreateDialog (Bundle savedInstanceState) {
        final NumberPicker numberPicker = new NumberPicker(getActivity());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(this.maxVal);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Amount?");
        builder.setPositiveButton("OK", (dialog, which) -> valueChangeListener.onValueChange(numberPicker, numberPicker.getValue(), numberPicker.getValue(), t));
        builder.setNegativeButton("CANCEL", (dialog, which) -> numberPicker.setValue(1));
        builder.setView(numberPicker);
        return builder.create();
    }
    public void setValueChangeListener (ListenerNumberPicked valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}