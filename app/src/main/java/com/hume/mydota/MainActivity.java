package com.hume.mydota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    TabsAdapter mTabsAdapter;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = this.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this,mViewPager);

        mTabsAdapter.addTab(actionBar.newTab().setText("英雄"),HeroListFragment.class, null);
        //mTabsAdapter.addTab(actionBar.newTab().setText("物品"),ItemsListFragment.class, null);
        //mTabsAdapter.addTab(actionBar.newTab().setText("收藏"),FavoriteListFragment.class, null);

        if(savedInstanceState!=null){
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab",0));
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                this.startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
        boolean instanceStateSaved = true;
    }



    /**
     *
     * @author tcp
     *
     */
    public static class TabsAdapter extends AbstractTabsAdapter {

        public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
            super(activity, pager);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }
}
