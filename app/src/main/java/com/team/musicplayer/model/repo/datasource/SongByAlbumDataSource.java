package com.team.musicplayer.model.repo.datasource;

import android.provider.MediaStore;

public class SongByAlbumDataSource extends SongDataSource {

    private long albumId;

    public SongByAlbumDataSource(Long albumId) {
        this.albumId = albumId;
    }

    @Override
    protected String where() {
        return MediaStore.Audio.Media.ALBUM_ID + " = " + albumId;
    }
}
