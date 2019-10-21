package com.team.musicplayer.ui.artist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.team.musicplayer.ServiceLocator;
import com.team.musicplayer.model.entity.Album;
import com.team.musicplayer.model.entity.Artist;
import com.team.musicplayer.model.repo.MediaStoreRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ArtistsViewModel extends AndroidViewModel {

    private MediaStoreRepo repo;
    private CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<PagedList<Artist>> artists = new MutableLiveData<>();

    public ArtistsViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).mediaStoreRepo();
    }

    void findAll() {
        disposable.add(repo.getAllArtists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artists::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
