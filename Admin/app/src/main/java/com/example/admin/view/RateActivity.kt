package com.example.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.admin.R
import com.example.admin.adapter.AdapterRecyclerRate
import com.example.admin.adapter.AdapterRecyclerRateFood
import com.example.admin.api.ApiService
import com.example.admin.databinding.ActivityRateBinding
import com.example.admin.databinding.LayoutCustomDialogRateFoodBinding
import com.example.admin.databinding.LayoutCustomDialogRateStarBinding
import com.example.admin.model.Rate
import com.example.admin.model.FoodInRate
import com.example.admin.model.TimesStar
import com.example.admin.onClickInterface.OnClickRateFood
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RateActivity : AppCompatActivity() {

    private lateinit var h2o : ActivityRateBinding
    private lateinit var dialog: AlertDialog
    private lateinit var diaLogRateStarBinding : LayoutCustomDialogRateStarBinding
    private lateinit var diaLogRateFoodBinding : LayoutCustomDialogRateFoodBinding
    private var listFoodInRate = mutableListOf<FoodInRate>()
    private var listRate = mutableListOf<Rate>()
    private lateinit var adapterRecyclerRateFood: AdapterRecyclerRateFood
    private lateinit var adapterRecyclerRate: AdapterRecyclerRate
    private var listTimes = mutableListOf<TimesStar>()
    private var hash = HashMap<Int,Int>()
    private var star : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        h2o = ActivityRateBinding.inflate(layoutInflater)
        setContentView(h2o.root)
        setSupportActionBar(h2o.toolbarMenu)

        getAllRate()

        h2o.btnAll.setOnClickListener {
            getAllRate()
        }

        h2o.btnStar.setOnClickListener {
            getRateByStar()
        }

        h2o.btnFood.setOnClickListener {
            getRateByFood()
        }

    }
    // hiện ra dialog để chọn các món trong danh sách
    private fun getRateByFood() {
        val build = AlertDialog.Builder(this@RateActivity)
        diaLogRateFoodBinding = LayoutCustomDialogRateFoodBinding.inflate(LayoutInflater.from(this@RateActivity))
        build.setView(diaLogRateFoodBinding.root)
        callApiGetRateByFood()
        diaLogRateFoodBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog = build.create()
        dialog.show()
    }

    // lấy ra toàn bộ món ăn trong đánh giá và khi click vào 1 món ăn sẽ hiện ra đánh giá theo món ăn đó
    private fun callApiGetRateByFood() {
        ApiService.create().getAllFoodInRate().enqueue(object : Callback<MutableList<FoodInRate>>{
            override fun onResponse(
                call: Call<MutableList<FoodInRate>>,
                response: Response<MutableList<FoodInRate>>
            ) {
                if (response.isSuccessful){
                    listFoodInRate = response.body()!!
                    setAdapterRecyclerRateFood()
                }
            }

            override fun onFailure(call: Call<MutableList<FoodInRate>>, t: Throwable) {
                Toast.makeText(this@RateActivity, "Không kết nối!!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // lấy ra toàn bộ món ăn trong đánh giá và khi click vào 1 món ăn sẽ hiện ra đánh giá theo món ăn đó
    private fun setAdapterRecyclerRateFood(){
        adapterRecyclerRateFood = AdapterRecyclerRateFood(listFoodInRate, object : OnClickRateFood{
            override fun onClickFood(pos: Int) {
                ApiService.create().getRateByFood(listFoodInRate[pos].getIdFood()).enqueue(object : Callback<MutableList<Rate>>{
                    override fun onResponse(
                        call: Call<MutableList<Rate>>,
                        response: Response<MutableList<Rate>>
                    ) {
                        if (response.isSuccessful){
                            listRate = response.body()!!
                            setAdapterRecyclerRate()
                        }
                    }

                    override fun onFailure(call: Call<MutableList<Rate>>, t: Throwable) {

                    }

                })
                dialog.dismiss()
            }

        })

        diaLogRateFoodBinding.recyclerRateByFood.adapter = adapterRecyclerRateFood
        diaLogRateFoodBinding.recyclerRateByFood
            .layoutManager = StaggeredGridLayoutManager(
            1,
            StaggeredGridLayoutManager.VERTICAL)
        diaLogRateFoodBinding.recyclerRateByFood.
        addItemDecoration(DividerItemDecoration(
            this,DividerItemDecoration.VERTICAL))
    }

    // lấy đánh giá theo số sao được chọn từ màn hình
    private fun getRateByStar() {
        val build = AlertDialog.Builder(this@RateActivity)
        diaLogRateStarBinding = LayoutCustomDialogRateStarBinding
            .inflate(
                LayoutInflater
                    .from(this@RateActivity)
            )
        build.setView(diaLogRateStarBinding.root)
        diaLogRateStarBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        diaLogRateStarBinding.btnCancel.setOnClickListener {
            getAllRate()
            dialog.dismiss()
        }

        ApiService.create().getTimesStar().enqueue(object : Callback<MutableList<TimesStar>>{
            override fun onResponse(
                call: Call<MutableList<TimesStar>>,
                response: Response<MutableList<TimesStar>>
            ) {
                if (response.isSuccessful){
                    listTimes = response.body()!!
                }
                for (values in listTimes){
                    hash[values.getValuesRate()] = values.getTimes()
                }
                for (values in 1 .. 5){
                    if (!hash.keys.contains(values)){
                        hash[values] = 0
                    }
                }

                diaLogRateStarBinding.txt1s.text = hash[1].toString()
                diaLogRateStarBinding.txt2s.text = hash[2].toString()
                diaLogRateStarBinding.txt3s.text = hash[3].toString()
                diaLogRateStarBinding.txt4s.text = hash[4].toString()
                diaLogRateStarBinding.txt5s.text = hash[5].toString()
                diaLogRateStarBinding.btnSelect.setOnClickListener {
                    if (diaLogRateStarBinding.radRate1s.isChecked){
                        star = 1
                    }
                    if (diaLogRateStarBinding.radRate2s.isChecked){
                        star = 2
                    }
                    if (diaLogRateStarBinding.radRate3s.isChecked){
                        star = 3
                    }
                    if (diaLogRateStarBinding.radRate4s.isChecked){
                        star = 4
                    }
                    if (diaLogRateStarBinding.radRate5s.isChecked){
                        star = 5
                    }
                    ApiService.create().getRateByStar(star).enqueue(object : Callback<MutableList<Rate>>{
                        override fun onResponse(
                            call: Call<MutableList<Rate>>,
                            response: Response<MutableList<Rate>>
                        ) {
                            if (response.isSuccessful){
                                listRate = response.body()!!
                                setAdapterRecyclerRate()
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<MutableList<Rate>>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                }

            }

            override fun onFailure(call: Call<MutableList<TimesStar>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        dialog = build.create()
        dialog.show()
    }

    // lấy ra toàn bộ đánh giá
    private fun getAllRate() {
        ApiService.create().getAllRate().enqueue(object : Callback<MutableList<Rate>>{
            override fun onResponse(
                call: Call<MutableList<Rate>>,
                response: Response<MutableList<Rate>>
            ) {
                if (response.isSuccessful){
                    listRate = response.body()!!
                    setAdapterRecyclerRate()
                }
            }

            override fun onFailure(call: Call<MutableList<Rate>>, t: Throwable) {
                Toast.makeText(this@RateActivity, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun setAdapterRecyclerRate(){
        adapterRecyclerRate = AdapterRecyclerRate(listRate)
        h2o.recyclerRate.adapter = adapterRecyclerRate
        h2o.recyclerRate.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        h2o.recyclerRate.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
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