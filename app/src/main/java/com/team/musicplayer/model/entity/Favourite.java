package com.team.musicplayer.model.entity;

import androidx.room.Embedded;
import androidx.room.Entity;

import java.util.Objects;

@Entity(primaryKeys = {"song_id"})
public class Favourite implements Identifiable {

    @Embedded
    private SongInfo songInfo;

    @Override
    public Object getIdentity() {
        return songInfo.getSongId();
    }

    public Favourite() {
        songInfo = new SongInfo();
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favourite favourite = (Favourite) o;
        return Objects.equals(songInfo, favourite.songInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songInfo);
    }
}
