package com.ckt.ckttodo.widgt;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ckt on 1/25/17.
 */

public class TaskDividerItemDecoration extends RecyclerView.ItemDecoration {


    private final static int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };


    private Drawable mDarwable;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


    private int mOrientation;

    public TaskDividerItemDecoration(Context context, int orientation) {

        final TypedArray array = context.obtainStyledAttributes(ATTRS);
        mDarwable = array.getDrawable(0);
        array.recycle();
        setOrientation(orientation);
    }


    private void setOrientation(int orientation) {

        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }

        mOrientation = orientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDarwable.getIntrinsicHeight();
            mDarwable.setBounds(left, top, right, bottom);
            mDarwable.draw(c);
        }
    }


    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDarwable.getIntrinsicHeight();
            mDarwable.setBounds(left, top, right, bottom);
            mDarwable.draw(c);
        }


    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDarwable.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDarwable.getIntrinsicWidth(), 0);
        }
    }
}
