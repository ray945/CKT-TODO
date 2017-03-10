package com.ckt.ckttodo.widgt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.formatter.IFillFormatter;

import java.util.Timer;
import java.util.TimerTask;

public class CircleAlarmTimerView extends View {
    private static final String TAG = "hu";

    // Status
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_RADIAN = "status_radian";
    private static final String STATUS_CURRENT_TIME = "status_current_time";

    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 30;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 15;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private static final float DEFAULT_TIMER_NUMBER_SIZE = 38;

    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xFFE9E2D9;
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_LINE_COLOR = 0xFFFECE02;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_NUMBER_COLOR = 0xFF181318;
    private static final int DEFAULT_TIMER_NUMBER_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TIMER_COLON_COLOR = 0xFFFA7777;

    // Paint
    private Paint mCirclePaint;
    private Paint mHighlightLinePaint;
    private Paint mLinePaint;
    private Paint mCircleButtonPaint;
    private Paint mNumberPaint;
    private Paint mTimerNumberPaint;
    private Paint mTimerColonPaint;

    // Dimension
    private float mGapBetweenCircleAndLine;
    private float mCircleButtonRadius;
    private float mCircleStrokeWidth;
    private float mTimerNumberSize;

    // Color
    private int mCircleColor;
    private int mCircleButtonColor;
    private int mLineColor;
    private int mHighlightLineColor;
    private int mNumberColor;
    private int mTimerNumberColor;

    // Parameters
    private float mCx;
    private float mCy;
    private float mRadius;
    private float mCurrentRadian;
    private int mCurrentTime; // seconds
    private int mRecordTime;

    private boolean mStarted;

    public boolean ismStarted() {
        return mStarted;
    }

    public void setState(boolean state){
        this.mStarted = state;
    }

    // TimerTask
    private static Timer timer = new Timer();
    private TimerTask timerTask;
    private CircleTimerListener mCircleTimerListener;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (mCurrentTime > 0 && mCurrentRadian > 0) {
                mCurrentRadian -= (2 * Math.PI) / mRecordTime;
                mCurrentTime--;
                    if (mCircleTimerListener != null) {
                        if (mCurrentTime == 0) {
                            mCurrentRadian = (float) (2 * Math.PI);
                            mStarted = false;
                            mCircleTimerListener.onTimerOver();
                        }
                        mCircleTimerListener.onTimerTimingValueChanged(mCurrentTime);
                    }

            }else {
                mCurrentTime = 0;
                mCurrentRadian = (float) (2 * Math.PI);
                pauseTimer();
                if (mCircleTimerListener != null) {
                    mCircleTimerListener.onTimerStop();
                }
            }
            invalidate();
        }
    };


    public CircleAlarmTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CircleAlarmTimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleAlarmTimerView(Context context) {
        this(context, null);
    }

    private void initialize() {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        mGapBetweenCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE,
                getContext().getResources().getDisplayMetrics());
        mCircleButtonRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_RADIUS, getContext()
                .getResources().getDisplayMetrics());
        mCircleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext()
                .getResources().getDisplayMetrics());
        mTimerNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_NUMBER_SIZE, getContext()
                .getResources().getDisplayMetrics());

        // Set default color or read xml attributes
        mCircleColor = DEFAULT_CIRCLE_COLOR;
        mCircleButtonColor = DEFAULT_CIRCLE_BUTTON_COLOR;
        mLineColor = DEFAULT_LINE_COLOR;
        mHighlightLineColor = DEFAULT_HIGHLIGHT_LINE_COLOR;
        mNumberColor = DEFAULT_NUMBER_COLOR;
        mTimerNumberColor = DEFAULT_TIMER_NUMBER_COLOR;

        // Init all paints
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerColonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CirclePaint
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        // CircleButtonPaint
        mCircleButtonPaint.setColor(mCircleButtonColor);
        mCircleButtonPaint.setAntiAlias(true);
        mCircleButtonPaint.setStyle(Paint.Style.FILL);

        // LinePaint
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mCircleButtonRadius * 2 + 8);
        mLinePaint.setStyle(Paint.Style.STROKE);

        // HighlightLinePaint
        mHighlightLinePaint.setColor(mHighlightLineColor);

        // NumberPaint
        mNumberPaint.setColor(mNumberColor);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);
        mNumberPaint.setStyle(Paint.Style.STROKE);
        mNumberPaint.setStrokeWidth(mCircleButtonRadius * 2 + 8);

        // TimerNumberPaint
        mTimerNumberPaint.setColor(mTimerNumberColor);
        mTimerNumberPaint.setTextSize(mTimerNumberSize);
        mTimerNumberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerColonPaint
        mTimerColonPaint.setColor(DEFAULT_TIMER_COLON_COLOR);
        mTimerColonPaint.setTextAlign(Paint.Align.CENTER);
        mTimerColonPaint.setTextSize(mTimerNumberSize);

        // Solve the target version related to shadow
        // setLayerType(View.LAYER_TYPE_SOFTWARE, null); // use this, when targetSdkVersion is greater than or equal to api 14
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float radius = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        canvas.drawCircle(mCx, mCy, radius, mNumberPaint);
        canvas.save();
        canvas.rotate(-90, mCx, mCy);
        float left = mCx - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine);
        float top = mCy - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine);
        float right = mCx + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine);
        float bottom = mCy + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine);
        RectF rect = new RectF(left, top, right, bottom);

        canvas.drawArc(rect, 0, (float) Math.toDegrees(mCurrentRadian), false, mLinePaint);
        canvas.restore();
        canvas.save();

        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, 0.01f, mLinePaint);
        canvas.restore();
        canvas.save();

        // TimerNumber
        canvas.rotate(0, mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, 0.01f, mLinePaint);

        canvas.restore();
        canvas.save();
        // TimerNumber


        canvas.rotate(0, mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCircleButtonRadius, mTimerColonPaint);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCircleButtonRadius, mCircleButtonPaint);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.drawText((mCurrentTime / 60 < 10 ? "0" + mCurrentTime / 60 : mCurrentTime / 60) + " " + (mCurrentTime % 60 < 10 ?
                "0" + mCurrentTime % 60 : mCurrentTime % 60), mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerNumberPaint);
        canvas.drawText(":", mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerColonPaint);

        canvas.restore();
        canvas.save();
        canvas.restore();
        super.onDraw(canvas);
    }

    private float getFontHeight(Paint paint) {
        // FontMetrics sF = paint.getFontMetrics();
        // return sF.descent - sF.ascent;
        Rect rect = new Rect();
        paint.getTextBounds("1", 0, 1, rect);
        return rect.height();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (height > width) {
            height = width = Math.min(height, width);
        } else {
            height = width = (int) (1.5 * (height));
        }
        this.mCx = width / 2;
        this.mCy = height / 2;
        // Radius
        if (mGapBetweenCircleAndLine + mCircleStrokeWidth >= mCircleButtonRadius) {
            this.mRadius = width / 2 ;
        } else {
            this.mRadius = width / 2 - (mCircleButtonRadius - mGapBetweenCircleAndLine -
                    mCircleStrokeWidth / 2);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_RADIAN, mCurrentRadian);
        bundle.putFloat(STATUS_CURRENT_TIME, mCurrentTime);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            mCurrentRadian = bundle.getFloat(STATUS_RADIAN);
            mCurrentTime = (int) bundle.getFloat(STATUS_CURRENT_TIME);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // Use tri to cal radian
    private float getRadian(float x, float y)
    {
        float alpha = (float) Math.atan((x - mCx) / (mCy - y));
        // Quadrant
        if (x > mCx && y > mCy)
        {
            // 2
            alpha += Math.PI;
        }
        else if (x < mCx && y > mCy)
        {
            // 3
            alpha += Math.PI;
        }
        else if (x < mCx && y < mCy)
        {
            // 4
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & event.getActionMasked())
        {
            case MotionEvent.ACTION_MOVE:
                if (!mStarted){
                    float temp = getRadian(event.getX(), event.getY());
                    mCurrentTime = (int) (60 / (2 * Math.PI) * temp * 61);
                    int time = mCurrentTime % 60;
                    if (time > 0) {
                        mCurrentTime = mCurrentTime - time;
                        setRecordTime(mCurrentTime);
                    }
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * start timer
     */
    public void startTimer()
    {
        if (mCurrentRadian > 0 && !mStarted)
        {
            timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    handler.obtainMessage().sendToTarget();
                }
            };
            timer.schedule(timerTask, 1000, 1000);
            mStarted = true;
            if (this.mCircleTimerListener != null)
            {
                this.mCircleTimerListener.onTimerStart(mCurrentTime);
            }
        }
    }


    /**
     * pause timer
     */
    public void pauseTimer()
    {
        if (mStarted)
        {
            timerTask.cancel();
            mStarted = false;
            if (this.mCircleTimerListener != null)
            {
                this.mCircleTimerListener.onTimerPause(mCurrentTime);
            }
        }
    }

    /**
     * set current time in seconds
     *
     * @param time
     */
    public void setCurrentTime(int time)
    {
        if (time >= 0 && time <= 3600)
        {
            mCurrentTime = time;
            if (mCircleTimerListener != null)
            {
                mCircleTimerListener.onTimerSetValueChanged(time);
            }
            this.mCurrentRadian = (float) ( 2 * Math.PI);
            invalidate();
        }
    }

    public void setRecordTime(int recordTime){
        this.mRecordTime = recordTime;
    }

    /**
     * set timer listener
     *
     * @param mCircleTimerListener
     */
    public void setCircleTimerListener(CircleTimerListener mCircleTimerListener)
    {
        this.mCircleTimerListener = mCircleTimerListener;
    }

    /**
     * get current time in seconds
     *
     * @return
     */
    public int getCurrentTime()
    {
        return mCurrentTime;
    }


    public interface CircleTimerListener {
        /**
         * launch timer stop event
         */
        void onTimerStop();

        /**
         * launch timer start event
         *
         * @param time
         */
        void onTimerStart(int time);

        /**
         * launch timer pause event
         *
         * @param time
         */
        void onTimerPause(int time);


        /**
         * launch timer timing value changed event
         *
         * @param time
         */
        void onTimerTimingValueChanged(int time);

        /**
         * launch timer set value changed event
         *
         * @param time
         */
        void onTimerSetValueChanged(int time);

        void onTimerOver();

    }
}