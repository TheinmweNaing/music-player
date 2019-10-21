package com.team.musicplayer.model.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.team.musicplayer.model.entity.Favourite;

import io.reactivex.Maybe;

@Dao
public abstract class FavouriteDao implements CudDao<Favourite> {

    @Query("SELECT * FROM FAVOURITE WHERE song_id = :id LIMIT 1")
    public abstract Maybe<Favourite> findById(long id);

    @Query("SELECT * FROM FAVOURITE")
    public abstract DataSource.Factory<Integer, Favourite> findAll();
}
