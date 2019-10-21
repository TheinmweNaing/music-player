package com.team.musicplayer.model.repo;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.team.musicplayer.model.dao.PlaylistDao;
import com.team.musicplayer.model.entity.Playlist;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class PlaylistRepo {
    private PlaylistDao dao;

    public PlaylistRepo(PlaylistDao dao) {
        this.dao = dao;
    }

    public Completable save(Playlist playlist) {
        return Completable.create(source -> {
            try {
                Playlist old = dao.findById(playlist.getSongInfo().getSongId()).blockingGet(new Playlist());
                if (old.getSongInfo().getSongId() > 0) {
                    dao.update(playlist);
                } else {
                    dao.insert(playlist);
                }
                source.onComplete();
            } catch (Exception e) {
                source.onError(e);
            }
        });
    }

    public Completable delete(Playlist playlist) {
        return dao.delete(playlist);
    }

    public Maybe<Playlist> findById(long id) {
        return dao.findById(id);
    }

    public Flowable<PagedList<Playlist>> findAll() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setMaxSize(25)
                .setPageSize(4)
                .build();
        return new RxPagedListBuilder<>(dao.findAll(), config).buildFlowable(BackpressureStrategy.LATEST);
    }
}
