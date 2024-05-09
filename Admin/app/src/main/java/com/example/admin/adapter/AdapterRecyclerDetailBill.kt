package com.example.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.databinding.LayoutItemsDetailBillBinding
import com.example.admin.model.DetailBill

class AdapterRecyclerDetailBill(private val listFood : List<DetailBill>) : RecyclerView.Adapter<AdapterRecyclerDetailBill.ViewHolder>(){

    private lateinit var h2o : LayoutItemsDetailBillBinding

    inner class ViewHolder(h2o : LayoutItemsDetailBillBinding) : RecyclerView.ViewHolder(h2o.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
        h2o = LayoutItemsDetailBillBinding.inflate(view, parent, false)
        return ViewHolder(h2o)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            h2o.txtIdFood.text = listFood[position].idFood
            h2o.txtAmount.text = listFood[position].amount.toString()
            h2o.txtNameFood.text = listFood[position].nameFood
            h2o.txtTotalCost.text = listFood[position].thanhTien.toString()
        }
    }

}