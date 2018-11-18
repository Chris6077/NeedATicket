package me.projectx.needaticket.listener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.projectx.needaticket.UserActivity;
import me.projectx.needaticket.pojo.LocalDatabase;

public class ListenerNavigationMenuHeader implements View.OnClickListener {
    //fields
    private AppCompatActivity resource;

    //constructors
    public ListenerNavigationMenuHeader(AppCompatActivity resource) {
        this.resource = resource;
    }

    //super
    @Override
    public void onClick(View v) {
        Intent intent= new Intent(resource,UserActivity.class);
        intent.putExtra("uID", LocalDatabase.getuID());
        resource.startActivity(intent);
    }
}
