package me.projectx.needaticket;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image_logo;
    private TextView textview_app_name;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setViews();
        animate();
        registrateEventHandlers();
    }

    private void registrateEventHandlers() {
        image_logo.setOnClickListener(this);
        content.setOnClickListener(this);
    }

    private void animate() {
        Animation anim_splash = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        image_logo.startAnimation(anim_splash);
        textview_app_name.startAnimation(anim_splash);
    }

    private void setViews() {
        image_logo = (ImageView) findViewById(R.id.image_welcome_logo);
        textview_app_name = (TextView) this.findViewById(R.id.textview_welcome_app_name_lowercase);
        content = (LinearLayout) findViewById(R.id.content_welcome);
    }

    private void showNewIntent(){
        final Intent login_activity = new Intent(this, LoginActivity.class);
        try{
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, new Pair<View, Sting>(image_logo, "logo"));
            startActivity(login_activity);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent login_activity = new Intent(this, LoginActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, new Pair<View, String>(image_logo, "logo"), new Pair<View, String>(textview_app_name, "content"));
    }
}
