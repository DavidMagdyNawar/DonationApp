package com.grad.graduationproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryAdapter(
    private var categoryList: List<Category> = mutableListOf(),
    val context: Context
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
//    var onItemClick: ((Item) -> Unit)? = null
    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(category: Category) {
            itemView.categoryName.text = category.name
        }
//        init {
//            itemView.setOnClickListener {
//                onItemClick?.invoke(categoryList[adapterPosition])
//                var intent = Intent(
//                    context,
//                    EventActivity::class.java
//                )
//                context.startActivity(intent)
//            }
//        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category , parent, false)
        return CategoryViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return categoryList.size
    }
    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        holder.bindData(categoryList[position])
    }
}
