package com.team.musicplayer.ui.song;

import android.os.Handler;

public class SongsByAlbumFragment extends SongsFragment {

    public static final String KEY_ALBUM_ID = "album_id";

    @Override
    protected void findSongs() {
        long albumId = getArguments().getLong(KEY_ALBUM_ID);
        new Handler().postDelayed(() -> {
            viewModel.findByAlbum(albumId);
        }, 500);
    }
}
