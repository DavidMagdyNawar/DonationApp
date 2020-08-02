package com.grad.graduationproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CategoriesViewModel() : ViewModel() {

    companion object {
        private val CATEGORIES =
            FirebaseDatabase.getInstance().getReference("/categories")

//        var ITEMS
//        fun getItems(categoryName: String): DatabaseReference {
//            return FirebaseDatabase.getInstance().getReference("/categories/$categoryName/items")
//
//
//        }
    }

    private val categoriesLiveData = FirebaseQueryLiveData(CATEGORIES)
    fun getCategories(): LiveData<DataSnapshot?> {
        return categoriesLiveData
    }

    fun getItems(reference: DatabaseReference): LiveData<DataSnapshot?> {

        return FirebaseQueryLiveData(reference)
    }


}