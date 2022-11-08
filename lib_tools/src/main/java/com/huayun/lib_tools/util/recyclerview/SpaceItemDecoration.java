package com.huayun.lib_tools.util.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 网格布局间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int count;

    /**
     *
     * @param space 间隔间距
     * @param count 列数
     */
    public SpaceItemDecoration(int space,int count) {
        this.space = space;
        this.count = count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) %count==0) {
            outRect.left = 0;
        }
    }

}