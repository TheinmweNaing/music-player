package com.team.musicplayer.ui.song;

import android.os.Handler;

public class SongsByArtistFragment extends SongsFragment {

    public static final String KEY_ARTIST_ID = "artist_id";

    @Override
    protected void findSongs() {
        long artistId = getArguments().getLong(KEY_ARTIST_ID);
        new Handler().postDelayed(() -> {
            viewModel.findByArtist(artistId);
        }, 500);
    }
}
