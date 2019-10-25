package com.team.musicplayer.model.repo.datasource;

import android.provider.MediaStore;

public class SongByArtistDataSource extends SongDataSource {

    private long artistId;

    public SongByArtistDataSource(Long artistId) {
        this.artistId = artistId;
    }

    @Override
    protected String where() {
        return MediaStore.Audio.Media.ARTIST_ID + " = " + artistId;
    }
}
