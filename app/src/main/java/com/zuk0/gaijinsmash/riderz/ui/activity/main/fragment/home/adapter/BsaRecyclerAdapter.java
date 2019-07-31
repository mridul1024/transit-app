package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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
        holder.mAdvisoryBinding.setBsa(bsa);

        if(bsa.getStation() == null) {
            holder.mAdvisoryBinding.bsaStationTextView.setVisibility(View.GONE);
        }
        if(bsa.getType() == null) {
            holder.mAdvisoryBinding.bsaStationTextView.setVisibility(View.GONE);
        } else if(bsa.getType().equalsIgnoreCase("DELAY")) {
            holder.mAdvisoryBinding.bsaStatusImageView.setImageDrawable(holder.mAdvisoryBinding.getRoot().getContext().getDrawable(R.drawable.ic_error_outline_black_24dp));
            holder.mAdvisoryBinding.bsaStatusImageView.setColorFilter(R.color.colorTextAlert);
        }

        holder.mAdvisoryBinding.bsaDismissBtn.setOnClickListener(view -> {
            //position is known so clear this from list
            mAdvisoryList.remove(position);
            notifyItemRemoved(position);
            if(mAdvisoryList.size() == 0) {
                //hide recyclerview
                View recyclerView = holder.mAdvisoryBinding.getRoot().findViewById(R.id.home_bsa_recyclerView);
                if(recyclerView != null) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdvisoryList.size();
    }
}
