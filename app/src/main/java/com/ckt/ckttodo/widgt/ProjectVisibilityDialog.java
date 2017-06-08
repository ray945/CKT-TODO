package com.ckt.ckttodo.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.Project;

import java.util.List;

/**
 * Created by admin on 2017/6/6.
 */

public class ProjectVisibilityDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout mRelativeLayoutPri;
    private RelativeLayout mRelativeLayoutPub;
    private TextView mTextViewSure;
    private TextView mTextViewCancel;
    private ImageView mImageViewPri;
    private ImageView mImageViewPub;
    private int mCurrentVisibility = 0;
    private VisibilityChangeListener mListener;

    public ProjectVisibilityDialog(Context context, VisibilityChangeListener listener, int currentVisibility) {
        super(context);
        this.mListener = listener;
        this.mCurrentVisibility = currentVisibility;
    }

    protected ProjectVisibilityDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ProjectVisibilityDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_project_visibility);
        mRelativeLayoutPri = (RelativeLayout) findViewById(R.id.project_pri);
        mRelativeLayoutPub = (RelativeLayout) findViewById(R.id.project_pub);
        mTextViewSure = (TextView) findViewById(R.id.text_button_sure);
        mTextViewCancel = (TextView) findViewById(R.id.text_button_cancel);
        mImageViewPri = (ImageView) findViewById(R.id.button_vis_pr_selected);
        mImageViewPub = (ImageView) findViewById(R.id.button_vis_pu_selected);
        mRelativeLayoutPri.setOnClickListener(this);
        mRelativeLayoutPub.setOnClickListener(this);
        mTextViewSure.setOnClickListener(this);
        mTextViewCancel.setOnClickListener(this);
        if (mCurrentVisibility == Project.PROJECT_PRIVATE) {
            mCurrentVisibility = Project.PROJECT_PRIVATE;
            mImageViewPri.setVisibility(View.VISIBLE);
            mImageViewPub.setVisibility(View.GONE);
        } else {
            mCurrentVisibility = Project.PROJECT_PUBLIC;
            mImageViewPri.setVisibility(View.GONE);
            mImageViewPub.setVisibility(View.VISIBLE);
        }
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.project_pri:
                mCurrentVisibility = Project.PROJECT_PRIVATE;
                mImageViewPri.setVisibility(View.VISIBLE);
                mImageViewPub.setVisibility(View.GONE);
                break;
            case R.id.project_pub:
                mCurrentVisibility = Project.PROJECT_PUBLIC;
                mImageViewPri.setVisibility(View.GONE);
                mImageViewPub.setVisibility(View.VISIBLE);
                break;
            case R.id.text_button_sure:
                dismiss();
                mListener.onVisibilityChangeListener(mCurrentVisibility);
                break;
            case R.id.text_button_cancel:
                dismiss();
                break;
        }
    }

    public interface VisibilityChangeListener {
        void onVisibilityChangeListener(int currentVisibility);
    }
}
