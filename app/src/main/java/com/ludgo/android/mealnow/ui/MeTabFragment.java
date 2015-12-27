package com.ludgo.android.mealnow.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ludgo.android.mealnow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeTabFragment extends Fragment {


    public MeTabFragment() {
        // Required empty public constructor
    }

    public static MeTabFragment newInstance() {
        return new MeTabFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me_tab, container, false);
    }

}
