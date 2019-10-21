package com.team.musicplayer.ui.album;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.team.musicplayer.ServiceLocator;
import com.team.musicplayer.model.entity.Album;
import com.team.musicplayer.model.entity.SongInfo;
import com.team.musicplayer.model.repo.MediaStoreRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AlbumsViewModel extends AndroidViewModel {

    private MediaStoreRepo repo;
    private CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<PagedList<Album>> albums = new MutableLiveData<>();

    public AlbumsViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).mediaStoreRepo();
    }

    void findAll() {
        disposable.add(repo.getAllAlbums()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(albums::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
