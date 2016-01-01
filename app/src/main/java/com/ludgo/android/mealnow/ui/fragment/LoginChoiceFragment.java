package com.ludgo.android.mealnow.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ludgo.android.mealnow.R;

public class LoginChoiceFragment extends Fragment {

    private Typeface indieFlower;

    public LoginChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        indieFlower = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IndieFlower.ttf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_choice, container, false);

        ((TextView) rootView.findViewById(R.id.app_title)).setTypeface(indieFlower);
        ((Button) rootView.findViewById(R.id.button_sign_in)).setTypeface(indieFlower);
        ((Button) rootView.findViewById(R.id.button_sign_up)).setTypeface(indieFlower);

        return rootView;
    }
}
