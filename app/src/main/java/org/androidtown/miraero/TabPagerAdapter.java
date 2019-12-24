package org.androidtown.miraero;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
                return itemDetailFragment;
            case 1:
                ItemReviewFragment itemReviewFragment = new ItemReviewFragment();
                return itemReviewFragment;
            case 2:
                ItemQNAFragment itemQNAFragment = new ItemQNAFragment();
                return itemQNAFragment;
            case 3:
                ItemOriginFragment itemOriginFragment = new ItemOriginFragment();
                return itemOriginFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
