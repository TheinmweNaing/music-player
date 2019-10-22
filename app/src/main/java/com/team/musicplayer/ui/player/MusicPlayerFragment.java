package com.team.musicplayer.ui.player;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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

                updateSeekBar();
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
        viewModel.setCurrentPosition(songPos);

        long songId = getArguments() != null ? getArguments().getLong(KEY_SONG_ID) : 0;
        viewModel.findById(songId);

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
            togglePlayMusic();
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

    private void togglePlayMusic() {
        if (player == null) return;

        if (!player.isPlaying()) {
            player.start();
            binding.btnPlay.setImageResource(R.drawable.ic_pause_black);
            seekBarRunnable = () -> {
                updateSeekBar();
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

    private void updateSeekBar() {
        int current = player.getCurrentPosition();
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
        if (player.isPlaying()) {
            seekBarHandler.removeCallbacks(seekBarRunnable);
            seekBarRunnable = null;
            player.stop();
            player.release();
        }
    }
}
