package com.fxk.android_tv.hatatv.Playlist.Parse;


import com.fxk.android_tv.hatatv.Playlist.Data.CollectData;

import java.util.ArrayList;

public class ParseChannelsByType {

    private ArrayList<CollectData> channel_list;

    public ParseChannelsByType(ArrayList<CollectData> channel_list){
        if(channel_list != null){
            this.channel_list = channel_list;
        }
    }

    public ArrayList<CollectData> getChannelByTypeList(String current_type){
        ArrayList<CollectData> result = new ArrayList<CollectData>();
        String y = "";
        if(!current_type.equals("Все")){
            for(int i=0; i<channel_list.size(); i++){
                y = channel_list.get(i).meta.group;
                if(current_type.equals(channel_list.get(i).meta.group)) {
                    result.add(channel_list.get(i));
                }
            }
        }else{  result = channel_list;  }
        return result;
    }
}
