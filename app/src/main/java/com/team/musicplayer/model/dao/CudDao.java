package com.team.musicplayer.model.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import io.reactivex.Completable;

public interface CudDao<T> {

    @Insert
    void insert(T entity);

    @Update
    void update(T entity);

    @Delete
    Completable delete(T entity);
}
