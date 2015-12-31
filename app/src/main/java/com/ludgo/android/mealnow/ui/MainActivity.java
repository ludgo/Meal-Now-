package com.ludgo.android.mealnow.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ludgo.android.mealnow.MealNowApplication;
import com.ludgo.android.mealnow.R;
import com.ludgo.android.mealnow.service.PublicOffersService;

public class MainActivity extends AppCompatActivity {

    private PublicOffersReceiver mPublicOffersReceiver;
    // Maintain reference to living fragments to be able to access them later
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Provide 2 tabs
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Keep reference to main Activity context
        MealNowApplication app = (MealNowApplication) this.getApplicationContext();
        app.setMainActivity(this);

        mPublicOffersReceiver = new PublicOffersReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mPublicOffersReceiver,
                new IntentFilter(PublicOffersService.PUBLIC_OFFERS_ACTION));

    }
    @Override
    protected void onPause() {
        unregisterReceiver(mPublicOffersReceiver);
        super.onPause();
    }

    /**
     * A class that extends FragmentStatePagerAdapter to save fragments state
     */
    public class TabAdapter extends FragmentStatePagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Use positions to set different fragments depending on position in ViewPager
         */
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new PublicTabFragment();
                    break;
                case 1:
                    fragment = MeTabFragment.newInstance();
                    break;
                default:
                    fragment = new PublicTabFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 2;
        }

        /**
         * Set string resources as titles for each fragment by it's position
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_public);
                case 1:
                    return getString(R.string.tab_me);
                default:
                    return getString(R.string.tab_public);
            }
        }
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    /**
     * A listener to custom action broadcasts,
     * informs UI components that app's database was updated
     */
    public static class PublicOffersReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            MealNowApplication app = (MealNowApplication) context.getApplicationContext();
            MainActivity main = app.getMainActivity();
            PublicTabFragment fragment = (PublicTabFragment) main.getRegisteredFragment(0);

            if (fragment != null) {
                fragment.swapAdapter();
            }
        }
    }
}
