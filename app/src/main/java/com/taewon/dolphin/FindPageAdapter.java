package com.taewon.dolphin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FindPageAdapter extends FragmentStatePagerAdapter {

    FindIdFragment fr1;
    FindPwFragment fr2;
    public FindPageAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fr1 = new FindIdFragment();
        fr2 = new FindPwFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return fr1;
        }
        else
        {
            return fr2;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
        {
            return "아이디 찾기";
        }
        else
        {
            return "비밀번호 찾기";
        }
    }
}
