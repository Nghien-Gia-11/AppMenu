package com.example.admin.model

class TimesStar(
    private var valueRate : Int,
    private var times : Int
) {

    fun getValuesRate() : Int {
        return valueRate
    }

    fun setValuesRate(valueRate : Int) {
        this.valueRate = valueRate
    }

    fun getTimes() : Int {
        return times
    }

    fun setTimes(times : Int) {
        this.times = valueRate
    }
}