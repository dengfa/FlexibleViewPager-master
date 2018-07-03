package com.wcb.flexibleviewpager.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.wcb.flexibleviewpager.R;

/**
 * Created by wcb on 2017/4/19.
 */

public class FlexibleViewPager extends RelativeLayout {
    private final GestureDetector mGestureDetector;
    private OnRefreshListener listener;
    private ViewPager mViewPager;
    private float mDownX;
    private View mLoadMoreLayout;
    private int mRight;
    private int mLoadMoreLayoutWidth;

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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View view = getChildAt(0);
        if (view instanceof ViewPager) {
            mViewPager = (ViewPager) view;
        }
        mLoadMoreLayout = getChildAt(1);
        mRight = mViewPager.getRight();
        mLoadMoreLayout.setTranslationX(mRight);
        mLoadMoreLayoutWidth = getResources().getDimensionPixelSize(R.dimen.load_more_layout_width);
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

    private boolean isAutoScrolling;

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
            //// TODO: 2018/7/3 这个distance为什么有时候不准？？？

            if (e1 != null) {
                Log.d("vincent", "onScroll e1 - " + e1.getRawX());
            }

            if (e2 != null) {
                Log.d("vincent", "onScroll e2 - " + e2.getRawX());
            }

            Log.d("vincent", "onScroll distanceX - " + distanceX);
            Log.d("vincent", "onScroll getTranslationX - " + mViewPager.getTranslationX());
            if (isAutoScrolling) {
                return true;
            }

            //加载更多中，右划
            if (mViewPager.getTranslationX() < 0 && distanceX < 0) {
                scrollToRight();
            }

            if (mViewPager.getTranslationX() - distanceX > 0) {
                distanceX = mViewPager.getTranslationX();
            }
            if (Math.abs(distanceX) < mLoadMoreLayoutWidth / 2) {
                if (mViewPager.getTranslationX() - distanceX < -mLoadMoreLayoutWidth) {
                    distanceX = mViewPager.getTranslationX() + mLoadMoreLayoutWidth;
                }
                mViewPager.setTranslationX(mViewPager.getTranslationX() - distanceX);
                mLoadMoreLayout.setTranslationX(mLoadMoreLayout.getTranslationX() - distanceX);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("vincent", "onFling velocityX - " + velocityX);
            Log.d("vincent", "onFling velocityY - " + velocityY);
            if (velocityX < -2000 || mViewPager.getTranslationX() < -mLoadMoreLayoutWidth / 2) {
                scrollToLeft();
                return true;
            } else if (velocityX > 2000 || mViewPager.getTranslationX() >= -mLoadMoreLayoutWidth / 2) {
                scrollToRight();
                return true;
            }
            return false;
        }
    }

    private void scrollToRight() {
        Log.d("vincent", "scrollToRight");
        ViewCompat.animate(mViewPager).translationX(0).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                isAutoScrolling = true;
            }

            @Override
            public void onAnimationEnd(View view) {
                isAutoScrolling = false;
            }

            @Override
            public void onAnimationCancel(View view) {
                isAutoScrolling = false;
            }
        });
        ViewCompat.animate(mLoadMoreLayout).translationX(mRight);
    }

    private void scrollToLeft() {
        Log.d("vincent", "scrollToLeft");
        ViewCompat.animate(mViewPager).translationX(-mLoadMoreLayoutWidth).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                isAutoScrolling = true;
            }

            @Override
            public void onAnimationEnd(View view) {
                isAutoScrolling = false;
            }

            @Override
            public void onAnimationCancel(View view) {
                isAutoScrolling = false;
            }
        });
        ViewCompat.animate(mLoadMoreLayout).translationX(mRight - mLoadMoreLayoutWidth);
    }
}