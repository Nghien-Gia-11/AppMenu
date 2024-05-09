package com.example.admin.model

class RevenueDay(
    private var days: Int,
    private var totalRevenue: Float
) {

    fun getDays(): Int {
        return days
    }

    fun setDays(months: Int) {
        this.days = months
    }

    fun getTotalRevenue(): Float {
        return totalRevenue
    }

    fun setTotalRevenue(totalRevenue: Float) {
        this.totalRevenue = totalRevenue
    }

}