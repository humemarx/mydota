package com.hume.mydota;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcp on 2015/1/19.
 */
public class HeroVideoListActivity extends Activity{
    private List<String> video_list = new ArrayList<>();
    private String vid = "XNjE4ODExNDg0";//视频id
    private String hero_keyname;
    private HeroItem herolist;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dota_video_listview);
        hero_keyname = this.getIntent().getStringExtra("heroitem");
        try {
            herolist = DataManager.getHeroItem(this,hero_keyname);//获取基本信息
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        for(int i=0; i<herolist.hero_video.length; i++){
            video_list.add(herolist.hero_video[i]);
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
