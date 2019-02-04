package me.projectx.needaticket.listener;
import me.projectx.needaticket.pojo.Ticket;
public interface ListenerNumberPicked {
    void onValueChange (android.widget.NumberPicker np, int oldNumber, int newNumber, Ticket t);
}
