package com.hume.mydota;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcp on 2015/1/19.
 */
public class HeroVideoListActivity extends Activity{
    private List<String> video_list = new ArrayList<>();
    private String vid = "XNjE4ODExNDg0";//视频id
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dota_video_listview);

        for(int i=0; i<10; ++i){
            video_list.add(vid);
//            Log.v("string",vid);
        }
        final VideoAdapter videoadapter = new VideoAdapter(HeroVideoListActivity.this,video_list);
        final ListView listview = (ListView)findViewById(R.id.video_listview);
        listview.setAdapter(videoadapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HeroVideoListActivity.this,HeroVideoActivity.class);//设置跳转意图
                intent.putExtra("vid",video_list.get(position));
                startActivity(intent);
            }
        });//设置监听
    }
}
