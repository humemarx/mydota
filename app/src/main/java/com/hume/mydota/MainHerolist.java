package com.hume.mydota;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 英雄列表
 * Created by tcp on 2014/12/26.
 */
public class MainHerolist extends Activity implements AdapterView.OnItemClickListener {

    private DataManager dataManager = new DataManager();
    private static List<HeroItem> mHeroList = new ArrayList<HeroItem>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_herodata);
        try {
            mHeroList = dataManager.getHeroList(MainHerolist.this);/*获取全部英雄信息*/
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final HeroImagesAdapter adapter = new HeroImagesAdapter(MainHerolist.this, mHeroList);
        GridView herogv = (GridView)findViewById(R.id.herodata_grid);
        herogv.setAdapter(adapter);
        /*设置监听*/
        herogv.setOnItemClickListener(this);
    }

    /**
     * 点击网格动作
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainHerolist.this,HeroDetailActivity.class);//跳转
        intent.putExtra("heroitem",mHeroList.get(position).keyName);//传递数据
        startActivity(intent);//启动新的活动
    }
}
