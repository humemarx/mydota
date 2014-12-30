package com.hume.mydota;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 英雄列表
 * Created by tcp on 2014/12/26.
 */
public class MainHerolist extends Activity implements AdapterView.OnItemClickListener {

    private DataManager dataManager = new DataManager();
    private static List<HeroItem> mHeroList = new ArrayList<HeroItem>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_herodata);

        try {
            mHeroList = dataManager.getHeroList(MainHerolist.this);/*获取全部英雄信息*/
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GridView herogv = (GridView)findViewById(R.id.herodata_grid);

        ArrayList<HashMap<String,Object>> al = new ArrayList<>();
        for(int i=0; i<mHeroList.size(); ++i){
            HashMap<String,Object>item = new HashMap<>();
            String imagevalue = Utils.getHeroImageUri(mHeroList.get(i).keyName);
            Bitmap bitmap = getImageFromAssetsFile(imagevalue);
            item.put("image", bitmap);
            item.put("text",mHeroList.get(i).name_l);
            al.add(item);
        }

//        Log.v("string",Utils.getHeroImageUri(mHeroList.get(0).keyName));

        SimpleAdapter simpleadpter = new SimpleAdapter(this,al,R.layout.fragment_herodata_grid_item,
                new String[]{"image","text"},new int[]{R.id.image_hero,R.id.text_hero_name});
        simpleadpter.setViewBinder(new MyViewBinder());/*绑定外部资源*/
        herogv.setAdapter(simpleadpter);/*设置适配器*/

        /*设置监听*/
        herogv.setOnItemClickListener(this);
    }

    /**
     * 点击网格动作
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainHerolist.this,HeroDetailActivity.class);//跳转
        Bundle bundle = new Bundle();
        ArrayList<HeroItem> bundlelist = new ArrayList<HeroItem>();
        bundlelist.add(mHeroList.get(position));
        intent.putExtra("heroitem",bundlelist);//传递数据
        startActivity(intent);//启动新的活动
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
