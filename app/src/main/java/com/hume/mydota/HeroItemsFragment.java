package com.hume.mydota;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hume.mydota.view.SimpleGridView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**出装推荐界面
 * Created by tcp on 2014/12/30.
 */
public class HeroItemsFragment extends FragmentActivity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_heroitems);

        ArrayList<HeroItem> bundlelist = (ArrayList<HeroItem>)this.getIntent().getSerializableExtra("heroitem");//获取数据
        HeroItem herolist = bundlelist.get(0);//获取英雄数据

        HeroDetailItem herodata = null;
        try {
            herodata = DataManager.getHeroDetailItem(HeroItemsFragment.this,herolist.keyName);//获取详细信息
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

        // 出装推荐视图绑定
        bindItembuildsItems(view, herodata, "Starting",R.id.llayout_hero_itembuilds_starting,R.id.grid_hero_itembuilds_starting);
        bindItembuildsItems(view, herodata, "Early",R.id.llayout_hero_itembuilds_early,R.id.grid_hero_itembuilds_early);
        bindItembuildsItems(view, herodata, "Core",R.id.llayout_hero_itembuilds_core,R.id.grid_hero_itembuilds_core);
        bindItembuildsItems(view, herodata, "Luxury",R.id.llayout_hero_itembuilds_luxury,R.id.grid_hero_itembuilds_luxury);
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
     * 绑定视图——推荐出装
     * @param cView
     * @param cItem
     * @param cItembuildsKey
     * @param layoutResId
     * @param itemsGridResId
     */
    private void bindItembuildsItems(View cView, HeroDetailItem cItem,String cItembuildsKey,int layoutResId, int itemsGridResId) {
        if (cItem.itembuilds == null || cItem.itembuilds.size() <= 0|| TextUtils.isEmpty(cItembuildsKey))
            return;
        final String[] cItemsb = cItem.itembuilds.get(cItembuildsKey);//获取推荐物品名称_keyname
        if (cItemsb == null || cItemsb.length <= 0)
            return;

        final SimpleGridView gridview = (SimpleGridView)cView.findViewById(itemsGridResId);
        ArrayList<HashMap<String,Object>> al = new ArrayList<>();
        DataManager dataManager = new DataManager();
        for(int i=0; i<cItemsb.length; ++i){
            HashMap<String,Object>item = new HashMap<>();
            ItemsItem itemsItem = null;
            try {
                itemsItem = dataManager.getItemsItem(HeroItemsFragment.this,cItemsb[i]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String imagevalue = Utils.getItemsImageUri(cItemsb[i]);
            Bitmap bitmap = getImageFromAssetsFile(imagevalue);
            item.put("image", bitmap);
            item.put("name",itemsItem.dname_l);//物品名称
            item.put("cost",String.valueOf(itemsItem.cost));//物品金钱
            al.add(item);
        }

        SimpleAdapter simpleadpter = new SimpleAdapter(this,al,R.layout.fragment_itemsdetail_components_grid_item,
                new String[]{"image","name","cost"},new int[]{R.id.image_items,R.id.text_items_name,R.id.text_items_cost});
        simpleadpter.setViewBinder(new MyViewBinder());/*绑定外部资源*/
        gridview.setAdapter(simpleadpter);
//        gridview.setOnItemClickListener(HeroItemsFragment.this);//设置监听
        cView.findViewById(layoutResId).setVisibility(View.VISIBLE);
    }

    class MyViewBinder implements SimpleAdapter.ViewBinder
    {
        /**
         * view：要板顶数据的视图
         * data：要绑定到视图的数据
         * textRepresentation：一个表示所支持数据的安全的字符串，结果是data.toString()或空字符串，但不能是Null
         * 返回值：如果数据绑定到视图返回真，否则返回假
         */
        @Override
        public boolean setViewValue(View view, Object data,String textRepresentation) {
            if((view instanceof ImageView)&(data instanceof Bitmap))
            {
                ImageView iv = (ImageView)view;
                Bitmap bmp = (Bitmap)data;
                iv.setImageBitmap(bmp);
                return true;
            }
            return false;
        }
    }
}
