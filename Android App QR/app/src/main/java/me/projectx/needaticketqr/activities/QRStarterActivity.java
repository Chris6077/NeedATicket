package me.projectx.needaticketqr.activities;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.itangqi.waveloadingview.WaveLoadingView;
import me.projectx.needaticketqr.R;
import me.projectx.needaticketqr.asynctask.TaskExecuteGraphQLMutation;
import me.projectx.needaticketqr.handler.HandlerState;
import me.projectx.needaticketqr.interfaces.InterfaceTaskDefault;

import static android.os.Build.MANUFACTURER;
import static butterknife.internal.Utils.arrayOf;
public class QRStarterActivity extends AppCompatActivity implements View.OnClickListener, InterfaceTaskDefault, ZXingScannerView.ResultHandler {
    private String hash;
    private long backPressedTime;
    private Toast backToast;
    @BindView(R.id.image_error) ImageView logo;
    @BindView(R.id.textview_ticket_scanner) TextView ticketScanner;
    @BindView(R.id.content_qr_starter) LinearLayout content;
    @BindView(R.id.waveLoadingView_qr_starter) WaveLoadingView mWaveLoadingView;
    @BindView(R.id.qrCodeScanner) ZXingScannerView qrCodeScanner;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrstarter);
        ButterKnife.bind(this);
        this.hash = getIntent().getStringExtra("hash");
        animate();
        registrateEventHandlers();
        setupWindowAnimations();
        setScannerProperties();
    }
    private void animate () {
        Animation splash = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        logo.startAnimation(splash);
        ticketScanner.startAnimation(splash);
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
    private void setScannerProperties(){
        ArrayList<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        qrCodeScanner.setFormats(formats);
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
        if (MANUFACTURER.equals("HUAWEI"))
            qrCodeScanner.setAspectTolerance(0.5f);
    }
    @Override public void onClick (View v) {
        readQRCode();
    }

    private void readQRCode(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1);
        }
        qrCodeScanner.startCamera();
        qrCodeScanner.setVisibility(View.VISIBLE);
        qrCodeScanner.setResultHandler(this);
        mWaveLoadingView.setVisibility(View.INVISIBLE);
    }
    @Override public void onPreExecute (Class resource) {
        mWaveLoadingView.startAnimation();
        mWaveLoadingView.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);
    }
    @Override public void onPostExecute (Object result, Class resource) {
        mWaveLoadingView.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
        try{
            if(((String)result).contains("ticket already redeemed")) {
                FancyToast.makeText(getApplicationContext(), "Ticket already redeemed", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                readQRCode();
            } else if(((String)result).contains("Argument passed in must be a single String")){
                FancyToast.makeText(getApplicationContext(), "Invalid ticket!", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                readQRCode();
            } else if(((String)result).contains("errors")) {
                FancyToast.makeText(getApplicationContext(), "Error redeeming ticket!", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                readQRCode();
            } else {
                FancyToast.makeText(getApplicationContext(), "Ticket valid - redeemed", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                readQRCode();
            }
        }
        catch (Exception ex){
            FancyToast.makeText(getApplicationContext(), "Invalid ticket", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }
    @Override public void handleResult (Result result) {
        try {
            qrCodeScanner.stopCamera();
            qrCodeScanner.setVisibility(View.INVISIBLE);
            TaskExecuteGraphQLMutation mCheckTask = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_check_ticket).replace("$hash", result.getText().substring(0,result.getText().length()-1)), hash, this);
            mCheckTask.execute();
        }
        catch(Exception ex){
            HandlerState.handle(ex,this);
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
            backToast = FancyToast.makeText(getApplicationContext(), "Press back again to log out", Toast.LENGTH_SHORT, FancyToast.INFO, false);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}