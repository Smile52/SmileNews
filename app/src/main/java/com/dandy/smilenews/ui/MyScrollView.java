package com.dandy.smilenews.ui;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Dandy on 2016/11/4.
 */

class MyNestedScrollView extends NestedScrollView {
    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;
    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }

}
