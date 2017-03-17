package com.rooksoto.parallel.activitylogin;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.rooksoto.parallel.BaseView;
import com.rooksoto.parallel.R;
import com.rooksoto.parallel.activitylogin.login.FragmentLoginLogin;
import com.rooksoto.parallel.activitylogin.splash.FragmentLoginSplash;
import com.rooksoto.parallel.activitylogin.wait.FragmentLoginWait;
import com.rooksoto.parallel.utility.CustomAlertDialog;
import com.rooksoto.parallel.utility.CustomToast;
import com.rooksoto.parallel.utility.firebase.CustomFirebaseMessagingService;

public class ActivityLogin extends AppCompatActivity implements BaseView {
    private ActivityLoginPresenter activityLoginPresenter = new ActivityLoginPresenter();
    private int containerID = R.id.activity_login_fragment_container;
    private CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog();
    private CustomToast mCustomToast = new CustomToast();
    private ImageView logoViewLeft;
    private ImageView logoViewRight;
    private boolean logoVisible = false;
    private FragmentLoginLogin mFragmentLoginLogin;
    private boolean isNew = true;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    public void initialize () {
        CustomFirebaseMessagingService.giveView(getWindow().getDecorView().getRootView());
        setViews();
    }

    @Override
    public void setViews () {
        activityLoginPresenter.giveActivity(this, getWindow().getDecorView().getRootView());
        loadFragmentSplash();
    }

    private void loadFragmentSplash () {
        FragmentLoginSplash mFragmentLoginSplash = new FragmentLoginSplash();
        activityLoginPresenter.loadFragment(mFragmentLoginSplash,
                R.animator.animator_nothing, R.animator.animator_fade_out, containerID, "Splash");
    }

    @Override
    public void onBackPressed () {
        Fragment currentFrag = getFragmentManager().findFragmentById(containerID);
        if (currentFrag instanceof FragmentLoginWait || currentFrag instanceof FragmentLoginSplash){
            CustomAlertDialog customAlertDialog = new CustomAlertDialog();
            customAlertDialog.exit(this);
        } else if (currentFrag instanceof FragmentLoginLogin){
            hideLoginLogo();
            getFragmentManager().beginTransaction().replace(containerID, new FragmentLoginSplash()).commit();
        } else {
            super.onBackPressed();
        }
    }

    private void hideLoginLogo() {
        ImageView logoViewLeft = (ImageView) findViewById(R.id.activity_login_logoleft);
        logoViewLeft.setVisibility(View.INVISIBLE);
        ImageView logoViewRight = (ImageView) findViewById(R.id.activity_login_logoright);
        logoViewRight.setVisibility(View.INVISIBLE);
        activityLoginPresenter.logoVisible = false;
    }
}
