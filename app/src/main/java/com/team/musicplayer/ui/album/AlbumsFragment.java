package com.team.musicplayer.ui.album;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.MainActivity;
import com.team.musicplayer.R;
import com.team.musicplayer.model.entity.Album;
import com.team.musicplayer.model.repo.datasource.SongByAlbumDataSource;
import com.team.musicplayer.ui.ListFragment;
import com.team.musicplayer.ui.song.SongAdapter;
import com.team.musicplayer.ui.song.SongsByAlbumFragment;
import com.team.musicplayer.ui.song.SongsViewModel;

public class AlbumsFragment extends ListFragment {

    private AlbumAdapter adapter;
    private AlbumsViewModel viewModel;

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        return adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AlbumAdapter();
        viewModel = ViewModelProviders.of(this).get(AlbumsViewModel.class);
        viewModel.albums.observe(this, list -> {
            adapter.submitList(list);
        });

        adapter.setAdapterItemClickListener(pos -> {
            Album album = adapter.getItemAt(pos);
            Bundle bundle = new Bundle();
            bundle.putLong(SongsByAlbumFragment.KEY_ALBUM_ID, album.getAlbumId());

            Navigation.findNavController(getView()).navigate(R.id.action_albumsFragment_to_songsByAlbumFragment, bundle);
        });

        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Albums");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Handler().postDelayed(viewModel::findAll, 500);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
    }
}
