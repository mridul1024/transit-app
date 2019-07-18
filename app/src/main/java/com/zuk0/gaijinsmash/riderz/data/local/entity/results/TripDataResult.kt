package com.zuk0.gaijinsmash.riderz.data.local.entity.results

class TripDataResult : BaseResult() {

    var origin = ""
        set(value) {
            field = value
            updateStatus()
        }

    var destination = ""
        set(value) {
            field = value
            updateStatus()
        }

    private fun updateStatus() {
        if(origin.isBlank() || destination.isBlank())
            status = Status.LOADING

        if(origin.isNotBlank() && destination.isNotBlank())
            status = Status.COMPLETE
    }
}