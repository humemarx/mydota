package com.hume.mydota;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hume.mydota.view.SimpleGridView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**出装推荐界面
 * Created by tcp on 2014/12/30.
 */
public class HeroItemsFragment extends FragmentActivity implements SimpleGridView.OnItemClickListener {
    private String hero_keyname;
    private HeroDetailItem herodata = null;
    private HeroItem herolist = null;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_heroitems);
        hero_keyname = this.getIntent().getStringExtra("heroitem");
        try {
            herodata = DataManager.getHeroDetailItem(this,hero_keyname);//获取详细信息
            herolist = DataManager.getHeroItem(this,hero_keyname);//获取基本信息
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        View view = findViewById(R.id.myhero_detail_view);

        ImageView image_hero = (ImageView)findViewById(R.id.image_hero);//英雄图像
        TextView text_hero_name_l = (TextView)findViewById(R.id.text_hero_name_l);//中文名称
        TextView text_hero_name = (TextView)findViewById(R.id.text_hero_name);//英文名称
        TextView text_hero_roles = (TextView)findViewById(R.id.text_hero_roles);//角色定位
        TextView text_hero_hp_faction_atk = (TextView)findViewById(R.id.text_hero_hp_faction_atk);//英雄类型

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

        switch (herolist.hp) {
            case "intelligence":
                hero_hp = "智力";
                break;
            case "agility":
                hero_hp = "敏捷";
                break;
            default:
                hero_hp = "力量";
                break;
        }

        if(herolist.faction.equals("radiant")){
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

        // 出装推荐视图绑定
        bindItembuildsItems(view, herodata, "Starting",R.id.llayout_hero_itembuilds_starting,R.id.grid_hero_itembuilds_starting);
        bindItembuildsItems(view, herodata, "Early",R.id.llayout_hero_itembuilds_early,R.id.grid_hero_itembuilds_early);
        bindItembuildsItems(view, herodata, "Core",R.id.llayout_hero_itembuilds_core,R.id.grid_hero_itembuilds_core);
        bindItembuildsItems(view, herodata, "Luxury",R.id.llayout_hero_itembuilds_luxury,R.id.grid_hero_itembuilds_luxury);
    }

    /**
     * 读取图像文件
     * @param fileName
     *              文件名
     * @return
     *              return bitmap
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
     * 绑定视图——推荐出装
     * @param cView
     * cview
     * @param cItem
     * cItem
     * @param cItembuildsKey
     * cItembuildsKey
     * @param layoutResId
     * layoutResId
     * @param itemsGridResId
     * itemsGridResId
     */
    private void bindItembuildsItems(View cView, HeroDetailItem cItem,String cItembuildsKey,int layoutResId, int itemsGridResId) {
        if(cItem.itembuilds != null && cItem.itembuilds.size() > 0)
        {
            final ArrayList<ItemsItem> cItembuilds = new ArrayList<>();
            final String[] cItemsb = cItem.itembuilds.get(cItembuildsKey);
            for (String aCItemsb : cItemsb) {
                ItemsItem itemsItem = null;
                try {
                    itemsItem = DataManager.getItemsItem(HeroItemsFragment.this, aCItemsb);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                cItembuilds.add(itemsItem);
            }
            final ItemsImagesAdapter adapter = new ItemsImagesAdapter(HeroItemsFragment.this, cItembuilds);
            final SimpleGridView gridview = (SimpleGridView)cView.findViewById(itemsGridResId);
            gridview.setAdapter(adapter);
            gridview.setOnItemClickListener(this);//设置监听
            cView.findViewById(layoutResId).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击网格动作
     * @param parent
     * parent
     * @param view
     * view
     * @param position
     * position
     * @param id
     * id
     */
    @Override
    public void onItemClick(ListAdapter parent, View view, int position, long id) {
        final ItemsItem cItem = (ItemsItem)parent.getItem(position);
        Intent intent = new Intent(HeroItemsFragment.this,ItemsDetailActivity.class);//跳转
        intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_KEY_NAME,cItem.keyName);//传递数据
        if (!TextUtils.isEmpty(cItem.parent_keyName)) {
            intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_PARENT_KEY_NAME,cItem.parent_keyName);//合成名称
        }
        startActivity(intent);//启动新的活动
    }
}
