package com.team.musicplayer.ui.song;

import com.team.musicplayer.R;
import com.team.musicplayer.model.entity.SongInfo;
import com.team.musicplayer.ui.AbstractPagedListAdapter;

public class SongAdapter extends AbstractPagedListAdapter<SongInfo> {

    @Override
    protected int layoutRes() {
        return R.layout.layout_song;
    }
}
