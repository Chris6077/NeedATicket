package me.projectx.needaticket.customgui;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMin implements InputFilter {

    private int min;

    public InputFilterMin(int min) {
        this.min = min;
    }

    public InputFilterMin(String min) {
        this.min = Integer.parseInt(min);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, Float.MAX_VALUE, input))
                return null;
        } catch (NumberFormatException nfe) {
            // We don't care about this
        }
        return "Invalid amount!";
    }

    private boolean isInRange(float a, float b, float c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}