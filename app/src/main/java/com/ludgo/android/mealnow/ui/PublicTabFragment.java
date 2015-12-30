package com.ludgo.android.mealnow.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ludgo.android.mealnow.R;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

public class PublicTabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PublicTabFragment() {
        // Required empty public constructor
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_public_tab, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.public_offers);
        setRecyclerViewLayoutManager();
        mAdapter = new PublicOffersAdapter(new String[11]);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager based on immediate screen width.
     */
    public void setRecyclerViewLayoutManager() {

        if (getResources().getDisplayMetrics().widthPixels >=
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics())) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public class PublicOffersAdapter extends RecyclerView.Adapter<PublicOffersAdapter.ViewHolder> {
        private String[] mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public View mView;
            public CircularImageView mAvatarView;
            public TextView mNameTextView;
            public TextView mLocationTextView;
            public TextView mOfferTextView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                mAvatarView = (CircularImageView) v.findViewById(R.id.user_picture);
                mNameTextView = (TextView) v.findViewById(R.id.user_name);
                mLocationTextView = (TextView) v.findViewById(R.id.offer_location);
                mOfferTextView = (TextView) v.findViewById(R.id.offer_meal);
            }
        }

        public PublicOffersAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public PublicOffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_public_offer, parent, false);
            // set the view's size, margins, paddings and layout parameters
            // TODO
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            if (position % 2 == 0) {
                Picasso.with(getActivity()).load("https://lh6.googleusercontent.com/-7UKKmZUAuTc/AAAAAAAAAAI/AAAAAAAAAJA/6vlHpEXEEL4/s96-c/photo.jpg?sz=192").into(holder.mAvatarView);
            } else {
                Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).into(holder.mAvatarView);
            }
            holder.mNameTextView.setText("John Doe");
            holder.mLocationTextView.setText("Palo Alto");
            String text = String.format(
                    getActivity().getResources().getString(R.string.offer_text),
                    "<font color=#FF7043>" + "Sushi" + "</font>" // such color as theme accent color
                    );
            holder.mOfferTextView.setText(Html.fromHtml(text));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
