package com.wcb.flexibleviewpager.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by wcb on 2017/4/19.
 */

public class FlexibleViewPager extends RelativeLayout {
    private final GestureDetector mGestureDetector;
    private OnRefreshListener listener;
    private ViewPager mViewPager;
    private float mDownX;

    public FlexibleViewPager(Context context) {
        this(context, null);
    }

    public FlexibleViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(new GestureListener());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = getChildAt(0);
        if (view instanceof ViewPager) {
            mViewPager = (ViewPager) view;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录位置
                mDownX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float xMove = mDownX - ev.getRawX();
                Log.d("vincent", "onInterceptTouchEvent" + " xMove - " + xMove);
                Log.d("vincent", "onInterceptTouchEvent" + " getTranslationX - " + mViewPager.getTranslationX());
                if (mViewPager.getTranslationX() < 0
                        || (xMove > 0 && (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1))) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }


    private class GestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("vincent", "onScroll distanceX - " + distanceX);
            Log.d("vincent", "onScroll getTranslationX - " + mViewPager.getTranslationX());
            if (mViewPager.getTranslationX() - distanceX > 0) {
                distanceX = mViewPager.getTranslationX();
            }
            if (Math.abs(distanceX) < 200) {
                mViewPager.setTranslationX(mViewPager.getTranslationX() - distanceX);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}