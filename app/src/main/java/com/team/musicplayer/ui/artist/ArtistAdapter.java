package com.team.musicplayer.ui.artist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.R;
import com.team.musicplayer.databinding.LayoutArtistBinding;
import com.team.musicplayer.model.entity.Artist;

public class ArtistAdapter extends PagedListAdapter<Artist, ArtistAdapter.ArtistViewHolder> {

    public static final DiffUtil.ItemCallback<Artist> DIFF_UTIL = new DiffUtil.ItemCallback<Artist>() {
        @Override
        public boolean areItemsTheSame(@NonNull Artist oldItem, @NonNull Artist newItem) {
            return oldItem.getArtistId() == newItem.getArtistId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Artist oldItem, @NonNull Artist newItem) {
            return oldItem.equals(newItem);
        }
    };

    ArtistAdapter() {
        super(DIFF_UTIL);
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArtistBinding binding = LayoutArtistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder {

        private LayoutArtistBinding binding;

        public ArtistViewHolder(@NonNull LayoutArtistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Artist artist) {
            binding.setObj(artist);
            binding.tvAlbum.setText(itemView.getContext().getResources().getQuantityString(R.plurals.numberOfAlbums, artist.getNumberOfAlbums(), artist.getNumberOfAlbums()));
            binding.tvTrack.setText(itemView.getContext().getResources().getQuantityString(R.plurals.numberOfTracks, artist.getNumberOfSongs(), artist.getNumberOfSongs()));
        }
    }
}
