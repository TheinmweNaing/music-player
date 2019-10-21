package com.team.musicplayer.model.repo;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.team.musicplayer.model.dao.FavouriteDao;
import com.team.musicplayer.model.entity.Favourite;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class FavouriteRepo {

    private FavouriteDao dao;

    public FavouriteRepo(FavouriteDao dao) {
        this.dao = dao;
    }

    public Completable save(Favourite favourite) {
        return Completable.create(source -> {
            try {
                Favourite old = dao.findById(favourite.getSongInfo().getSongId()).blockingGet(new Favourite());
                if (old.getSongInfo().getSongId() > 0) {
                    dao.update(favourite);
                } else {
                    dao.insert(favourite);
                }
                source.onComplete();
            } catch (Exception e) {
                source.onError(e);
            }
        });
    }

    public Completable delete(Favourite favourite) {
        return dao.delete(favourite);
    }

    public Maybe<Favourite> findById(long id) {
        return dao.findById(id);
    }

    public Flowable<PagedList<Favourite>> findAll() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setMaxSize(25)
                .setPageSize(4)
                .build();
        return new RxPagedListBuilder<>(dao.findAll(), config).buildFlowable(BackpressureStrategy.LATEST);
    }
}
