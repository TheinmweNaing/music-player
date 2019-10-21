package com.team.musicplayer;

import android.content.Context;

import androidx.room.Room;

import com.team.musicplayer.model.AppDatabase;
import com.team.musicplayer.model.repo.FavouriteRepo;
import com.team.musicplayer.model.repo.MediaStoreRepo;
import com.team.musicplayer.model.repo.PlaylistRepo;

public abstract class ServiceLocator {

    private static ServiceLocator instance;

    public static ServiceLocator getInstance(Context context) {
        if (instance == null) {
            instance = new DefaultServiceLocator(context);
        }
        return instance;
    }

    public abstract Context context();

    public abstract FavouriteRepo favouriteRepo();

    public abstract PlaylistRepo playlistRepo();

    public abstract MediaStoreRepo mediaStoreRepo();

    static class DefaultServiceLocator extends ServiceLocator {

        private AppDatabase database;
        private Context context;

        public DefaultServiceLocator(Context context) {
            database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
            this.context = context;
        }

        @Override
        public Context context() {
            return context;
        }

        @Override
        public FavouriteRepo favouriteRepo() {
            return new FavouriteRepo(database.favouriteDao());
        }

        @Override
        public PlaylistRepo playlistRepo() {
            return new PlaylistRepo(database.playlistDao());
        }

        @Override
        public MediaStoreRepo mediaStoreRepo() {
            return new MediaStoreRepo();
        }
    }

}
