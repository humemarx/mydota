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
 * Created by tcp on 2014/12/29.
 */
public class MainItemlist extends Activity implements AdapterView.OnItemClickListener{

    private DataManager dataManager = new DataManager();
    private static List<ItemsItem> mItemList = new ArrayList<ItemsItem>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_itemsdata);
        try {
            mItemList = dataManager.getItemsList(MainItemlist.this);/*获取全部物品信息*/
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ItemsImagesAdapter adapter = new ItemsImagesAdapter(this, mItemList);
        final GridView grid = (GridView) findViewById(R.id.itemsdata_grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);//设置监听
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainItemlist.this,ItemsDetailActivity.class);//跳转
        intent.putExtra("ItemsItem",mItemList.get(position).keyName);//传递数据
        startActivity(intent);//启动新的活动
    }
}
