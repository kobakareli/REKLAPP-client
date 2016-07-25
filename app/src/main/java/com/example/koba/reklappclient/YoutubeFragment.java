package com.example.koba.reklappclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koba.reklappclient.RequestBodies.AddUserBody;
import com.example.koba.reklappclient.RequestBodies.TransferRequestBody;
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

    private int PUSH_BUTTON_APPEARANCES = 5;
    private final int PUSH_BUTTON_DURATION = 5000;

    private User user;
    private String videoId;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private Advertisement currentAd;
    private String userNumber;
    private RetroFitServer api;
    private TextView price;
    private TextView company;
    private TextView product;
    private TextView description;

    private YouTubePlayer youtubePlayer;
    private int duration;
    private int pushButtonInterval;
    private int pushButtonAppeared = 0;
    private boolean wasPushed = false;

    private CountDownTimer interval;
    private CountDownTimer visible;
    private boolean isIntervalCanceled = false;
    private boolean isVisibleCanceled = false;

    private boolean watched = true;

    private int videoCount = 0;

    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playback_layout, container, false);

        prefs = getActivity().getSharedPreferences(
                getResources().getString(R.string.preferences_key), Context.MODE_PRIVATE);
        if (prefs.contains("videoCount")) {
            videoCount = prefs.getInt("videoCount", 0);
        }
        else {
            prefs.edit().putInt("videoCount", videoCount).commit();
        }

        Bundle args = getArguments();
        user = args.getParcelable("user");
        userNumber = user.mobile_number;

        fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab2.setVisibility(View.GONE);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasPushed = true;
            }
        });

        company = (TextView) rootView.findViewById(R.id.company);
        product = (TextView) rootView.findViewById(R.id.product_name);
        description = (TextView) rootView.findViewById(R.id.product_description);
        price = (TextView) rootView.findViewById(R.id.ad_price);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(RetroFitServer.URI)
                .build();
        api = adapter.create(RetroFitServer.class);

        videoId = "j5-yKhDd64s";
        getNextAd(rootView);


        return rootView;
    }

    private void updateVideo(View rootView) {
        if (getActivity() == null) {
            return;
        }
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        Drawable nextIcon = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nextIcon = getResources().getDrawable(R.mipmap.ic_next, getContext().getTheme());
        } else {
            nextIcon = getResources().getDrawable(R.mipmap.ic_next);
        }
        fab.setImageDrawable(nextIcon);
        fab.setVisibility(View.GONE);
        fab.animate().translationX(100).alpha(0.0f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (watched) {
                    TransferRequestBody trb = new TransferRequestBody(currentAd.getPrice());
                    api.transferMoney(user.mobile_number, "self", trb, new Callback<AddUserBody>() {
                        @Override
                        public void success(AddUserBody addUserBody, Response response) {
                            videoCount ++;
                            prefs.edit().putInt("videoCount", videoCount).commit();
                            String problem = addUserBody.getProblem();
                            if (problem.compareTo(getResources().getString(R.string.transfer_success_status)) == 0) {
                                user.money += currentAd.getPrice();
                                api.updatePairDate(currentAd.getPairId(), new Callback<AddUserBody>() {
                                    @Override
                                    public void success(AddUserBody addUserBody, Response response) {
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                    }
                                });
                                Fragment current = ((AppActivity) getActivity()).getFragmentById(GlobalVariables.YOUTUBE_FRAGMENT_ID, user);
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                manager.beginTransaction()
                                        .replace(R.id.flContent, current)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(), problem, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
                }
                else {
                    api.increaseViewsLeft(currentAd.getAdId(), new Callback<AddUserBody>() {
                        @Override
                        public void success(AddUserBody addUserBody, Response response) {

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                    Fragment current = ((AppActivity) getActivity()).getFragmentById(GlobalVariables.YOUTUBE_FRAGMENT_ID, user);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.flContent, current)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                player.setPlayerStateChangeListener(playerStateChangeListener);
                player.setPlaybackEventListener(playbackListener);
                if (!wasRestored) {
                    youtubePlayer = player;
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    player.loadVideo(currentAd.getURL());
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                fab.setVisibility(View.VISIBLE);
                fab.animate().translationX(0).alpha(1.0f).setDuration(1000);
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });
    }

    private void updateTexts() {
        price.setText(Html.fromHtml("<b>ვიდეოს საფასური: </b>\n" + currentAd.getPrice()));
        company.setText(Html.fromHtml("<b>კომპანიის დასახელება: </b>\n" + currentAd.getCompany()));
        product.setText(Html.fromHtml("<b>პროდუქტის დასახელება: </b>\n" + currentAd.getProduct()));
        description.setText(Html.fromHtml("<b>პროდუქტის აღწერა: </b>\n" + currentAd.getDescription()));
    }

    private YouTubePlayer.PlaybackEventListener playbackListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onPlaying() {
            if((PUSH_BUTTON_APPEARANCES - pushButtonAppeared) != 0) {
                pushButtonInterval = duration / (PUSH_BUTTON_APPEARANCES - pushButtonAppeared) - PUSH_BUTTON_DURATION;
                if(fab2.getVisibility() == View.GONE && interval != null) {
                    wasPushed = true;
                    isIntervalCanceled = false;
                    disappearPush();
                }
                if (fab2.getVisibility() == View.VISIBLE && visible != null) {
                    isVisibleCanceled = false;
                    appearPush();
                }
            }
        }

        @Override
        public void onPaused() {
            if(interval != null) {
                duration -= youtubePlayer.getCurrentTimeMillis();
                interval.cancel();
                isIntervalCanceled = true;
            }
            if (visible != null) {
                visible.cancel();
                isVisibleCanceled = true;
            }
        }

        @Override
        public void onStopped() {
            if (interval != null) {
                interval.cancel();
                isIntervalCanceled = true;
            }
            if (visible != null) {
                visible.cancel();
                isVisibleCanceled = true;
            }
            interval = null;
            visible = null;
        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
            duration = youtubePlayer.getDurationMillis();
            if (duration >= 60000) {
                pushButtonInterval = duration/PUSH_BUTTON_APPEARANCES - PUSH_BUTTON_DURATION;
                disappearPush();
            }
            else if(duration < 60000 && duration >= 30000) {
                PUSH_BUTTON_APPEARANCES = 3;
                pushButtonInterval = duration/PUSH_BUTTON_APPEARANCES - PUSH_BUTTON_DURATION;
                disappearPush();
            }
            else if(duration < 30000 && duration >= 10000) {
                PUSH_BUTTON_APPEARANCES = 1;
                pushButtonInterval = (int)(duration/(1.25*PUSH_BUTTON_APPEARANCES)) - PUSH_BUTTON_DURATION;
                disappearPush();
            }
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
            if (fab.getVisibility() != View.VISIBLE) {
                fab.setVisibility(View.VISIBLE);
                fab.animate().translationX(0).alpha(1.0f).setDuration(1000);
            }
        }

        @Override
        public void onVideoStarted() {
        }
    };

    private void getNextAd(final View rootView) {
        if (videoCount >= GlobalVariables.USER_DAILY_LIMIT) {
            Toast.makeText(getActivity(), "თქვენ უკვე გადააჭარბეთ დღიურ ლიმიტს", Toast.LENGTH_SHORT).show();
            return;
        }
        api.getRandomAdvertisement(userNumber, new Callback<Advertisement>() {
            @Override
            public void success(Advertisement advertisement, Response response) {
                if(advertisement != null && advertisement.getURL() != null && advertisement.getURL().length() != 0) {
                    currentAd = advertisement;
                    updateVideo(rootView);
                    updateTexts();
                    return;
                }
                Toast.makeText(getActivity(), advertisement.getStatus(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void disappearPush() {
        if (fab2 != null) {
            fab2.setVisibility(View.GONE);
            fab2.animate().translationY(100).alpha(0.0f).setDuration(1000);
            if(interval != null && visible != null) {
                if (wasPushed) {
                    wasPushed = false;
                }
                else {
                    watched = false;
                    interval.cancel();
                    visible.cancel();
                    isVisibleCanceled = true;
                    isIntervalCanceled = true;
                    if (fab.getVisibility() != View.VISIBLE) {
                        fab.setVisibility(View.VISIBLE);
                        fab.animate().translationX(0).alpha(1.0f).setDuration(1000);
                    }
                    Activity activity = getActivity();
                    if (activity != null) {
                        Toast.makeText(activity, "თქვენ არ დააჭირეთ ღილაკს. რეკლამა ნაყურებლად არ ჩაითვლება", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            }
            interval = new CountDownTimer(pushButtonInterval, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    if (!isIntervalCanceled && fab2 != null && fab2.getVisibility() == View.GONE) {
                        appearPush();
                    }
                }
            }.start();
        }
    }

    private void appearPush() {
        if (fab2 != null) {
            fab2.setVisibility(View.VISIBLE);
            pushButtonAppeared ++;
            fab2.animate().translationY(0).alpha(1.0f).setDuration(1000);
            visible = new CountDownTimer(PUSH_BUTTON_DURATION, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    if (!isVisibleCanceled && fab2 != null && fab2.getVisibility() == View.VISIBLE) {
                        disappearPush();
                    }
                }
            }.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fab2 = null;
        if(interval != null) {
            interval.cancel(); interval = null;
            isIntervalCanceled = true;
        }
        if (visible != null) {
            visible.cancel(); visible = null;
            isVisibleCanceled = true;
        }

    }
}
