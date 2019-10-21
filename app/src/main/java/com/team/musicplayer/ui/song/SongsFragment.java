package com.team.musicplayer.ui.song;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.ui.ListFragment;

public class SongsFragment extends ListFragment {

    private SongAdapter adapter;
    private SongsViewModel viewModel;

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        return adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SongAdapter();
        viewModel = ViewModelProviders.of(this).get(SongsViewModel.class);
        viewModel.songs.observe(this, list -> {
            adapter.submitList(list);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.findAll();
    }
}
