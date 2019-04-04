package me.projectx.needaticket.customgui;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;
public class InputFilterMin implements InputFilter {
    private int min;
    private Pattern mPattern;
    public InputFilterMin (int min) {
        this.min = min;
        mPattern=Pattern.compile("[0-9]{0," + (7-1) + "}+((\\.[0-9]{0," + (2-1) + "})?)||(\\.)?");
    }
    @Override
    public CharSequence filter (CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            float input = Float.parseFloat(dest.toString() + source.toString());
            if (isInRange(min, Float.MAX_VALUE, input) && mPattern.matcher(dest).matches()) return null;
        } catch (NumberFormatException nfe) { /* We don't care about this */}
        return "Invalid amount!";
    }
    private boolean isInRange (float a, float b, float c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}