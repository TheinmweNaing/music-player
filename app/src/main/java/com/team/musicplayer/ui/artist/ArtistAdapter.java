package com.team.musicplayer.ui.artist;

import android.content.Context;

import androidx.annotation.NonNull;

import com.team.musicplayer.BR;
import com.team.musicplayer.R;
import com.team.musicplayer.model.entity.Artist;
import com.team.musicplayer.ui.AbstractPagedListAdapter;

public class ArtistAdapter extends AbstractPagedListAdapter<Artist> {


    @Override
    protected int layoutRes() {
        return R.layout.layout_artist;
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder<Artist> holder, int position) {
        super.onBindViewHolder(holder, position);
        Artist artist = getItem(position);
        if (artist != null) {
            Context ctx = holder.itemView.getContext();
            holder.bindVariable(BR.numberOfAlbum, ctx.getResources().getQuantityString(R.plurals.numberOfAlbums, artist.getNumberOfAlbums(), artist.getNumberOfAlbums()));
            holder.bindVariable(BR.numberOfTrack, ctx.getResources().getQuantityString(R.plurals.numberOfTracks, artist.getNumberOfSongs(), artist.getNumberOfSongs()));
        }
    }
}
