package com.ishanknijhawan.incablet_app

data class Customer(
    val list: ArrayList<CustomerItem>
)

data class CustomerItem(
    val age: Int,
    val date: String,
    val gender: String,
    val id: String,
    val img: String,
    val name: String,
    val status: String,
    var isExpanded: Boolean = false
)


