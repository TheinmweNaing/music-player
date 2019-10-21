package com.team.musicplayer.model.entity;

import androidx.room.ColumnInfo;

import java.util.Objects;

public class SongInfo {

    @ColumnInfo(name = "song_id")
    private long songId;
    private String title;
    private String artist;
    private String path;
    private long duration;

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongInfo songInfo = (SongInfo) o;
        return songId == songInfo.songId &&
                duration == songInfo.duration &&
                Objects.equals(title, songInfo.title) &&
                Objects.equals(artist, songInfo.artist) &&
                Objects.equals(path, songInfo.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, title, artist, path, duration);
    }
}
