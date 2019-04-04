package me.projectx.needaticket.listener;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.projectx.needaticket.activities.UserActivity;
public class ListenerNavigationMenuHeader implements View.OnClickListener {
    private AppCompatActivity resource;
    private String uID;
    public ListenerNavigationMenuHeader (AppCompatActivity resource, String uID) {
        this.resource = resource;
        this.uID = uID;
    }
    @Override public void onClick (View v) {
        Intent intent = new Intent(resource, UserActivity.class);
        intent.putExtra("uID", uID);
        resource.startActivity(intent);
    }
}
