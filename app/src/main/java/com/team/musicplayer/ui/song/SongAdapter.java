package com.team.musicplayer.ui.song;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.databinding.LayoutSongBinding;
import com.team.musicplayer.model.entity.SongInfo;
import com.team.musicplayer.ui.AdapterItemClickListener;

public class SongAdapter extends PagedListAdapter<SongInfo, SongAdapter.SongViewHolder> {

    private static final DiffUtil.ItemCallback<SongInfo> DIFF_UTIL = new DiffUtil.ItemCallback<SongInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull SongInfo oldItem, @NonNull SongInfo newItem) {
            return oldItem.getSongId() == newItem.getSongId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SongInfo oldItem, @NonNull SongInfo newItem) {
            return oldItem.equals(newItem);
        }
    };

    private AdapterItemClickListener adapterItemClickListener;

    SongAdapter() {
        super(DIFF_UTIL);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSongBinding binding = LayoutSongBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    public SongInfo getItemAt(int position) {
        return getItem(position);
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        private LayoutSongBinding binding;

        public SongViewHolder(@NonNull LayoutSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (adapterItemClickListener != null) adapterItemClickListener.onClick(getAdapterPosition());
            });
        }

        void bind(SongInfo songInfo) {
            binding.setObj(songInfo);
        }
    }
}
