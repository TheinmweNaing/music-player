package com.team.musicplayer.model.repo.datasource;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

public class MediaStoreDataSourceFactory<T> extends DataSource.Factory<Integer, T> {

    private Class<? extends MediaStoreDataSource<T>> clazz;

    public MediaStoreDataSourceFactory(Class<? extends MediaStoreDataSource<T>> clazz) {
        this.clazz = clazz;
    }

    @NonNull
    @Override
    public DataSource<Integer, T> create() {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }
}
