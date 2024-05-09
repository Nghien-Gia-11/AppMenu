package com.example.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.databinding.LayoutItemsBillBinding
import com.example.admin.model.Bill
import com.example.admin.onClickInterface.OnClickBill

class AdapterRecyclerBill(private val listBill : List<Bill>,private val onClick : OnClickBill) : RecyclerView.Adapter<AdapterRecyclerBill.ViewHolder>() {

    private lateinit var h2o : LayoutItemsBillBinding

    inner class ViewHolder(h2o : LayoutItemsBillBinding) : RecyclerView.ViewHolder(h2o.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
        h2o = LayoutItemsBillBinding.inflate(view, parent, false)
        return ViewHolder(h2o)
    }

    override fun getItemCount(): Int {
        return listBill.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            h2o.btnIdBill.text = listBill[position].idBill
            h2o.btnIdBill.setOnClickListener {
                onClick.onClickIdBill(position)
            }
        }

    }
}