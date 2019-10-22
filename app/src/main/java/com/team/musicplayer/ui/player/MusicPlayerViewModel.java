package com.team.musicplayer.ui.player;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.musicplayer.ServiceLocator;
import com.team.musicplayer.model.entity.SongInfo;
import com.team.musicplayer.model.repo.MediaStoreRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MusicPlayerViewModel extends AndroidViewModel {

    private MediaStoreRepo repo;
    private CompositeDisposable disposable = new CompositeDisposable();

    private List<Long> idList = new ArrayList<>();
    private int currentPosition;
    private boolean shuffle;

    final MutableLiveData<SongInfo> song = new MutableLiveData<>();

    public MusicPlayerViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).mediaStoreRepo();
        disposable.add(repo.getAllSongIds(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ids -> {
                    idList.clear();
                    idList.addAll(ids);
                }));
    }

    void findById(long id) {
        disposable.add(repo.findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(song::setValue, Throwable::printStackTrace));
    }

    void playNext() {
        if (shuffle) {
            Random random = new Random();
            currentPosition = random.nextInt(idList.size());
            findByPosition(currentPosition);
        } else {
            currentPosition += 1;
            findByPosition(currentPosition);
        }
    }

    void playPrev() {
        if (shuffle) {
            Random random = new Random();
            currentPosition = random.nextInt(idList.size());
            findByPosition(currentPosition);
        } else {
            currentPosition -= 1;
            findByPosition(currentPosition);
        }
    }

    void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    void toggleShuffle() {
        shuffle = !shuffle;
    }

    private void findByPosition(int position) {
        if (position >= 0 && position < idList.size()) {
            findById(idList.get(position));
        } else {
            currentPosition = 0;
            findById(idList.get(currentPosition));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
