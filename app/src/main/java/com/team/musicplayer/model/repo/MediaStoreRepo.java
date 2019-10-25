package com.team.musicplayer.model.repo;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.team.musicplayer.MusicPlayerApplication;
import com.team.musicplayer.model.entity.Album;
import com.team.musicplayer.model.entity.Artist;
import com.team.musicplayer.model.entity.SongInfo;
import com.team.musicplayer.model.repo.datasource.AlbumDataSource;
import com.team.musicplayer.model.repo.datasource.ArtistDataSource;
import com.team.musicplayer.model.repo.datasource.MediaStoreDataSourceFactory;
import com.team.musicplayer.model.repo.datasource.SongByAlbumDataSource;
import com.team.musicplayer.model.repo.datasource.SongByArtistDataSource;
import com.team.musicplayer.model.repo.datasource.SongDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

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

    public Observable<PagedList<SongInfo>> getSongsByAlbum(long albumId) {
        MediaStoreDataSourceFactory<SongInfo> factory = new MediaStoreDataSourceFactory<>(SongByAlbumDataSource.class, albumId);
        return new RxPagedListBuilder<>(factory, config).buildObservable();
    }

    public Observable<PagedList<SongInfo>> getSongsByArtist(long artistId) {
        MediaStoreDataSourceFactory<SongInfo> factory = new MediaStoreDataSourceFactory<>(SongByArtistDataSource.class, artistId);
        return new RxPagedListBuilder<>(factory, config).buildObservable();
    }

    public Single<SongInfo> findById(long id) {
        return Single.create(source -> {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION};
            String sortBy = MediaStore.Audio.Media.TITLE;
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 and " + MediaStore.Audio.Media._ID + " = " + id;
            Cursor cursor = MusicPlayerApplication.getApplication().getContentResolver().query(uri, projection, selection, null, sortBy);
            if (cursor == null) {
                source.onError(new RuntimeException("Song not found!"));
            } else {
                cursor.moveToFirst();
                SongInfo songInfo = new SongInfo();
                songInfo.setSongId(cursor.getLong(0));
                songInfo.setTitle(cursor.getString(1));
                songInfo.setArtist(cursor.getString(2));
                songInfo.setDuration(cursor.getLong(3));
                source.onSuccess(songInfo);
                cursor.close();
            }
        });
    }

    public Observable<List<Long>> getAllSongIds(String where) {
        return Observable.create(source -> {
            List<Long> list = new ArrayList<>();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Audio.Media._ID};
            String sortBy = MediaStore.Audio.Media.TITLE;
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 " + (where != null ? where : "");
            Cursor cursor = MusicPlayerApplication.getApplication().getContentResolver().query(uri, projection, selection, null, sortBy);
            if (cursor == null) {
                source.onError(new RuntimeException("Songs not found!"));
            } else {
                cursor.moveToFirst();
                do {
                    list.add(cursor.getLong(0));
                } while (cursor.moveToNext());
                cursor.close();
                source.onNext(list);
            }
        });
    }

}
