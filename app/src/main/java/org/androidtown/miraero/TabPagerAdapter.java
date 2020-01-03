package org.androidtown.miraero;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    long item_id;

    public TabPagerAdapter(FragmentManager fm, long item_id) {
        super(fm);
        this.item_id = item_id;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
                return itemDetailFragment;
            case 1:
                ItemReviewFragment itemReviewFragment = new ItemReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("item_id", item_id);
                itemReviewFragment.setArguments(bundle);
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
