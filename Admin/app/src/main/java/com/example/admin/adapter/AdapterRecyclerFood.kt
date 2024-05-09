package com.example.admin.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admin.R
import com.example.admin.databinding.LayoutItemFoodBinding
import com.example.admin.model.Food
import com.example.admin.onClickInterface.OnClickFood

class AdapterRecyclerFood(private val listFood : List<Food>, private val onClick : OnClickFood) : RecyclerView.Adapter<AdapterRecyclerFood.ViewHolder>() {

    private val url = "http://172.16.0.2:3333//api/images/"

    private lateinit var h2o : LayoutItemFoodBinding

    inner class ViewHolder(h2o : LayoutItemFoodBinding) : RecyclerView.ViewHolder(h2o.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
        h2o = LayoutItemFoodBinding.inflate(view, parent, false)
        return ViewHolder(h2o)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            h2o.txtIdFood.text = listFood[position].getIdFood()
            h2o.txtNameFood.text = listFood[position].getNameFood()
            h2o.txtCost.text = listFood[position].getCost().toString()
            h2o.txtCost.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            h2o.txtSalePrice.text = listFood[position].getSalePrice().toString()
            if (listFood[position].getStatus()){
                h2o.imgStatus.setImageResource(R.drawable.trues)
            }
            else{
                h2o.imgStatus.setImageResource(R.drawable.falses)
            }
            if (listFood[position].getImages().isNotEmpty()){
                Glide.with(h2o.root).load(url+listFood[position].getImages()).into(h2o.imgFood)
            }
            else{
                Glide.with(h2o.root).load(R.drawable.ic_launcher_background).into(h2o.imgFood)
            }
            holder.itemView.setOnClickListener {
                onClick.onClick(position)
            }
        }
    }
}