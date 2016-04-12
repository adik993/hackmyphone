package com.adrian.hackmyphone;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.adrian.hackmyphone.fragments.BluetoothFragment;
import com.adrian.hackmyphone.fragments.SensorsFragment;
import com.adrian.hackmyphone.fragments.TelephonyFragment;
import com.adrian.hackmyphone.fragments.WifiFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG=getClass().getName();


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if(savedInstanceState==null) {
            replaceFragment(SensorsFragment.newInstance());
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sensors && !fragmentExists(SensorsFragment.class)) {
            replaceFragment(SensorsFragment.newInstance());
        } else if(id == R.id.telephony && !fragmentExists(TelephonyFragment.class)) {
            replaceFragment(TelephonyFragment.newInstance());
        } else if(id == R.id.wifi && !fragmentExists(WifiFragment.class)) {
            replaceFragment(WifiFragment.newInstance());
        } else if(id == R.id.bluetooth && !fragmentExists(BluetoothFragment.class)) {
            replaceFragment(BluetoothFragment.newInstance());
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean fragmentExists(Class<? extends Fragment> clazz) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment != null && clazz.isInstance(fragment);
    }

    public void replaceFragment(Fragment newFragment) {
        getFragmentManager().popBackStackImmediate(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, TAG);
        transaction.commit();
    }
}
