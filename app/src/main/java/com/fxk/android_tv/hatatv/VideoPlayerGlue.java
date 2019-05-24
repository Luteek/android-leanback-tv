package com.fxk.android_tv.hatatv;

import android.content.Context;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackSeekDataProvider;


import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;


class VideoPlayerGlue extends PlaybackTransportControlGlue<LeanbackPlayerAdapter> {

    /** Listens for when skip to next and previous actions have been dispatched. */
    public interface OnActionClickedListener {

        /** Skip to the previous item in the queue. */
        void onPrevious();

        /** Skip to the next item in the queue. */
        void onNext();
    }

    private final OnActionClickedListener mActionListener;


    public VideoPlayerGlue(Context context, LeanbackPlayerAdapter playerAdapter,
                           OnActionClickedListener actionListener) {
        super(context, playerAdapter);
        if(getSeekProvider() == null)
             setSeekEnabled(false);
        mActionListener = actionListener;

    }

    @Override
    protected void onCreatePrimaryActions(ArrayObjectAdapter adapter) {
        super.onCreatePrimaryActions(adapter);
        // disable PLAY|PAUSE
        adapter.clear();
        PlaybackSeekDataProvider ct = getSeekProvider();
        setControlsOverlayAutoHideEnabled(true);
    }

    @Override
    protected void onCreateSecondaryActions(ArrayObjectAdapter adapter) {
        super.onCreateSecondaryActions(adapter);
    }

    @Override
    public void onActionClicked(Action action) {
        if (shouldDispatchAction(action)) {
            dispatchAction(action);
            return;
        }
        // Super class handles play/pause and delegates to abstract methods next()/previous().
        super.onActionClicked(action);
    }

    // Should dispatch actions that the super class does not supply callbacks for.
    private boolean shouldDispatchAction(Action action) {
        return false;
    }

    private void dispatchAction(Action action) {
        // Primary actions are handled manually.
        if (action instanceof PlaybackControlsRow.MultiAction) {
            PlaybackControlsRow.MultiAction multiAction = (PlaybackControlsRow.MultiAction) action;
            multiAction.nextIndex();
            // Notify adapter of action changes to handle secondary actions, such as, thumbs up/down
            // and repeat.
            notifyActionChanged(
                    multiAction,
                    (ArrayObjectAdapter) getControlsRow().getSecondaryActionsAdapter());
        }
    }

    private void notifyActionChanged(
            PlaybackControlsRow.MultiAction action, ArrayObjectAdapter adapter) {
        if (adapter != null) {
            int index = adapter.indexOf(action);
            if (index >= 0) {
                adapter.notifyArrayItemRangeChanged(index, 1);
            }
        }
    }

    @Override
    public void next() {
        mActionListener.onNext();
    }

    @Override
    public void previous() {
        mActionListener.onPrevious();
    }
}
