package com.grad.graduationproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grad.graduationproject.model.Category
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryAdapter(
    private var categoryList: List<Category> = mutableListOf(),
    val context: Context
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
//    var onItemClick: ((Item) -> Unit)? = null
    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(category: Category) {
            itemView.categoryName.text = category.name

            Glide
                .with(context)
                .load(category.image)
                .centerCrop()
                .into(itemView.categoryImage);
        }

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

    fun filterList(filteredList: ArrayList<Category>) {
        categoryList = filteredList
        notifyDataSetChanged()
    }
}
