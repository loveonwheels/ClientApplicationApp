package com.mSIHAT.client.fragments.pagerAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.AppointmentDetailsFragment;
import com.mSIHAT.client.fragments.AppointmentPatientDetailsFragment;
import com.mSIHAT.client.fragments.PractitionerDetailsFragment;
import com.mSIHAT.client.utils.Constants;

/**
 * Created by alamchristian on 2/22/16.
 */
public class AppointmentDetailFragmentsPagerAdapter extends FragmentPagerAdapter{
    private final int PAGE_COUNT = 3;
    private Bundle bundle;

    private Context context;

    private String[] titles;

    public AppointmentDetailFragmentsPagerAdapter(FragmentManager fm, Context context, Bundle bundle) {
        super(fm);
        Log.e("apiontmet2","gere22");
        this.context = context;
        this.bundle = bundle;
        titles = context.getResources().getStringArray(R.array.appointment_details_pager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:

                fragment = AppointmentDetailsFragment.newInstance(bundle.getParcelable(Constants.PARCEL_APPOINTMENT_DETAILS));
            break;
            case 1:
               fragment = AppointmentPatientDetailsFragment.newInstance(bundle.getParcelable(Constants.PARCEL_APPOINTMENT_DETAILS_PATIENT), bundle.getParcelable(Constants.PARCEL_APPOINTMENT_DETAILS_CONDITIONS));
            break;
            case 2:
                fragment = PractitionerDetailsFragment.newInstance(bundle.getParcelable(Constants.PARCEL_APPOINTMENT_DETAILS_PRACTITIONER));
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
    public CharSequence getPageTitle(int position) {
//        return titles[position];
        return null;
    }
}
