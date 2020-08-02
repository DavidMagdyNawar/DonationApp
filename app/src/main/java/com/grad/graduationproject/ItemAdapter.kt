package com.grad.graduationproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grad.graduationproject.model.Item
import kotlinx.android.synthetic.main.item.view.*

class ItemAdapter(
    val itemList: MutableList<Item> = mutableListOf(),
    val context: Context
) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: Item) {
            itemView.itemName.text = item.name
            itemView.itemDescription.text = item.description
            itemView.itemLocation.text = item.location
            itemView.itemContact.text = item.phonNumber

            Glide
                .with(context)
                .load(item.image)
                .centerCrop()
                .into(itemView.itemImage);
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(itemList[position])
    }


}

