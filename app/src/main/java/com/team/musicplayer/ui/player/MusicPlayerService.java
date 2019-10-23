package com.team.musicplayer.ui.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
    public static final String ACTION_MUSIC_DURATION = "com.team.musicplayer.MusicDuration";
    public static final String ACTION_MUSIC_PLAYING = "com.team.musicplayer.MusicPlaying";
    public static final String ACTION_STOP_PLAYING = "com.team.musicplayer.StopPlaying";

    public static final String KEY_CURRENT_POSITION = "position";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_CURRENT_SONG_POSITION = "song_position";
    public static final String KEY_MUSIC_PLAYING = "playing";

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
        if (ACTION_STOP_PLAYING.equals(intent.getAction())) {
            Log.d("TAG", "stop media");
            stopForeground(true);
            Intent stopIntent = new Intent(ACTION_STOP_PLAYING);
            LocalBroadcastManager.getInstance(this).sendBroadcast(stopIntent);
            stopSelf();
        } else {
            //createNotificationChannel();
            Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent stopIntent = new Intent(this, MusicPlayerService.class);
            stopIntent.setAction(ACTION_STOP_PLAYING);

            PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, "2201")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Music Player")
                    .setContentIntent(pendingIntent)
                    .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Close", stopPendingIntent)
                    .build();
            startForeground(2201, notification);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        stopMediaPlayer();
        super.onDestroy();
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
                        sendDuration();
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

        sendIsMusicPlaying();

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
        Intent intent = new Intent(ACTION_MUSIC_CURRENT_POSITION);
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
                    case 300:
                        service.sendIsMusicPlaying();
                        service.sendDuration();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    private void sendIsMusicPlaying() {
        Intent intent = new Intent(ACTION_MUSIC_PLAYING);
        intent.putExtra(KEY_MUSIC_PLAYING, player.isPlaying());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendDuration() {
        int duration = player.getDuration();
        Intent intent = new Intent(ACTION_MUSIC_DURATION);
        intent.putExtra(KEY_DURATION, duration);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "2201", "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void stopMediaPlayer() {
        if (player.isPlaying()) {
            seekBarHandler.removeCallbacks(seekBarRunnable);
            seekBarRunnable = null;
            player.stop();
            player.release();
        }
    }
}
