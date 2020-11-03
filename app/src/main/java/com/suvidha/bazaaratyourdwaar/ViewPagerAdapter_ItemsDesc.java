package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter_ItemsDesc extends FragmentStatePagerAdapter {
    int numberofTabs;
    private String[] tabTitles = new String[]{"Description", "Reviews"};
    public ViewPagerAdapter_ItemsDesc(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numberofTabs = behavior;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
            {
                DescriptionFragment descriptionFragment = new DescriptionFragment();
                return descriptionFragment;
            }
            case 1:
            {
                ReviewFragment reviewFragment = new ReviewFragment();
                return  reviewFragment;
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberofTabs;
    }
}
