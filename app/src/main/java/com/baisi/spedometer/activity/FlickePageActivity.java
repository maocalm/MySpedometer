package com.baisi.spedometer.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.baisi.spedometer.R;
import com.baisi.spedometer.app.PedometerApplication;
import com.baisi.spedometer.utiles.Firebase;
import com.baisi.spedometer.utiles.NetUtils;
import com.baisi.spedometer.utiles.ScreenUtil;
import com.baisi.spedometer.utiles.StatusBarUtil;
import com.baisi.spedometer.view.DipUtils;
import com.baisi.spedometer.view.RoundProgress;
import com.bestgo.adsplugin.ads.AdAppHelper;
import com.bestgo.adsplugin.ads.activity.ShowAdFilter;

public class FlickePageActivity extends AppCompatActivity implements ShowAdFilter, RoundProgress.endAnimListener {

    private ImageView mIvBg;
    private ImageView mIvLogo;

    private TextView mTvTitle;
    private TextView app_name;
    private RoundProgress mRpClose;
    private boolean mNativeLoaded;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        setContentView(R.layout.activity_flicke_page);
        mRpClose = findViewById(R.id.rp_close);
        mIvLogo = findViewById(R.id.iv_logo);
        mTvTitle = findViewById(R.id.tv_title);
        app_name = findViewById(R.id.app_name);
        mRpClose.setEndAnimListener(this);
        mHandler = new Handler();
        //AdAppHelper.getInstance(getApplicationContext()).loadNewNative();
        //AdAppHelper.getInstance(getApplicationContext()).loadNewNative();
        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "欢迎页面");
        startSplashAnim();
    }

    private void startSplashAnim() {
        ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(mIvLogo, "alpha", 0, 1).setDuration(1000);
        AnimatorSet set = new AnimatorSet();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIvLogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIvLogo.setVisibility(View.VISIBLE);
                startLogoAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.playTogether(alphaAnim1);
        set.start();
    }

    private void startLogoAnim() {
        float dy = DipUtils.dip2px(this, 8);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mIvLogo, "translationY",
                -dy, dy, -dy, dy, -dy, dy, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mIvBg, "translationY",
                -dy, dy, -dy, dy, -dy, dy, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(3000);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                startTextUpAnim();
                mTvTitle.setVisibility(View.VISIBLE);
                app_name.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.playTogether(animator1);
        set.start();
    }

    private void startTextUpAnim() {
        ObjectAnimator tranlationAnim = ObjectAnimator.ofFloat(mTvTitle, "translationY",
                ScreenUtil.getScreenHeight(this), 0);
        ObjectAnimator tranlationAnim2 = ObjectAnimator.ofFloat(app_name, "translationY",
                ScreenUtil.getScreenHeight(this), 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(tranlationAnim, tranlationAnim2);
        animatorSet.setDuration(2000);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTvTitle.setVisibility(View.VISIBLE);
                app_name.setVisibility(View.VISIBLE);
                if (AdAppHelper.getInstance(getApplicationContext()).isFullAdLoaded()
                        && AdAppHelper.getInstance(getApplicationContext()).isNativeLoaded()) {
                    startMainActivity();
                } else if (!NetUtils.isConnected(FlickePageActivity.this)) {
                    startMainActivity();
                } else {
                    startNativeAnim();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        tranlationAnim.start();
    }

    private void startMainActivity() {
        Intent intent = new Intent(FlickePageActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }


    private void startNativeAnim() {
        mRpClose.setVisibility(View.VISIBLE);
        mRpClose.startAnim(3000);
    }

    @Override
    public void endAnim() {
        if (AdAppHelper.getInstance(getApplicationContext()).isFullAdLoaded() && AdAppHelper.getInstance(getApplicationContext()).
                isNativeLoaded()) {
            Firebase.getInstance(PedometerApplication.getInstance()).logEvent("广告加载成功", "WelcomeActivity");
            startMainActivity();
        } else {
            if (mHandler == null) {
                mHandler = new Handler();
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, 1000);
        }
    }

    @Override
    public boolean allowShowAd() {
        return false;
    }
}
