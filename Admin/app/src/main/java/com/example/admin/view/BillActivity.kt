package com.example.admin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.admin.R
import com.example.admin.adapter.AdapterRecyclerBill
import com.example.admin.adapter.AdapterRecyclerDetailBill
import com.example.admin.api.ApiService
import com.example.admin.databinding.ActivityBillBinding
import com.example.admin.databinding.LayoutCustomDialogBinding
import com.example.admin.model.Bill
import com.example.admin.model.DetailBill
import com.example.admin.onClickInterface.OnClickBill
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillActivity : AppCompatActivity() {

    private lateinit var h2o : ActivityBillBinding
    private lateinit var adapterRecyclerBill : AdapterRecyclerBill
    private lateinit var adapterRecyclerDetailBill: AdapterRecyclerDetailBill
    private lateinit var diaLogBinding : LayoutCustomDialogBinding

    private var listBill = mutableListOf<Bill>()
    private var listDetailBill = mutableListOf<DetailBill>()
    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        h2o = ActivityBillBinding.inflate(layoutInflater)
        setContentView(h2o.root)
        setSupportActionBar(h2o.toolbarMenu)
        addDataListBill()
        setAdapterRecyclerBill()
        searchIdBill()
    }


    private fun addDataListBill() {
        ApiService.create().getAllBill().enqueue(object : Callback<MutableList<Bill>>{
            override fun onResponse(
                call: Call<MutableList<Bill>>,
                response: Response<MutableList<Bill>>
            ) {
                if (response.isSuccessful){
                    listBill = response.body()!!
                    setAdapterRecyclerBill()
                }
            }

            override fun onFailure(call: Call<MutableList<Bill>>, t: Throwable) {
                Toast.makeText(
                    this@BillActivity,
                    "Call API Failed",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapterRecyclerBill() {
        adapterRecyclerBill = AdapterRecyclerBill(listBill, object : OnClickBill{
            override fun onClickIdBill(pos: Int) {
                showDialogCustom(listBill[pos].idBill)
            }
        })
        h2o.recyclerBill.adapter = adapterRecyclerBill
        h2o.recyclerBill.layoutManager = StaggeredGridLayoutManager(
            1,
            StaggeredGridLayoutManager.VERTICAL)
    }

    private fun searchIdBill() {
        h2o.btnSearch.setOnClickListener {
            if (h2o.edtSearch.text.isNotEmpty()){
                ApiService.create().searchDetailBillByIdBill(h2o.edtSearch.text.toString()).enqueue(object : Callback<MutableList<Bill>>{
                    override fun onResponse(
                        call: Call<MutableList<Bill>>,
                        response: Response<MutableList<Bill>>
                    ) {
                        if (response.isSuccessful){
                            listBill = response.body()!!
                            setAdapterRecyclerBill()
                        }
                    }

                    override fun onFailure(call: Call<MutableList<Bill>>, t: Throwable) {
                        Toast.makeText(
                            this@BillActivity,
                            "Không tìm thấy mã hóa đơn !",
                            Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                addDataListBill()
            }
        }
    }

    private fun showDialogCustom(idBill: String) {
        val build = AlertDialog.Builder(this@BillActivity)
        diaLogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(this@BillActivity))
        build.setView(diaLogBinding.root)
        diaLogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        ApiService.create().getDetailBill(idBill).enqueue(object : Callback<MutableList<DetailBill>>{
            override fun onResponse(
                call: Call<MutableList<DetailBill>>,
                response: Response<MutableList<DetailBill>>
            ) {
                if (response.isSuccessful){
                    listDetailBill = response.body()!!
                    if (listDetailBill.isNotEmpty()){
                        diaLogBinding.txtIdBill.text = listDetailBill[1].idFood
                        diaLogBinding.txtCustomer.text = listDetailBill[1].idCustomer
                        diaLogBinding.txtTimeBill.text = listDetailBill[1].createAt.toString()
                        var total = 0f
                        for (value in listDetailBill){
                            total += value.thanhTien
                        }
                        diaLogBinding.txtTotalBill.text = total.toString()
                        setAdapterRecyclerDetailBill()
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<DetailBill>>, t: Throwable) {
                Toast.makeText(this@BillActivity,"Không truy cập được ",Toast.LENGTH_SHORT).show()
            }
        })

        dialog = build.create()
        dialog.show()
    }

    private fun setAdapterRecyclerDetailBill(){
        adapterRecyclerDetailBill = AdapterRecyclerDetailBill(listDetailBill)
        diaLogBinding.recyclerDetailBill.adapter = adapterRecyclerDetailBill
        diaLogBinding.recyclerDetailBill.layoutManager = StaggeredGridLayoutManager(
            1,
            StaggeredGridLayoutManager.VERTICAL)
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