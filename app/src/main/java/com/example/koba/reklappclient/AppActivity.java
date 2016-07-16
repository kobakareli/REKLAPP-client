package com.example.koba.reklappclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koba on 16/07/2016.
 */
public class AppActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String YOUTUBE_API_KEY = "AIzaSyBqzMy33km9EzeA1BE1PXRe6n7OckncUxE";
    private static final int RECOVERY_REQUEST = 1;
    private FloatingActionButton fab;
    private String videoId;
    private YouTubePlayerView youTubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        videoId = "j5-yKhDd64s"; //TODO fetch new url from server
        updateVideo();
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

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setPlayerStateChangeListener(playerStateChangeListener);
        if (!wasRestored) {
            player.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void updateVideo() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.animate().translationX(100).alpha(0.0f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppActivity.this, AppActivity.class);
                startActivity(intent);
                finish();
            }
        });
        youTubeView = (YouTubePlayerView) findViewById(R.id.ad_player);
        youTubeView.initialize(YOUTUBE_API_KEY, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
