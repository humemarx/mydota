package com.hume.mydota;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 英雄列表
 * Created by tcp on 2014/12/26.
 */
public class herolist extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_herodata);
        GridView herogv = (GridView)findViewById(R.id.herodata_grid);

        ArrayList<HashMap<String,Object>> al = new ArrayList<>();

        for(int i=0; i<50; ++i){
            HashMap<String,Object>item = new HashMap<>();
            item.put("image",R.drawable.ic_launcher);
            item.put("text","Dota英雄");
            al.add(item);
        }

        SimpleAdapter simpleadpter = new SimpleAdapter(this,al,R.layout.fragment_itemsdata_grid_item,
                new String[]{"image","text"},new int[]{R.id.image_hero,R.id.text_hero_name});
        herogv.setAdapter(simpleadpter);/*设置适配器*/

        /*设置监听*/
        herogv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(herolist.this,"Dota英雄",Toast.LENGTH_LONG);
            }
        });
    }
}
