package com.huayun.lib_tools.util.recyclerview;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义recyclerView点击item滑动居中
 */
public class CenterLayoutManager extends LinearLayoutManager {

    static int lastPosition = 0;
    static int targetPosition = 0;

    public CenterLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        CenterSmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int lastPosition, int position) {
        CenterLayoutManager.lastPosition = lastPosition;
        CenterLayoutManager.targetPosition = position;
        smoothScrollToPosition(recyclerView, state, position);
    }

    public static class CenterSmoothScroller extends LinearSmoothScroller {
        private static float duration = 400f;

        public CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            float newDuration = duration / (Math.abs(targetPosition - lastPosition));
            return newDuration / displayMetrics.densityDpi;
        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
            return super.calculateTimeForScrolling(dx);
        }
    }
}