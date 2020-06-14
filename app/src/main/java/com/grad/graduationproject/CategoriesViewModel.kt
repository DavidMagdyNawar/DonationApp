package com.grad.graduationproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase


class CategoriesViewModel : ViewModel() {
    companion object {
        private val CATEGORIES =
            FirebaseDatabase.getInstance().getReference("/categories")
    }
    private val categoriesLiveData = FirebaseQueryLiveData(CATEGORIES)
    fun getCategories(): LiveData<DataSnapshot?> {
        return categoriesLiveData
    }
}