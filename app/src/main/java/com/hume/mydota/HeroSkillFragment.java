package com.hume.mydota;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.hume.mydota.view.SimpleGridView;
import com.hume.mydota.view.SimpleListView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by tcp on 2014/12/30.
 */
public class HeroSkillFragment extends FragmentActivity {
    private String hero_keyname;
    private HeroDetailItem herodata = null;
    private HeroItem herolist = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_heroskill);
        hero_keyname = this.getIntent().getStringExtra("heroitem");
        try {
            herodata = DataManager.getHeroDetailItem(this,hero_keyname);//获取详细信息
            herolist = DataManager.getHeroItem(this,hero_keyname);//获取基本信息
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        // 绑定视图——技能
        if (herodata.abilities != null && herodata.abilities.size() > 0) {
            final HeroAbilitiesAdapter adapter = new HeroAbilitiesAdapter(HeroSkillFragment.this, herodata.abilities);
            final SimpleListView list = Utils.findById(HeroSkillFragment.this, R.id.list_hero_abilities);
            list.setAdapter(adapter);
            // list.setOnItemClickListener(this);
        }
        else {
            findViewById(R.id.llayout_hero_abilities).setVisibility(View.GONE);
        }

        // 绑定视图——技能加点
        if (herodata.skillup != null && herodata.skillup.size() > 0) {
            final HeroSkillupAdapter adapter = new HeroSkillupAdapter(HeroSkillFragment.this, herodata.skillup);
            final SimpleListView list = Utils.findById(HeroSkillFragment.this, R.id.list_hero_skillup);
            list.setAdapter(adapter);
            // list.setOnItemClickListener(this);
        }
        else {
            findViewById(R.id.llayout_hero_skillup).setVisibility(View.GONE);
        }
    }


    /**
     *
     */
    private final Html.ImageGetter mImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            final Resources res = HeroSkillFragment.this.getResources();
            Drawable drawable = null;
            if (source.equals("mana"))
                drawable = res.getDrawable(R.drawable.mana);
            else if (source.equals("cooldown"))
                drawable = res.getDrawable(R.drawable.cooldown);

            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
                return drawable;
            } else {
                return null;
            }
        }
    };
    /**
     * 技能 List Adapter
     */
    private final class HeroAbilitiesAdapter extends BaseAdapter {
        private final class ViewHolder {
            public ImageView image;
            public TextView dname;
            public TextView affects;
            public TextView attrib;
            public TextView desc;
            public TextView cmb;
            public TextView dmg;
            public TextView notes;
            public TextView lore;
            public ImageView image_skill;
            public TextView text_skill;
            public VideoView video_skill;
        }

        private final LayoutInflater mInflater;
        private final List<AbilityItem> mAbilities;
        public HeroAbilitiesAdapter(Context context,List<AbilityItem> abilities) {
            super();
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mAbilities = abilities;
        }

        @Override
        public int getCount() {
            return mAbilities.size();
        }
        @Override
        public Object getItem(int position) {
            return mAbilities.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;

            final ViewHolder holder;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.fragment_herodetail_abilities_list_item,parent, false);

                holder = new ViewHolder();
                holder.affects = Utils.findById(view, R.id.text_abilities_affects);
                holder.attrib = Utils.findById(view, R.id.text_abilities_attrib);
                holder.dname = Utils.findById(view, R.id.text_abilities_dname);
                holder.cmb = Utils.findById(view, R.id.text_abilities_cmb);
                holder.desc = Utils.findById(view, R.id.text_abilities_desc);
                holder.dmg = Utils.findById(view, R.id.text_abilities_dmg);
                holder.notes = Utils.findById(view, R.id.text_abilities_notes);
                holder.lore = Utils.findById(view, R.id.text_abilities_lore);
                holder.image = Utils.findById(view, R.id.image_abilities);

                holder.image_skill = Utils.findById(view,R.id.image_skill_video);
                holder.video_skill = Utils.findById(view,R.id.skill_video);
                holder.text_skill = Utils.findById(view,R.id.text_skill_video);

                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();

            final AbilityItem item = (AbilityItem) getItem(position);
            Bitmap bitmap = getImageFromAssetsFile(Utils.getAbilitiesImageUri(item.keyName));
            holder.image.setImageBitmap(bitmap);
            holder.dname.setText(item.dname);
            Utils.bindHtmlTextView(holder.affects, item.affects);
            Utils.bindHtmlTextView(holder.attrib, item.attrib);
            Utils.bindHtmlTextView(holder.cmb, item.cmb, mImageGetter);
            Utils.bindHtmlTextView(holder.dmg, item.dmg);
            Utils.bindHtmlTextView(holder.desc, item.desc);
            Utils.bindHtmlTextView(holder.notes, item.notes);
            Utils.bindHtmlTextView(holder.lore, item.lore);
            holder.image_skill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String skill_video = herolist.skill_video[position];
                    Log.v("skill",skill_video);
                    Log.v("skill",String.valueOf(position));
                    if(skill_video.indexOf("http")!=-1){
                        holder.video_skill.setMediaController(new MediaController(HeroSkillFragment.this));
                        holder.video_skill.setVideoURI(Uri.parse(skill_video));
                        holder.image_skill.setVisibility(View.GONE);//设置消失
                        holder.video_skill.setVisibility(View.VISIBLE);//设置可见
                        holder.video_skill.start();
                        holder.video_skill.requestFocus();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "暂无视频", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });

            holder.text_skill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.video_skill.setVisibility(View.GONE);
                    holder.image_skill.setVisibility(View.VISIBLE);
                }
            });
            return view;
        }
    }

    /**
     * 技能加点 List Adapter
     */
    private final class HeroSkillupAdapter extends BaseAdapter {
        private final class ViewHolder {
            public TextView groupName;
            public TextView desc;
            public SimpleGridView abilityKeys;
        }

        private final LayoutInflater mInflater;
        private final List<HeroSkillupItem> mAbilities;
        private final Context mContext;

        public HeroSkillupAdapter(Context context, List<HeroSkillupItem> abilities) {
            super();

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mAbilities = abilities;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mAbilities.size();
        }
        @Override
        public Object getItem(int position) {
            return mAbilities.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            final ViewHolder holder;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.fragment_herodetail_skillup_list_item,parent, false);

                holder = new ViewHolder();
                holder.groupName = Utils.findById(view, R.id.text_skillup_groupName);
                holder.desc = Utils.findById(view, R.id.text_skillup_desc);
                holder.abilityKeys = Utils.findById(view, R.id.grid_skillup_abilitys);

                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();

            final HeroSkillupItem item = (HeroSkillupItem) getItem(position);
            holder.groupName.setText(item.groupName);
            Utils.bindHtmlTextView(holder.desc, item.desc);
            if (item.abilityKeys != null && holder.abilityKeys != null) {
                holder.abilityKeys.setAdapter(new HeroSkillupAbilityKeysAdapter(mContext, item.abilityKeys));
            }
            return view;
        }
    }

    /**
     * 技能加点 List Adapter
     */
    private final class HeroSkillupAbilityKeysAdapter extends BaseAdapter {private final class ViewHolder {
            public ImageView image;
        }

        private final LayoutInflater mInflater;
        private final String[] mAbilities;

        public HeroSkillupAbilityKeysAdapter(Context context, String[] abilities) {
            super();

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mAbilities = abilities;
        }

        @Override
        public int getCount() {
            return mAbilities.length;
        }
        @Override
        public Object getItem(int position) {
            return mAbilities[position];
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            final ViewHolder holder;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.fragment_herodetail_skillup_ability_item,parent, false);
                holder = new ViewHolder();
                holder.image = Utils.findById(view, R.id.image_skillup_ability);
                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();
            Bitmap bitmap = getImageFromAssetsFile(Utils.getAbilitiesImageUri((String) getItem(position)));
            holder.image.setImageBitmap(bitmap);
//            ImageLoader.getInstance().displayImage(Utils.getAbilitiesImageUri((String) getItem(position)),holder.image, mImageLoadOptions);
            return view;
        }
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
}
