package com.team.musicplayer.model.repo;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.team.musicplayer.model.entity.Album;
import com.team.musicplayer.model.entity.Artist;
import com.team.musicplayer.model.entity.SongInfo;
import com.team.musicplayer.model.repo.datasource.AlbumDataSource;
import com.team.musicplayer.model.repo.datasource.ArtistDataSource;
import com.team.musicplayer.model.repo.datasource.MediaStoreDataSourceFactory;
import com.team.musicplayer.model.repo.datasource.SongDataSource;

import io.reactivex.Observable;

public class MediaStoreRepo {

    private final PagedList.Config config = new PagedList.Config.Builder()
            .setMaxSize(25)
            .setPageSize(4)
            .build();

    public Observable<PagedList<SongInfo>> getAllSongs() {
        MediaStoreDataSourceFactory<SongInfo> factory = new MediaStoreDataSourceFactory<>(SongDataSource.class);
        return new RxPagedListBuilder<>(factory, config).buildObservable();
    }

    public Observable<PagedList<Album>> getAllAlbums() {
        MediaStoreDataSourceFactory<Album> factory = new MediaStoreDataSourceFactory<>(AlbumDataSource.class);
        return new RxPagedListBuilder<>(factory, config).buildObservable();
    }

    public Observable<PagedList<Artist>> getAllArtists() {
        MediaStoreDataSourceFactory<Artist> factory = new MediaStoreDataSourceFactory<>(ArtistDataSource.class);
        return new RxPagedListBuilder<>(factory, config).buildObservable();
    }

}
