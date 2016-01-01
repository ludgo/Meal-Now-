package com.ludgo.android.mealnow.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ludgo.android.mealnow.R;
import com.ludgo.android.mealnow.ui.activity.LoginActivity;

public class PromptLoginFragment extends Fragment {

    private Typeface indieFlower;


    public PromptLoginFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_prompt_login, container, false);

        Button loginButton = (Button) rootView.findViewById(R.id.button_login);
        loginButton.setTypeface(indieFlower);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
