package com.hume.mydota;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


/**
 * Created by tcp on 2014/12/29.
 */
public class HeroIntroduceFragment extends FragmentActivity {
    private static final String TAG = "HeroIntroduce";
    /**
     * 英雄名称 Intent 参数
     */
    public final static String KEY_HERO_DETAIL_KEY_NAME = "KEY_HERO_DETAIL_KEY_NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        Utils.fillFragment(this, HeroDetailFragment.newInstance(this.getIntent().getStringExtra(KEY_HERO_DETAIL_KEY_NAME)));
    }

    public static class HeroDetailFragment extends Fragment{
        private HeroDetailItem mHeroDetailItem;
        private MenuItem mMenuCheckAddCollection;

        /**
         *
         * @param hero_keyName
         * @return
         */
        static HeroDetailFragment newInstance(String hero_keyName) {
            final HeroDetailFragment f = new HeroDetailFragment();
            final Bundle b = new Bundle();
            b.putString(KEY_HERO_DETAIL_KEY_NAME, hero_keyName);
            f.setArguments(b);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            final String hero_keyName = this.getArguments().getString(KEY_HERO_DETAIL_KEY_NAME);
            Log.v(TAG, "arg.hero_keyName=" + hero_keyName);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_herodetail, container, false);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);

            inflater.inflate(R.menu.fragment_herodetail, menu);
        }

        @Override
        public void onPrepareOptionsMenu(Menu menu) {
            super.onPrepareOptionsMenu(menu);

            // ----加收藏按钮---
            final MenuItem check = menu.findItem(R.id.menu_check_addcollection);
            mMenuCheckAddCollection = check;
            tryFillMenuCheckAddCollection();
        }

        /**
         * fill MenuItem Check AddCollection
         */
        private void tryFillMenuCheckAddCollection() {
            Log.d(TAG, String.format("mMenuCheckAddCollection=%s,mHeroDetailItem=%s",
                    mMenuCheckAddCollection, mHeroDetailItem));

            if (mMenuCheckAddCollection == null || mHeroDetailItem == null) {
                return;
            }

            final MenuItem check = mMenuCheckAddCollection;
            check.setChecked(mHeroDetailItem.hasFavorite == 1);
            Utils.configureStarredMenuItem(check);
            check.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final boolean isChecked = !item.isChecked();
                    final HeroDetailItem hero = mHeroDetailItem;
                    item.setChecked(isChecked);

                    Utils.configureStarredMenuItem(item);
                    hero.hasFavorite = isChecked ? 1 : 0;
                    if (isChecked) {
                        final FavoriteItem c = new FavoriteItem();
                        c.keyName = hero.keyName;
                        c.type = FavoriteItem.KEY_TYPE_HERO;
                        DBAdapter.getInstance().addFavorite(c);
                    } else {
                        DBAdapter.getInstance().deleteFavorite(hero.keyName);
                    }
                    return true;
                }
            });
        }

    }
}
