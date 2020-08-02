package com.grad.graduationproject.model

data class Item(val name:String, val image:String , val description:String, val location :String, val phonNumber:String)
{
    constructor(): this ("","","","","")
}