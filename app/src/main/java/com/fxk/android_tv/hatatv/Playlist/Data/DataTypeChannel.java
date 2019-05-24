package com.fxk.android_tv.hatatv.Playlist.Data;


import java.util.ArrayList;

public class DataTypeChannel {
    public ArrayList<TypeChannel> pair_type_channel;
    public ArrayList<String> types;

    public DataTypeChannel(ArrayList<CollectData> data){
        pair_type_channel = new ArrayList<TypeChannel>();

        for(int i =0 ; i < data.size(); i++){
            pair_type_channel.add(new TypeChannel(data.get(i).meta.group, data.get(i).id));
        }

        collectTypes();
    }

    public void collectTypes(){
        types = new ArrayList<String>();
        Boolean status = false;
        if(pair_type_channel != null){
            types.add("Все");
            for(int i = 0; i < pair_type_channel.size(); i++){
                status = false;
                for(int y =0; y < types.size(); y++){
                    if(types.get(y).equals(pair_type_channel.get(i).group))
                        status = true;
                }
                if(!status)
                    types.add(pair_type_channel.get(i).group);
            }
        }
    }

    public class TypeChannel{
        public String group;
        public String channel_id;

        public TypeChannel(String group, String channel_id){
            this.group = group;
            this.channel_id = channel_id;
        }
    }
}
