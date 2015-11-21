package org.sssta.qaq;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.sssta.qaq.fragment.BasePageFragment;
import org.sssta.qaq.fragment.CollectionFragment;
import org.sssta.qaq.fragment.GalleryFragment;
import org.sssta.qaq.fragment.MainFragment;
import org.sssta.qaq.widget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTabLayout;

    private List<BasePageFragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.view_main_pager);
        mViewPager.setOffscreenPageLimit(3);

        mFragments.add(new MainFragment());
        mFragments.add(new GalleryFragment());
        mFragments.add(new CollectionFragment());

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mTabLayout.setTabsFromPagerAdapter(adapter);
        final TabLayout.TabLayoutOnPageChangeListener listener =
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager.addOnPageChangeListener(listener);

        mViewPager.setAdapter(adapter);

    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return  mFragments.get(position).getTitle();
        }
    }
}
