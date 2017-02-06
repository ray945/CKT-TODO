package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;
import android.view.LayoutInflater;

import com.ckt.ckttodo.R;

public class SwipeUpBaseActivity extends Activity {

    protected SwipeUpLayout layout;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        layout = (SwipeUpLayout) LayoutInflater.from( this ).inflate(R.layout.swipe_back_layout, null );
        layout.attachToActivity( this );
        layout.setOnSwipeBackListener( new SwipeUpLayout.OnSwipeBackListener( ) {
            @Override
            public void onFinish( ) {
                onSwipeBackFinish( );
            }
        } );
    }

    @Suppress
    public void onSwipeBackFinish( ) {
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        reset();
    }

    public void reset( ) {
        if( layout != null ) {
            layout.reset( );
        }
    }

}
