package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        }
    }

    @Override
    public int getItemCount() {
        return mAdvisoryList.size();
    }
}
