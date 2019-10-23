package com.team.musicplayer.ui.playlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.team.musicplayer.ServiceLocator;
import com.team.musicplayer.model.entity.Playlist;
import com.team.musicplayer.model.repo.PlaylistRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlaylistViewModel extends AndroidViewModel {

    private PlaylistRepo repo;
    private CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<PagedList<Playlist>> playlists = new MutableLiveData<>();

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).playlistRepo();
    }

    void findAll() {
        disposable.add(repo.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playlists::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
