package com.team.musicplayer.ui.album;

import android.content.Context;

import androidx.annotation.NonNull;

import com.team.musicplayer.BR;
import com.team.musicplayer.R;
import com.team.musicplayer.model.entity.Album;
import com.team.musicplayer.ui.AbstractPagedListAdapter;

public class AlbumAdapter extends AbstractPagedListAdapter<Album> {

    @Override
    protected int layoutRes() {
        return R.layout.layout_album;
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractPagedListAdapter.AbstractViewHolder<Album> holder, int position) {
        super.onBindViewHolder(holder, position);
        Album album = getItem(position);
        if (album != null) {
            Context ctx = holder.itemView.getContext();
            holder.bindVariable(BR.numberOfTrack, ctx.getResources().getQuantityString(R.plurals.numberOfTracks, album.getNumberOfSongs(), album.getNumberOfSongs()));
        }
    }

}
