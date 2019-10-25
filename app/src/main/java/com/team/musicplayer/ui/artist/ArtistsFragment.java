package com.team.musicplayer.ui.artist;

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
import com.team.musicplayer.model.entity.Artist;
import com.team.musicplayer.ui.ListFragment;
import com.team.musicplayer.ui.album.AlbumAdapter;
import com.team.musicplayer.ui.album.AlbumsViewModel;
import com.team.musicplayer.ui.song.SongsByArtistFragment;

public class ArtistsFragment extends ListFragment {

    private ArtistAdapter adapter;
    private ArtistsViewModel viewModel;

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        return adapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArtistAdapter();
        viewModel = ViewModelProviders.of(this).get(ArtistsViewModel.class);
        viewModel.artists.observe(this, list -> {
            adapter.submitList(list);
        });

        adapter.setAdapterItemClickListener(pos -> {
            Artist artist = adapter.getItemAt(pos);
            Bundle bundle = new Bundle();
            bundle.putLong(SongsByArtistFragment.KEY_ARTIST_ID, artist.getArtistId());

            Navigation.findNavController(getView()).navigate(R.id.action_artistsFragment_to_songsByArtistFragment, bundle);
        });

        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Artists");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Handler().postDelayed(viewModel::findAll, 500);
    }

}
