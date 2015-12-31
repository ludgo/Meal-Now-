package com.ludgo.android.mealnow.ui;


import android.content.Intent;
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
import com.ludgo.android.mealnow.model.PublicOffer;
import com.ludgo.android.mealnow.service.PublicOffersService;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PublicTabFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PublicOffersAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PublicTabFragment() {
        // Required empty public constructor
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_public_tab, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.public_offers);
        setRecyclerViewLayoutManager();
        swapAdapter();

        getActivity().startService(new Intent(getActivity(), PublicOffersService.class));

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager based on immediate screen width
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

    /**
     * Update UI showing public offers
     */
    public void swapAdapter() {
        List<PublicOffer> currentDatabaseState = PublicOffer.getActiveOffers();
        if (mAdapter == null
                || !mAdapter.getDataset().equals(currentDatabaseState)) {

            mAdapter = new PublicOffersAdapter(currentDatabaseState);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * An adapter to populate this class' recycler view with public offers
     */
    public class PublicOffersAdapter extends RecyclerView.Adapter<PublicOffersAdapter.ViewHolder> {

        private List<PublicOffer> mPublicOffers;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public PublicOffer mPublicOffer;

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

        public PublicOffersAdapter(List<PublicOffer> list) {
            mPublicOffers = list;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public PublicOffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_public_offer, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.mPublicOffer = mPublicOffers.get(position);

            if (holder.mPublicOffer.user_picture == null) {
                Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).into(holder.mAvatarView);
            } else {
                Picasso.with(getActivity()).load(holder.mPublicOffer.user_picture + "?sz=192").into(holder.mAvatarView);
            }
            holder.mNameTextView.setText(holder.mPublicOffer.user_name);
            holder.mLocationTextView.setText(holder.mPublicOffer.offer_location);
            String text = String.format(
                    getActivity().getResources().getString(R.string.offer_text),
                    "<font color=#FF7043>" + holder.mPublicOffer.offer_meal + "</font>" // such color as theme accent color
            );
            holder.mOfferTextView.setText(Html.fromHtml(text));

        }

        // Return the size of data set
        @Override
        public int getItemCount() {
            return (mPublicOffers == null) ? 0 : mPublicOffers.size();
        }

        public List<PublicOffer> getDataset() {
            return mPublicOffers;
        }
    }
}
