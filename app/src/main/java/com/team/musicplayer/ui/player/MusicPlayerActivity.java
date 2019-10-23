package com.team.musicplayer.ui.player;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.team.musicplayer.R;
import com.team.musicplayer.databinding.ActivityMusicPlayerBinding;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    public static final String KEY_SONG_ID = "song_id";
    public static final String KEY_SONG_POSITION = "song_position";

    private Messenger messenger;
    private ActivityMusicPlayerBinding binding;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            long songId = getIntent().getLongExtra(KEY_SONG_ID, 0);
            try {
                if (songId > 0) {
                    Message msg1 = Message.obtain();
                    msg1.what = 100;
                    msg1.obj = songId;
                    messenger.send(msg1);

                    Message msg2 = Message.obtain();
                    msg2.what = 200;
                    messenger.send(msg2);
                } else {
                    Message msg3 = Message.obtain();
                    msg3.what = 300;
                    messenger.send(msg3);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger = null;
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) return;

            switch (intent.getAction()) {
                case MusicPlayerService.ACTION_MUSIC_DURATION:
                    int duration = intent.getIntExtra(MusicPlayerService.KEY_DURATION, 0);
                    binding.seekBar.setMax(duration / 1000);
                    binding.tvEndMinute.setText(getMinutesAndSeconds(duration));
                    break;
                case MusicPlayerService.ACTION_MUSIC_CURRENT_POSITION:
                    updateSeekBar(intent.getIntExtra(MusicPlayerService.KEY_CURRENT_POSITION, 0));
                    break;
                case MusicPlayerService.ACTION_MUSIC_PLAYING:
                    if (!intent.getBooleanExtra(MusicPlayerService.KEY_MUSIC_PLAYING, false)) {
                        binding.btnPlay.setImageResource(R.drawable.ic_pause_black);
                    } else {
                        binding.btnPlay.setImageResource(R.drawable.ic_play_arrow_black);
                    }
                    break;

                case  MusicPlayerService.ACTION_STOP_PLAYING:
                    if (messenger != null) {
                        unbindService(serviceConnection);
                    }
                    LocalBroadcastManager.getInstance(MusicPlayerActivity.this).unregisterReceiver(broadcastReceiver);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_music_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.btnPlay.setOnClickListener(v -> {
            //togglePlayMusic();
            //startMusic();
            if (messenger != null) {
                try {
                    Message message = Message.obtain();
                    message.what = 3;
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        startMusic();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.ACTION_MUSIC_PLAYING);
        intentFilter.addAction(MusicPlayerService.ACTION_MUSIC_CURRENT_POSITION);
        intentFilter.addAction(MusicPlayerService.ACTION_MUSIC_DURATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (messenger != null) {
            unbindService(serviceConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void startMusic() {
        Intent i = new Intent();
        i.setClass(this, MusicPlayerService.class);
        startService(i);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    private void updateSeekBar(int current) {
        //int current = player.getCurrentPosition();
        binding.seekBar.setProgress(current / 1000);
        binding.tvStartMinute.setText(getMinutesAndSeconds(current));
    }

    private String getMinutesAndSeconds(long duration) {
        long m = TimeUnit.MILLISECONDS.toMinutes(duration);
        long s = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(m);
        return String.format(Locale.ENGLISH, "%02d:%02d", m, s);
    }

}
