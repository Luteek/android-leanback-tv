package com.fxk.android_tv.hatatv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;

import com.fxk.android_tv.hatatv.Auth.UidData;
import com.fxk.android_tv.hatatv.Network.Connection;
import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;
import com.fxk.android_tv.hatatv.Playlist.Data.DataTypeChannel;
import com.fxk.android_tv.hatatv.Playlist.Parse.ParseChannelsByType;
import com.fxk.android_tv.hatatv.Presenters.CardPresenter;
import com.fxk.android_tv.hatatv.Presenters.CustomPresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends BrowseFragment implements MVP.MainFragment,
        LoaderManager.LoaderCallbacks<HashMap<String, List<CollectData>>>{

    private CustomPresenter customPresenter;
    private ArrayList<CollectData> mCollectdata;
    private ParseChannelsByType parseChannelsByType;
    private DataTypeChannel types;
    private ArrayList<String> mTypes;

    private BackgroundManager backgroundManager;
    private DisplayMetrics displayMetrics;

    private ArrayObjectAdapter rowsAdapter;
    private int NUM_ROWS = 4;

   @Override
    public void onActivityCreated(Bundle savedInstanceState){
       super.onActivityCreated(savedInstanceState);

       //initial CustomPresenter
       customPresenter = new CustomPresenter(this, new Connection(), new UidData(getActivity()));
       customPresenter.onReady();

       // инициализируем UI элементы основной активности
       prepareBackgroundManager();
       setupUIElements();
       // привязываем евентлистнер
       setupEventListeners();
   }

   /**
    *  С помощью MVP получаем данные от сервера
    *  CollectData
    * */
    @Override
    public void getData(ArrayList<CollectData> collectData) {
        if(collectData.size() > 0){
            mCollectdata = collectData;
            types = new DataTypeChannel(collectData);
            mTypes = types.types;
            NUM_ROWS = mTypes.size();

            parseChannelsByType = new ParseChannelsByType(collectData);
            buildRowsAdapter();
        }
    }

    /**
     * Действительно при возвращении в этот фрагмент из любого уголка программы
     * Есть явные причины не использовать
     * Заклепка для закрытия сообщения об ошибке. =(
     * */
    @Override
    public void onResume() {
        if(mCollectdata != null){
            //initial CustomPresenter
            customPresenter = new CustomPresenter(this, new Connection(), new UidData(getActivity()));
            customPresenter.onReady();
        }
        super.onResume();
    }

    /**
     * Создаем фрагмент ошибки
     * Создаем обхект bundle для передачи текста ошибки
     * с помощью менеджера отображаем фрагмент
     * */
    @Override
    public void getException(String str) {
        ErrorFragment errorFragment = new ErrorFragment();
        Bundle bundle_error = new Bundle();
        bundle_error.putString("error_type", str);
        errorFragment.setArguments(bundle_error);

        getFragmentManager().beginTransaction().replace(R.id.main_browse_fragment, errorFragment)
                .addToBackStack(null).addToBackStack(null).commit();
    }

    /**
     * Неарилизованная должным образом функция изменяющая бэкграунд основной активности
     * */
    private void prepareBackgroundManager() {
        backgroundManager = BackgroundManager.getInstance(getActivity());
        backgroundManager.attach(getActivity().getWindow());
        backgroundManager.setColor(getResources().getColor(R.color.colorPrimaryMiddle));
        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }


    /**
     * Определяем основные UI параметры (цвет, текст) основной активности
     * */
    private void setupUIElements() {
        setTitle(getString(R.string.browse_title));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.colorPrimaryMiddle));
        setSearchAffordanceColor(getResources().getColor(R.color.colorAccent));
    }

    /**
     * rowsAdapter конструктор UI элементов.
     * Включает в себя Header - группы телеканалов. правая сторона
     * Так же перечень listRowAdapter - массивы канаов отсортированные по группам. Зависит от
     * CardPresenter - UI телеканалов как элементов
     * */
    private void buildRowsAdapter() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        for (int i = 0; i < NUM_ROWS; ++i) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            ArrayList<CollectData> mChannelByType = parseChannelsByType.getChannelByTypeList(mTypes.get(i));

            for(int y = 0; y < mChannelByType.size(); ++y){
                listRowAdapter.add(mChannelByType.get(y));
            }

            HeaderItem header = new HeaderItem(i, mTypes.get(i));
            rowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        this.setAdapter(rowsAdapter);
    }



    @NonNull
    @Override
    public Loader<HashMap<String, List<CollectData>>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<HashMap<String, List<CollectData>>> loader,
                                        HashMap<String, List<CollectData>> stringListHashMap) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<HashMap<String, List<CollectData>>> loader) {

    }

    private void setupEventListeners() {
       setOnItemViewClickedListener(new ItemViewClickedListner());
    }

    /**
     * Класс обработчик кликов по элементам телеканалов
     * Если object является объектом класса CollectData, программа понимает что был произведен клик
     * по телеканалу. Как следствие формируем Intent, в который засовываем объект телеканала по которому кликнули,
     * а так же отправляем массив объектов телеканала отсортированный по группе ? TODO
     * Все добро передаем сразу в PlaybackActivity
     * */
    private final class ItemViewClickedListner implements OnItemViewClickedListener{
        @Override
        public void onItemClicked(Presenter.ViewHolder viewHolder, Object o, RowPresenter.ViewHolder viewHolder1, Row row) {
            if(o instanceof CollectData){
                CollectData channel = (CollectData) o;
                Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                intent.putExtra(MainActivity.CHANNEL, channel);

                intent.putParcelableArrayListExtra("ARRAY", mCollectdata);
                startActivity(intent);
            }
        }
    }
}
