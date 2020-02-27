package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa
import com.zuk0.gaijinsmash.riderz.databinding.ListRowAdvisoryBinding

class BsaRecyclerAdapter(private val mAdvisoryList: MutableList<Bsa>) : RecyclerView.Adapter<BsaRecyclerAdapter.ViewHolder>() {
    class ViewHolder(val mAdvisoryBinding: ListRowAdvisoryBinding) : RecyclerView.ViewHolder(mAdvisoryBinding.root)

    private var recyclerView: RecyclerView? = null
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListRowAdvisoryBinding>(LayoutInflater.from(parent.context), R.layout.list_row_advisory, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bsa = mAdvisoryList[position]
        if (bsa != null) {
            holder.mAdvisoryBinding.bsa = bsa
            if (bsa.station == null) {
                holder.mAdvisoryBinding.bsaStationTextView.visibility = View.GONE
            }
            if (bsa.type == null) {
                holder.mAdvisoryBinding.bsaTypeTextView.visibility = View.GONE
            } else if (bsa.type.equals(DELAY, ignoreCase = true)) {
                holder.mAdvisoryBinding.bsaTypeTextView.visibility = View.VISIBLE
                holder.mAdvisoryBinding.bsaStatusImageView.setImageDrawable(holder.mAdvisoryBinding.root.context.getDrawable(R.drawable.ic_error_outline_black_24dp))
                holder.mAdvisoryBinding.bsaStatusImageView.setColorFilter(R.color.colorTextAlert)
            }
        }
        // Reveal animation
        holder.mAdvisoryBinding.container.animation = AnimationUtils.loadAnimation(holder.mAdvisoryBinding.root.context, R.anim.slide_in_left)
        holder.mAdvisoryBinding.bsaDismissBtn.setOnClickListener { view: View? ->
            holder.mAdvisoryBinding.container
                    .animate()
                    .translationX(0f)
                    .setDuration(500)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            holder.mAdvisoryBinding.container.visibility = View.GONE
                            mAdvisoryList.removeAt(position)
                            notifyItemRemoved(position)
                            if (mAdvisoryList.size == 0) { //todo hide recyclerview
                                if (recyclerView != null) recyclerView!!.visibility = View.GONE
                            }
                        }
                    })
                    .start()
        }
    }

    override fun getItemCount(): Int {
        return mAdvisoryList.size
    }

    fun update(newData: List<Bsa>) {
        val diffResult = DiffUtil.calculateDiff(BsaDiffCallback(mAdvisoryList, newData))
        mAdvisoryList.clear()
        mAdvisoryList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    internal inner class BsaDiffCallback(private val oldList: List<Bsa>, private val newList: List<Bsa>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    companion object {
        private const val DELAY = "DELAY"
    }

}