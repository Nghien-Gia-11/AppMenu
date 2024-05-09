package com.example.admin.model

class RevenueMonth(
    private var months: Int,
    private var totalRevenue: Float
) {

    fun getMonth(): Int {
        return months
    }

    fun setMonth(months: Int) {
        this.months = months
    }

    fun getTotalRevenue(): Float {
        return totalRevenue
    }

    fun setTotalRevenue(totalRevenue: Float) {
        this.totalRevenue = totalRevenue
    }

}