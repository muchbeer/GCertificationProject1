package muchbeer.raum.com.gcertificationproject1.recyclerv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


import muchbeer.raum.com.gcertificationproject1.R;

/**
 * Created by omrierez on 12.11.17.
 */

public class Divider extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public Divider(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.list_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}