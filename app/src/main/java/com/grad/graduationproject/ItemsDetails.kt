package com.grad.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.grad.graduationproject.model.Item
import kotlinx.android.synthetic.main.activity_items_details.*

class ItemsDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_details)
        var name = intent.getStringExtra("name")
        var itemList: MutableList<Item> = mutableListOf()

        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        rv_item.layoutManager = layoutManager
        val itemAdapter = ItemAdapter(
            itemList,
            applicationContext
        )
        rv_item.adapter = itemAdapter

        if (intent.getStringExtra("name")?.contains(" ")!!) {
            name = intent.getStringExtra("name")!!.replace(" ", "_")

        }
        val viewModel = ViewModelProviders.of(this).get(
            CategoriesViewModel::class.java
        )
//        CategoriesViewModel.ITEMS = FirebaseDatabase.getInstance().getReference("/categories/$name/items")

        val itemsLiveData = viewModel.getItems(
            (FirebaseDatabase.getInstance().getReference("/categories/$name/items"))
        )
        itemsLiveData.observe(this,
            Observer<DataSnapshot?> { dataSnapshot ->
                itemList.clear()
                val d = dataSnapshot!!.children
                d.forEach {
                    val item: Item? = it.getValue(
                        Item::class.java
                    )
                    if (item != null) {
                        itemList.add(item)
                    }
                }
                itemAdapter.notifyDataSetChanged()
            }
        )
    }
}
