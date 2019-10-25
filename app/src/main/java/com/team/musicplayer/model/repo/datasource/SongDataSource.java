package com.team.musicplayer.model.repo.datasource;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.team.musicplayer.model.entity.SongInfo;

public class SongDataSource extends MediaStoreDataSource<SongInfo> {

    @Override
    protected SongInfo newInstance(Cursor cursor) {
        SongInfo songInfo = new SongInfo();
        songInfo.setSongId(cursor.getLong(0));
        songInfo.setTitle(cursor.getString(1));
        songInfo.setArtist(cursor.getString(2));
        songInfo.setDuration(cursor.getLong(3));
        return songInfo;
    }

    @Override
    protected String[] projection() {
        return new String[]{MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION};
    }

    @Override
    protected Uri contentUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String sortBy() {
        return MediaStore.Audio.Media.TITLE;
    }

    @Override
    protected String where() {
        return null;
    }
}
