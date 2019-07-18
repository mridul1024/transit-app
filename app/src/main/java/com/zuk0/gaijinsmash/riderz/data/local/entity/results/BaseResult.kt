package com.zuk0.gaijinsmash.riderz.data.local.entity.results

abstract class BaseResult {

    enum class Status { LOADING, COMPLETE, ERROR }

    var status: Status = Status.LOADING //default
}