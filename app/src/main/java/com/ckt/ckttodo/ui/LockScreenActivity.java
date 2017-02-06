package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ckt.ckttodo.R;

public class LockScreenActivity extends SwipeUpBaseActivity {

    private HomeKeyBroadcast mHomeKeyBroadcast;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        unLock();
    }

    private void unLock() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    private void registerHomeKeyBroadcast() {
        if (mHomeKeyBroadcast == null) {
            mHomeKeyBroadcast = new HomeKeyBroadcast(this, new HomeKeyBroadcast.OnHomeKeyListener() {
                @Override
                public void onHomeKey() {
                    Toast.makeText(getApplicationContext(), "onHomeKey", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        mHomeKeyBroadcast.registerBroadcast();
    }

    private void unRegisterHomeKeyBroadcast() {
        if (mHomeKeyBroadcast != null) {
            mHomeKeyBroadcast.unregisterBroadcast();
        }
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerHomeKeyBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterHomeKeyBroadcast();
    }

}
