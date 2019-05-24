package com.fxk.android_tv.hatatv;

import com.fxk.android_tv.hatatv.Auth.UidData;
import com.fxk.android_tv.hatatv.Network.DataException;
import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;

import java.util.ArrayList;

public interface MVP {
    interface Presenter{
        void onReady();
    }

    interface MainFragment{
        void getData(ArrayList<CollectData> channelList);
        void getException(String str);
    }

    interface GetConnector{
        interface OnGetCollectData{
            void responseCollectData(ArrayList<CollectData> collectData);
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void getCollectData(OnGetCollectData onGetCollectData, String hashuid);

        interface OnSendException{
            void responseAnswer();
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void sendException(OnSendException onSendException, DataException data_exception, UidData uid);

        interface OnAuth{
            void getAuth(Integer http_code);
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void sendUID(OnAuth onAuth,  UidData.RequestDataUID uid);
    }
}
