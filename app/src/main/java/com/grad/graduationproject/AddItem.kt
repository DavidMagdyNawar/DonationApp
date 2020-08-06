package com.grad.graduationproject

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.grad.graduationproject.model.Category
import com.grad.graduationproject.model.Item
import kotlinx.android.synthetic.main.activity_add_item.*
import java.util.*


class AddItem : AppCompatActivity(), OnItemSelectedListener {

    companion object {
        const val PICk_IMAGE_CODE = 1000
    }

    lateinit var storageReference: StorageReference
    var url = ""
    private lateinit var viewModel: CategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        storageReference =
            FirebaseStorage.getInstance().reference.child("image/" + UUID.randomUUID().toString())
        addCategoryCard.setOnClickListener {
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select a picture"),
                PICk_IMAGE_CODE
            )

        }
        viewModel = ViewModelProviders.of(this).get(
            CategoriesViewModel::class.java
        )
        var listValue = mutableListOf<String>()

        val catKeyValue = viewModel.getCategories()
        catKeyValue.observe(this,
            Observer<DataSnapshot?> { dataSnapshot ->
                val d = dataSnapshot!!.children
                listValue.clear()
                d.forEach {
                    val cat: Category? = it.getValue(
                        Category::class.java
                    )
                    if (cat != null) {
                        var value = cat.name
                        if (cat.name.contains("_")) {
                            value = cat.name.replace("_", " ")
                        }
                        listValue.add(value)
                    }
                }
                var array: ArrayAdapter<String> =
                    ArrayAdapter<String>(this, R.layout.spinner_item, listValue);
                array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                with(spinner1) {
                    adapter = array
                    setSelection(0, false)
                    onItemSelectedListener = this@AddItem
                    gravity = Gravity.CENTER
                }
            }

        )



        addCard.setOnClickListener {
            if (url != ""
                && itemName.text.toString() != ""
                && itemDescription.text.toString() != ""
                && itemLocation.text.toString() != ""
                && itemContact.text.toString() != ""
            ) {
                val item = Item(
                    name = itemName.text.toString(),
                    description = itemDescription.text.toString(),
                    location = itemLocation.text.toString(),
                    phonNumber = itemContact.text.toString(),
                    image = url
                )
                var spinnerValue = spinner1.selectedItem.toString()
                if (spinnerValue.contains(" ")) {
                    spinnerValue = spinnerValue.replace(" ", "_")
                }
                FirebaseDatabase.getInstance().reference.child("categories/${spinnerValue}/items")
                    .child(UUID.randomUUID().toString()).setValue(item)
                Toast.makeText(this,"added successfully ",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICk_IMAGE_CODE) {
            progressBar.visibility = View.VISIBLE
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Failed to upload the image", Toast.LENGTH_SHORT).show()
                    uploadedImage.visibility = View.GONE
                    addCategoryImage.visibility = View.VISIBLE
                    addCategoryImage.isClickable = true
                    addCategoryCard.isClickable = true

                }
                storageReference.downloadUrl
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            url = downloadUri.toString()
                            uploadedImage.visibility = View.VISIBLE
                            addCategoryImage.visibility = View.GONE
                            addCategoryImage.isClickable = false
                            addCategoryCard.isClickable = false

                            Glide
                                .with(this)
                                .load(url)
                                .centerCrop()
                                .into(uploadedImage);
                            progressBar.visibility = View.GONE
                        }
                    }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "not selected", Toast.LENGTH_SHORT).show()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}