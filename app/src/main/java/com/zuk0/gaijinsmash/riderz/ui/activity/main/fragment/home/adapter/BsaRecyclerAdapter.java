package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa;
import com.zuk0.gaijinsmash.riderz.databinding.ListRowAdvisoryBinding;

import java.util.List;

public class BsaRecyclerAdapter extends RecyclerView.Adapter<BsaRecyclerAdapter.ViewHolder>  {

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ListRowAdvisoryBinding mAdvisoryBinding;

        ViewHolder(final ListRowAdvisoryBinding viewBinding) {
            super(viewBinding.getRoot());
            this.mAdvisoryBinding = viewBinding;
        }
    }

    private List<Bsa> mAdvisoryList;
    public BsaRecyclerAdapter(List<Bsa> bsaList) { mAdvisoryList = bsaList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListRowAdvisoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_row_advisory, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bsa bsa = mAdvisoryList.get(position);
        if(bsa != null) {
            holder.mAdvisoryBinding.setBsa(bsa);
        }

        if(bsa.getStation() == null) {
            holder.mAdvisoryBinding.bsaStationTextView.setVisibility(View.GONE);
        }
        if(bsa.getType() == null) {
            holder.mAdvisoryBinding.bsaStationTextView.setVisibility(View.GONE);
        } else if(bsa.getType().equalsIgnoreCase("DELAY")) {
            holder.mAdvisoryBinding.bsaStatusImageView.setImageDrawable(holder.mAdvisoryBinding.getRoot().getContext().getDrawable(R.drawable.ic_error_outline_black_24dp));
            holder.mAdvisoryBinding.bsaStatusImageView.setColorFilter(R.color.colorTextAlert);
        }

        // Reveal animation
        holder.mAdvisoryBinding.container.setAnimation(AnimationUtils.loadAnimation(holder.mAdvisoryBinding.getRoot().getContext(), R.anim.slide_in_left));

        holder.mAdvisoryBinding.bsaDismissBtn.setOnClickListener(view -> {
            if(mAdvisoryList.size() == 1) {
                holder.mAdvisoryBinding.container.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                holder.mAdvisoryBinding.container.setVisibility(View.GONE);
                            }
                        })
                        .start();
                mAdvisoryList.clear();
            } else {
                mAdvisoryList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdvisoryList.size();
    }

    public void update(List<Bsa> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BsaDiffCallback(mAdvisoryList, newData));

        mAdvisoryList.clear();
        mAdvisoryList.addAll(newData);
        diffResult.dispatchUpdatesTo(this);
    }

    class BsaDiffCallback extends DiffUtil.Callback {

        private List<Bsa> oldList;
        private List<Bsa> newList;

        BsaDiffCallback(List<Bsa> oldList, List<Bsa> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
