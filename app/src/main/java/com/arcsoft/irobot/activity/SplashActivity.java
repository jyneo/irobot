package com.arcsoft.irobot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.arcsoft.irobot.R;

/**
 *
 * Created by yj2595 on 2018/3/5.
 */

public class SplashActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        Handler x = new Handler();
        x.postDelayed(new SplashHandler(), 2000);
    }

    class SplashHandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class));
            SplashActivity.this.finish();
        }
    }
}
