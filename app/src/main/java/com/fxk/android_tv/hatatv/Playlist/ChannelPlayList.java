package com.fxk.android_tv.hatatv.Playlist;

import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;

import java.util.ArrayList;
import java.util.List;

public class ChannelPlayList {
    private List<CollectData> playlist;
    private int currentPosition;

    public ChannelPlayList() {
        playlist = new ArrayList<>();
        currentPosition = 0;
    }

    /**
     * Clears the videos from the playlist.
     */
    public void clear() {
        playlist.clear();
    }

    /**
     * Adds a video to the end of the playlist.
     *
     * @param channel to be added to the playlist.
     */
    public void add(CollectData channel) {
        playlist.add(channel);
    }

    /**
     * Sets current position in the playlist.
     *
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * Returns the size of the playlist.
     *
     * @return The size of the playlist.
     */
    public int size() {
        return playlist.size();
    }

    /**
     * Moves to the next video in the playlist. If already at the end of the playlist, null will
     * be returned and the position will not change.
     *
     * @return The next video in the playlist.
     */
    public CollectData next() {
        if ((currentPosition + 1) < size()) {
            currentPosition++;
            return playlist.get(currentPosition);
        }
        return null;
    }

    /**
     * Moves to the previous video in the playlist. If the playlist is already at the beginning,
     * null will be returned and the position will not change.
     *
     * @return The previous video in the playlist.
     */
    public CollectData previous() {
        if (currentPosition - 1 >= 0) {
            currentPosition--;
            return playlist.get(currentPosition);
        }
        return null;
    }
}
