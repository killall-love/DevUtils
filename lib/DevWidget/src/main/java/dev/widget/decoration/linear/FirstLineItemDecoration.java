package dev.widget.decoration.linear;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.widget.decoration.BaseItemDecoration;

/**
 * detail: RecyclerView 分割线 ( 在开头添加一条分割线 )
 * @author Ttt
 */
public class FirstLineItemDecoration
        extends BaseItemDecoration {

    public FirstLineItemDecoration(float lineHeight) {
        super(lineHeight);
    }

    public FirstLineItemDecoration(
            float lineHeight,
            int lineColor
    ) {
        super(lineHeight, lineColor);
    }

    // ==========
    // = 处理方法 =
    // ==========

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            int itemCount = state.getItemCount();
            if (!mSingleLineDraw && itemCount <= 1) {
                return;
            }
            outRect.set(0, (int) mLineHeight, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    @Override
    public void onDraw(
            @NonNull Canvas canvas,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        int itemCount = state.getItemCount();
        if (!mSingleLineDraw && itemCount <= 1) {
            return;
        }

        View child    = parent.getChildAt(0);
        int  position = parent.getChildAdapterPosition(child);
        if (position == 0) {
            canvas.drawRect(
                    child.getLeft() + mLineLeft,
                    child.getTop() - mLineHeight - this.getOffset(),
                    child.getRight() - mLineRight,
                    child.getTop() - this.getOffset(),
                    mLinePaint
            );
        }
    }
}