package com.fxk.android_tv.hatatv.Presenters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;

import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;
import com.fxk.android_tv.hatatv.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CardPresenter extends Presenter {
    private Context context;
    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private Drawable defaultCardImage;

    private int mSelectedBackgroundColor = -1;
    private int mDefaultBackgroundColor = -1;
    private Drawable mDefaultCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mDefaultBackgroundColor =
                ContextCompat.getColor(viewGroup.getContext(), R.color.colorPrimaryDark);
        mSelectedBackgroundColor =
                ContextCompat.getColor(viewGroup.getContext(), R.color.colorAccent);
        mDefaultCardImage = viewGroup.getResources().getDrawable(R.drawable.exo_controls_play, null);

        ImageCardView cardView = new ImageCardView(viewGroup.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    private void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;

        // Both background colors should be set because the view's
        // background is temporarily visible during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        CollectData collectData = (CollectData) o;

        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setTitleText(collectData.name);
        cardView.setContentText("hataTV");

        String logo = collectData.meta.logo;
        if(logo != null){
            Resources res = cardView.getResources();
            int width = 300;
            int height = 200;
            cardView.setMainImageDimensions(width, height);


            Picasso picasso = new Picasso.Builder(viewHolder.view.getContext())
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.e("picas", exception.getMessage());
                        }
                    })
                    .build();

            picasso.load(Uri.parse(logo))
                    .placeholder(R.drawable.exo_edit_mode_logo)
                    .error(R.drawable.exo_edit_mode_logo)
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        // Remove references to images so that the garbage collector can free up memory.
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
