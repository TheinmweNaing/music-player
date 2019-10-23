package com.team.musicplayer.ui.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.team.musicplayer.MainActivity;
import com.team.musicplayer.R;
import com.team.musicplayer.model.repo.MediaStoreRepo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MusicPlayerService extends Service {

    public static final String ACTION_MUSIC_CURRENT_POSITION = "com.team.musicplayer.CurrentPosition";
    public static final String ACTION_MUSIC_PLAYER_SERVICE = "com.team.musicplayer.PlayerService";

    public static final String KEY_CURRENT_POSITION = "position";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_CURRENT_SONG_POSITION = "song_position";

    private final Messenger messenger = new Messenger(new PlayerHandler(this));
    private CompositeDisposable disposable = new CompositeDisposable();

    private MediaStoreRepo repo;
    private MediaPlayer player;
    private List<Long> idList = new ArrayList<>();
    private int currentPosition;
    private boolean shuffle;
    private Runnable seekBarRunnable;
    private Handler seekBarHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        this.repo = new MediaStoreRepo();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, "2201")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Music Player")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2201, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        if (player.isPlaying()) {
            seekBarHandler.removeCallbacks(seekBarRunnable);
            seekBarRunnable = null;
            player.stop();
            player.release();
        }
    }

    private void initSongs() {
        disposable.add(repo.getAllSongIds(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(list -> {
                idList.clear();
                idList.addAll(list);
            }));
    }

    private void initMediaPlayer(long songId) {
        disposable.add(repo.findById(songId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(song -> {
                    if (player != null) player.release();

                    player = new MediaPlayer();
                    player.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());

                    player.setOnPreparedListener(mediaPlayer -> {
                        int duration = mediaPlayer.getDuration();
                        Intent intent = new Intent(ACTION_MUSIC_PLAYER_SERVICE);
                        intent.putExtra(KEY_DURATION, duration);
                        intent.putExtra(KEY_CURRENT_POSITION, mediaPlayer.getCurrentPosition());
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                        updateSeekBar();
                        seekBarHandler = new Handler();
                        togglePlayMusic();
                    });

                    player.setOnCompletionListener(mediaPlayer -> {
                        if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                    });

                    try {
                        Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(song.getSongId()));
                        player.setDataSource(this, uri);
                        player.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
    }

    private void togglePlayMusic() {
        if (player == null) return;

        if (!player.isPlaying()) {
            player.start();
            seekBarRunnable = () -> {
                updateSeekBar();
                seekBarHandler.postDelayed(seekBarRunnable, 1000);
            };
            seekBarHandler.postDelayed(seekBarRunnable, 1000);
        } else {
            seekBarHandler.removeCallbacks(seekBarRunnable);
            seekBarRunnable = null;
            player.pause();
        }
    }

    private void updateSeekBar() {
        int current = player.getCurrentPosition();
        Intent intent = new Intent(ACTION_MUSIC_PLAYER_SERVICE);
        intent.putExtra(KEY_CURRENT_POSITION, current);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    static class PlayerHandler extends Handler {

        private WeakReference<MusicPlayerService> reference;

        PlayerHandler(MusicPlayerService service) {
            this.reference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            MusicPlayerService service = reference.get();
            if (service != null) {
                switch (msg.what) {
                    case 1:
                        service.shuffle = !service.shuffle;
                        break;
                    case 2:
                        break;
                    case 3:
                        service.togglePlayMusic();
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 100:
                        service.initMediaPlayer((long) msg.obj);
                        break;
                    case 200:
                        service.initSongs();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }
}
