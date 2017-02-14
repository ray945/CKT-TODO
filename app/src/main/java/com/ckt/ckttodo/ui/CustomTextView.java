package com.ckt.ckttodo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author
 * @version 1.0
 * @date 2017/2/10
 */
public class CustomTextView extends TextView {
    protected WeakReference<Context> contextWeakReference;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;
    private boolean mAnimating = true;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.contextWeakReference = new WeakReference<>(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        mViewWidth = getMeasuredWidth();
        if (mViewWidth > 0) {
            mPaint = getPaint();
            mLinearGradient = new LinearGradient(-150, 0, 0, 0, new int[]{0xFF7B7A7A,0xFF7B7A7A, 0xFF7B7A7A,0xffffffff,0xffffffff,0xffffffff,0xFF7B7A7A,0xFF7B7A7A,0xFF7B7A7A}, 
                    new float[]{0, 0.25f,0.5f,0.75f,1.0f,0.75f,0.5f,0.25f, 0}, Shader.TileMode.MIRROR);
            mPaint.setShader(mLinearGradient);
            mGradientMatrix = new Matrix();
        }

        if (mAnimating && mGradientMatrix != null) {
            mTranslate += mViewWidth / 40;
            if (mTranslate > mViewWidth + 800) {
                mTranslate = 0;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(80);
        }
    }
}