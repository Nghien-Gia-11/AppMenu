package com.example.admin.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.GONE
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.AdapterView.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.admin.R
import com.example.admin.api.ApiService
import com.example.admin.databinding.ActivityRevenueBinding
import com.example.admin.model.RevenueDay
import com.example.admin.model.RevenueMonth
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RevenueActivity : AppCompatActivity() {

    private lateinit var h2o: ActivityRevenueBinding
    private lateinit var years : Array<String>
    private lateinit var month : Array<String>
    private lateinit var day : Array<String>
    private lateinit var itemDay : List<String>
    private lateinit var itemMonth : String
    private lateinit var adapterDay : ArrayAdapter<String>
    private lateinit var itemYears : String
    private lateinit var lsDataMonth : List<RevenueMonth>
    private lateinit var lsDataDays : List<RevenueDay>
    private var hashMonth = HashMap<Int, Float>()
    private var hashDays = HashMap<Int, Float>()
    private var check = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        h2o = ActivityRevenueBinding.inflate(layoutInflater)
        setContentView(h2o.root)
        setSupportActionBar(h2o.toolbarMenu)

        setSpinner()

        getMonthAndDay()

        h2o.btnRevenue.setOnClickListener {
            if (itemMonth.isEmpty()){
                revenueMonth()
            }
            else{
                revenueDays()
                Log.e("thang",itemMonth)
            }
        }
    }

    private fun revenueDays() {
        ApiService.create().getRevenueByDay(
            itemYears.toInt(),
            itemMonth.toInt(),
            itemDay[0].toInt(),
            itemDay[1].toInt()
        ).enqueue(object : Callback<MutableList<RevenueDay>>{
            override fun onResponse(
                call: Call<MutableList<RevenueDay>>,
                response: Response<MutableList<RevenueDay>>
            ) {
                if (response.isSuccessful){
                    lsDataDays = response.body()!!
                    for (values in lsDataDays){
                        hashDays[values.getDays()] = values.getTotalRevenue()
                    }
                    for (days in itemDay[0].toInt() .. itemDay[1].toInt()){
                        if (!hashDays.keys.contains(days)){
                            hashDays[days] = 0f
                        }
                    }
                    setDataBarChart(hashDays, 0)
                }
            }

            override fun onFailure(call: Call<MutableList<RevenueDay>>, t: Throwable) {
                Toast.makeText(this@RevenueActivity, "Call API FAILED", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun revenueMonth() {
        ApiService.create().getRevenueByMonth(itemYears.toInt()).enqueue(object : Callback<MutableList<RevenueMonth>>{
            override fun onResponse(
                call: Call<MutableList<RevenueMonth>>,
                response: Response<MutableList<RevenueMonth>>
            ) {
                if (response.isSuccessful){
                    lsDataMonth = response.body()!!

                    for (values in lsDataMonth){
                        hashMonth[values.getMonth()] = values.getTotalRevenue()
                    }
                    for (month in 1 .. 12){
                        if (!hashMonth.keys.contains(month)){
                            hashMonth[month] = 0f
                        }
                    }
                    setDataBarChart(hashMonth, 1)
                }
            }

            override fun onFailure(call: Call<MutableList<RevenueMonth>>, t: Throwable) {
                Toast.makeText(this@RevenueActivity,"Call api failed", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun setDataBarChart(hash: HashMap<Int, Float>, check : Int) {
        val list: ArrayList<BarEntry> = ArrayList()
        for (cnt in hash.keys){
            list.add(BarEntry(cnt.toFloat(),hash[cnt]!!))
        }

        val barDataSet : BarDataSet = if (check == 1){
            BarDataSet(list, "các tháng")
        } else{
            BarDataSet(list, "các ngày")
        }
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        val labels = resources.getStringArray(R.array.month)
        barDataSet.valueTextColor = Color.BLACK
        val xAxis = h2o.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.textSize = 9f

        val barData = BarData(barDataSet)

        h2o.barChart.setFitBars(true)

        h2o.barChart.data = barData


        h2o.barChart.description.text = ""

        h2o.barChart.animateY(200)


    }


    private fun getMonthAndDay() {
        h2o.day.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                itemDay = if (p0?.getItemAtPosition(p2).toString().isNotEmpty()){
                    p0?.getItemAtPosition(p2).toString().split("-")
                } else{
                    listOf("","")
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        h2o.month.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                itemMonth = p0?.getItemAtPosition(p2).toString()
                if (itemMonth.isEmpty()){
                    h2o.day.visibility = GONE
                }
                else{
                    h2o.day.visibility = VISIBLE
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        h2o.years.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    itemYears = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }





    private fun setSpinner() {
        years = resources.getStringArray(R.array.years)
        val adapterYears = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            years
        )
        h2o.years.adapter = adapterYears
        month = resources.getStringArray(R.array.month)
        val adapterMonth = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            month)
        h2o.month.adapter = adapterMonth
        day = resources.getStringArray(R.array.day)
        adapterDay = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            day)
        h2o.day.adapter = adapterDay
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnuBack) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}