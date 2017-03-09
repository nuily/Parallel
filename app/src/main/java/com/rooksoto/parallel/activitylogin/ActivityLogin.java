package com.rooksoto.parallel.activitylogin;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rooksoto.parallel.R;
import com.rooksoto.parallel.activityHub.ActivityHub;
import com.rooksoto.parallel.activityStart.ActivityStart;
import com.rooksoto.parallel.activitylogin.login.FragmentLoginLogin;
import com.rooksoto.parallel.activitylogin.wait.FragmentLoginWait;
import com.rooksoto.parallel.utility.CustomAlertDialog;
import com.rooksoto.parallel.utility.CustomToast;

public class ActivityLogin extends AppCompatActivity {
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

        loadFragmentSplash();
    }

    private void initialize () {
    }

    private void loadFragmentSplash () {
        FragmentLoginSplash mFragmentLoginSplash = new FragmentLoginSplash();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.animator_nothing, R.animator.animator_fade_out)
                .add(containerID, mFragmentLoginSplash, "Splash")
                .commit();
    }

    private void loadFragmentLogin () {
        mFragmentLoginLogin = FragmentLoginLogin.newInstance(isNew);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.animator_fade_in, R.animator.animator_fade_out_right)
                .replace(containerID, mFragmentLoginLogin, "Login")
                .commit();

        if (!logoVisible) {
            logoViewLeft = (ImageView) findViewById(R.id.activity_login_logoleft);
            logoViewLeft.setVisibility(View.VISIBLE);
            Animation fadeInUp = AnimationUtils.loadAnimation(this, R.anim.fadeinup);
            logoViewLeft.startAnimation(fadeInUp);

            logoViewRight = (ImageView) findViewById(R.id.activity_login_logoright);
            logoViewRight.setVisibility(View.VISIBLE);
            Animation fadeInDown = AnimationUtils.loadAnimation(this, R.anim.fadeindown);
            logoViewRight.startAnimation(fadeInDown);
            logoVisible = true;
        }
        isNew = false;
    }

    private void loadFragmentCreateAccount () {
        FragmentLoginCreateAccount mFragmentLoginCreateAccount = new FragmentLoginCreateAccount();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.animator_fade_in_right, R.animator.animator_fade_out_right)
                .replace(containerID, mFragmentLoginCreateAccount)
                .commit();
    }

    private void loadFragmentWait () {
        final FragmentLoginWait mFragmentLoginWait = new FragmentLoginWait();

        Animation fadeOutDown = AnimationUtils.loadAnimation(this, R.anim.fadeoutdown);
        logoViewLeft.startAnimation(fadeOutDown);
        Animation fadeOutUp = AnimationUtils.loadAnimation(this, R.anim.fadeoutup);
        fadeOutUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart (Animation animation) {}

            @Override
            public void onAnimationEnd (Animation animation) {
                logoViewLeft.setVisibility(View.INVISIBLE);
                logoViewRight.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat (Animation animation) {
            }
        });
        logoViewRight.startAnimation(fadeOutUp);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.animator_fade_in_right, R.animator.animator_fade_out_right)
                .replace(containerID, mFragmentLoginWait)
                .commit();
    }

    public void onClickToLogin (View view) {
        loadFragmentLogin();
    }

    public void onClicktoCreateAccount (View view) {
        loadFragmentCreateAccount();
    }

    public void onClicktoWait (View view) {
        loadFragmentWait();
        mCustomToast.show(getWindow().getDecorView().getRootView(), "Login successful");
    }

    public void onClicktoActivityHub (View view) {
        Intent fromActivityStartToActivityHub = new Intent(this, ActivityHub.class);
        startActivity(fromActivityStartToActivityHub);
        finish();
    }

    @Override
    public void onBackPressed () {
        Fragment currentFragment = getFragmentManager().findFragmentById(containerID);
        if (currentFragment instanceof FragmentLoginLogin || currentFragment instanceof FragmentLoginWait) {
            mCustomAlertDialog.exit(this);
        } else if (currentFragment instanceof FragmentLoginCreateAccount) {
            loadFragmentLogin();
        } else {
            super.onBackPressed();
        }
    }

    public void onClickToActivityStart (View view) {
        finish();
        Intent intentToActivityStart = new Intent(this, ActivityStart.class);
        startActivity(intentToActivityStart);
    }
}