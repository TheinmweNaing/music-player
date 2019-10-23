package com.team.musicplayer.ui.favourite;

import androidx.annotation.NonNull;

import com.team.musicplayer.BR;
import com.team.musicplayer.R;
import com.team.musicplayer.model.entity.Favourite;
import com.team.musicplayer.ui.AbstractPagedListAdapter;

public class FavouriteAdapter extends AbstractPagedListAdapter<Favourite> {

    @Override
    protected int layoutRes() {
        return R.layout.layout_song;
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder<Favourite> holder, int position) {
        holder.bindVariable(BR.obj, getItem(position).getSongInfo());
    }
}
