package me.projectx.needaticket.listener;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import me.projectx.needaticket.R;
import me.projectx.needaticket.activities.ConcertsActivity;
import me.projectx.needaticket.activities.TicketsActivity;
import me.projectx.needaticket.activities.WalletActivity;
import me.projectx.needaticket.activities.WelcomeActivity;

public class ListenerNavigationMenu implements NavigationView.OnNavigationItemSelectedListener {
    private Context resource;
    private String uID;

    public ListenerNavigationMenu(Context obj, String uID) {
        this.resource = obj;
        this.uID = uID;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mntm_concerts) {
            startActivity(ConcertsActivity.class);
        } else if (item.getItemId() == R.id.mntm_my_tickets) {
            startActivity(TicketsActivity.class);
        } else if (item.getItemId() == R.id.mntm_wallet) {
            startActivity(WalletActivity.class);
        } else if (item.getItemId() == R.id.mntm_logout) {
            startActivity(WelcomeActivity.class);
        }
        return true;
    }

    private void startActivity(Class classname) {
        Intent activity = new Intent(this.resource, classname);
        activity.putExtra("uID", uID);
        activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.resource.startActivity(activity);
    }
}
