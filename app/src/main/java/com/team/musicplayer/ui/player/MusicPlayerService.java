package com.team.musicplayer.ui.player;

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

import com.team.musicplayer.model.repo.MediaStoreRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MusicPlayerService extends Service {

    private final Messenger messenger = new Messenger(new PlayerHandler());
    private CompositeDisposable disposable = new CompositeDisposable();

    private MediaStoreRepo repo;
    private MediaPlayer player;
    private List<Long> idList = new ArrayList<>();
    private int currentPosition;
    private boolean shuffle;

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

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    class PlayerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    toggleMusicPlay();
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
            super.handleMessage(msg);
        }
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

                    });

                    player.setOnCompletionListener(mediaPlayer -> {

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

    private void toggleMusicPlay() {

    }
}
