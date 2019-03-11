package me.projectx.needaticket.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.projectx.needaticket.R;
import me.projectx.needaticket.handler.HandlerState;
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.image_welcome_logo) ImageView logo;
    @BindView(R.id.textview_welcome_app_name_lowercase) TextView appName;
    @BindView(R.id.content_welcome) LinearLayout content;
    private Toast backToast;
    private long backPressedTime = 0;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        animate();
        registrateEventHandlers();
        setupWindowAnimations();
    }
    private void animate () {
        Animation splash = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        logo.startAnimation(splash);
        appName.startAnimation(splash);
    }
    private void registrateEventHandlers () {
        logo.setOnClickListener(this);
        content.setOnClickListener(this);
    }
    private void setupWindowAnimations () {
        Fade fade = new Fade();
        fade.setDuration(1000);
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(fade);
        getWindow().setReturnTransition(fade);
    }
    @Override public void onClick (View v) {
        showNewIntent();
    }
    private void showNewIntent () {
        final Intent loginActivity = new Intent(this, LoginActivity.class);
        try {
            startActivity(loginActivity);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            final Intent loginActivity = new Intent(this, LoginActivity.class);
            try {
                finish();
                startActivity(loginActivity);
            } catch (Exception e) {
                HandlerState.handle(e, getApplicationContext());
            }
        } else {
            backToast = FancyToast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT, FancyToast.INFO, false);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
