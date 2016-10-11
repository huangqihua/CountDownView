package com.example.huangqihua.mycountdownviewapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by huangqihua on 16/10/11.
 */
public class SplashActivity extends AppCompatActivity {

    private HQHCountDownView countDownView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_splash);

        countDownView = (HQHCountDownView) findViewById(R.id.count_down_view);

        assert countDownView != null;
        countDownView.start();
        countDownView.setCountDownTimerListener(new HQHCountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {
                Toast.makeText(SplashActivity.this, "倒计时开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinishCount() {
                Toast.makeText(SplashActivity.this, "倒计时结束,跳转", Toast.LENGTH_SHORT).show();
                startActivity();
            }

        });
    }


    public void start(View view) {
        countDownView.stop();
        startActivity();
    }

    private void startActivity() {
        countDownView.stop();
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
