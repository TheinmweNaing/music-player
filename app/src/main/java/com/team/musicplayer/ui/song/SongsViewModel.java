package com.team.musicplayer.ui.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.team.musicplayer.ServiceLocator;
import com.team.musicplayer.model.entity.SongInfo;
import com.team.musicplayer.model.repo.MediaStoreRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SongsViewModel extends AndroidViewModel {

    private MediaStoreRepo repo;
    private CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<PagedList<SongInfo>> songs = new MutableLiveData<>();

    public SongsViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).mediaStoreRepo();
    }

    void findAll() {
        disposable.add(repo.getAllSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs::setValue));
    }
    void findByAlbum(long albumId) {
        disposable.add(repo.getSongsByAlbum(albumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs::setValue));
    }
    void findByArtist(long artistId) {
        disposable.add(repo.getSongsByArtist(artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
