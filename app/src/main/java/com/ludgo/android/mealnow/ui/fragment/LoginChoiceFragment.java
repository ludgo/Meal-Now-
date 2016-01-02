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

import com.google.android.gms.common.SignInButton;
import com.ludgo.android.mealnow.R;
import com.ludgo.android.mealnow.ui.activity.LoginActivity;
import com.ludgo.android.mealnow.util.Constants;

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

        // Customise Google sign in button
        SignInButton googleButton = (SignInButton) rootView.findViewById(R.id.button_google);
        googleButton.setColorScheme(SignInButton.COLOR_LIGHT);
        googleButton.setScopes(((LoginActivity) getActivity()).getGoogleSignInOptions().getScopeArray());
        for (int i = 0; i < googleButton.getChildCount(); i++) {
            View v = googleButton.getChildAt(i);
            if (googleButton.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(R.string.google_sign_in);
            }
        }
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).signIn(Constants.AUTH_PROVIDER_GOOGLE);
            }
        });

        return rootView;
    }
}
