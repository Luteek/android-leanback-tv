package com.fxk.android_tv.hatatv;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v17.leanback.app.VideoSupportFragment;
import android.support.v17.leanback.app.VideoSupportFragmentGlueHost;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.CursorObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.PlaybackSeekDataProvider;
import android.support.v17.leanback.widget.PlaybackSeekUi;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import com.fxk.android_tv.hatatv.Auth.UidData;
import com.fxk.android_tv.hatatv.Network.Connection;
import com.fxk.android_tv.hatatv.Network.DataException;
import com.fxk.android_tv.hatatv.Playlist.ChannelPlayList;
import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;
import com.fxk.android_tv.hatatv.Presenters.CardPresenter;
import com.fxk.android_tv.hatatv.Presenters.CustomPresenter;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaybackFragment extends VideoSupportFragment implements MVP.MainFragment{
    private static final int UPDATE_DELAY = 16;
    private ArrayList<CollectData> mCollectData;
    private VideoPlayerGlue mPlayerGlue;
    private LeanbackPlayerAdapter mPlayerAdapter;
    private SimpleExoPlayer mPlayer;
    private TrackSelector mTrackSelector;
    private CollectData mVideo;

    private UidData uid;

    private CustomPresenter customPresenter;

    private String GROUP_CHANNEL;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Получаем из переданого Intent объект выбраного телеканала, а так же массив объектов телеканала
        mVideo = getActivity().getIntent().getParcelableExtra(MainActivity.CHANNEL);

        uid = new UidData(getActivity());

        //initial CustomPresenter
        customPresenter = new CustomPresenter(this, new Connection(), new UidData(getActivity()));

        GROUP_CHANNEL = mVideo.meta.group;
        mCollectData = getActivity().getIntent().getParcelableArrayListExtra("ARRAY");
    }

//    @Override
//    public void showControlsOverlay(boolean runAnimation){
//        // бездействие способствует отсутствию контроллеров
//
//    }

    /**
     * Остаток от реализации leanback samples. скорее всего не пригодится
     * */
    private CursorObjectAdapter setupRelatedVideosCursor() {
        CursorObjectAdapter videoCursorAdapter = new CursorObjectAdapter(new CardPresenter());
        return videoCursorAdapter;
    }

    /**
     * Реализует инициализацию всех UI элементов в Плайбек активити.
     * */
    private ArrayObjectAdapter initializeRelatedVideosRow() {
        ClassPresenterSelector presenterSelector = new ClassPresenterSelector();
        mPlayerGlue.getControlsRow().setHeaderItem(new HeaderItem(null));
        presenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        presenterSelector.addClassPresenter(
                mPlayerGlue.getControlsRow().getClass(), mPlayerGlue.getPlaybackRowPresenter());
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(presenterSelector);

        rowsAdapter.add(mPlayerGlue.getControlsRow());

        /**
         * Создает новый listrow - перечень телеканалов при клике вниз.
         * */
        HeaderItem header = new HeaderItem(GROUP_CHANNEL);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        ArrayList<CollectData> mChannel = mCollectData;

        /**
         * Если выбранный телеканал соответствует выбранной группе то добавляет в массив.
         * */
        for(int y = 0; y < mChannel.size(); ++y){
            if(mChannel.get(y).meta.group.equals(GROUP_CHANNEL))
                listRowAdapter.add(mChannel.get(y));
        }

        ListRow row = new ListRow(header, listRowAdapter);
        rowsAdapter.add(row);

        setOnItemViewClickedListener(new ItemViewClickedListener());
        return rowsAdapter;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }

    /** Pauses the player. */
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onPause() {
        super.onPause();

        if (mPlayerGlue != null && mPlayerGlue.isPlaying()) {
            mPlayerGlue.pause();
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), mTrackSelector);
        mPlayerAdapter = new LeanbackPlayerAdapter(getActivity(), mPlayer, UPDATE_DELAY);
        mPlayerGlue = new VideoPlayerGlue(getActivity(), mPlayerAdapter, null);
        mPlayerGlue.setSeekProvider(null);
        mPlayerGlue.setSeekEnabled(false);
        VideoSupportFragmentGlueHost host = new VideoSupportFragmentGlueHost(this);

        mPlayerGlue.setHost(host);
        mPlayerGlue.playWhenPrepared();

        ArrayObjectAdapter mRowsAdapter = initializeRelatedVideosRow();
        setAdapter(mRowsAdapter);

        play(mVideo);

        mPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                DataException ex = new DataException();
                ex.meta.function_name = "ExoPlaybackException";
                ex.meta.error_detail = error.getCause().getMessage();
                customPresenter.sendExcept(ex, uid);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mTrackSelector = null;
            mPlayerGlue = null;
            mPlayerAdapter = null;
        }
    }

    private void play(CollectData video) {
        try{
            mPlayerGlue.setTitle(video.name);
            mPlayerGlue.setSubtitle("");
            mPlayerGlue.setControlsOverlayAutoHideEnabled(true);
            prepareMediaForPlaying(Uri.parse(video.service_uri));
            mPlayerGlue.play();
        }catch (Exception e){
            DataException ex = new DataException();
            ex.meta.function_name = "Play";
            ex.meta.error_detail = e.getMessage();
            customPresenter.sendExcept(ex, uid);
        }

    }

    private void prepareMediaForPlaying(Uri mediaSourceUri) {
        try{
            String userAgent = Util.getUserAgent(getActivity(), "VideoPlayerGlue");
            DataSource.Factory factory = new DefaultDataSourceFactory(getActivity(), userAgent, null);
            if(mediaSourceUri != null){
                mPlayer.stop();
                if(!mPlayer.isLoading()){
                    HlsMediaSource mediaSource = new HlsMediaSource(mediaSourceUri, factory,null, null);
                    mPlayer.prepare(mediaSource);
                }
            }
        }catch (Exception e){
            DataException ex = new DataException();
            ex.meta.function_name = "prepareMediaForPlaying";
            ex.meta.error_detail = e.getMessage();
            customPresenter.sendExcept(ex,uid);
        }
    }

    @Override
    public void getData(ArrayList<CollectData> channelList) {

    }

    @Override
    public void getException(String str) {

    }

    /** Opens the video page when a related video has been clicked. */
    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {

            /**
             * обработчик клика по перечню телеканалов в этом активити. Передает плееру выбранный объект
             * */
            if (item instanceof CollectData) {
                CollectData video = (CollectData) item;
                play(video);
            }
        }
    }

}
