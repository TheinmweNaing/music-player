package com.team.musicplayer.model.entity;

import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(primaryKeys = {"song_id"})
public class Favourite {

    @Embedded
    private SongInfo songInfo;

    public Favourite() {
        songInfo = new SongInfo();
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }
}
