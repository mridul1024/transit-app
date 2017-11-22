package com.example.gaijinsmash.transitapp.fragment_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends  SmartFragmentStatePagerAdapter {

    private List<Fragment> mFragments = null;
    private Fragment mCurrentFragment;

    public FragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragments = new ArrayList<Fragment>();
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
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if(getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    // Our custom method that populates this Adapter with Fragments
    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
