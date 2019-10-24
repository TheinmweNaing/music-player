package com.team.musicplayer.ui.playlist;

import androidx.annotation.NonNull;

import com.team.musicplayer.BR;
import com.team.musicplayer.R;
import com.team.musicplayer.model.entity.Playlist;
import com.team.musicplayer.ui.AbstractPagedListAdapter;

public class PlaylistAdapter extends AbstractPagedListAdapter<Playlist> {

    @Override
    protected int layoutRes() {
        return R.layout.layout_song;
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder<Playlist> holder, int position) {
        holder.bindVariable(BR.obj, getItemAt(position).getSongInfo());
    }
}
