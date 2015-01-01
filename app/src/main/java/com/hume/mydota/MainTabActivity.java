package com.hume.mydota;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by tcp on 2014/12/25.
 */
public class MainTabActivity extends ActivityGroup {
    int currentView = 0;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static int maxTabIndex = 2;
    private GestureDetector gestureDetector;
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

        gestureDetector = new GestureDetector(new MyGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };

        /*设置标签的字体颜色和大小*/
        for(int i=0; i<tabWidget.getTabCount(); ++i){
            TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(12);//设置字体大小
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));//设置颜色
        }

    }

    // 左右滑动刚好页面也有滑动效果
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            TabHost tabHost = (TabHost)findViewById(R.id.tabhost);

            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (currentView == maxTabIndex) {
                        currentView = 0;
                    } else {
                        currentView++;
                    }
                    tabHost.setCurrentTab(currentView);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (currentView == 0) {
                        currentView = maxTabIndex;
                    } else {
                        currentView--;
                    }
                    tabHost.setCurrentTab(currentView);
                }
            } catch (Exception e) {
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }
}
