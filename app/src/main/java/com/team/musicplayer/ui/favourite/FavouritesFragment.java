package com.team.musicplayer.ui.favourite;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.ui.ListFragment;

public class FavouritesFragment extends ListFragment {

    private FavouriteAdapter adapter;
    private FavouritesViewModel viewModel;

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        return adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FavouriteAdapter();
        viewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        viewModel.favourites.observe(this, adapter::submitList);
        new Handler().postDelayed(viewModel::findAll, 500);
    }
}
