package com.grad.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var layoutManager: LinearLayoutManager
    var categotyList: MutableList<Category> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        categotyList
        layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        rv_category.layoutManager = layoutManager
        categotyList.add(0, Category("Trouser"))
        categotyList.add(1, Category("Trouser"))
        categotyList.add(2, Category("Trouser"))
        categotyList.add(3, Category("Trouser"))

        val categoryAdapter =
            CategoryAdapter(
                categotyList,
                applicationContext
            )
        rv_category.adapter = categoryAdapter
    }
}