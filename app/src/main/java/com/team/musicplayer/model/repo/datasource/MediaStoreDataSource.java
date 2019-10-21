package com.team.musicplayer.model.repo.datasource;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.team.musicplayer.MusicPlayerApplication;

import java.util.ArrayList;
import java.util.List;

public abstract class MediaStoreDataSource<T> extends PageKeyedDataSource<Integer, T> {

    protected abstract T newInstance(Cursor cursor);

    protected abstract String[] projection();

    protected abstract Uri contentUri();

    protected abstract String sortBy();

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, T> callback) {
        callback.onResult(queryMedia(0), 0, 1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) {
        int offset = (params.key * 25) + 1;
        callback.onResult(queryMedia(offset), params.key + 1);
    }

    private List<T> queryMedia(int offset) {
        List<T> list = new ArrayList<>();
        String pagination = sortBy() + " LIMIT 25 OFFSET " + offset;
        Cursor cursor = MusicPlayerApplication.getApplication().getContentResolver().query(contentUri(), projection(), null, null, pagination);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(newInstance(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }
}
