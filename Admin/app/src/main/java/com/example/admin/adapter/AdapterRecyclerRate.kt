package com.example.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.databinding.LayoutItemRateBinding
import com.example.admin.model.Rate

class AdapterRecyclerRate(private val listRate : List<Rate>) : RecyclerView.Adapter<AdapterRecyclerRate.ViewHolder>(){

    private lateinit var h2o : LayoutItemRateBinding

    inner class ViewHolder(h2o : LayoutItemRateBinding) : RecyclerView.ViewHolder(h2o.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
        h2o = LayoutItemRateBinding.inflate(view, parent, false)
        return ViewHolder(h2o)
    }

    override fun getItemCount(): Int {
        return listRate.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            h2o.txtTime.text = listRate[position].getCreateAt().toString()
            h2o.txtNameFood.text = listRate[position].getNameFood()
            h2o.txtComment.text = listRate[position].getComment()
            h2o.ratingBar.rating = listRate[position].getValueRate().toFloat()
        }
    }
}