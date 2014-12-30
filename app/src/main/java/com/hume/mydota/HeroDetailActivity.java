package com.hume.mydota;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//设置不确定的进度
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herodetail);

        TabHost tabhost = (TabHost)findViewById(R.id.tabherodetail);
        tabhost.setup(this.getLocalActivityManager());
        TabWidget tabWidget = tabhost.getTabWidget();

        ImageView icon_hero = (ImageView)findViewById(R.id.icon_hero);
        TextView text_heroname = (TextView)findViewById(R.id.text_heroname);
        ArrayList<HeroItem> bundlelist = (ArrayList<HeroItem>)this.getIntent().getSerializableExtra("heroitem");//获取数据
        HeroItem herolist = bundlelist.get(0);//获取英雄数据
        Bitmap bitmap = getImageFromAssetsFile(Utils.getHeroIconUri(herolist.keyName));//获取头像
        icon_hero.setImageBitmap(bitmap);//设置头像
        text_heroname.setText("英雄图谱——"+herolist.name_l);//设置名称

        Intent intent01 = new Intent(HeroDetailActivity.this, HeroIntroduceFragment.class);//基本信息
        intent01.putExtra("heroitem",bundlelist);//传递数据

        Intent intent02 = new Intent(HeroDetailActivity.this,HeroSkillFragment.class);//技能加点
        intent02.putExtra("heroitem",bundlelist);

        Intent intent03 = new Intent(HeroDetailActivity.this,HeroItemsFragment.class);//出装推荐
        intent03.putExtra("heroitem",bundlelist);

        tabhost.addTab(tabhost.newTabSpec("tab1")
                .setIndicator("基本介绍")
                .setContent(intent01));

        tabhost.addTab(tabhost.newTabSpec("tab2")
                .setIndicator("技能加点")
                .setContent(intent02));

        tabhost.addTab(tabhost.newTabSpec("tab3")
                .setIndicator("出装推荐")
                .setContent(intent03));

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
}
