package com.team.musicplayer.model.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.team.musicplayer.model.entity.Playlist;

import io.reactivex.Maybe;

@Dao
public abstract class PlaylistDao implements CudDao<Playlist> {

    @Query("SELECT * FROM PLAYLIST WHERE song_id = :id LIMIT 1")
    public abstract Maybe<Playlist> findById(long id);

    @Query("SELECT * FROM PLAYLIST")
    public abstract DataSource.Factory<Integer, Playlist> findAll();
}
