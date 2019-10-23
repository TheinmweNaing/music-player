package com.team.musicplayer.model.entity;

import java.util.Objects;

public class Album implements Identifiable {

    private long albumId;
    private String name;
    private String artist;
    private String image;
    private int numberOfSongs;

    @Override
    public Object getIdentity() {
        return albumId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return albumId == album.albumId &&
                numberOfSongs == album.numberOfSongs &&
                Objects.equals(name, album.name) &&
                Objects.equals(artist, album.artist) &&
                Objects.equals(image, album.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, name, artist, image, numberOfSongs);
    }

}
