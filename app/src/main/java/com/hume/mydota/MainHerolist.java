package com.hume.mydota;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 英雄列表
 * Created by tcp on 2014/12/26.
 */
public class MainHerolist extends Activity {

    private DataManager dataManager = new DataManager();
    private static List<HeroItem> mHeroList = new ArrayList<HeroItem>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_herodata);

        try {
            mHeroList = dataManager.getHeroList(MainHerolist.this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GridView herogv = (GridView)findViewById(R.id.herodata_grid);

        ArrayList<HashMap<String,Object>> al = new ArrayList<>();

//        for(int i=0; i<50; ++i){
//            HashMap<String,Object>item = new HashMap<>();
//            item.put("image",R.drawable.ic_launcher);
//            item.put("text","Dota英雄");
//            al.add(item);
//        }
        for(int i=0; i<mHeroList.size(); ++i){
            HashMap<String,Object>item = new HashMap<>();
//            String imagevalue = Utils.getHeroImageUri(mHeroList.get(i).keyName);
            String imagevalue = "assets/heroes_images/gyrocopter_full.jpg";
            Bitmap bitmap = getImageFromAssetsFile(imagevalue);
            item.put("image", bitmap);
            item.put("text",mHeroList.get(i).name_l);
            al.add(item);
        }

        Log.v("string",Utils.getHeroImageUri(mHeroList.get(0).keyName));

        SimpleAdapter simpleadpter = new SimpleAdapter(this,al,R.layout.fragment_herodata_grid_item,
                new String[]{"image","text"},new int[]{R.id.image_hero,R.id.text_hero_name});
        herogv.setAdapter(simpleadpter);/*设置适配器*/

        /*设置监听*/
        herogv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainHerolist.this,"Dota英雄",Toast.LENGTH_LONG);
            }
        });

    }


    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }

    private Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
