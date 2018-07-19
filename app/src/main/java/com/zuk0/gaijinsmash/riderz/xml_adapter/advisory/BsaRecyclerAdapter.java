package com.zuk0.gaijinsmash.riderz.xml_adapter.advisory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BSA;

import java.util.List;

import butterknife.BindView;

public class BsaRecyclerAdapter extends RecyclerView.Adapter<BsaRecyclerAdapter.ViewHolder>  {

    private List<BSA> mAdvisoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bsa_station_textView) TextView stationName;
        @BindView(R.id.bsa_type_textView) TextView bsaType;
        @BindView(R.id.bsa_description_textView) TextView bsaDescription;

        ViewHolder(View view) {
            super(view);
        }
    }

    public BsaRecyclerAdapter(List<BSA> advisoryList) { mAdvisoryList = advisoryList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_advisory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BSA bsa = mAdvisoryList.get(position);
        //Todo: add date
        holder.stationName.setText(bsa.getStation());
        holder.bsaType.setText(bsa.getType());
        holder.bsaDescription.setText(bsa.getDescription());
    }

    @Override
    public int getItemCount() {
        return mAdvisoryList.size();
    }
}
