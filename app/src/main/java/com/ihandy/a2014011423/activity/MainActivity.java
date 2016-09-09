package com.ihandy.a2014011423.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ihandy.a2014011423.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

//import android.support.design.widget.;

public class MainActivity extends AppCompatActivity {
//    private static Map<String, String> watched_categories;
//    private static Map<String, String> unwatched_categories;
    static final int CATEGORYMANAGEMENT = 1;
    private DrawerLayout mDrawerLayout;
    private FragmentTab fragmentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init ImageLoader
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainActivity.this));

        /// set up toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        /// To enable the Up button for an activity that has a parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup navigation drawer layout and navigation view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        // Setup click events on the Navigation view items
        mNavigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    mDrawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.nav_item_favorites:
                            Intent intent0 = new Intent(MainActivity.this, FavoritesActivity.class);
                            startActivity(intent0);
                            break;
                        case R.id.nav_item_category_management:
                            Intent intent1 = new Intent(MainActivity.this, CategorySettingActivity.class);
                            startActivity(intent1);
                            break;
                        case R.id.nav_item_about_me:
                            Intent aboutMeIntent = new Intent(MainActivity.this, AboutActivity.class);
                            startActivity(aboutMeIntent);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        /** Setup toggle for the toolbars
         */
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // check network and handle offline situation
        fragmentTab = new FragmentTab();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container_body, fragmentTab).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentTab = new FragmentTab();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container_body, fragmentTab).commit();
    }
}
