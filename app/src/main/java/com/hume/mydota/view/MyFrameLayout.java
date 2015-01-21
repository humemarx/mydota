package com.hume.mydota.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by tcp on 2015/1/21.
 */
public class MyFrameLayout extends FrameLayout {

    public MyFrameLayout(Context context){
        super(context);
    }
    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}
