package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.zuk0.gaijinsmash.riderz.R.*
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.databinding.ListColEstimateBinding
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils
import java.util.*

class EstimateRecyclerAdapter(val estimateList: MutableList<Estimate>) : Adapter<EstimateRecyclerAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListColEstimateBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    private val timers: MutableList<CountDownTimer> = ArrayList()
    private lateinit var binding: ListColEstimateBinding

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> TITLE_VIEW_TYPE
            else -> ETD_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListColEstimateBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estimate = estimateList[position]

        //set car length of train
        val carLength = estimate.length.toString() + " car"
        holder.binding.estimateResultCarTv.text = carLength

        //set time remaining
        holder.binding.estimateResultMinutesTv.text = estimate.minutes

        val minutes: String? = estimate.minutes
        if (!minutes.equals("Leaving", ignoreCase = true)) {
            timers.add(beginTimer(holder.binding.estimateResultMinutesTv, minutes?.toInt() as Int, this, position))
        } else {
            holder.binding.estimateResultMinutesTv.text = "0"
        }

        BartRoutesUtils.setLineBarByColor(holder.binding.background.context, estimate.color, holder.binding.estimateResultTrainIcon)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        Log.i("recycler", "onDetachedFromRecyclerView")
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        destroyTimers()
        Log.d("onDetachedFromView", "timers cleared")
    }

    override fun getItemCount(): Int {
        return estimateList.size
    }

    fun update(newData: List<Estimate>) {
        val diffResult = DiffUtil.calculateDiff(EstimateDiffCallback(estimateList, newData))
        estimateList.clear()
        estimateList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    fun destroyTimers() {
        for (timer in timers) {
            timer.cancel()
        }
        timers.clear()
    }

    private fun beginTimer(textView: TextView, minutesLeft: Int, adapter: EstimateRecyclerAdapter, position: Int): CountDownTimer {
        val untilFinished = (minutesLeft * 60000).toLong()
        return object : CountDownTimer(untilFinished, 1000) {
            var minutes = textView.context.resources.getString(string.minutes)
            override fun onTick(millisUntilFinished: Long) {
                val remainingTime = (millisUntilFinished / 60000).toString() + " " + minutes
                textView.text = remainingTime
            }

            override fun onFinish() {
                textView.text = textView.context.resources.getString(string.leaving)
                //todo show swipe to dismiss and/or button


                textView.postDelayed({
                    val newList: MutableList<Estimate> = ArrayList(estimateList)
                    newList.removeAt(position)
                    update(newList)
                }, 5000)
            }
        }.start()
    }

    companion object {
        private const val TITLE_VIEW_TYPE = 1000
        private const val ETD_VIEW_TYPE = 2000
        private const val FAV_VIEW_TYPE = 2000
    }

}