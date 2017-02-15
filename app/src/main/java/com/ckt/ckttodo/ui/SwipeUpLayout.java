package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SwipeUpLayout extends FrameLayout {

    private static final String TAG = SwipeUpLayout.class.getSimpleName();
    private static final long MAX_VELOCITY_Y = -4000;
    private static final long MAX_VELOCITY_X = 4000;
    private VelocityTracker mVelocityTracker;
    private int mPointerId;
    private int mMaxVelocity;
    private boolean needFinish = false;

    private View mContentView;
    private int mTouchSlop;
    private int downX;
    private int downY;
    private int tempX;
    private int tempY;
    private Scroller mScroller;
    private int viewWidth;
    private int viewHeight;
    private boolean isSilding;
    private boolean isFinish;
    private Activity mActivity;

    private OnSwipeBackListener mListener;

    public interface OnSwipeBackListener {
        void onFinish();
    }

    public void setOnSwipeBackListener(OnSwipeBackListener l) {
        mListener = l;
    }

    public SwipeUpLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeUpLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        mMaxVelocity = ViewConfiguration.get(context).getMaximumFlingVelocity();
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    private void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isFinish) {
            return true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = tempY = (int) ev.getRawY();
                downX = tempX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                if (moveX - downX > mTouchSlop && Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                touchEvenMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchEvenUp();
                releaseVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    private void recodeInfo(final float velocityX, final float velocityY) {
        if (mContentView.getScrollX() < 0 && !needFinish) {
            needFinish = velocityX > MAX_VELOCITY_X;
        } else {
            needFinish = false;
        }
    }

    private void touchEvenUp() {
        isSilding = false;
        if (Math.abs(mContentView.getScrollX()) >= viewWidth * 3 / 8 && mContentView.getScrollX() < 0 || needFinish) {
            isFinish = true;
            scrollUp();
        } else {
            scrollOrigin();
            isFinish = false;
        }
    }

    private void touchEvenMove(MotionEvent event) {

        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        float velocityX = mVelocityTracker.getXVelocity(mPointerId);
        float velocityY = mVelocityTracker.getYVelocity(mPointerId);
        recodeInfo(velocityX, velocityY);

        int moveX = (int) event.getRawX();
        int deltaX = tempX - moveX;
        tempX = moveX;
        if (moveX - downX > mTouchSlop || Math.abs(tempX - downX) > mTouchSlop) {
            isSilding = true;
        }
        if (isSilding) {
            if ((moveX - downY <= 0 && downX < viewWidth / 2 && deltaX < 0) || (deltaX > 0 && (mContentView.getScrollX() + deltaX) <= 0)) {
                mContentView.scrollBy(deltaX, 0);
            }
        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            viewWidth = this.getWidth();
            viewHeight = this.getHeight();
        }
    }

    private void scrollUp() {
        final int delta = (viewWidth + mContentView.getScrollX());
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0, Math.abs(delta) - 200);
        postInvalidate();
    }

    private void scrollOrigin() {
        int delta = mContentView.getScrollX();
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0, Math.abs(delta) + 400);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            if (mScroller.isFinished() && isFinish) {
                mActivity.finish();
                if (mListener != null) {
                    mListener.onFinish();
                }
            }
        }
    }

    public void reset() {
        isFinish = false;
        mScroller.setFinalX(0);
        mScroller.setFinalY(0);
        postInvalidate();
    }

}
