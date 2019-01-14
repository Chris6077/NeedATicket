package me.projectx.needaticket;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.projectx.needaticket.handler.HandlerState;

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
        setupWindowAnimations();
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
        image_logo = findViewById(R.id.image_welcome_logo);
        textview_app_name = this.findViewById(R.id.textview_welcome_app_name_lowercase);
        content = findViewById(R.id.content_welcome);
    }

    private void showNewIntent(){
        final Intent login_activity = new Intent(this, LoginActivity.class);
        try{
            startActivity(login_activity);
        } catch (Exception e){
            HandlerState.handle(e, getApplicationContext());
        }
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(fade);
        getWindow().setReturnTransition(fade);
    }

    @Override
    public void onClick(View v) {
        showNewIntent();
    }
}
