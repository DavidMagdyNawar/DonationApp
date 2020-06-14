package com.grad.graduationproject


data class Category(
    val name: String,
    val description: String,
    val contact: String,
    val image: String
) {
    constructor() : this("", "", "", "")
}