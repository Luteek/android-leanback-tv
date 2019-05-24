package com.fxk.android_tv.hatatv.Playlist.Data;

import com.google.gson.annotations.SerializedName;

public class DataChannel {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;

    @SerializedName("group_title")
    public Integer group_title;

    @SerializedName("tvg_logo")
    public String tvg_logo;
    @SerializedName("url")
    public String url;
    @SerializedName("status")
    public Boolean status;
}
