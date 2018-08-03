package com.zuk0.gaijinsmash.riderz.ui.adapter.bsa;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BsaRecyclerAdapter extends RecyclerView.Adapter<BsaRecyclerAdapter.ViewHolder>  {

    private List<Bsa> mAdvisoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bsa_station_textView) TextView stationName;
        @BindView(R.id.bsa_type_textView) TextView bsaType;
        @BindView(R.id.bsa_description_textView) TextView bsaDescription;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public BsaRecyclerAdapter(List<Bsa> bsaList) { mAdvisoryList = bsaList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_advisory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bsa bsa = mAdvisoryList.get(position);
        if(bsa.getStation() == null) {
            holder.stationName.setVisibility(View.GONE);
        } else {
            holder.stationName.setText(bsa.getStation());
        }
        if(bsa.getType() == null) {
            holder.bsaType.setVisibility(View.GONE);
        } else {
            holder.bsaType.setText(bsa.getType());
        }
        holder.bsaDescription.setText(bsa.getDescription());
    }

    @Override
    public int getItemCount() {
        return mAdvisoryList.size();
    }
}
