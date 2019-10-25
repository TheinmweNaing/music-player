package com.team.musicplayer.model.repo.datasource;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.team.musicplayer.model.entity.Album;

public class AlbumDataSource extends MediaStoreDataSource<Album> {

    @Override
    protected Album newInstance(Cursor cursor) {
        Album album = new Album();
        album.setAlbumId(cursor.getLong(0));
        album.setName(cursor.getString(1));
        album.setNumberOfSongs(cursor.getInt(2));
        album.setArtist(cursor.getString(3));
        return album;
    }

    @Override
    protected String[] projection() {
        return new String[]{MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ARTIST};
    }

    @Override
    protected Uri contentUri() {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String sortBy() {
        return  MediaStore.Audio.Albums.ALBUM;
    }

    @Override
    protected String where() {
        return null;
    }
}
