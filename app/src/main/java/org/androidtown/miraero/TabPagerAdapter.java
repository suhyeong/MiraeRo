package org.androidtown.miraero;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int TabCount;

    public TabPagerAdapter(FragmentManager fm, int TabCount) {
        super(fm);
        this.TabCount = TabCount;
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
                ItemOriginFragment itemOriginFragment = new ItemOriginFragment();
                return itemOriginFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}
