package com.grad.graduationproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var layoutManager: LinearLayoutManager
    private var categoryList: MutableList<Category> = mutableListOf()
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(
            CategoriesViewModel::class.java
        )

        layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        rv_category.layoutManager = layoutManager
        categoryAdapter = CategoryAdapter(categoryList, applicationContext)
        rv_category.adapter = categoryAdapter

        val categoriesLiveData = viewModel.getCategories()
        categoriesLiveData.observe(this,
            Observer<DataSnapshot?> { dataSnapshot ->
                categoryList.clear()
                val d = dataSnapshot!!.children
                d.forEach {
                    val cat: Category? = it.getValue(Category::class.java)
                    if (cat != null) {
                        categoryList.add(cat)
                    }
                }
                categoryAdapter.notifyDataSetChanged()
            }
        )
    }
}

