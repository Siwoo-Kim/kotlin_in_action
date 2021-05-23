package com.siwoo.lesson2


data class Company(val manager: Manager?)
data class Manager(val address: Address?)
data class Address(val city: City?)
data class City(val name: String?)

fun main(args: Array<String>) {
    val x: Int = 3
    val y: Int? = x // nullable is parent of non-nullable

    val map = HashMap<String, Company>()
    
    val cityName = if (map["mycompany"] != null) map["mycompany"] else null
    println(cityName)
    val city = map["mycompany"]?.manager?.address?.city?.name
    println(city)
    
    println(map["mycompany"]?.manager?.address?.city?.name ?: "notexit")
    
    
}