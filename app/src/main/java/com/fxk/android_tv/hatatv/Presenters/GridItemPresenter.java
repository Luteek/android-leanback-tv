package com.fxk.android_tv.hatatv.Presenters;

import android.graphics.Color;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fxk.android_tv.hatatv.R;

public class GridItemPresenter extends Presenter {

    private static final int WIDTH_ITEM = 300;
    private static final int HEIGHT_ITEM = 200;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(WIDTH_ITEM, HEIGHT_ITEM));
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.setBackgroundColor(ContextCompat.getColor(viewGroup.getContext(), R.color.colorAccent));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        ((TextView) viewHolder.view).setText(o.toString());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
