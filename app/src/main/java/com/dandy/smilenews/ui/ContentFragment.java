package com.dandy.smilenews.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dandy.smilenews.R;
import com.dandy.smilenews.config.Config;

/**
 * Created by Dandy on 2016/10/28.
 */

public class ContentFragment extends Fragment implements Config{


    public static Fragment instance(int postion){
        ContentFragment fragment=new ContentFragment();
        Bundle bundle = new Bundle() ;
        bundle.putInt(KEY_POSTION,postion);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_content,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments() ;
        Log.e("dandy","pos "+bundle.getInt(KEY_POSTION));
    }
}
