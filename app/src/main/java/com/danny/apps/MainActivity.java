package com.danny.apps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.danny.apps.materialtests.R;
import com.swapi.models.People;
import com.swapi.sw.StarWarsApi;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private Toolbar mToolbar;
    private LeftNavigationDrawerFragment mLeftNavigationDrawerFragment;
    private RightNavigationDrawerFragment rNavigationDrawerFragment;

    private ImageButton rMenu;
    private ImageButton lMenu;
    private Button male;
    private Button female;
    private View mUnderBar;
    private View fUnderBar;

    public ArrayList<People> peopleArrayList;

    private FragmentManager manager;

    private MaleFragment mFragment;
    private FemaleFragment fFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StarWarsApi.init();

        setContentView(R.layout.activity_main_topdrawer);
        mToolbar = new Toolbar(this);

        mLeftNavigationDrawerFragment = (LeftNavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mLeftNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        rNavigationDrawerFragment = (RightNavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer_right);
        rNavigationDrawerFragment.setup(R.id.fragment_drawer_right, (DrawerLayout) findViewById(R.id.drawer), mToolbar);



        manager = getSupportFragmentManager();
        mFragment = new MaleFragment();
        fFragment = new FemaleFragment();
        manager.beginTransaction().replace(R.id.container,mFragment).commit();

        rMenu = (ImageButton) findViewById(R.id.btn_right_menu);
        lMenu = (ImageButton) findViewById(R.id.btn_left_menu);
        male = (Button) findViewById(R.id.btn_male);
        female = (Button) findViewById(R.id.btn_female);
        mUnderBar = findViewById(R.id.under_line_m);
        fUnderBar = findViewById(R.id.under_line_f);

        rMenu.setOnClickListener(this);
        lMenu.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        peopleArrayList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String home) {
        mFragment.getFilteredPeoples(home);
        fFragment.getFilteredPeoples(home);
    }

    @Override
    public void onBackPressed() {
        if (mLeftNavigationDrawerFragment.isDrawerOpen())
            mLeftNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right_menu:
                rNavigationDrawerFragment.openDrawer();
                break;
            case R.id.btn_left_menu:
                mLeftNavigationDrawerFragment.openDrawer();
                break;
            case R.id.btn_male:
//                mFragment = new MaleFragment();
                manager.beginTransaction().remove(mFragment).commit();
                openFragment(mFragment);
//                for (Fragment f : manager.getFragments()) {
//                    manager.beginTransaction().remove(f).commit();
//                }
//                manager.beginTransaction().replace(R.id.container,mFragment).commit();
                mUnderBar.setVisibility(View.VISIBLE);
                fUnderBar.setVisibility(View.GONE);
                break;
            case R.id.btn_female:
//                fFragment = new FemaleFragment();
                manager.beginTransaction().remove(fFragment).commit();
                openFragment(fFragment);
//                for (Fragment f : manager.getFragments()) {
//                    manager.beginTransaction().remove(f).commit();
//                }
//                manager.beginTransaction().replace(R.id.container,fFragment).commit();
                mUnderBar.setVisibility(View.GONE);
                fUnderBar.setVisibility(View.VISIBLE);
                break;
        }
    }

//    public void setFilterPeoples(String name) {
//        ArrayList<People> peoples = new ArrayList<>();
//        for (People p: peopleArrayList) {
//            if (name.equalsIgnoreCase(p.homeWorldUrl)) peoples.add(p);
//        }
//        mFragment.setPeoples(peoples);
//        fFragment.setPeoples(peoples);
//        rNavigationDrawerFragment.closeDrawer();
//    }

    @Override
    public void onBackStackChanged() {

    }

    public void openFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getName();
        FragmentManager fragmentManager= (this)
                .getSupportFragmentManager();

        boolean fragmentPopped = fragmentManager
                .popBackStackImmediate(fragmentTag , 0);

        if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) {

            FragmentTransaction ftx = fragmentManager.beginTransaction();
            ftx.addToBackStack(fragment.getClass().getSimpleName());
            ftx.add(R.id.container, fragment);
            ftx.commit();
        }
    }
}
