package com.example.admin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var h2o : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        h2o = ActivityMainBinding.inflate(layoutInflater)
        setContentView(h2o.root)

        btnAddFood()
        btnRevenue()
        btnRate()
        btnBill()


    }

    private fun btnBill() {
        h2o.btnBill.setOnClickListener {
            var intentBill = Intent(this, BillActivity::class.java)

            startActivity(intentBill)
        }
    }

    private fun btnRate() {
        h2o.btnRate.setOnClickListener {
            var intentRate = Intent(this, RateActivity::class.java)

            startActivity(intentRate)
        }
    }

    private fun btnRevenue() {
        h2o.btnRevenue.setOnClickListener {
            var intentRevenue = Intent(this, RevenueActivity::class.java)

            startActivity(intentRevenue)
        }
    }

    private fun btnAddFood() {
        h2o.btnAddFood.setOnClickListener {
            var intentAddFood = Intent(this, AddFoodActivity::class.java)

            startActivity(intentAddFood)
        }
    }
}