package com.fxk.android_tv.hatatv.Network;


import com.fxk.android_tv.hatatv.Auth.UidData;
import com.fxk.android_tv.hatatv.MVP;
import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Connection implements MVP.GetConnector{
    private ServerApi api;
    private static  String URL = "";
    private UidData.RequestDataUID uid;

    public Connection(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(6, TimeUnit.SECONDS)
                .readTimeout(6, TimeUnit.SECONDS)
                .writeTimeout(6, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        api = retrofit.create(ServerApi.class);
    }

    @Override
    public void getCollectData(final OnGetCollectData onGetCollectData, String hashuid) {
        api.getCollectData(hashuid).enqueue(new Callback<ArrayList<CollectData>>() {
            @Override
            public void onResponse(Call<ArrayList<CollectData>> call, Response<ArrayList<CollectData>> response) {
                if(response.code() == 200)
                    onGetCollectData.responseCollectData(response.body());
                else{
                    onGetCollectData.responseErrorConnection("getCollectData",response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CollectData>> call, Throwable t) {
                    onGetCollectData.onFailureConnection("getCollectData", t);
            }
        });
    }

    @Override
    public void sendException(OnSendException onSendException, DataException data_exception, UidData u) {
        api.sendException(u.uid.hashid, data_exception).enqueue(new Callback<DataException>() {
            @Override
            public void onResponse(Call<DataException> call, Response<DataException> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<DataException> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    @Override
    public void sendUID(final OnAuth onAuth, UidData.RequestDataUID uid) {
        this.uid = uid;
        api.auth(uid).enqueue(new Callback<UidData.RequestDataUID>() {
            @Override
            public void onResponse(Call<UidData.RequestDataUID> call, Response<UidData.RequestDataUID> response) {
                onAuth.getAuth(response.code());
            }

            @Override
            public void onFailure(Call<UidData.RequestDataUID> call, Throwable t) {
                onAuth.onFailureConnection("sendUID",t);
            }
        });
    }
}
