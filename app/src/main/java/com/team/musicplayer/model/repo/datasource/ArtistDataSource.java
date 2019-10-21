package com.team.musicplayer.model.repo.datasource;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.team.musicplayer.model.entity.Artist;

public class ArtistDataSource extends MediaStoreDataSource<Artist> {

    @Override
    protected Artist newInstance(Cursor cursor) {
        Artist artist = new Artist();
        artist.setArtistId(cursor.getLong(0));
        artist.setName(cursor.getString(1));
        artist.setNumberOfAlbums(cursor.getInt(2));
        artist.setNumberOfSongs(cursor.getInt(3));
        return artist;
    }

    @Override
    protected String[] projection() {
        return new String[]{MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
    }

    @Override
    protected Uri contentUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String sortBy() {
        return  MediaStore.Audio.Artists.ARTIST;
    }
}
