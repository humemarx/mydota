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

/**物品详细
 * Created by tcp on 2015/1/8.
 */
public class ItemsDetailActivity extends FragmentActivity implements SimpleGridView.OnItemClickListener{
    private ImageView item_icon;
    private TextView item_dname_l,item_dname,item_cost,item_mc,item_cd,item_attrib,item_desc,item_notes,item_lore;
    private String item_keyname;
    private ItemsItem itemslist = null;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_itemsdetail);
        /*数据初始化*/
        item_keyname = this.getIntent().getStringExtra("ItemsItem");//获取物品数据名称
        try {
            itemslist = DataManager.getItemsItem(ItemsDetailActivity.this, item_keyname);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        item_icon = (ImageView)findViewById(R.id.image_items);//物品图标
        item_dname_l = (TextView)findViewById(R.id.text_items_dname_l);//物品名称
        item_dname = (TextView)findViewById(R.id.text_items_dname);//物品简称
        item_cost = (TextView)findViewById(R.id.text_items_cost);//物品金钱
        item_mc = (TextView)findViewById(R.id.text_items_mana);//魔法消耗
        item_cd = (TextView)findViewById(R.id.text_items_cd);//冷却时间
        item_attrib = (TextView)findViewById(R.id.text_items_attrib);//属性加成
        item_desc = (TextView)findViewById(R.id.text_items_desc);
        item_notes = (TextView)findViewById(R.id.text_items_notes);
        item_lore = (TextView)findViewById(R.id.text_items_lore);

        /*基本信息处理*/
        Bitmap bitmap = getImageFromAssetsFile(Utils.getItemsImageUri(itemslist.keyName));//获取头像
        item_icon.setImageBitmap(bitmap);//设置头像
        item_dname_l.setText(itemslist.dname_l);//设置名称
        item_dname.setText(itemslist.dname);//设置简称
        item_cost.setText(String.valueOf(itemslist.cost));//设置金钱

        /*合成卷轴处理*/
        if (itemslist.isrecipe) {
            final View layout_items_desc = findViewById(R.id.layout_items_desc);
            layout_items_desc.setVisibility(View.GONE);
            return;
        }
        item_desc.setText(itemslist.desc);
        item_attrib.setText(itemslist.attrib);
        item_notes.setText(itemslist.notes);
        item_lore.setText(itemslist.lore);
        /*魔法消耗处理-mc*/
        // mc
        if (!TextUtils.isEmpty(itemslist.mc)) {
            item_mc.setText(itemslist.mc);
        } else {
            findViewById(R.id.rlayout_items_mana).setVisibility(View.GONE);
        }
        /*冷却时间处理-cd*/
        if (itemslist.cd > 0) {
            item_cd.setText(String.valueOf(itemslist.cd));
        } else {
            findViewById(R.id.rlayout_items_cd).setVisibility(View.GONE);
        }


        /*合成所需处理*/
        if (itemslist.components != null && itemslist.components.length > 0) {
            final ItemsImagesAdapter adapter = new ItemsImagesAdapter(ItemsDetailActivity.this, itemslist.components_i);
            final SimpleGridView grid = (SimpleGridView) findViewById(R.id.grid_items_components);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(this);
            findViewById(R.id.llayout_items_components).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.llayout_items_components).setVisibility(View.GONE);
        }

        /*可以合成物品*/
        if (itemslist.tocomponents != null && itemslist.tocomponents.length > 0) {
            final ItemsImagesAdapter adapter = new ItemsImagesAdapter(ItemsDetailActivity.this, itemslist.tocomponents_i);
            final SimpleGridView grid = (SimpleGridView) findViewById(R.id.grid_items_tocomponents);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(this);//设置监听
            findViewById(R.id.llayout_items_tocomponents).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.llayout_items_tocomponents).setVisibility(View.GONE);
        }

        /*推荐使用英雄*/
        if (itemslist.toheros != null && itemslist.toheros.length > 0) {
            final HeroImagesAdapter adapter = new HeroImagesAdapter(ItemsDetailActivity.this, itemslist.toheros_i);
            final SimpleGridView grid = (SimpleGridView) findViewById(R.id.grid_items_toheros);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(this);//设置监听
            findViewById(R.id.llayout_items_toheros).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.llayout_items_toheros).setVisibility(View.GONE);
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

    /**
     *
     * @param parent
     *            The AdapterView where the click happened.
     * @param view
     *            The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter)
     * @param position
     *            The position of the view in the adapter.
     * @param id
     */
    @Override
    public void onItemClick(ListAdapter parent, View view, int position, long id) {
        Intent intent = null;
        if(parent instanceof ItemsImagesAdapter){
            final ItemsItem cItem = (ItemsItem)parent.getItem(position);
            intent = new Intent(this,ItemsDetailActivity.class);//跳转物品界面
            intent.putExtra("ItemsItem",cItem.keyName);//传递数据
        }else if (parent instanceof HeroImagesAdapter){
            final HeroItem cItem = (HeroItem)parent.getItem(position);
            intent = new Intent(this,HeroDetailActivity.class);//跳转英雄界面
            intent.putExtra("heroitem",cItem.keyName);//传递数据
        }
        startActivity(intent);//启动新的活动
    }
}
