package com.team.musicplayer.ui.playlist;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.ui.ListFragment;

public class PlaylistFragment extends ListFragment {

    private PlaylistAdapter adapter;
    private PlaylistViewModel viewModel;

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        return adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PlaylistAdapter();
        viewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        viewModel.playlists.observe(this, adapter::submitList);
        new Handler().postDelayed(viewModel::findAll, 500);
    }
}
