package com.fxk.android_tv.hatatv.Playlist.Data;

import com.google.gson.annotations.SerializedName;

public class DataProgrammes {
    @SerializedName("channel_id")
    public Integer id;
    @SerializedName("date")
    public String date;
    @SerializedName("programme_name")
    public String programme_name;
    @SerializedName("programme_category")
    public String programme_category;
    @SerializedName("programme_logo")
    public String programme_logo;
    @SerializedName("programme_desc")
    public String programme_desc;
    @SerializedName("time_start")
    public String time_start;
    @SerializedName("time_stop")
    public String time_stop;
}
