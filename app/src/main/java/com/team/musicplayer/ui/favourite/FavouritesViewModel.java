package com.team.musicplayer.ui.favourite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.team.musicplayer.ServiceLocator;
import com.team.musicplayer.model.entity.Favourite;
import com.team.musicplayer.model.repo.FavouriteRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FavouritesViewModel extends AndroidViewModel {

    private FavouriteRepo repo;
    private CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<PagedList<Favourite>> favourites = new MutableLiveData<>();

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).favouriteRepo();
    }

    void findAll() {
        disposable.add(repo.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favourites::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
