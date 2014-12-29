package com.hume.mydota;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by tcp on 2014/12/25.
 */
public class MainTabActivity extends ActivityGroup {
    @Override
    protected void onCreate(Bundle savedInstacnceState){
        super.onCreate(savedInstacnceState);
        setContentView(R.layout.dotatabs);

        TabHost tabhost = (TabHost)findViewById(R.id.tabhost);
        tabhost.setup(this.getLocalActivityManager());
        LayoutInflater inflater = LayoutInflater.from(this);
        /*添加每个布局显示的内容*/
//        inflater.inflate(R.layout.fragment_herodata_grid_item,tabhost.getTabContentView());
//        inflater.inflate(R.layout.fragment_itemsdata_grid_item,tabhost.getTabContentView());
        inflater.inflate(R.layout.activity_about,tabhost.getTabContentView());
        TabWidget tabWidget = tabhost.getTabWidget();

        tabhost.addTab(tabhost.newTabSpec("tab1")
                .setIndicator(getString(R.string.main_actionBar_tab_hero))
                .setContent(new Intent(MainTabActivity.this,MainHerolist.class)));

        tabhost.addTab(tabhost.newTabSpec("tab2")
                .setIndicator(getString(R.string.main_actionBar_tab_item))
                .setContent(new Intent(MainTabActivity.this,MainItemlist.class)));

        tabhost.addTab(tabhost.newTabSpec("tab3")
                .setIndicator(getString(R.string.main_actionBar_tab_skill))
                .setContent(R.id.webv_about_desc));

        /*设置标签的字体颜色和大小*/
        for(int i=0; i<tabWidget.getTabCount(); ++i){
            TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(12);//设置字体大小
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));//设置颜色
        }
    }
}
