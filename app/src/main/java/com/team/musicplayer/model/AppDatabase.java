package com.team.musicplayer.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.team.musicplayer.model.dao.FavouriteDao;
import com.team.musicplayer.model.dao.PlaylistDao;
import com.team.musicplayer.model.entity.Favourite;
import com.team.musicplayer.model.entity.Playlist;

@Database(entities = {Favourite.class, Playlist.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavouriteDao favouriteDao();

    public abstract PlaylistDao playlistDao();

}
