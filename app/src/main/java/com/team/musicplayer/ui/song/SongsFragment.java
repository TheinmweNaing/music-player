package com.team.musicplayer.ui.song;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.MainActivity;
import com.team.musicplayer.ui.ListFragment;
import com.team.musicplayer.ui.player.MusicPlayerActivity;

public class SongsFragment extends ListFragment {

    private SongAdapter adapter;
    protected SongsViewModel viewModel;

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

        adapter.setAdapterItemClickListener(pos -> {
            /*Bundle args = new Bundle();
            args.putLong(MusicPlayerFragment.KEY_SONG_ID, adapter.getItemAt(pos).getSongId());
            args.putInt(MusicPlayerFragment.KEY_SONG_POSITION, pos);*/
            Intent intent = new Intent(requireActivity(), MusicPlayerActivity.class);
            intent.putExtra(MusicPlayerActivity.KEY_SONG_ID, adapter.getItemAt(pos).getSongId());
            intent.putExtra(MusicPlayerActivity.KEY_SONG_POSITION, pos);
            startActivity(intent);
            //Navigation.findNavController(getView()).navigate(R.id.action_songsFragment_to_musicPlayerFragment, args);
        });

        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Songs");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findSongs();
    }

    protected void findSongs() {
        new Handler().postDelayed(viewModel::findAll, 500);
    }
}
