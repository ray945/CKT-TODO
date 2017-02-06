package com.ckt.ckttodo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class ScreenOffBroadcast {

    private Context context;
    private boolean isRegister = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver( ) {
        @Override
        public void onReceive( Context context, Intent intent ) {
            receiveBroadcast( intent );
        }
    };

    public ScreenOffBroadcast( Context context ) {
        this.context = context.getApplicationContext( );
    }

    public void registerBroadcast( ) {
        if( null != context && null != mReceiver && !isRegister ) {
            IntentFilter intentFilter = new IntentFilter( );
            intentFilter.addAction( Intent.ACTION_SCREEN_OFF );
            intentFilter.setPriority( IntentFilter.SYSTEM_HIGH_PRIORITY );
            context.registerReceiver( mReceiver, intentFilter );
            isRegister = true;
        }
    }

    public void unregisterBroadcast( ) {
        if( null != context && null != mReceiver && isRegister ) {
            context.unregisterReceiver( mReceiver );
            isRegister = false;
        }
    }

    private void receiveBroadcast( Intent intent ) {
        String action = intent.getAction( );
        if( action.equals( Intent.ACTION_SCREEN_OFF ) ) {
            Intent lockScreen = new Intent( context, LockScreenActivity.class );
            lockScreen.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity( lockScreen );
        }
    }

}
