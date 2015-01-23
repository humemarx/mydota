package com.hume.mydota;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import java.util.List;

/**
 * 英雄数据列表
 * Created by tcp on 2014/12/25.
 */
public class HeroListFragment extends Fragment
        implements OnQueryTextListener, OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<HeroItem>>{


    private static final int KEY_MENU_HERO_ROLE = 0;
    private static final int KEY_MENU_HERO_TYPE = 1;
    private static final int KEY_MENU_HERO_ATTACK = 2;
    private static final int KEY_MENU_HERO_FACTIONS = 3;
    private static final int KEY_MENU_HERO_STATSALL = 4;

    private String[] menu_hero_query_keys = new String[5];
    private static final String KEY_MENU_HERO_QUERY_ALL = "all";
    private static final String KEY_MENU_HERO_QUERY_DEFAULT = "default";
    private static final String KEY_STATE_MENU_HERO_QUERY_KEYS = "KEY_STATE_MENU_HERO_QUERY_KEYS";
    private HeroListAdapter mAdapter = null;
    private GridView mGridView = null;

    public HeroListFragment() {
        super();
        menu_hero_query_keys[KEY_MENU_HERO_ROLE] = KEY_MENU_HERO_QUERY_ALL;
        menu_hero_query_keys[KEY_MENU_HERO_TYPE] = KEY_MENU_HERO_QUERY_ALL;
        menu_hero_query_keys[KEY_MENU_HERO_ATTACK] = KEY_MENU_HERO_QUERY_ALL;
        menu_hero_query_keys[KEY_MENU_HERO_FACTIONS] = KEY_MENU_HERO_QUERY_ALL;
        menu_hero_query_keys[KEY_MENU_HERO_STATSALL] = KEY_MENU_HERO_QUERY_DEFAULT;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        android.content.Loader<Object> cLoader = getLoaderManager().getLoader(0);
        if (cLoader != null && savedInstanceState != null) {
            menu_hero_query_keys = savedInstanceState.getStringArray(KEY_STATE_MENU_HERO_QUERY_KEYS);
        }
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_herodata, container,false);

        mAdapter = new HeroListAdapter(this.getActivity());
        mAdapter.setFilterAccepter(mHeroListFilterAccepter);

        mGridView = ((GridView) v.findViewById(R.id.herodata_grid));
        mGridView.setAdapter(mAdapter);
        mGridView.setTextFilterEnabled(true);
        mGridView.setOnItemClickListener(this);
        return v;
    }

    /**
     * 物品网格项 点击动作
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Utils.startHeroDetailActivity(this.getActivity(),
                (HeroItem) parent.getItemAtPosition(position));
    }

    @Override
    public android.content.Loader<List<HeroItem>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<HeroItem>> loader, List<HeroItem> data) {
        mAdapter.setData(data);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }


    @Override
    public void onLoaderReset(android.content.Loader<List<HeroItem>> arg0) {
        mAdapter.setData(null);
    }

    /**
     * HeroSearchView onQueryTextSubmit
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * HeroSearchView onQueryTextChange
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mGridView.clearTextFilter();
        } else {
            mGridView.setFilterText(newText);
        }
        return false;
    }
    /**
     * HeroListAdapter item filter accepter
     */
    private final AbstractArrayAdapter.ArrayFilterAccepter<HeroItem> mHeroListFilterAccepter = new AbstractArrayAdapter.ArrayFilterAccepter<HeroItem>() {
        /**
         * 搜索英雄名称 Accepter
         *
         * @param cDataItem
         * @param constraint
         * @return
         */
        @SuppressLint("DefaultLocale")
        @Override
        public boolean Accept(HeroItem cDataItem, CharSequence constraint) {
            if (cDataItem == null || TextUtils.isEmpty(cDataItem.name_l)) {
                return false;
            }
            if (TextUtils.isEmpty(constraint)) {
                return true;
            }

            final String prefixString = constraint.toString().toLowerCase();
            if (cDataItem.name_l.indexOf(prefixString) > -1) {
                return true;
            }

            if (!TextUtils.isEmpty(cDataItem.name)
                    && cDataItem.name.toLowerCase().startsWith(prefixString)) {
                return true;
            }

            if (cDataItem.nickname_l != null && cDataItem.nickname_l.length > 0&& Utils.exists(cDataItem.nickname_l, prefixString, true)) {
                return true;
            }
            return false;
        }
    };

    /**
     *
     */
    final class HeroListAdapter extends AbstractArrayAdapter<HeroItem> {
        private final class ViewHolder {
            public TextView text;
            public ImageView image;
        }

        private final LayoutInflater mInflater;

        public HeroListAdapter(Context context) {
            super(context);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            setNotifyOnChange(true);
        }

        public void setData(List<HeroItem> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            final ViewHolder holder;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.fragment_herodata_grid_item,
                        parent, false);

                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text_hero_name);
                holder.image = (ImageView) view.findViewById(R.id.image_hero);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final HeroItem item = getItem(position);
            holder.image.setImageURI(Uri.parse(Utils.getHeroImageUri(item.keyName)));
            //ImageLoader.getInstance().displayImage(Utils.getHeroImageUri(item.keyName),holder.image, mImageLoadOptions);
            holder.text.setText(item.name_l);

            return view;
        }
    }

}
