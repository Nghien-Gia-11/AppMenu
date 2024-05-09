package com.example.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.databinding.LayoutItemRateFoodBinding
import com.example.admin.model.FoodInRate
import com.example.admin.onClickInterface.OnClickRateFood

class AdapterRecyclerRateFood(private val listFood : List<FoodInRate>, private var onClick : OnClickRateFood) :  RecyclerView.Adapter<AdapterRecyclerRateFood.ViewHolder>(){

    private lateinit var h2o : LayoutItemRateFoodBinding

    inner class ViewHolder(h2o : LayoutItemRateFoodBinding) : RecyclerView.ViewHolder(h2o.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
        h2o = LayoutItemRateFoodBinding.inflate(view, parent, false)
        return ViewHolder(h2o)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            h2o.txtIdFood.text = listFood[position].getIdFood()
            h2o.txtNameFood.text = listFood[position].getNameFood()
            h2o.txtValues.text = listFood[position].getValues().toString()
            holder.itemView.setOnClickListener {
                onClick.onClickFood(position)
            }
        }
    }

}