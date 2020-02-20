package com.example.kwlee.customitemviewtest.models.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.kwlee.customitemviewtest.models.listeners.OnViewTouchedListener;

public class InterceptorFrameLayout extends FrameLayout {

  private OnViewTouchedListener mOnViewTouchedListener;


  public InterceptorFrameLayout(Context context) {
    super(context);
  }


  public InterceptorFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  @Override
  public boolean onInterceptTouchEvent (MotionEvent ev) {
    if (mOnViewTouchedListener != null) {
      mOnViewTouchedListener.onViewTouchOccurred(ev);
    }
    return super.onInterceptTouchEvent(ev);
  }


  public void setOnViewTouchedListener (OnViewTouchedListener mOnViewTouchedListener) {
    this.mOnViewTouchedListener = mOnViewTouchedListener;
  }

}
