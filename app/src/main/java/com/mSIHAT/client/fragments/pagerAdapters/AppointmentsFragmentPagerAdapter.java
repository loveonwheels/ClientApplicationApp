package com.mSIHAT.client.fragments.pagerAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mSIHAT.client.fragments.AppointmentsFragment;
import com.mSIHAT.client.fragments.MultiAppointmentsFragment;
import com.mSIHAT.client.utils.Constants;

public class AppointmentsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    public static int app_status;

    private final int PAGE_COUNT = 2;
    private Bundle bundle;

    private Context context;

    public AppointmentsFragmentPagerAdapter(FragmentManager fm, Context context, Bundle bundle){
        super(fm);
        this.context = context;
        this.bundle = bundle;
        app_status = bundle.getInt(Constants.EXTRA_APPOINTMENT_STATUS);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = AppointmentsFragment.newInstance(bundle.getInt(Constants.EXTRA_USER_ID),
                        app_status);
                break;
            case 1:
                fragment = MultiAppointmentsFragment.newInstance(bundle.getInt(Constants.EXTRA_USER_ID),
                        app_status);
                break;
            default:
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
