package com.dandy.smilenews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dandy.smilenews.adapter.ViewPagerAdapter;
import com.dandy.smilenews.base.BaseActivity;
import com.dandy.smilenews.config.Config;
import com.dandy.smilenews.entity.News;
import com.dandy.smilenews.net.RetrofitUtil;
import com.dandy.smilenews.ui.ContentFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,Config {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private List<String> mTitles=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findView(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTabLayout=findView(R.id.tab_layout);
        mViewPager=findView(R.id.viewpager);

        DrawerLayout drawer = findView(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findView(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getData();
        initViews();
        mAdapter=new ViewPagerAdapter(getSupportFragmentManager(),mTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    private void getData(){
        Subscriber<News> subscriber=new Subscriber<News>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("smile","获取失败");
            }

            @Override
            public void onNext(News news) {
                Log.e("smile","获取出来的"+news.getResult().getData().size());
            }
        };
        RetrofitUtil.getInstance().getNews(subscriber);
    }

    private void initViews(){
        for (int i=0;i<ARRYTITLES.length;i++){
            mTitles.add(ARRYTITLES[i]);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findView(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findView(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}