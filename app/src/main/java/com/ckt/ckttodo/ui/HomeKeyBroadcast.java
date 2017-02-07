package com.ckt.ckttodo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeKeyBroadcast {

    private final static String SYSTEM_REASON = "reason";
    private final static String SYSTEM_HOME_KEY = "homekey"; 
    private final static String SYSTEM_RECENT_APPS = "recentapps"; 

    private Context mContext = null;
    private boolean isRegister = false;

    private HomeKeyBroadcast.OnHomeKeyListener mHomeKeyListener = null;

    public interface OnHomeKeyListener {
        void onHomeKey();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver( ) {
        @Override
        public void onReceive( Context context, Intent intent ) {
            receiveBroadcast( intent );
        }
    };

    private void receiveBroadcast( Intent intent ) {
        String action = intent.getAction( );
        if( Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals( action ) ) {
            String reason = intent.getStringExtra( SYSTEM_REASON );
            if( null != reason ) {
                if( reason.equals( SYSTEM_HOME_KEY ) ) {   
                    if( null != mHomeKeyListener ) {
                        mHomeKeyListener.onHomeKey( );
                    }
                } else if( reason.equals( SYSTEM_RECENT_APPS ) ) {
                }
            }
        }
    }

    public HomeKeyBroadcast( Context context, HomeKeyBroadcast.OnHomeKeyListener l ) {
        this.mContext = context.getApplicationContext( );
        this.mHomeKeyListener = l;
    }

    public void registerBroadcast( ) {
        if( null != mContext && null != mReceiver && !isRegister ) {
            IntentFilter intentFilter = new IntentFilter( );
            intentFilter.addAction( Intent.ACTION_CLOSE_SYSTEM_DIALOGS );
            mContext.registerReceiver( mReceiver, intentFilter );
            isRegister = true;
        }
    }

    public void unregisterBroadcast( ) {
        if( null != mContext && null != mReceiver && isRegister ) {
            mContext.unregisterReceiver( mReceiver );
            isRegister = false;
        }
    }


}
