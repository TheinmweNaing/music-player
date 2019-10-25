package com.team.musicplayer.model.repo.datasource;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import java.lang.reflect.InvocationTargetException;

public class MediaStoreDataSourceFactory<T> extends DataSource.Factory<Integer, T> {

    private Class<? extends MediaStoreDataSource<T>> clazz;
    private Object[] args;

    public MediaStoreDataSourceFactory(Class<? extends MediaStoreDataSource<T>> clazz, Object... args) {
        this.clazz = clazz;
        this.args = args;
    }

    @NonNull
    @Override
    public DataSource<Integer, T> create() {
        try {
            if (args != null && args.length > 0) {
                Class<?>[] types = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    types[i] = args[i].getClass();
                }

                return clazz.getDeclaredConstructor(types).newInstance(args);
            }
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }
}
