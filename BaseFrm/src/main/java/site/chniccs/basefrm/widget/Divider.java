package site.chniccs.basefrm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class Divider
        extends RecyclerView.ItemDecoration
{
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int mDividerHeight = 2;
    private int mOrientation;

    public Divider(Context paramContext, int paramInt)
    {
        if ((paramInt != 1) && (paramInt != 0)) {
            throw new IllegalArgumentException("paramInt must be LinearLayoutManager.VERTICAL or LinearLayoutManager.HORIZONTAL");
        }
        this.mOrientation = paramInt;
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(ATTRS);
        this.mDivider = localTypedArray.getDrawable(0);
        localTypedArray.recycle();
    }

    private void drawHorizontal(Canvas paramCanvas, RecyclerView paramRecyclerView)
    {
        int i = paramRecyclerView.getPaddingLeft();
        int j = paramRecyclerView.getMeasuredWidth() - paramRecyclerView.getPaddingRight();
        int k = paramRecyclerView.getChildCount();
        for (int m = 0; m < k; m++)
        {
            View localView = paramRecyclerView.getChildAt(m);
            RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)localView.getLayoutParams();
            int n = localView.getBottom() + localLayoutParams.bottomMargin;
            int i1 = n + this.mDividerHeight;
            if (this.mDivider != null)
            {
                this.mDivider.setBounds(i, n, j, i1);
                this.mDivider.draw(paramCanvas);
            }
        }
    }

    private void drawVertical(Canvas paramCanvas, RecyclerView paramRecyclerView)
    {
        int i = paramRecyclerView.getPaddingTop();
        int j = paramRecyclerView.getMeasuredHeight() - paramRecyclerView.getPaddingBottom();
        int k = paramRecyclerView.getChildCount();
        for (int m = 0; m < k; m++)
        {
            View localView = paramRecyclerView.getChildAt(m);
            RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)localView.getLayoutParams();
            int n = localView.getRight() + localLayoutParams.rightMargin;
            int i1 = n + this.mDividerHeight;
            if (this.mDivider != null)
            {
                this.mDivider.setBounds(n, i, i1, j);
                this.mDivider.draw(paramCanvas);
            }
        }
    }

    public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
        super.getItemOffsets(paramRect, paramView, paramRecyclerView, paramState);
        paramRect.set(0, 0, 0, this.mDividerHeight);
    }

    public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
        super.onDraw(paramCanvas, paramRecyclerView, paramState);
        if (this.mOrientation == 1)
        {
            drawVertical(paramCanvas, paramRecyclerView);
            return;
        }
        drawHorizontal(paramCanvas, paramRecyclerView);
    }
}
