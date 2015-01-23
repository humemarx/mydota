package com.hume.mydota;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tcp on 2014/12/25.
 */
public class HeroDetailActivity extends ActivityGroup {
    private static final String TAG = "HeroDetailActivity";
    /**
     * 英雄名称 Intent 参数
     */
    public final static String KEY_HERO_DETAIL_KEY_NAME = "KEY_HERO_DETAIL_KEY_NAME";
    int currentView = 0;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static int maxTabIndex = 4;
    private GestureDetector gestureDetector;

    private String hero_keyname;
    private HeroItem herolist = null;
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//设置不确定的进度
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herodetail);
        AnimationTabHost tabhost = (AnimationTabHost)findViewById(R.id.tabherodetail);
        tabhost.setup(this.getLocalActivityManager());
        TabWidget tabWidget = tabhost.getTabWidget();
        ImageView icon_hero = (ImageView)findViewById(R.id.icon_hero);
        TextView text_heroname = (TextView)findViewById(R.id.text_heroname);
        /*数据初始化*/
        hero_keyname = this.getIntent().getStringExtra("heroitem");//获取英雄数据名称
        try {
            herolist = DataManager.getHeroItem(this,hero_keyname);//获取基本信息
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = getImageFromAssetsFile(Utils.getHeroIconUri(hero_keyname));//获取头像
        icon_hero.setImageBitmap(bitmap);//设置头像
        text_heroname.setText("英雄图谱——"+herolist.name_l);//设置名称

        Intent intent01 = new Intent(HeroDetailActivity.this, HeroIntroduceFragment.class);//基本信息
        Intent intent02 = new Intent(HeroDetailActivity.this,HeroSkillFragment.class);//技能加点
        Intent intent03 = new Intent(HeroDetailActivity.this,HeroItemsFragment.class);//出装推荐
        Intent intent05 = new Intent(HeroDetailActivity.this,HeroVideoListActivity.class);//教学视频

        intent01.putExtra("heroitem",hero_keyname);//传递数据
        intent02.putExtra("heroitem",hero_keyname);
        intent03.putExtra("heroitem",hero_keyname);
        intent05.putExtra("heroitem",hero_keyname);

        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("基本介绍").setContent(intent01));
        tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("技能加点").setContent(intent02));
        tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("出装推荐").setContent(intent03));
        tabhost.addTab(tabhost.newTabSpec("tab4").setIndicator("使用技巧").setContent(R.id.view4));
        tabhost.addTab(tabhost.newTabSpec("tab5").setIndicator("教学视频").setContent(intent05));

        tabhost.setOpenAnimation(true);//滑动动画显示
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
            tv.setTextSize(10);//设置字体大小
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));//设置颜色
        }
    }

    /**
     * 获取文件图像
     * @param fileName
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try{
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    // 左右滑动刚好页面也有滑动效果
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
            TabHost tabHost = (TabHost)findViewById(R.id.tabherodetail);
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
