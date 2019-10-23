/*
package com.team.musicplayer.ui.player;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.team.musicplayer.R;
import com.team.musicplayer.databinding.FragmentMusicPlayerBinding;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicPlayerFragment extends Fragment {

    public static final String KEY_SONG_ID = "song_id";
    public static final String KEY_SONG_POSITION = "song_position";

    private MusicPlayerViewModel viewModel;
    private FragmentMusicPlayerBinding binding;
    private MediaPlayer player;
    private Runnable seekBarRunnable;
    private Handler seekBarHandler;
    private Messenger messenger;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            long songId = getArguments() != null ? getArguments().getLong(KEY_SONG_ID) : 0;
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
                    if (intent.getBooleanExtra(MusicPlayerService.KEY_MUSIC_PLAYING, false)) {
                        binding.btnPlay.setImageResource(R.drawable.ic_pause_black);
                    } else {
                        binding.btnPlay.setImageResource(R.drawable.ic_play_arrow_black);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MusicPlayerViewModel.class);

        viewModel.song.observe(this, song -> {
            if (player != null) {
                player.stop();
                player.release();
            }

            binding.tvTitle.setText(song.getTitle());
            binding.tvArtist.setText(song.getArtist());

            player = new MediaPlayer();
            player.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());

            player.setOnPreparedListener(mediaPlayer -> {
                int duration = mediaPlayer.getDuration();
                binding.seekBar.setMax(duration / 1000);
                binding.tvEndMinute.setText(getMinutesAndSeconds(duration));

                updateSeekBar(mediaPlayer.getDuration());
                seekBarHandler = new Handler();
                togglePlayMusic();
            });

            player.setOnCompletionListener(mediaPlayer -> {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                viewModel.playNext();
            });

            try {
                Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(song.getSongId()));
                player.setDataSource(requireContext(), uri);
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        int songPos = getArguments() != null ? getArguments().getInt(KEY_SONG_POSITION) : 0;
        //viewModel.setCurrentPosition(songPos);

        long songId = getArguments() != null ? getArguments().getLong(KEY_SONG_ID) : 0;
        //viewModel.findById(songId);

        startMusic();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMusicPlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvTitle.setSelected(true);

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

        binding.btnNext.setOnClickListener(v -> viewModel.playNext());
        binding.btnPrevious.setOnClickListener(v -> viewModel.playPrev());
        binding.btnShuffle.setOnClickListener(v -> viewModel.toggleShuffle());
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (player != null && fromUser) {
                    player.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.ACTION_MUSIC_PLAYING);
        intentFilter.addAction(MusicPlayerService.ACTION_MUSIC_CURRENT_POSITION);
        intentFilter.addAction(MusicPlayerService.ACTION_MUSIC_DURATION);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (messenger != null) {
            requireActivity().unbindService(serviceConnection);
        }
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }

    private void startMusic() {
        Intent i = new Intent();
        i.setClass(requireContext(), MusicPlayerService.class);
        requireActivity().startService(i);
        requireActivity().bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void togglePlayMusic() {
        if (player == null) return;

        if (!player.isPlaying()) {
            player.start();
            binding.btnPlay.setImageResource(R.drawable.ic_pause_black);
            seekBarRunnable = () -> {
                updateSeekBar(player.getCurrentPosition());
                seekBarHandler.postDelayed(seekBarRunnable, 1000);
            };
            seekBarHandler.postDelayed(seekBarRunnable, 1000);
        } else {
            seekBarHandler.removeCallbacks(seekBarRunnable);
            seekBarRunnable = null;
            player.pause();
            binding.btnPlay.setImageResource(R.drawable.ic_play_arrow_black);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null && player.isPlaying()) {
            seekBarHandler.removeCallbacks(seekBarRunnable);
            seekBarRunnable = null;
            player.stop();
            player.release();
        }
    }
}
*/
