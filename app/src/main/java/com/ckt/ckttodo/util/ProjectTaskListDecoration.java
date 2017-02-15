package com.ckt.ckttodo.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ckt.ckttodo.R;

/**
 * Created by zhiwei.li
 */

class ProjectTaskListDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    ProjectTaskListDecoration(Context context) {
        this.divider = context.getResources().getDrawable(R.drawable.projects_tasks_divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //default drawHorizontalLine
        int left = parent.getPaddingLeft() + 40;
        int right = parent.getWidth() - parent.getPaddingRight();
        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, divider.getIntrinsicHeight());
    }


}
