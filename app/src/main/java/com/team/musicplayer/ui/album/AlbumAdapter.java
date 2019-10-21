package com.team.musicplayer.ui.album;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.R;
import com.team.musicplayer.databinding.LayoutAlbumBinding;
import com.team.musicplayer.model.entity.Album;

public class AlbumAdapter extends PagedListAdapter<Album, AlbumAdapter.AlbumViewHolder> {

    public static final DiffUtil.ItemCallback<Album> DIFF_UTIL = new DiffUtil.ItemCallback<Album>() {
        @Override
        public boolean areItemsTheSame(@NonNull Album oldItem, @NonNull Album newItem) {
            return oldItem.getAlbumId() == newItem.getAlbumId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Album oldItem, @NonNull Album newItem) {
            return oldItem.equals(newItem);
        }
    };

    AlbumAdapter() {
        super(DIFF_UTIL);
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutAlbumBinding binding = LayoutAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {

        private LayoutAlbumBinding binding;

        public AlbumViewHolder(@NonNull LayoutAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Album album) {
            binding.setObj(album);
            binding.tvTrack.setText(itemView.getContext().getResources().getQuantityString(R.plurals.numberOfTracks, album.getNumberOfSongs(),album.getNumberOfSongs()));
        }
    }
}
