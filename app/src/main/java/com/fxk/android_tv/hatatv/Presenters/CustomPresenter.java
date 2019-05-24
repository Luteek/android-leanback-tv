package com.fxk.android_tv.hatatv.Presenters;

import com.fxk.android_tv.hatatv.Auth.UidData;
import com.fxk.android_tv.hatatv.MVP;
import com.fxk.android_tv.hatatv.Network.DataException;
import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;
import com.fxk.android_tv.hatatv.Playlist.Parse.ParseChannelsByType;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomPresenter implements MVP.Presenter,
                                        MVP.GetConnector.OnAuth,
                                        MVP.GetConnector.OnGetCollectData,
                                        MVP.GetConnector.OnSendException{

    private MVP.MainFragment mainFragment;
    private MVP.GetConnector getConnector;
    private UidData uid;
    private ArrayList<CollectData> collectDataArrayList;
    private ParseChannelsByType parseChannelsByType;
    private String current_id;

    public CustomPresenter(MVP.MainFragment mw, MVP.GetConnector gc, UidData uid){
        this.mainFragment = mw;
        this.getConnector = gc;
        this.uid = uid;
        current_id = "";
    }

    @Override
    public void onReady() {
        if(uid.getHASH_UID() != null){
            getConnector.sendUID(this, uid.uid);
            getConnector.getCollectData(this, uid.uid.hashid);
        }
    }

    @Override
    public void responseCollectData(ArrayList<CollectData> collectData) {
        mainFragment.getData(collectData);
    }

    @Override
    public void responseAnswer() {

    }

    @Override
    public void getAuth(Integer http_code) {

    }

    public void sendExcept(DataException ex, UidData uid){
        getConnector.sendException(this, ex, uid);
    }

    @Override
    public void responseErrorConnection(String function_name, int code) {

    }

    @Override
    public void onFailureConnection(String function_name, Throwable t) {
        mainFragment.getException(t.getMessage());
    }
}
