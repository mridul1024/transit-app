package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EstimateRecyclerAdapter extends RecyclerView.Adapter<EstimateRecyclerAdapter.ViewHolder>{

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView origin;
        private TextView destination;
        private TextView minutes;
        private TextView line;
        private TextView length;
        private TextView trainHeader;

        ViewHolder(View view) {
            super(view);
            origin = view.findViewById(R.id.etd_originTv);
            destination = view.findViewById(R.id.etd_destinationTv);
            minutes = view.findViewById(R.id.etd_minutesTv);
            line = view.findViewById(R.id.etd_colored_line);
            length = view.findViewById(R.id.etd_car_tv);
            trainHeader = view.findViewById(R.id.etd_trainHeader);
        }
    }

    private List<Estimate> mEstimateList;
    private Etd mEtd;

    public EstimateRecyclerAdapter(List<Estimate> estimateList) {
        mEstimateList = estimateList;
    }

    private List<CountDownTimer> timers = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_estimate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estimate estimate = mEstimateList.get(position);
        holder.origin.setText(estimate.getOrigin());
        holder.trainHeader.setText(estimate.getTrainHeaderStation());
        holder.destination.setText(estimate.getDestination());

        String minutes = estimate.getMinutes();
        if(!minutes.equalsIgnoreCase("Leaving")) {
            timers.add(beginTimer(holder.minutes, Integer.valueOf(minutes), this, position));
        } else {
            holder.minutes.setText(R.string.leaving);
        }
        String sb = String.valueOf(estimate.getLength())
                + " "
                + "car";
        holder.length.setText(sb);
        BartRoutesUtils.setLineBarByColor(holder.line.getContext(), estimate.getColor(), holder.line);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.i("recycler", "onDetachedFromRecyclerView");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        timers.clear();
        Log.d("onDetachedFromView", "timers cleared");
    }

    @Override
    public int getItemCount() {
        return mEstimateList.size();
    }

    public void destroyTimers() {
        for(CountDownTimer timer: timers) {
            timer.cancel();
        }
        timers.clear();
    }

    private CountDownTimer beginTimer(TextView textView, int minutesLeft, EstimateRecyclerAdapter adapter, int position) {
        long untilFinished = (long) (minutesLeft * 60000);
        return new CountDownTimer(untilFinished, 1000) {
            String minutes = textView.getContext().getResources().getString(R.string.minutes);

            @Override
            public void onTick(long millisUntilFinished) {
                String remainingTime = String.valueOf(millisUntilFinished / 60000)+ " " + minutes;
                textView.setText(remainingTime);
            }

            @Override
            public void onFinish() {
                textView.setText(textView.getContext().getResources().getString(R.string.leaving));
            }
        }.start();
    }
}
