package com.team.musicplayer.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.team.musicplayer.BR;
import com.team.musicplayer.model.entity.Identifiable;

import java.lang.ref.WeakReference;
import java.util.Objects;

public abstract class AbstractPagedListAdapter<T extends Identifiable> extends PagedListAdapter<T, AbstractPagedListAdapter.AbstractViewHolder<T>> {

    private AdapterItemClickListener adapterItemClickListener;

    protected abstract int layoutRes();

    public AbstractPagedListAdapter() {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return Objects.equals(oldItem.getIdentity(), newItem.getIdentity());
            }

            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    @NonNull
    @Override
    public AbstractViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutRes(), parent, false);
        return new AbstractViewHolder<>(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }

    public T getItemAt(int position) {
        return getItem(position);
    }


    protected static class AbstractViewHolder<T extends Identifiable> extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        private WeakReference<AbstractPagedListAdapter<T>> reference;

        AbstractViewHolder(@NonNull ViewDataBinding binding, AbstractPagedListAdapter<T> adapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.reference = new WeakReference<>(adapter);

            itemView.setOnClickListener(v -> {
                if (reference.get() != null && reference.get().adapterItemClickListener != null) {
                    reference.get().adapterItemClickListener.onClick(getAdapterPosition());
                }
            });
        }

        public void bind(T obj) {
            binding.setVariable(BR.obj, obj);
        }

        public void bindVariable(int variableId, Object value) {
            binding.setVariable(variableId, value);
        }
    }
}
