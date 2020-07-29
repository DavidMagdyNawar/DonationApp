package com.grad.graduationproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.grad.graduationproject.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


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
        categoryAdapter = CategoryAdapter(
            categoryList,
            applicationContext
        )
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

        fab_add.setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            startActivity(intent)
        }

        categorySearch.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString());

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Category> = ArrayList()
        for (item in categoryList) {
            if (item.name.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        categoryAdapter.filterList(filteredList)
    }

//    categorySearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//        override fun onQueryTextSubmit(query: String?): Boolean {
//            return false
//        }
//
//        override fun onQueryTextChange(newText: String?): Boolean {
//            adapter.filter.filter(newText)
//            return false
//        }
//
//    })


}
