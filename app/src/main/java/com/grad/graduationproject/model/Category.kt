package com.grad.graduationproject.model


data class Category(
    val name: String,
    val description: String,
    val contact: String,
    val image: String
) {
    constructor() : this("", "", "", "")
}