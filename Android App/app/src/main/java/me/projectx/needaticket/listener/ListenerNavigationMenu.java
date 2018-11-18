package me.projectx.needaticket.listener;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import me.projectx.needaticket.ConcertsActivity;
import me.projectx.needaticket.R;
import me.projectx.needaticket.TicketsActivity;
import me.projectx.needaticket.WelcomeActivity;
import me.projectx.needaticket.pojo.LocalDatabase;

public class ListenerNavigationMenu implements NavigationView.OnNavigationItemSelectedListener {
    private Context resource;

    public ListenerNavigationMenu(Context obj){
        this.resource = obj;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mntm_concerts){
            startActivity(ConcertsActivity.class);
        } else if(item.getItemId() == R.id.mntm_my_tickets){
            startActivity(TicketsActivity.class);
        } else if(item.getItemId() == R.id.mntm_logout){
            LocalDatabase.setuID(null);
            startActivity(WelcomeActivity.class);
        }
        return true;
    }

    private void startActivity(Class classname){
        Intent activity = new Intent(this.resource,classname);
        activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.resource.startActivity(activity);
        //((AppCompatActivity)obj).overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
    }
}