package com.hume.mydota;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by tcp on 2014/12/25.
 */
public class HeroDetailActivity extends ActivityGroup {
    private static final String TAG = "HeroDetailActivity";
    /**
     * 英雄名称 Intent 参数
     */
    public final static String KEY_HERO_DETAIL_KEY_NAME = "KEY_HERO_DETAIL_KEY_NAME";


    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//设置不确定的进度
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herodetail);

        TabHost tabhost = (TabHost)findViewById(R.id.tabherodetail);
        tabhost.setup(this.getLocalActivityManager());
        TabWidget tabWidget = tabhost.getTabWidget();

        tabhost.addTab(tabhost.newTabSpec("tab1")
                .setIndicator("基本介绍")
                .setContent(new Intent(HeroDetailActivity.this, HeroIntroduceFragment.class)));

        tabhost.addTab(tabhost.newTabSpec("tab2")
                .setIndicator("技能加点")
                .setContent(R.id.view2));

        tabhost.addTab(tabhost.newTabSpec("tab3")
                .setIndicator("初装推荐")
                .setContent(R.id.view3));

        tabhost.addTab(tabhost.newTabSpec("tab4")
                .setIndicator("使用技巧")
                .setContent(R.id.view4));

        tabhost.addTab(tabhost.newTabSpec("tab5")
                .setIndicator("教学视频")
                .setContent(R.id.view5));

        /*设置标签的字体颜色和大小*/
        for(int i=0; i<tabWidget.getTabCount(); ++i){
            TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(10);//设置字体大小
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));//设置颜色
        }
    }
}
