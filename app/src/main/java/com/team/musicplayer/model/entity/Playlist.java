package com.team.musicplayer.model.entity;

import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(primaryKeys = {"song_id"})
public class Playlist {

    @Embedded
    private SongInfo songInfo;

    public Playlist() {
        songInfo = new SongInfo();
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }
}
