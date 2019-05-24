package com.fxk.android_tv.hatatv.Network;

import com.fxk.android_tv.hatatv.Auth.UidData;
import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;
import com.fxk.android_tv.hatatv.Playlist.Data.DataChannel;
import com.fxk.android_tv.hatatv.Playlist.Data.DataProgrammes;
import com.fxk.android_tv.hatatv.Playlist.Data.DataTypeChannel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerApi {
    @GET("/iptv/getList")
    Call<ArrayList<DataChannel>> getList();

    @GET("/iptv/getTele")
    Call<ArrayList<DataProgrammes>> getTele(@Query("chnlid") int id);

    @GET("/iptv/getChannelTypes")
    Call<ArrayList<DataTypeChannel>> getChannelTypes();

    @GET("/mobile/v1/device/{hash_uid}/channels")
    Call<ArrayList<CollectData>> getCollectData(@Path("hash_uid")String hash_uid);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("/mobile/v1/device")
    Call<UidData.RequestDataUID> auth(@Body UidData.RequestDataUID uid);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("/mobile/v1/device/{hash_uid}/detail_log")
    Call<DataException> sendException(@Path("hash_uid")String hash_uid, @Body DataException except);
}
