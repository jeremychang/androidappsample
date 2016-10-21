package com.example.jeremy.appsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.jeremy.appsample.fragments.Test1Fragment;
import com.example.jeremy.appsample.fragments.Test2Fragment;

// If you're using the v7 appcompat library, your activity should
// instead extend AppCompatActivity, which is a subclass of FragmentActivity.
public class FragmentRootActivity extends FragmentActivity {

    public final static String TAG = "FragmentRootActivity";
    public static final int FRAGMENT_TEST1 = 0;
    public static final int FRAGMENT_TEST2 = 1;
    public static final int FRAGMENT_COUNT = 2;
    int mCurrentFragmentIndex = -1;


    Test1Fragment getTest1Fragment() {
        return (Test1Fragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_test1);
    }

    Test2Fragment getTest2Fragment() {
        return (Test2Fragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_test2);
    }

    Fragment getFragment(int index) {
        switch(index) {
            case FRAGMENT_TEST1:
                return getTest1Fragment();
            case FRAGMENT_TEST2:
                return getTest2Fragment();
        }
        return null;
    }

    void showFragment(int index) {
        if (mCurrentFragmentIndex == index)
            return;
        // hide current page first
        Fragment fragment = getFragment(mCurrentFragmentIndex);
        if (fragment != null) {
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.hide(fragment).commitAllowingStateLoss();
        }
        // show new page
        mCurrentFragmentIndex = index;
        fragment = getFragment(index);
        if (fragment == null) {
            return;
        }

        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.show(fragment).commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction ft;
            Fragment fragment;

            for (int i = 0; i < FRAGMENT_COUNT; i++) {
                fragment = getFragment(i);
                if (fragment == null) continue;
                ft = this.getSupportFragmentManager().beginTransaction();
                ft.hide(fragment).commitAllowingStateLoss();
            }
            showFragment(FRAGMENT_TEST2);
        }
    }

}
