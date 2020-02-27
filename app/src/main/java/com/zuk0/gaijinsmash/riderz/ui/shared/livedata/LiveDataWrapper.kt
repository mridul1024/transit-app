package com.zuk0.gaijinsmash.riderz.ui.shared.livedata

class LiveDataWrapper<T> private
constructor(val status: Status,
            val data: T,
            val msg: String?) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        @JvmStatic
        fun <T> success(data: T): LiveDataWrapper<*> {
            return LiveDataWrapper<Any?>(Status.SUCCESS, data, null)
        }

        @JvmStatic
        fun <T> error(data: T, msg: String?): LiveDataWrapper<*> {
            return LiveDataWrapper<Any?>(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T, msg: String?): LiveDataWrapper<*> {
            return LiveDataWrapper<Any?>(Status.LOADING, data, msg)
        }
    }

}