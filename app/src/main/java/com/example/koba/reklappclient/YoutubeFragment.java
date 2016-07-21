package com.example.koba.reklappclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Koba on 17/07/2016.
 */
public class YoutubeFragment extends Fragment {

    public static final String YOUTUBE_API_KEY = "AIzaSyBqzMy33km9EzeA1BE1PXRe6n7OckncUxE";
    private String videoId;
    private FloatingActionButton fab;
    private Advertisement currentAd;
    private String userNumber;
    private RetroFitServer api;
    private TextView company;
    private TextView product;
    private TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playback_layout, container, false);

        Bundle args = getArguments();
        User user = args.getParcelable("user");
        userNumber = user.mobile_number;

        company = (TextView) rootView.findViewById(R.id.company);
        product = (TextView) rootView.findViewById(R.id.product_name);
        description = (TextView) rootView.findViewById(R.id.product_description);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(RetroFitServer.URI)
                .build();
        api = adapter.create(RetroFitServer.class);

        videoId = "j5-yKhDd64s"; // currentAd.getURL();
        getNextAd();


        return rootView;
    }

    private void updateVideo() {
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        Drawable nextIcon = getResources().getDrawable(R.mipmap.ic_next);
        fab.setImageDrawable(nextIcon);
        fab.setVisibility(View.GONE);
        fab.animate().translationX(100).alpha(0.0f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNextAd();
            }
        });
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                player.setPlayerStateChangeListener(playerStateChangeListener);
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.loadVideo(videoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });
    }

    private void updateTexts() {
        company.setText(Html.fromHtml("<b>კომპანიის დასახელება: </b>\n" + currentAd.getCompany()));
        product.setText(Html.fromHtml("<b>პროდუქტის დასახელება: </b>\n" + currentAd.getProduct()));
        description.setText(Html.fromHtml("<b>პროდუქტის აღწერა: </b>\n" + currentAd.getDescription()));
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
            fab.setVisibility(View.VISIBLE);
            fab.animate().translationX(0).alpha(1.0f).setDuration(1000);
        }

        @Override
        public void onVideoStarted() {
        }
    };

    private void getNextAd() {
        api.getRandomAdvertisement(userNumber, new Callback<Advertisement>() {
            @Override
            public void success(Advertisement advertisement, Response response) {
                if(advertisement != null && advertisement.getURL() != null && advertisement.getURL().length() != 0) {
                    currentAd = advertisement;
                    updateVideo();
                    updateTexts();
                    return;
                }
                Toast.makeText(getActivity(), "რეკლამა ვერ მოიძებნა", Toast.LENGTH_SHORT);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
