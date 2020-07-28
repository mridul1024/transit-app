package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.bsa

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa
import com.zuk0.gaijinsmash.riderz.databinding.ListRowBsaBinding
import com.zuk0.gaijinsmash.riderz.databinding.UserRatingLayoutBinding
import com.zuk0.gaijinsmash.riderz.utils.CrashLogUtil
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import java.sql.Date

class BsaRecyclerAdapter(private val mAdvisoryList: MutableList<Bsa>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class BsaViewHolder(val mAdvisoryBinding: ListRowBsaBinding) : RecyclerView.ViewHolder(mAdvisoryBinding.root)
    class UserRatingViewHolder(val userRatingLayoutBinding: UserRatingLayoutBinding) : RecyclerView.ViewHolder(userRatingLayoutBinding.root.rootView)

    private var isUserRatingVisible = false
    private var isUserRatingAlreadyDone = false

    private var recyclerView: RecyclerView? = null
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> {
                return if (isUserRatingVisible) {
                    USER_RATING_TYPE
                } else {
                    BSA_TYPE
                }
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            USER_RATING_TYPE -> {
                val binding = UserRatingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserRatingViewHolder(binding)
            }
            else -> {
                val binding = DataBindingUtil.inflate<ListRowBsaBinding>(LayoutInflater.from(parent.context), R.layout.list_row_bsa, parent, false)
                BsaViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BsaViewHolder -> {
                val bsa = mAdvisoryList[position]
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
                // Reveal animation
                holder.mAdvisoryBinding.container.animation = AnimationUtils.loadAnimation(holder.mAdvisoryBinding.root.context, R.anim.slide_in_right)

                // Hide animation
                holder.mAdvisoryBinding.bsaDismissBtn.setOnClickListener {
                    dismissCard(holder.mAdvisoryBinding.container, position)
                }
            }
            is UserRatingViewHolder -> {
                //animate entrace
                holder.userRatingLayoutBinding.root.animation = AnimationUtils.loadAnimation(holder.userRatingLayoutBinding.root.context, R.anim.slide_in_right)


                if (isUserRatingAlreadyDone) {
                    holder.userRatingLayoutBinding.title.text = holder.userRatingLayoutBinding.root.context.getString(R.string.user_rating_title2)
                    holder.userRatingLayoutBinding.sendBtn.text = holder.userRatingLayoutBinding.root.context.getString(R.string.user_rating_forgot)
                    holder.userRatingLayoutBinding.laterBtn.text = holder.userRatingLayoutBinding.root.context.getString(R.string.user_rating_already_done)
                }
                holder.userRatingLayoutBinding.laterBtn.setOnClickListener {
                    Log.d(TAG, "Rating selected: ${holder.userRatingLayoutBinding.ratingBar.rating}")
                    if (isUserRatingAlreadyDone) {
                        updateUserRatingAction(it.context, USER_ACTION_IGNORE)
                    } else {
                        updateUserRatingAction(it.context, USER_ACTION_NO)
                    }
                    dismissCard(holder.userRatingLayoutBinding.root, position)
                }
                holder.userRatingLayoutBinding.sendBtn.setOnClickListener {
                    updateUserRatingAction(it.context, USER_ACTION_YES)
                    try {
                        val uri = Uri.parse("market://details?id=${it.context.packageName}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        it.context.startActivity(intent)
                    } catch (e: Exception) {
                        CrashLogUtil.logException(e) // user does not have GooglePlay Store
                        val uri = Uri.parse("https://play.google.com/store/apps/details?id=" + it.context?.packageName.toString())
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        it.context.startActivity(intent)
                    }
                    dismissCard(holder.userRatingLayoutBinding.root, position)
                }
            }
        }
    }

    private fun dismissCard(card: View, position: Int) {
        val listener = object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                card.visibility = View.GONE
                mAdvisoryList.removeAt(position)
                notifyItemRemoved(position)
                if (mAdvisoryList.size == 0) { //todo hide recyclerview
                    if (isUserRatingViewRequested(card.context)) {
                        notifyItemChanged(0)
                    } else {
                        recyclerView?.visibility = View.GONE
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
                //do nothing
            }

            override fun onAnimationStart(animation: Animation?) {
                // do nothing
            }
        }
        val exitAnimation = AnimationUtils.loadAnimation(card.context, R.anim.slide_out_left)
        exitAnimation.setAnimationListener(listener)
        card.startAnimation(exitAnimation)
    }

    private fun isUserRatingViewRequested(context: Context?): Boolean {
        //if user has already rated
        isUserRatingAlreadyDone = isAlreadyRated(context)
        if (isUserRatingAlreadyDone) {
            if (isRatingComplete(context)) return false //never show again

            Log.d(TAG, "User already rated app")
            if (isPassedWaitingPeriod(context)) {
                //show dialog again with updated buttons
                return true
            }
        } else  {
            if (isRatingComplete(context)) return false //never show again
            isUserRatingVisible = true
            mAdvisoryList.add(0, Bsa())
            return true
        }
        return false
    }

    private fun isAlreadyRated(context: Context?): Boolean {
        context?.let {
            val prefs = PreferenceManager.getDefaultSharedPreferences(it)
            return prefs.getBoolean(SHARED_PREFS_KEY_USER_HAS_RATED, false)
        }
        return false
    }

    private fun isPassedWaitingPeriod(context: Context?): Boolean {
        context?.let {
            val prefs = PreferenceManager.getDefaultSharedPreferences(it)
            val timeOfLastAction = prefs.getLong(SHARED_PREFS_KEY_USER_LAST_ACTION, 0)
            if (timeOfLastAction > 0) {
                val timeNow = System.currentTimeMillis()
                val numOfDaysPassed = TimeDateUtils.durationOfDaysBetweenDates(Date(timeOfLastAction), Date(timeNow))
                return numOfDaysPassed <= DURATION_OF_DAYS
            }
        }
        return false
    }

    private fun isRatingComplete(context: Context?): Boolean {
        context?.let {
            val prefs = PreferenceManager.getDefaultSharedPreferences(it)
            return prefs.getBoolean(SHARED_PREFS_KEY_USER_COMPLETE, false)
        }
        return false
    }

    private fun updateUserRatingAction(context: Context?, action: Int) {
        context?.let {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(it)
            when (action) {
                0 -> {
                    //no
                    sharedPreferences.edit()
                            .putLong(SHARED_PREFS_KEY_USER_LAST_ACTION, System.currentTimeMillis())
                            .apply()
                }
                1 -> {
                    //yes
                    sharedPreferences.edit()
                            .putLong(SHARED_PREFS_KEY_USER_LAST_ACTION, System.currentTimeMillis())
                            .putBoolean(SHARED_PREFS_KEY_USER_HAS_RATED, true) //assumption because we can't actually track if user rated or not
                            .apply()
                }
                2 -> { //don't ever show again
                    sharedPreferences.edit()
                            .putLong(SHARED_PREFS_KEY_USER_LAST_ACTION, System.currentTimeMillis())
                            .putBoolean(SHARED_PREFS_KEY_USER_HAS_RATED, true) //assumption because we can't actually track if user rated or not
                            .putBoolean(SHARED_PREFS_KEY_USER_COMPLETE, true)
                            .apply()
                }
                else -> Log.e(TAG, "unknown action: $action")
            }
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
        private const val TAG = "BsaRecyclerAdapter"
        private const val DELAY = "DELAY"
        private const val USER_RATING_TYPE = 222
        private const val BSA_TYPE = 111
        private const val SHARED_PREFS_KEY_USER_HAS_RATED = "SHARED_PREFS_KEY_USER_HAS_RATED"
        private const val SHARED_PREFS_KEY_USER_LAST_ACTION = "SHARED_PREFS_KEY_USER_LAST_ACTION"
        private const val SHARED_PREFS_KEY_USER_COMPLETE = "SHARED_PREFS_KEY_USER_COMPLETE"
        private const val USER_ACTION_NO = 0
        private const val USER_ACTION_YES = 1
        private const val USER_ACTION_IGNORE = 2
        private const val DURATION_OF_DAYS = 30
    }

}