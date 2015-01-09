package com.hume.mydota;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;


/**基本信息界面
 * Created by tcp on 2014/12/29.
 */
public class HeroIntroduceFragment extends FragmentActivity {
    private String hero_keyname;
    private HeroDetailItem herodata = null;
    private HeroItem herolist = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_herodetail);
        hero_keyname = this.getIntent().getStringExtra("heroitem");//获取英雄数据名称
        try {
            herodata = DataManager.getHeroDetailItem(HeroIntroduceFragment.this,hero_keyname);//获取详细信息
            herolist = DataManager.getHeroItem(this,hero_keyname);//获取基本信息
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        View view = findViewById(R.id.myhero_detail_view);

        ImageView image_hero = (ImageView)findViewById(R.id.image_hero);//英雄图像
        TextView text_hero_name_l = (TextView)findViewById(R.id.text_hero_name_l);//中文名称
        TextView text_hero_name = (TextView)findViewById(R.id.text_hero_name);//英文名称
        TextView text_hero_roles = (TextView)findViewById(R.id.text_hero_roles);//角色定位
        TextView text_hero_hp_faction_atk = (TextView)findViewById(R.id.text_hero_hp_faction_atk);//英雄类型
        TextView text_hero_bio = (TextView)findViewById(R.id.text_hero_bio);//英雄介绍

        String imagevalue = Utils.getHeroImageUri(herolist.keyName);
        Bitmap bitmap = getImageFromAssetsFile(imagevalue);
        String hero_nickname = herolist.nickname_l[0];
        String hero_role = herolist.roles_l[0];
        String hero_hp;
        String hero_faction;
        for(int i=1; i<herolist.nickname_l.length; ++i){
            hero_nickname += "/"+herolist.nickname_l[i];
        }
        for(int i=1; i<herolist.roles_l.length; ++i){
            hero_role += "/"+herolist.roles_l[i];
        }

        if(herolist.hp == "intelligence"){
            hero_hp = "智力";
        }
        else if(herolist.hp == "agility"){
            hero_hp = "敏捷";
        }else{
            hero_hp = "力量";
        }

        if(herolist.faction == "radiant"){
            hero_faction = "天辉";
        }else {
            hero_faction = "夜魇";
        }
        String hero_atk = herolist.atk_l+"/"+hero_faction+"/"+hero_hp;
        image_hero.setImageBitmap(bitmap);
        text_hero_name_l.setText(herolist.name_l);
        text_hero_name.setText(hero_nickname);
        text_hero_roles.setText(hero_role);
        text_hero_hp_faction_atk.setText(hero_atk);
        text_hero_bio.setText("       "+herodata.bio_l);
        // 绑定英雄属性信息
        bindStatsView(view, herodata);
        // 绑定英雄详细属性信息
        bindDetailstatsView(view, herodata);
    }


    /**
     * 读取图像文件
     * @param fileName
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 绑定视图-统计信息
     *
     * @param cView
     * @param cItem
     */
    private void bindStatsView(View cView, HeroDetailItem cItem) {
        if (cItem == null || cItem.stats1 == null
                || cItem.stats1.size() != 6) {
            return;
        }

        final LinearLayout layoutStats1 = Utils.findById(cView, R.id.layout_hero_stats1);
        final LinearLayout layoutStats2 = Utils.findById(cView, R.id.layout_hero_stats2);
        if (layoutStats1 == null || layoutStats2 == null) {
            return;
        }

        final Context context = cView.getContext();
        final Resources res = context.getResources();
        final String[] labels = res.getStringArray(R.array.array_hero_stats);
        final int[] resIds = new int[] {
                R.drawable.overviewicon_int, R.drawable.overviewicon_agi,
                R.drawable.overviewicon_str, R.drawable.overviewicon_attack,
                R.drawable.overviewicon_speed, R.drawable.overviewicon_defense
        };

        final LayoutInflater inflater = LayoutInflater.from(context);
        final int hpIndex = (cItem.hp.equals("intelligence") ? 0: (cItem.hp.equals("agility") ? 1 : 2));
        ViewGroup cParent = layoutStats1;
        View view = null;
        TextView text1 = null;
        TextView text2 = null;
        ImageView image = null;
        for (int i = 0; i < cItem.stats1.size(); i++) {
            cParent = (i <= 2 ? layoutStats1 : layoutStats2);
            view = inflater.inflate(R.layout.fragment_herodetail_stats_list_item, cParent,false);

            text1 = Utils.findById(view, R.id.text_hero_stats_label);
            text1.setText(labels[i]);

            image = Utils.findById(view, R.id.image_hero_stats_icon);
            image.setImageResource(resIds[i]);

            text2 = Utils.findById(view, R.id.text_hero_stats_value);
            text2.setText(cItem.stats1.get(i)[2]);
            if (hpIndex == i) {
                image = Utils.findById(view, R.id.image_hero_stats_icon_primary);
                image.setVisibility(View.VISIBLE);
                text1.setTextColor(Color.RED);
                text2.setTextColor(Color.RED);
            }
            cParent.addView(view);
        }
    }

    /**
     * 绑定视图-详细统计信息
     *
     * @param cView
     * @param cItem
     */
    @SuppressWarnings("deprecation")
    private void bindDetailstatsView(View cView, HeroDetailItem cItem) {
        if (cItem == null||cItem.detailstats1 == null||cItem.detailstats1.size()!=5||cItem.detailstats2==null||cItem.detailstats2.size()!=3){
            return;
        }
        final TableLayout table = (TableLayout) cView.findViewById(R.id.table_hero_detailstats);
        if (table == null) {
            return;
        }
        final Context context = cView.getContext();
        final TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        rowLayout.weight = 1f;
        final TableLayout.LayoutParams tableLayout = new TableLayout.LayoutParams();
        final String[] detailstatsLabel = context.getResources().getStringArray(R.array.array_hero_detailstats);
        final Drawable rowBg = context.getResources().getDrawable(R.drawable.hero_detailstats_table_bg);
        int count = cItem.detailstats1.size();
        int iCount = 0;
        String[] iItem = null;
        TableRow row = null;
        TextView text = null;
        // --detailstats1
        for (int i = 0; i < count; i++) {
            row = new TableRow(context);
            iItem = cItem.detailstats1.get(i);
            iCount = iItem.length;
            for (int ii = 0; ii < iCount; ii++) {
                text = new TextView(context);
                text.setPadding(0, 3, 0, 3);
                if (i <= 0) {
                    text.setText(ii == 0 ? detailstatsLabel[i] : iItem[ii]);
                } else { // INFO:源数据颠倒
                    text.setText(ii == 0 ? detailstatsLabel[i] : iItem[iCount - ii]);
                }
                text.setTextColor(Color.WHITE);//设置字体颜色
                row.addView(text, rowLayout);
            }
            if (i % 2 == 1) {
                row.setBackgroundDrawable(rowBg);
            }
            table.addView(row, tableLayout);
        }
        // --detailstats2
        count = cItem.detailstats2.size();
        for (int i = 0; i < count; i++) {
            row = new TableRow(context);
            iItem = cItem.detailstats2.get(i);
            iCount = iItem.length;
            for (int ii = 0; ii < iCount; ii++) {
                text = new TextView(context);
                text.setPadding(0, 3, 0, 3);
                text.setText(ii == 0 ? detailstatsLabel[i + 5] : iItem[ii]);
                text.setTextColor(Color.WHITE);//设置字体颜色
                row.addView(text, rowLayout);
            }
            if (i % 2 == 0) {
                row.setBackgroundDrawable(rowBg);
            }
            table.addView(row, tableLayout);
        }
    }
}
